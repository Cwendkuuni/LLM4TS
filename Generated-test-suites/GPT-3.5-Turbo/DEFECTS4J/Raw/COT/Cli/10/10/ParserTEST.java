import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

	private Parser parser;
	private Options options;

	@Before
	public void setUp() {
		parser = new Parser() {
			@Override
			protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
				return null;
			}

			@Override
			public void processArgs(Option opt, ListIterator iter) throws ParseException {
				
			}
		};
		
		options = new Options();
	}

	@Test
	public void testParse() throws ParseException {
		String[] arguments = { "-a", "-b", "value1", "--c", "value2", "--d", "value3", "arg1", "arg2" };
		CommandLine cmd = parser.parse(options, arguments);

		assertTrue(cmd.hasOption("a"));
		assertTrue(cmd.hasOption("b"));
		assertTrue(cmd.hasOption("c"));
		assertTrue(cmd.hasOption("d"));
		assertEquals("value1", cmd.getOptionValue("b"));
		assertEquals("value2", cmd.getOptionValue("c"));
		assertEquals("value3", cmd.getOptionValue("d"));
		assertEquals(2, cmd.getArgs().length);
		assertEquals("arg1", cmd.getArgs()[0]);
		assertEquals("arg2", cmd.getArgs()[1]);
	}

	@Test(expected = MissingArgumentException.class)
	public void testParseMissingArgument() throws ParseException {
		String[] arguments = { "-a" };
		parser.parse(options, arguments);
	}

	@Test(expected = MissingOptionException.class)
	public void testParseRequiredOptionMissing() throws ParseException {
		options.addOption(Option.builder("a").required().build());
		
		String[] arguments = { };
		parser.parse(options, arguments);
	}

	@Test
	public void testProcessProperties() {
		Properties properties = new Properties();
		properties.setProperty("a", "value1");
		properties.setProperty("b", "true");
		properties.setProperty("c", "false");

		parser.processProperties(properties);

		assertTrue(parser.cmd.hasOption("a"));
		assertTrue(parser.cmd.hasOption("b"));
		assertTrue(parser.cmd.hasOption("c"));
		assertEquals("value1", parser.cmd.getOptionValue("a"));
	}

	@Test
	public void testProcessArgs() throws ParseException {
		Option opt = Option.builder("a").hasArgs().build();
		ListIterator<Object> iter = Arrays.asList("arg1", "arg2").listIterator();

		parser.processArgs(opt, iter);

		assertEquals(2, opt.getValues().length);
		assertEquals("arg1", opt.getValues()[0]);
		assertEquals("arg2", opt.getValues()[1]);
	}

	@Test(expected = MissingArgumentException.class)
	public void testProcessArgsMissingArgument() throws ParseException {
		Option opt = Option.builder("a").hasArg().build();
		ListIterator<Object> iter = Collections.emptyList().listIterator();

		parser.processArgs(opt, iter);
	}

	@Test(expected = UnrecognizedOptionException.class)
	public void testProcessOptionUnrecognizedOption() throws ParseException {
		Option opt = Option.builder("a").build();
		ListIterator<Object> iter = Arrays.asList("-b").listIterator();

		parser.processOption("-b", iter);
	}

	@Test
	public void testProcessOption() throws ParseException {
		Option opt = Option.builder("a").build();
		ListIterator<Object> iter = Collections.singletonList("-a").listIterator();

		parser.processOption("-a", iter);

		assertTrue(parser.cmd.hasOption("a"));
	}
}