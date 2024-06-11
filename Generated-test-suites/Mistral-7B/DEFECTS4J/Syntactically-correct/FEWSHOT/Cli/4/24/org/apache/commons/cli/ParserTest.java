package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParseWithEmptyArgs() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithValidArgs() throws ParseException {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "test.txt"});
        assertEquals(1, cmd.getArgs().size());
        assertEquals("test.txt", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithRequiredArgs() throws ParseException {
        Options options = new Options();
        options.addOption("i", "input", true, "input file").required(true);
        Parser parser = new DefaultParser();
        try {
            parser.parse(options, new String[0]);
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Missing required option 'i'", e.getMessage());
        }
    }

    @Test
    public void testParseWithValidAndOptionalArgs() throws ParseException {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "test.txt", "-o", "output.txt"});
        assertEquals(2, cmd.getArgs().size());
        assertEquals("test.txt", cmd.getArgs().get(0));
        assertEquals("output.txt", cmd.getArgs().get(1));
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");
        Parser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{"-x", "test.txt"});
            fail();
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -x", e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionGroups() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group options");
        group.setRequired(true);
        options.addOptionGroup(group);
        group.addOption("i", "input", true, "input file");
        group.addOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--group"});
        assertTrue(group.isSelected());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptionInGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group options");
        group.setRequired(true);
        options.addOptionGroup(group);
        group.addOption("i", "input", true, "input file");
        Parser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test
    public void testParseWithValidArgsAndProperties() throws ParseException {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("input", "test.txt");
        CommandLine cmd = parser.parse(options, new String[0], properties);
        assertEquals(0, cmd.getArgs().size());
        assertEquals("test.txt", cmd.getOptionValues("i")[0]);
    }
}