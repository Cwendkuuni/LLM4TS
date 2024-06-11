package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer value");
        options.addOption("c", 3, "Option C with an integer value");
        options.addOption("d", "Default is \"DEFAULT\"");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOption(new Option("e", "e", true, "Option E in group1"));
        options.addOption(new Option("f", null, false, "Option F"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2"));
        options.addOption(new Option("g", "g", true, "Option G in group2"));

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a", "1", "-b", "2", "--group1", "-e", "--c", "3", "--group2", "-g"};
        CommandLine cmd = parser.parse(arguments);

        assertEquals(1, cmd.getArgs().length);
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals("3", cmd.getOptionValue("c"));
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("z"));
        assertEquals(true, cmd.getOptionValue("a", false));
        assertEquals(true, cmd.getOptionValue("e", false));
        assertEquals(true, cmd.getOptionValue("g", false));
        assertEquals("DEFAULT", cmd.getOptionValue("d"));
        assertNull(cmd.getOptionValue("f"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a"};
        parser.parse(arguments);
        fail("MissingOptionException expected");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgumentForOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A with an argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-a"};
        parser.parse(arguments);
        fail("MissingArgumentException expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {"-z"};
        parser.parse(arguments);
        fail("UnrecognizedOptionException expected");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "false");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = {};
        CommandLine cmd = parser.parse(arguments, properties);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.getOptionValue("a", false));
        assertFalse(cmd.getOptionValue("b", false));
        assertTrue(cmd.hasOption("a", properties));
        assertFalse(cmd.getOptionValue("b", properties));
    }
}