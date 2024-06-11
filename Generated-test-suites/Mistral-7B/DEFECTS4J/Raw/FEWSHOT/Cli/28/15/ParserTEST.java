package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with an argument");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "1", "-b", "2", "-c", "3"});

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.getOptionValue("a").equals("true"));
        assertEquals(2, cmd.getNumArgs());
        assertEquals("2", cmd.getArg(0));
        assertEquals("3", cmd.getArg(1));

        assertEquals(1, cmd.getNumOptions());
        assertEquals("Option A", cmd.getOptionNames()[0]);
        assertEquals(true, cmd.getOptionValues()[0].equals("true"));

        assertEquals(2, cmd.getNumOptions("b"));
        assertEquals("2", cmd.getOptionValues("b")[0]);
        assertEquals("3", cmd.getOptionValues("b")[1]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        Options options = new Options();
        options.addOption("a", 1, "Option A with an argument");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-a"});
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", false, "Option B");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-a"});
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.parse(options, new String[]{"-z"});
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Properties properties = new Properties();
        properties.setProperty("a", "true");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{}, properties);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.getOptionValue("a").equals("true"));
    }

    @Test
    public void testParseWithGroups() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(false);
        group.addOption("a", true, "Option A in Group");
        options.addOptionGroup(group);
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "--group", "-b"});

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(group.isSelected());

        OptionGroup selectedGroup = (OptionGroup) cmd.getOptionValue("group");
        assertEquals("group", selectedGroup.getLongOpt());
        assertTrue(selectedGroup.isSelected());
    }

    @Test
    public void testParseWithMixedOptions() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with an argument");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-a", "--b", "2", "-c", "3", "-a"});

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.getOptionValue("a").equals("true"));
        assertEquals(2, cmd.getNumArgs());
        assertEquals("2", cmd.getArg(0));
        assertEquals("3", cmd.getArg(1));

        assertEquals(3, cmd.getNumOptions());
        assertEquals("Option A", cmd.getOptionNames()[0]);
        assertEquals(true, cmd.getOptionValues()[0].equals("true"));

        assertEquals(2, cmd.getNumOptions("b"));
        assertEquals("2", cmd.getOptionValues("b")[0]);
        assertEquals("3", cmd.getOptionValues("b")[1]);

        assertEquals(1, cmd.getNumOptions("c"));
        assertEquals("3", cmd.getOptionValues("c")[0]);
    }
}