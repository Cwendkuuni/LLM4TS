
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

	private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser() {
			@Override
			protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
				return null;
			}
		};
	}

	@Test
	public void testSetOptions() {
		Options options = new Options();
		parser.setOptions(options);
		
		assertEquals(options, parser.getOptions());
	}

	@Test
	public void testGetOptions() {
		Options options = new Options();
		parser.setOptions(options);
		
		assertEquals(options, parser.getOptions());
	}

	@Test
	public void testGetRequiredOptions() {
		Options options = new Options();
		Option option = new Option("o", "option", false, "an option");
		options.addRequiredOption(option);
		parser.setOptions(options);
		
		assertEquals(1, parser.getRequiredOptions().size());
		assertEquals(option, parser.getRequiredOptions().get(0));
	}

	@Test
	public void testParseOptionsStringArray() throws ParseException {
		Options options = new Options();
		Option option = new Option("o", "option", false, "an option");
		options.addOption(option);
		parser.setOptions(options);
		
		String[] args = {"-o"};
		CommandLine cmd = parser.parse(options, args);
		
		assertTrue(cmd.hasOption("o"));
	}

	@Test
	public void testParseOptionsStringArrayProperties() throws ParseException {
		Options options = new Options();
		Option option = new Option("o", "option", true, "an option");
		options.addOption(option);
		parser.setOptions(options);
		
		String[] args = {"-o", "value"};
		Properties props = new Properties();
		props.setProperty("option", "value");
		CommandLine cmd = parser.parse(options, args, props);
		
		assertTrue(cmd.hasOption("o"));
		assertEquals("value", cmd.getOptionValue("o"));
	}

	@Test
	public void testParseOptionsStringArrayBoolean() throws ParseException {
		Options options = new Options();
		Option option = new Option("o", "option", true, "an option");
		options.addOption(option);
		parser.setOptions(options);
		
		String[] args = {"-o", "value"};
		CommandLine cmd = parser.parse(options, args, true);
		
		assertTrue(cmd.hasOption("o"));
		assertEquals("value", cmd.getOptionValue("o"));
	}

	@Test
	public void testParseOptionsStringArrayPropertiesBoolean() throws ParseException {
		Options options = new Options();
		Option option = new Option("o", "option", true, "an option");
		options.addOption(option);
		parser.setOptions(options);
		
		String[] args = {"-o", "value"};
		Properties props = new Properties();
		props.setProperty("option", "value");
		CommandLine cmd = parser.parse(options, args, props, true);
		
		assertTrue(cmd.hasOption("o"));
		assertEquals("value", cmd.getOptionValue("o"));
	}

	@Test
	public void testProcessProperties() {
		Options options = new Options();
		Option option = new Option("o", "option", true, "an option");
		options.addOption(option);
		parser.setOptions(options);
		
		Properties props = new Properties();
		props.setProperty("option", "value");
		parser.processProperties(props);
		
		assertTrue(parser.getCommandLine().hasOption("o"));
		assertEquals("value", parser.getCommandLine().getOptionValue("o"));
	}

	@Test
	public void testCheckRequiredOptions() throws MissingOptionException {
		Options options = new Options();
		Option option = new Option("o", "option", false, "an option");
		option.setRequired(true);
		options.addRequiredOption(option);
		parser.setOptions(options);
		
		String[] args = {"-o"};
		parser.parse(options, args);
		
		assertEquals(0, parser.getRequiredOptions().size());
	}

	@Test(expected = MissingOptionException.class)
	public void testCheckRequiredOptionsThrowsException() throws MissingOptionException {
		Options options = new Options();
		Option option = new Option("o", "option", false, "an option");
		option.setRequired(true);
		options.addRequiredOption(option);
		parser.setOptions(options);
		
		parser.checkRequiredOptions();
	}

	@Test
	public void testProcessArgs() throws ParseException {
		Options options = new Options();
		Option option = new Option("o", "option", true, "an option");
		options.addOption(option);
		parser.setOptions(options);
		
		String[] args = {"-o", "value"};
		CommandLine cmd = parser.parse(options, args);
		
		assertTrue(cmd.hasOption("o"));
		assertEquals(1, cmd.getOptionValues("o").length);
		assertEquals("value", cmd.getOptionValues("o")[0]);
	}

	@Test(expected = MissingArgumentException.class)
	public void testProcessArgsThrowsException() throws ParseException {
		Options options = new Options();
		Option option = new Option("o", "option", true, "an option");
		options.addOption(option);
		parser.setOptions(options);
		
		String[] args = {"-o"};
		parser.parse(options, args);
	}

	@Test
	public void testProcessOption() throws ParseException {
		Options options = new Options();
		Option option = new Option("o", "option", false, "an option");
		options.addOption(option);
		parser.setOptions(options);
		
		String[] args = {"-o"};
		CommandLine cmd = parser.parse(options, args);
		
		assertTrue(cmd.hasOption("o"));
	}

	@Test(expected = UnrecognizedOptionException.class)
	public void testProcessOptionThrowsException() throws ParseException {
		Options options = new Options();
		option.setRequired(true);
		parser.setOptions(options);
		
		String[] args = {"-o"};
		parser.parse(options, args);
	}

}
