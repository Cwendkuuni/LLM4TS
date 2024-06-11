package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() {
        Options options = new Options();
        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, new String[0]);
            assertTrue(cmd.getArgs().isEmpty());
            assertNull(parser.getOptions());
            assertNull(parser.getRequiredOptions());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithSimpleOptions() {
        String[] args = {"-a", "1", "--b", "2"};
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");
        options.addOption("b", true, "An option with a boolean value");

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals(1, cmd.getOptionValue("a", "default").intValue());
            assertEquals(2, cmd.getOptionValue("b", "default").intValue());
            assertTrue(cmd.getArgs().isEmpty());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithArguments() {
        String[] args = {"arg1", "arg2", "arg3"};
        Options options = new Options();
        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            assertEquals(3, cmd.getArgs().size());
            assertEquals("arg1", cmd.getArgs().get(0));
            assertEquals("arg2", cmd.getArgs().get(1));
            assertEquals("arg3", cmd.getArgs().get(2));
            assertNull(parser.getOptions());
            assertNull(parser.getRequiredOptions());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionalOptions() {
        String[] args = {"-a", "value", "--b", "value2"};
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");
        options.addOption("b", true, "An option with a boolean value")
                .setValueSeparator('=')
                .required(false);

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("a"));
            assertEquals("value2", cmd.getOptionValue("b"));
            assertTrue(cmd.getArgs().isEmpty());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithRequiredOptions() {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value")
                .isRequired(true);
        options.addOption("b", true, "An option with a boolean value")
                .isRequired(true);

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            String missingOptions = e.getMessage();
            assertTrue(missingOptions.contains("A required option is missing"));
            assertTrue(missingOptions.contains("option a"));
            assertTrue(missingOptions.contains("option b"));
        }
    }

    @Test
    public void testParseWithProperties() {
        String[] args = {"--property1=value1", "--property2=value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        options.addOption("p", true, "property");
        options.registerParser("property", new StringParser());

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args, properties);
            assertEquals(2, cmd.getArgs().size());
            assertNull(parser.getOptions());
            assertNull(parser.getRequiredOptions());
            assertEquals(2, properties.size());
            assertEquals("value1", properties.getProperty("property1"));
            assertEquals("value2", properties.getProperty("property2"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithLongOptions() {
        String[] args = {"--long-option", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("longOption"));
            assertEquals("value", cmd.getOptionValue("longOption"));
            assertTrue(cmd.getArgs().isEmpty());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithGroupedOptions() {
        String[] args = {"--group", "-a", "1", "-b", "2"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Grouped options");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption(new Option("a", true, "Option A"));
        group.addOption(new Option("b", true, "Option B"));

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("group"));
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals(1, cmd.getOptionValue("a", "default").intValue());
            assertEquals(2, cmd.getOptionValue("b", "default").intValue());
            assertTrue(cmd.getArgs().isEmpty());
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithPositionalOptions() {
        String[] args = {"arg1", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("arg", true, "Position argument");
        options.addPositionalOption("arg", "The position argument");

        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            assertEquals(3, cmd.getArgs().size());
            assertEquals("arg1", cmd.getArgs().get(0));
            assertEquals("arg2", cmd.getArgs().get(1));
            assertEquals("arg3", cmd.getArgs().get(2));
            assertTrue(cmd.hasOption("arg"));
            assertEquals("arg3", cmd.getOptionValue("arg"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}