package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testParseBasic() throws ParseException {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");
        options.addOption("b", "option-b", false, "Option B");
        DefaultParser parser = new DefaultParser();

        String[] args = new String[]{"-a", "arg-a"};
        CommandLine cmd = parser.parse(options, args, true);

        assertEquals(1, cmd.getArgs().length);
        assertEquals("arg-a", cmd.getArgs()[0]);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertEquals("Option A", cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithRequiredArgs() throws ParseException {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");
        options.addOption("b", "option-b", true, "Option B");
        DefaultParser parser = new DefaultParser();

        String[] args = new String[]{"-a", "arg-a", "-b", "arg-b"};
        CommandLine cmd = parser.parse(options, args, true);

        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg-a", cmd.getArgs()[0]);
        assertEquals("arg-b", cmd.getArgs()[1]);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("arg-a", cmd.getOptionValue("a"));
        assertEquals("arg-b", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredArg() throws ParseException {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");
        DefaultParser parser = new DefaultParser();

        String[] args = new String[]{"-a"};
        parser.parse(options, args, true);
        fail("Should throw ParseException for missing required arg");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingOptionalArg() throws ParseException {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");
        DefaultParser parser = new DefaultParser();

        String[] args = new String[]{"-a"};
        parser.parse(options, args, false);
        fail("Should throw MissingArgumentException for missing optional arg");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");
        DefaultParser parser = new DefaultParser();

        String[] args = new String[]{"-x", "arg-x"};
        parser.parse(options, args, true);
        fail("Should throw UnrecognizedOptionException for unrecognized option");
    }

    @Test
    public void testParseWithValueProperties() throws ParseException {
        Options options = new Options();
        options.addOptions(new Option[] {
                new Option("a", "option-a", true, "Option A")
                        .setLongOpt("long-option-a")
                        .setValueSeparator('=')
        });
        DefaultParser parser = new DefaultParser();

        String[] args = new String[]{"--long-option-a=arg-a"};
        CommandLine cmd = parser.parse(options, args, true);

        assertEquals(1, cmd.getArgs().length);
        assertEquals("arg-a", cmd.getArgs()[0]);
        assertTrue(cmd.hasOption("a"));
        assertEquals("arg-a", cmd.getOptionValue("option-a"));
        assertEquals("arg-a", cmd.getOptionValue("long-option-a"));
        assertNull(cmd.getOptionValue("option-b"));
    }

    @Test
    public void testParseWithOptionalGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Option Group");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption(new Option("a", "option-a", false, "Option A"));
        DefaultParser parser = new DefaultParser();

        String[] args = new String[]{"-a"};
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("a"));
        assertEquals("", cmd.getOptionValue("a"));
        assertFalse(group.isSelected());
    }

    @Test
    public void testParseWithSelectedGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Option Group");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption(new Option("a", "option-a", false, "Option A"));
        group.setSelected(new Option("a", "option-a", false, "Option A"));
        DefaultParser parser = new DefaultParser();

        String[] args = new String[0];
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("a"));
        assertEquals("", cmd.getOptionValue("a"));
        assertTrue(group.isSelected());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Option Group");
        group.setRequired(true);
        options.addOptionGroup(group);
        group.addOption(new Option("a", "option-a", false, "Option A"));
        DefaultParser parser = new DefaultParser();

        String[] args = new String[0];
        parser.parse(options, args, true);
        fail("Should throw MissingOptionException for missing required group");
    }

    @Test
    public void testParseHelp() throws ParseException {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");
        options.addHelp("help");
        DefaultParser parser = new DefaultParser();

        String[] args = new String[]{"--help"};
        CommandLine cmd = parser.parse(options, args, true);

        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("Test", options);
        assertEquals(help, cmd.getHelp());
    }
}