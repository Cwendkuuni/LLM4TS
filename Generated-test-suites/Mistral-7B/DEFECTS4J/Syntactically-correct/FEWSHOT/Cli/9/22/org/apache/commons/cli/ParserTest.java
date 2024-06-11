package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithValidArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", "argument", true, "An argument");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "value"};

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(1, cmd.getArgs().size());
        assertEquals("value", cmd.getArgs()[0]);
    }

    @Test
    public void testParseWithLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "A long option");
        Parser parser = new DefaultParser();
        String[] arguments = {"--longOption"};

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("longOption"));
    }

    @Test
    public void testParseWithMixedOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", "argumentA", true, "Argument A");
        options.addOption("b", "argumentB", true, "Argument B");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "valueA", "--b", "valueB"};

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(2, cmd.getArgs().size());
        assertEquals("valueA", cmd.getArgs()[0]);
        assertEquals("valueB", cmd.getArgs()[1]);
    }

    @Test
    public void testParseWithMultipleValuesForOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A with multiple values");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "value1", "-a", "value2"};

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(2, cmd.getArgs().size());
        List<String> values = cmd.getOptionValues("a");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", "requiredOption", true, "Required option");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "valueA"};

        try {
            parser.parse(options, arguments);
            fail("MissingRequiredOptionException expected");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: b", e.getMessage());
        }

        String[] argumentsWithRequiredOption = {"-a", "valueA", "-b"};
        CommandLine cmd = parser.parse(options, argumentsWithRequiredOption);
        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(2, cmd.getArgs().size());
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        String[] arguments = {"-x", "valueX"};

        try {
            parser.parse(options, arguments);
            fail("UnrecognizedOptionException expected");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: x", e.getMessage());
        }
    }

    @Test
    public void testParseWithValueFromProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("a", "valueFromProperties");

        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(1, cmd.getArgs().size());
        assertEquals("valueFromProperties", cmd.getArgs()[0]);
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group", "Option group");
        optionGroup.addOption("a", true, "Option A");
        optionGroup.addOption("b", true, "Option B");
        options.addOptionGroup(optionGroup);
        Parser parser = new DefaultParser();
        String[] arguments = {"--group", "-a"};

        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(0, cmd.getArgs().size());
        assertEquals("group", cmd.getOptionValues("group").get(0));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "valueA", "-b", "valueB"};

        CommandLine cmd = parser.parse(options, arguments, true);
        assertNotNull(cmd);
        assertFalse(cmd.getArgs().isEmpty());
        assertEquals(2, cmd.getArgs().size());
        assertEquals("valueA", cmd.getArgs()[0]);
        assertEquals("valueB", cmd.getArgs()[1]);
    }

    @Test
    public void testParseWithStopAtNonOptionAndUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "valueA", "-x", "valueX"};

        try {
            parser.parse(options, arguments, true);
            fail("UnrecognizedOptionException expected");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: x", e.getMessage());
        }
    }

    @Test
    public void testParseWithStopAtNonOptionAndMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", "requiredOption", true, "Required option");
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "valueA"};

        try {
            parser.parse(options, arguments, true);
            fail("MissingRequiredOptionException expected");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: b", e.getMessage());
        }
    }
}