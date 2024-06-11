package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for the Parser class.
 */
public class ParserTest {

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}
     * with valid arguments and options.
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseValid() throws ParseException {
        final String[] arguments = new String[]{"-a", "-b", "--optionC=valueC", "--optionD"};
        final String[] expectedArgs = new String[]{"-a", "-b", "--optionC", "valueC", "--optionD"};
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");

        final CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null);

        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertEquals(2, cmd.getOptions().size());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}
     * with invalid arguments.
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalid() throws ParseException {
        final String[] arguments = new String[]{"--invalidOption"};
        final Options options = new Options();
        final CommandLineParser parser = new DefaultParser();

        parser.parse(options, arguments, null);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}
     * with missing required options.
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException {
        final String[] arguments = new String[]{"-a"};
        final Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "requiredB");

        final CommandLineParser parser = new DefaultParser();
        parser.parse(options, arguments, null);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}
     * with multiple values for an option.
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseMultipleValuesForOption() throws ParseException {
        final String[] arguments = new String[]{"-t", "test1", "--type", "type1", "--type", "type2"};
        final String[] expectedArgs = new String[]{"-t", "test1"};
        final Options options = new Options();
        options.addOption("t", true, "test");
        options.addOption("type", true, "type");

        final CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option optionType = optionsList.get(1);
        List<String> values = optionType.getValues();
        assertEquals(2, values.size());
        assertEquals("type1", values.get(0));
        assertEquals("type2", values.get(1));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}
     * with long options.
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseLongOptions() throws ParseException {
        final String[] arguments = new String[]{"--longOption1", "--longOption2=value"};
        final Options options = new Options();
        options.addOption("longOption1", false, "Long option 1");
        options.addOption("longOption2", true, "Long option 2");

        final CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        Option optionLongOption1 = optionsList.get(0);
        assertFalse(optionLongOption1.hasValue());
        Option optionLongOption2 = optionsList.get(1);
        List<String> values = optionLongOption2.getValues();
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}
     * with property values.
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseProperties() throws ParseException {
        final String[] arguments = new String[]{"--property=value"};
        final Properties properties = new Properties();
        properties.setProperty("property", "value");
        final Options options = new Options();
        options.addOption("p", true, "Property");

        final CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(1, optionsList.size());
        Option optionProperty = optionsList.get(0);
        List<String> values = optionProperty.getValues();
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}
     * with stopAtNonOption.
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        final String[] arguments = new String[]{"--invalidOption", "test"};
        final Options options = new Options();
        options.addOption("t", false, "test");

        final CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, null, true);

        List<String> args = cmd.getArgs();
        assertEquals(1, args.size());
        assertEquals("test", args.get(0));
    }

    /**
     * Test method for {@link Parser#flatten(Options, String[], boolean)}
     * with stopAtNonOption.
     */
    @Test
    public void testFlattenWithStopAtNonOption() {
        final String[] arguments = new String[]{"--invalidOption", "test"};
        final Options options = new Options();

        final Parser parser = new DefaultParser();
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        String[] expectedFlattenedArguments = new String[]{"--invalidOption", "test"};
        assertArrayEquals(expectedFlattenedArguments, flattenedArguments);
    }
}