package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] arguments = {};

        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseSingleOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "An option");
        Parser parser = new DefaultParser();
        String[] arguments = {"-o"};

        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o"));
        assertFalse(cmd.hasOption("n"));
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseSingleValueOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with value");
        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "value"};

        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o"));
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseMultipleOptions() {
        Options options = new Options();
        options.addOption("o", "option1", false, "Option 1");
        options.addOption("n", "option2", false, "Option 2");
        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "-n"};

        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.hasOption("n"));
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseMultipleValuesOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with value");
        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "value1", "-o", "value2"};

        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o"));
        List<String> values = cmd.getValues("o");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseMultipleArgs() {
        Options options = new Options();
        options.addOption("o", "option", false, "An option");
        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "arg1", "arg2"};

        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o"));
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test
    public void testParseRequiredOptionMissing() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option");
        options.addRequiredOption("r", "requiredOption", true, "A required option");
        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "value"};

        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: requiredOption", e.getMessage());
        }
    }

    @Test
    public void testParseOptionalGroupRequiredOptionMissing() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("g", "groupOption", false, "A group option"));
        group.addOption(new Option("r", "requiredOption", true, "A required option in a group"));
        options.addOptionGroup(group);
        options.addOption("o", "option", false, "An option");

        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "value"};

        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: requiredOption", e.getMessage());
        }
    }

    @Test
    public void testParseOptionalGroupOptionalOptionMissing() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.addOption(new Option("g", "groupOption", false, "A group option"));
        group.addOption(new Option("o", "option", true, "An option in a group"));
        options.addOptionGroup(group);
        options.addOption("o", "option2", false, "Another option");

        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "value"};

        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o"));
        assertEquals("value", cmd.getOptionValue("o2")); // the second option "o" is used
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseProperties() {
        Options options = new Options();
        options.addOption("p", "property", true, "A property");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("p", "value");

        CommandLine cmd = parser.parse(options, (String[]) null, properties);
        assertTrue(cmd.hasOption("p"));
        assertEquals("value", cmd.getOptionValue("p"));
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseInvalidOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "An option");
        Parser parser = new DefaultParser();
        String[] arguments = {"-i", "invalidOption"};

        try {
            parser.parse(options, arguments);
            fail();
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: invalidOption", e.getMessage());
        }
    }

    @Test
    public void testParseInvalidOptionValue() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with value");
        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "invalidValue"};

        try {
            parser.parse(options, arguments);
            fail();
        } catch (ParseException e) {
            assertEquals("The option 'o' does not accept the value 'invalidValue'", e.getMessage());
        }
    }

    @Test
    public void testParseMissingValueOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "An option with value");
        Parser parser = new DefaultParser();
        String[] arguments = {"-o"};

        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingArgumentException e) {
            assertEquals("Missing required long option 'o' with value", e.getMessage());
        }
    }

    @Test
    public void testParseMissingValueOptionInGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.addOption(new Option("g", "groupOption", false, "A group option"));
        group.addOption(new Option("o", "option", true, "An option in a group with value"));
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        String[] arguments = {"-g"};

        try {
            parser.parse(options, arguments);
            fail();
        } catch (MissingArgumentException e) {
            assertEquals("Missing required long option 'o' with value", e.getMessage());
        }
    }

    @Test
    public void testParseUnmatchedArguments() {
        Options options = new Options();
        options.addOption("o", "option", false, "An option");
        Parser parser = new DefaultParser();
        String[] arguments = {"-o", "arg1", "arg2", "arg3"};

        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("o"));
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg2", cmd.getArgs().get(0));
        assertEquals("arg3", cmd.getArgs().get(1));
    }

    @Test
    public void testParseUnmatchedArgumentsInGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.addOption(new Option("g", "groupOption", false, "A group option"));
        group.addOption(new Option("o", "option", true, "An option in a group with value"));
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        String[] arguments = {"-g"};

        CommandLine cmd = parser.parse(options, arguments);
        assertFalse(cmd.hasOption("o"));
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0)); // arg1 is the first argument after the group option
    }
}