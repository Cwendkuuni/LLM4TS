import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
	
	private Options options;
	private CommandLineParser parser;
	private CommandLine cmd;

	@Before
	public void setUp() {
		options = mock(Options.class);
		parser = new Parser() {
			@Override
			protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
				return arguments;
			}
		};
		cmd = new CommandLine();
		when(options.helpOptions()).thenReturn(Arrays.asList(new Option("--help")));
	}
	
	@Test
	public void testParse_DefaultArguments_Success() throws ParseException {
		String[] arguments = {"-f", "fileName"};
		parser.parse(options, arguments, cmd, false);
		
		assertTrue(cmd.hasOption("-f"));
		assertEquals("fileName", cmd.getOptionValue("-f"));
	}
	
	@Test
	public void testParse_NullOptionsAndArguments_Success() throws ParseException {
		String[] arguments = null;
		parser.parse(null, arguments, cmd, false);
		
		assertTrue(cmd.getOptions().isEmpty());
		assertTrue(cmd.getArgs().isEmpty());
	}
	
	@Test(expected=UnrecognizedOptionException.class)
	public void testParse_UnrecognizedOption_ThrowsUnrecognizedOptionException() throws ParseException {
		String[] arguments = {"-u", "userName"};
		parser.parse(options, arguments, cmd, false);
	}
	
	@Test
	public void testParse_StopAtNonOption_StopParsingWhenNonOptionIsEncountered() throws ParseException {
		String[] arguments = {"-f", "fileName", "nonOption"};
		parser.parse(options, arguments, cmd, true);
		
		assertTrue(cmd.hasOption("-f"));
		assertEquals("fileName", cmd.getOptionValue("-f"));
		assertTrue(cmd.getArgs().contains("nonOption"));
	}
	
	@Test(expected=ParseException.class)
	public void testProcessOption_ArgumentDoesNotRepresentAnOption_ThrowsParseException() throws ParseException {
		Option opt = mock(Option.class);
		when(options.hasOption("-u")).thenReturn(false);
		
		parser.processOption("-u", null, opt);
	}
	
	@Test(expected=MissingArgumentException.class)
	public void testProcessArgs_ArgumentValueIsRequiredButNotProvided_ThrowsMissingArgumentException() throws ParseException {
		Option opt = mock(Option.class);
		when(opt.hasOptionalArg()).thenReturn(false);
		
		parser.processArgs(opt, new String[] {});
	}
	
	@Test
	public void testProcessArgs_ArgumentValueIsProvided_Success() throws ParseException {
		Option opt = mock(Option.class);
		when(opt.hasOptionalArg()).thenReturn(false);
		parser.processArgs(opt, new String[] {"value"});
		
		assertTrue(cmd.hasOption(opt.getKey()));
		assertEquals("value", cmd.getOptionValue(opt.getKey()));
	}
	
	@Test
	public void testProcessProperties_PropertiesIsNull_DoNothing() {
		Properties properties = null;
		parser.processProperties(properties, cmd);
		
		assertTrue(cmd.getOptions().isEmpty());
	}
	
	@Test
	public void testProcessProperties_ValidProperties_AddOptionsToCommandLine() {
		Properties properties = new Properties();
		properties.setProperty("-f", "fileName");
		properties.setProperty("-u", "userName");
		
		parser.processProperties(properties, cmd);
		
		assertTrue(cmd.hasOption("-f"));
		assertTrue(cmd.hasOption("-u"));
		assertEquals("fileName", cmd.getOptionValue("-f"));
		assertEquals("userName", cmd.getOptionValue("-u"));
	}
	
	@Test
	public void testCheckRequiredOptions_AllRequiredOptionsArePresent_DoNothing() throws MissingOptionException {
		RequiredOption requiredOption1 = mock(RequiredOption.class);
		RequiredOption requiredOption2 = mock(RequiredOption.class);
		List<RequiredOption> requiredOptions = new ArrayList<>();
		requiredOptions.add(requiredOption1);
		requiredOptions.add(requiredOption2);
		
		when(options.getRequiredOptions()).thenReturn(requiredOptions);
		when(requiredOption1.getKey()).thenReturn("-f");
		when(requiredOption2.getKey()).thenReturn("-u");
		cmd.addOption(new Option("-f"));
		cmd.addOption(new Option("-u"));
		
		parser.checkRequiredOptions(options, cmd);
	}
	
	@Test(expected=MissingOptionException.class)
	public void testCheckRequiredOptions_OneRequiredOptionIsMissing_ThrowsMissingOptionException() throws MissingOptionException {
		RequiredOption requiredOption1 = mock(RequiredOption.class);
		RequiredOption requiredOption2 = mock(RequiredOption.class);
		List<RequiredOption> requiredOptions = new ArrayList<>();
		requiredOptions.add(requiredOption1);
		requiredOptions.add(requiredOption2);
		
		when(options.getRequiredOptions()).thenReturn(requiredOptions);
		when(requiredOption1.getKey()).thenReturn("-f");
		when(requiredOption2.getKey()).thenReturn("-u");
		cmd.addOption(new Option("-f"));
		
		parser.checkRequiredOptions(options, cmd);
	}
	
	@Test
	public void testCheckRequiredOptions_NoRequiredOptions_DoNothing() throws MissingOptionException {
		when(options.getRequiredOptions()).thenReturn(new ArrayList<>());
		
		parser.checkRequiredOptions(options, cmd);
	}

}