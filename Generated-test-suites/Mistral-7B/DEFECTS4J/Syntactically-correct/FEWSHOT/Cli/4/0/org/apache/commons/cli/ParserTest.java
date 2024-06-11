package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", "action", true, "an action");
        options.addOption("b", "boolean", false, "a boolean value");

        String[] arguments = {"-a", "--action=run"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a").equals("run"));
        assertFalse(cmd.hasOption("b"));

        options = new Options();
        options.addOption("b", "boolean", false, "a boolean value");
        options.addRequiredOption("a", "action", true, "an action");

        arguments = new String[]{"--action=run"};
        cmd = new DefaultParser().parse(options, arguments);

        assertFalse(cmd.hasOption("a"));
        assertThrows(MissingArgumentException.class, () -> cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));

        options = new Options();
        options.addRequiredOption("a", "action", true, "an action");
        options.addOption("b", "boolean", false, "a boolean value");

        arguments = new String[]{"-a", "--action=run", "--boolean"};
        cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a").equals("run"));
        assertTrue(cmd.hasOption("b"));

        options.addOptionGroup(new OptionGroup("group", "Group"));
        options.getOptionGroup("group").addOption("c", "choice", true, "a choice");
        options.getOptionGroup("group").addOption("d", "another-choice", true, "another choice");

        arguments = new String[]{"-c", "--group=c"};
        cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.getOptionGroup("group").isSelected());
        assertFalse(cmd.hasOption("d"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", "action", true, "an action");

        Properties properties = new Properties();
        properties.setProperty("action", "run");

        String[] arguments = {};
        CommandLine cmd = new DefaultParser().parse(options, arguments, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a").equals("run"));

        options.addOption("b", "boolean", false, "a boolean value");

        arguments = new String[]{"--boolean"};
        cmd = new DefaultParser().parse(options, arguments, properties);

        assertTrue(cmd.hasOption("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", "action", true, "an action");

        String[] arguments = {};
        new DefaultParser().parse(options, arguments);

        fail("MissingOptionException expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "action", true, "an action");

        String[] arguments = {"--x"};
        new DefaultParser().parse(options, arguments);

        fail("UnrecognizedOptionException expected");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", "action", true, "an action");

        String[] arguments = {"-a"};
        new DefaultParser().parse(options, arguments);

        fail("MissingArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidBooleanValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", "boolean", false, "a boolean value");

        String[] arguments = {"--boolean=invalid"};
        new DefaultParser().parse(options, arguments);

        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.addOption("a", "option", true, "an option");
        options.addOptionGroup(group);

        String[] arguments = {"--group=invalid"};
        new DefaultParser().parse(options, arguments);

        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOptionValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", "action", true, "an action");

        String[] arguments = {"-a", "--action=\"invalid\""};
        new DefaultParser().parse(options, arguments);

        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOptionValueWithQuotes() throws ParseException {
        Options options = new Options();
        options.addOption("a", "action", true, "an action");

        String[] arguments = {"-a", "--action=\"invalid \""};
        new DefaultParser().parse(options, arguments);

        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOptionValueWithTrailingQuotes() throws ParseException {
        Options options = new Options();
        options.addOption("a", "action", true, "an action");

        String[] arguments = {"-a", "--action=\"invalid\""};
        new DefaultParser().parse(options, arguments);

        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOptionValueWithQuotesAndTrailingQuotes() throws ParseException {
        Options options = new Options();
        options.addOption("a", "action", true, "an action");

        String[] arguments = {"-a", "--action=\"invalid\""};
        new DefaultParser().parse(options, arguments);

        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", "empty", false, "an empty value");

        String[] arguments = {"--empty"};
        new DefaultParser().parse(options, arguments);

        fail("IllegalArgumentException expected");
    }

    @Test
    public void testParseEmptyValueAsValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", "empty", false, "an empty value");

        String[] arguments = {"--empty=\"\""};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testParseMultipleValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", "value", false, "an value");

        String[] arguments = {"--value=value1", "--value=value2"};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        String[] values = cmd.getOptionValues("a");
        assertEquals(2, values.length);
        assertTrue(values[0].equals("value1"));
        assertTrue(values[1].equals("value2"));
    }

    @Test
    public void testParseMultipleValuesWithQuotes() throws ParseException {
        Options options = new Options();
        options.addOption("a", "value", false, "an value");

        String[] arguments = {"--value=\"value1\"", "--value=\"value2\""};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        String[] values = cmd.getOptionValues("a");
        assertEquals(2, values.length);
        assertTrue(values[0].equals("value1"));
        assertTrue(values[1].equals("value2"));
    }

    @Test
    public void testParseMultipleValuesWithTrailingQuotes() throws ParseException {
        Options options = new Options();
        options.addOption("a", "value", false, "an value");

        String[] arguments = {"--value=\"value1\"", "--value=\"value2\""};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        String[] values = cmd.getOptionValues("a");
        assertEquals(2, values.length);
        assertTrue(values[0].equals("value1"));
        assertTrue(values[1].equals("value2"));
    }

    @Test
    public void testParseMultipleValuesWithQuotesAndTrailingQuotes() throws ParseException {
        Options options = new Options();
        options.addOption("a", "value", false, "an value");

        String[] arguments = {"--value=\"value1\"", "--value=\"value2\""};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        String[] values = cmd.getOptionValues("a");
        assertEquals(2, values.length);
        assertTrue(values[0].equals("value1"));
        assertTrue(values[1].equals("value2"));
    }

    @Test
    public void testParseMultipleValuesWithQuotesAndTrailingQuotesAndEmptyValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", "value", false, "an value");

        String[] arguments = {"--value=\"value1\"", "--value=\"value2\""};
        CommandLine cmd = new DefaultParser().parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        String[] values = cmd.getOptionValues("a");
        assertEquals(2, values.length);
        assertTrue(values[0].equals("value1"));
        assertTrue(values[1].equals("value2"));

        options = new Options();
        options.addOption("a", "value", false, "an value");

        String[] arguments2 = {"--value=\"value1\"", "--value=\"value2\""};
        cmd = new DefaultParser().parse(options, arguments2);

        assertTrue(cmd.hasOption("a"));
        values = cmd.getOptionValues("a");
        assertEquals(2, values.length);
        assertTrue(values[0].equals("value1"));
        assertTrue(values[1].equals("value2"));

        options.addOption("empty", "empty", false, "an empty value");

        String[] arguments3 = {"--value=\"value1\"", "--value=\"value2\"", "--empty"};
        cmd = new DefaultParser().parse(options, arguments3);

        assertTrue(cmd.hasOption("a"));
        values = cmd.getOptionValues("a");
        assertEquals(2, values.length);
        assertTrue(values[0].equals("value1"));
        assertTrue(values[1].equals("value2"));

        assertTrue(cmd.hasOption("empty"));
        assertNull(cmd.getOptionValue("empty"));
    }

    @Test(expected = IllegalStateException.class)
    public void testParseParseError() throws ParseException {
        Options options = new Options();
        options.addOption("a", "action", true, "an action");

        String[] arguments = {"--action"};
        new DefaultParser().parse(options, arguments);

        fail("ParseError expected");
    }
}