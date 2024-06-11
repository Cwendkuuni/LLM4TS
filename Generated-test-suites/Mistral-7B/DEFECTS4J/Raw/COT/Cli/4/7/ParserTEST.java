import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] arguments = new String[]{"--test", "-t", "1", "-a", "value", "arg"};
        Options options = new Options();
        Option optionTest = new Option("t", "test", true, "Test option");
        Option optionA = new Option("a", "arg", 1, "Arg option");
        optionTest.setRequired(false);
        optionA.setRequired(true);
        options.addOption(optionTest);
        options.addOption(optionA);

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals(2, commandLine.getArgs().size());
        assertEquals("value", commandLine.getOptionValue("a"));
        assertTrue(commandLine.hasOption("test"));
        assertFalse(commandLine.hasOption("unknown"));
    }

    @Test
    public void testParse_EmptyArguments() throws ParseException {
        String[] arguments = new String[0];
        Options options = new Options();

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals(0, commandLine.getArgs().size());
    }

    @Test
    public void testParse_MissingRequiredOption() throws ParseException {
        String[] arguments = new String[]{"--test"};
        Options options = new Options();
        Option optionA = new Option("a", "arg", 1, "Arg option");
        optionA.setRequired(true);
        options.addOption(optionA);

        try {
            Parser parser = new DefaultParser();
            parser.parse(options, arguments);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Missing required option 'a'", e.getMessage());
        }
    }

    @Test
    public void testParse_InvalidOption() throws ParseException {
        String[] arguments = new String[]{"--invalid"};
        Options options = new Options();

        try {
            Parser parser = new DefaultParser();
            parser.parse(options, arguments);
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: invalid", e.getMessage());
        }
    }

    @Test
    public void testParseProperties() throws ParseException {
        String[] arguments = new String[]{"--test", "-t", "1", "--property=value"};
        Properties properties = new Properties();
        properties.setProperty("test", "testValue");

        Options options = new Options();
        Option optionTest = new Option("t", "test", true, "Test option");
        Option optionProperty = new Option("p", "property", true, "Property option");
        optionTest.setRequired(false);
        optionProperty.setLongOpt("property");
        options.addOption(optionTest);
        options.addOption(optionProperty);

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, properties);

        assertEquals(1, commandLine.getArgs().size());
        assertEquals("testValue", commandLine.getOptionValue("property"));
        assertEquals("testValue", commandLine.getOptionValue("p"));
        assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testParseProperties_MissingOption() throws ParseException {
        String[] arguments = new String[]{"--test", "--property"};
        Properties properties = new Properties();

        Options options = new Options();
        Option optionTest = new Option("t", "test", true, "Test option");
        Option optionProperty = new Option("p", "property", true, "Property option");
        optionTest.setRequired(false);
        optionProperty.setLongOpt("property");
        options.addOption(optionTest);
        options.addOption(optionProperty);

        Parser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(options, arguments, properties);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals("Option 'property' is missing", e.getMessage());
        }
    }

    @Test
    public void testParseProperties_InvalidOption() throws ParseException {
        String[] arguments = new String[]{"--test", "--invalid"};
        Properties properties = new Properties();

        Options options = new Options();
        Option optionTest = new Option("t", "test", true, "Test option");
        Option optionInvalid = new Option("i", "invalid", true, "Invalid option");
        optionTest.setRequired(false);
        optionInvalid.setLongOpt("invalid");
        options.addOption(optionTest);
        options.addOption(optionInvalid);

        Parser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(options, arguments, properties);
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: invalid", e.getMessage());
        }
    }

    @Test
    public void testParse_StopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"--test", "arg"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, true);

        assertEquals(1, commandLine.getArgs().size());
        assertEquals("arg", commandLine.getArgs().get(0));
    }

    @Test
    public void testParse_MultipleStopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"--test", "--option", "arg1", "--", "arg2"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, true);

        assertEquals(1, commandLine.getOptions().size());
        Option optionTest = commandLine.getOption("test");
        assertEquals("--test", optionTest.getOpt());
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));
    }

    @Test
    public void testParse_EmptyStopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"--test", "--"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, true);

        assertEquals(0, commandLine.getArgs().size());
    }

    @Test
    public void testParse_EmptyStopAtNonOption_NoDash() throws ParseException {
        String[] arguments = new String[]{"--test", "--"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, true);

        assertEquals(0, commandLine.getArgs().size());
    }

    @Test
    public void testFlatten() {
        String[] arguments = new String[]{"--test", "-t", "1", "-a", "value", "arg"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        String[] expectedArguments = new String[]{"--test", "--t", "1", "t", "1", "-a", "value", "a", "value", "arg"};

        assertArrayEquals(expectedArguments, parsedArguments);
    }

    @Test
    public void testFlatten_Empty() {
        String[] arguments = new String[0];
        Options options = new Options();

        Parser parser = new DefaultParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        String[] expectedArguments = new String[0];

        assertArrayEquals(expectedArguments, parsedArguments);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = new String[]{"--test", "-t", "1", "--", "arg1", "arg2"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        String[] parsedArguments = parser.flatten(options, arguments, true);

        String[] expectedArguments = new String[]{"--test", "-t", "1"};

        assertArrayEquals(expectedArguments, parsedArguments, 0, 3);
    }

    @Test
    public void testProcessProperties() {
        String[] arguments = new String[]{"--test", "--property=value"};
        Properties properties = new Properties();

        Parser parser = new DefaultParser();
        parser.processProperties(properties);

        assertEquals(1, properties.size());
        assertEquals("value", properties.getProperty("test"));
    }

    @Test
    public void testCheckRequiredOptions() throws ParseException {
        String[] arguments = new String[]{"--test"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.parse(options, arguments, null, false);

        Option optionRequired = new Option("r", "required", true, "Required option");
        optionRequired.setRequired(true);
        options.addOption(optionRequired);

        Parser parser2 = new DefaultParser();

        try {
            parser2.parse(options, arguments, null, false);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals("Option 'required' is missing", e.getMessage());
        }
    }
}