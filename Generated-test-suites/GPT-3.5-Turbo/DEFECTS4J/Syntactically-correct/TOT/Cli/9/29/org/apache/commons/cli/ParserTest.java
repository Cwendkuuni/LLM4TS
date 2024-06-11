import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {
	private Options options;
	private CommandLineParser parser;
	private CommandLine cmd;
	
	@Before
	public void setUp() {
		options = new Options();

		Option option1 = new Option("a", "opt1", false, "Option 1");
		Option option2 = new Option("b", "opt2", true, "Option 2");
		Option option3 = new Option("c", "opt3", false, "Option 3");
		
		options.addOption(option1);
		options.addOption(option2);
		options.addOption(option3);
		
		parser = new DefaultParser();
	}
	
	@Test
	public void testParse_NoOptions_ReturnsEmptyCommandLine() throws ParseException {
		String[] args = {};
		cmd = parser.parse(options, args);
		
		assertTrue(cmd.getOptions().length == 0);
		assertTrue(cmd.getArgList().isEmpty());
	}
	
	@Test
	public void testParse_SingleShortOption_ReturnsCommandLineWithOption() throws ParseException {
		String[] args = {"-a"};
		cmd = parser.parse(options, args);
		
		assertTrue(cmd.hasOption("a"));
		assertTrue(cmd.getOptions().length == 1);
		assertTrue(cmd.getArgList().isEmpty());
	}
	
	@Test
	public void testParse_SingleLongOption_ReturnsCommandLineWithOption() throws ParseException {
		String[] args = {"--opt1"};
		cmd = parser.parse(options, args);
		
		assertTrue(cmd.hasOption("opt1"));
		assertTrue(cmd.getOptions().length == 1);
		assertTrue(cmd.getArgList().isEmpty());
	}
	
	@Test
	public void testParse_SingleOptionWithValue_ReturnsCommandLineWithOptionAndValue() throws ParseException {
		String[] args = {"-b", "value"};
		cmd = parser.parse(options, args);
		
		assertTrue(cmd.hasOption("b"));
		assertTrue(cmd.getOptionValue("b").equals("value"));
		assertTrue(cmd.getOptions().length == 1);
		assertTrue(cmd.getArgList().isEmpty());
	}
	
	@Test
	public void testParse_InvalidOption_ThrowsUnrecognizedOptionException() {
		try {
			String[] args = {"-d"};
			cmd = parser.parse(options, args);
			
			fail("Should have thrown UnrecognizedOptionException");
		} catch (UnrecognizedOptionException e) {
			assertTrue(true);
		} catch (ParseException e) {
			fail("Should have thrown UnrecognizedOptionException");
		}
	}
	
	@Test
	public void testParse_MissingArgument_ThrowsMissingArgumentException() {
		try {
			String[] args = {"-b"};
			cmd = parser.parse(options, args);
			
			fail("Should have thrown MissingArgumentException");
		} catch (MissingArgumentException e) {
			assertTrue(true);
		} catch (ParseException e) {
			fail("Should have thrown MissingArgumentException");
		}
	}
	
	@Test
	public void testParse_StopAtNonOption_ReturnsCommandLineWithOptionAndArgs() throws ParseException {
		String[] args = {"-a", "arg1", "-b", "value", "arg2", "arg3", "--opt3"};
		cmd = parser.parse(options, args, true);
		
		assertTrue(cmd.hasOption("a"));
		assertTrue(cmd.hasOption("b"));
		assertTrue(cmd.getOptionValue("b").equals("value"));
		assertTrue(cmd.hasOption("opt3"));
		assertTrue(cmd.getOptions().length == 3);
		assertTrue(cmd.getArgList().size() == 3);
		assertTrue(cmd.getArgList().get(0).equals("arg1"));
		assertTrue(cmd.getArgList().get(1).equals("arg2"));
		assertTrue(cmd.getArgList().get(2).equals("arg3"));
	}
	
	@Test
	public void testParse_Properties_ReturnsCommandLineWithOptionAndProperties() throws ParseException {
		String[] args = {"-a"};
		Properties properties = new Properties();
		properties.setProperty("opt2", "value");
		cmd = parser.parse(options, args, properties);
		
		assertTrue(cmd.hasOption("a"));
		assertTrue(cmd.hasOption("b"));
		assertTrue(cmd.getOptionValue("b").equals("value"));
		assertTrue(cmd.getOptions().length == 2);
		assertTrue(cmd.getArgList().isEmpty());
	}
	
	@Test
	public void testParse_CheckRequiredOptions_MissingOptions_ThrowsMissingOptionException() {
		Option option4 = new Option("d", "opt4", true, "Option 4");
		options.addOption(option4);
		
		try {
			String[] args = {"-a"};
			cmd = parser.parse(options, args);
			
			fail("Should have thrown MissingOptionException");
		} catch (MissingOptionException e) {
			assertTrue(true);
		} catch (ParseException e) {
			fail("Should have thrown MissingOptionException");
		}
	}
}