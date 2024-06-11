package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertNotNull(cmd);
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseSimple() throws ParseException {
        String[] args = {"-a", "1", "--b", "2"};
        Options options = new Options();
        Option aOption = new Option("a", "a").hasArg(true);
        Option bOption = new Option("b", "b").hasArg(true);
        options.addOption(aOption);
        options.addOption(bOption);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithGroups() throws ParseException {
        String[] args = {"--group", "-g", "1", "-a", "2", "--b", "3"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        Option gOption = new Option("g", "g");
        group.addOption(gOption);
        Option aOption = new Option("a", "a").hasArg(true);
        Option bOption = new Option("b", "b").hasArg(true);
        options.addOptionGroup(group);
        options.addOption(aOption);
        options.addOption(bOption);
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals(1, cmd.getOptions().size());
        Option g = cmd.getOption("group");
        assertTrue(g.isSelected());
        assertEquals("1", g.getValue());
        assertEquals(2, cmd.getArgs().size());
        assertEquals("2", cmd.getOptionValue("a"));
        assertEquals("3", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        String[] args = {"--foo", "1"};
        Options options = new Options();
        Option aOption = new Option("a", "a").hasArg(true);
        options.addOption(aOption);
        Parser parser = new DefaultParser();
        parser.parse(options, args);
        fail();
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] args = {"--group"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        Option gOption = new Option("g", "g");
        group.addOption(gOption);
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        parser.parse(options, args);
        fail();
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        String[] args = {"-a"};
        Options options = new Options();
        Option aOption = new Option("a", "a").hasArg(true);
        options.addOption(aOption);
        Parser parser = new DefaultParser();
        parser.parse(options, args);
        fail();
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnknownOption() throws ParseException {
        String[] args = {"--unknown"};
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.parse(options, args);
        fail();
    }
}