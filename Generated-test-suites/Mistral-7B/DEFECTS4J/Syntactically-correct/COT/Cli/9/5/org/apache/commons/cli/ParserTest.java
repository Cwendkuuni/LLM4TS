import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testSetOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test(expected = NullPointerException.class)
    public void testSetOptionsNull() {
        Parser parser = new DefaultParser();
        parser.setOptions(null);
    }

    @Test
    public void testGetOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = options.getRequiredOptions();
        parser.setOptions(options);
        List<Option> actual = parser.getRequiredOptions();
        assertEquals(requiredOptions, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testGetRequiredOptionsNull() {
        Parser parser = new DefaultParser();
        parser.getRequiredOptions();
    }

    @Test
    public void testFlattenEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = new String[0];
        String[] actual = parser.flatten(new Options(), arguments, false);
        String[] expected = new String[0];
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenSingleOption() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--option";
        String[] arguments = {option};
        String[] actual = parser.flatten(new Options(), arguments, false);
        String[] expected = {option};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenMultipleOptions() throws ParseException {
        Parser parser = new DefaultParser();
        String option1 = "--option1";
        String option2 = "--option2";
        String[] arguments = {option1, option2};
        String[] actual = parser.flatten(new Options(), arguments, false);
        String[] expected = {option1, option2};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithArguments() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--option";
        String arg = "arg";
        String[] arguments = {option, arg};
        String[] actual = parser.flatten(new Options(), arguments, false);
        String[] expected = {option, arg};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--option";
        String arg = "arg";
        String nonOption = "--non-option";
        String[] arguments = {option, arg, nonOption};
        String[] actual = parser.flatten(new Options(), arguments, true);
        String[] expected = {option, arg, nonOption};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testParseEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        CommandLine actual = parser.parse(options, new String[0]);
        CommandLine expected = new CommandLine();
        assertEquals(expected, actual);
    }

    @Test
    public void testParseSingleOption() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--option";
        String[] arguments = {option};
        CommandLine actual = parser.parse(new Options(), arguments);
        CommandLine expected = new CommandLine();
        expected.addOption(parser.getOptions().getOption(option));
        assertEquals(expected, actual);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--required-option";
        Options options = new Options().addRequiredOption(option);
        String[] arguments = new String[0];
        parser.parse(options, arguments);
    }

    @Test
    public void testParseSingleOptionalOption() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--optional-option";
        String arg = "arg";
        String[] arguments = {option, arg};
        CommandLine actual = parser.parse(new Options().addOption(option),
               arguments);
        CommandLine expected = new CommandLine();
        expected.addOption(parser.getOptions().getOption(option));
        expected.addArg(arg);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseMultipleOptions() throws ParseException {
        Parser parser = new DefaultParser();
        String option1 = "--option1";
        String option2 = "--option2";
        String arg = "arg";
        String[] arguments = {option1, option2, arg};
        CommandLine actual = parser.parse(new Options().addOptions(
                new Option(option1, true), new Option(option2, arg, true)),
               arguments);
        CommandLine expected = new CommandLine();
        expected.addOption(parser.getOptions().getOption(option1));
        expected.addOption(parser.getOptions().getOption(option2), arg);
        expected.addArg(arg);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--option";
        String arg = "arg";
        Properties properties = new Properties();
        properties.setProperty(option, arg);
        CommandLine actual = parser.parse(new Options().addOption(option),
                 new String[0], properties);
        CommandLine expected = new CommandLine();
        expected.addOption(parser.getOptions().getOption(option));
        expected.addArg(arg);
        assertEquals(expected, actual);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        String unrecognizedOption = "--unrecognized-option";
        String[] arguments = {unrecognizedOption};
        parser.parse(new Options(), arguments);
    }

    @Test
    public void testProcessPropertiesEmpty() {
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        parser.processProperties(properties);
        assertTrue(properties.isEmpty());
    }

    @Test
    public void testProcessPropertiesSingleValue() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--option";
        String value = "value";
        Properties properties = new Properties();
        properties.setProperty(option, value);
        parser.processProperties(properties);
        Option optionInstance = parser.getOptions().getOption(option);
        assertEquals(1, optionInstance.getValues().length);
        assertEquals(value, optionInstance.getValues()[0]);
    }

    @Test
    public void testProcessPropertiesMultipleValues() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--option";
        String value1 = "value1";
        String value2 = "value2";
        Properties properties = new Properties();
        properties.setProperty(option, value1 + "," + value2);
        parser.processProperties(properties);
        Option optionInstance = parser.getOptions().getOption(option);
        assertEquals(2, optionInstance.getValues().length);
        assertArrayEquals(new String[]{value1, value2}, optionInstance.getValues());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--option";
        String nonOption = "--non-option";
        String arg = "arg";
        String[] arguments = {option, arg, nonOption};
        CommandLine actual = parser.parse(new Options(), arguments, true);
        CommandLine expected = new CommandLine();
        expected.addOption(parser.getOptions().getOption(option));
        expected.addArg(arg);
        expected.addArg(nonOption);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckRequiredOptionsEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        parser.checkRequiredOptions();
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissing() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--required-option";
        Options options = new Options().addRequiredOption(option);
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    @Test
    public void testCheckRequiredOptionsPresent() throws ParseException {
        Parser parser = new DefaultParser();
        String option = "--required-option";
        String arg = "arg";
        String[] arguments = {option, arg};
        Options options = new Options().addRequiredOption(option);
        parser.setOptions(options);
        parser.parse(options, arguments);
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgsEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        Option option = new Option("option", true);
        ListIterator<String> iterator = Arrays.asList("", "").listIterator();
        parser.processArgs(option, iterator);
        assertFalse(option.hasValues());
        assertNull(option.getValueForProcessing());
    }

    @Test
    public void testProcessArgsSingleValue() throws ParseException {
        Parser parser = new DefaultParser();
        Option option = new Option("option", true);
        String value = "value";
        ListIterator<String> iterator = Arrays.asList("--option", value).listIterator();
        parser.processArgs(option, iterator);
        assertTrue(option.hasValues());
        assertArrayEquals(new String[]{value}, option.getValues());
        assertEquals(value, option.getValueForProcessing());
    }

    @Test
    public void testProcessArgsMultipleValues() throws ParseException {
        Parser parser = new DefaultParser();
        Option option = new Option("option", true);
        String value1 = "value1";
        String value2 = "value2";
        ListIterator<String> iterator = Arrays.asList("--option", value1, "--option", value2).listIterator();
        parser.processArgs(option, iterator);
        assertTrue(option.hasValues());
        assertArrayEquals(new String[]{value1, value2}, option.getValues());
        assertNull(option.getValueForProcessing());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Option option = new Option("option", true);
        ListIterator<String> iterator = Arrays.asList("--option").listIterator();
        parser.processArgs(option, iterator);
    }
}