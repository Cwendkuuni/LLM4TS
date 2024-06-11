package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        String[] arguments = {"input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals("output.txt", cmd.getOptionValue("o"));
        assertNotNull(cmd.getOptionValue("output"));
        assertNull(cmd.getOptionValue("unknown"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("output", "output.txt");
        String[] arguments = {"input.txt", "--", "--output", "output.txt"};
        CommandLine cmd = parser.parse(options, arguments, properties);
        assertEquals("output.txt", cmd.getOptionValue("o"));
        assertNotNull(cmd.getOptionValue("output"));
        assertNull(cmd.getOptionValue("unknown"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        String[] arguments = {"input.txt", "-o", "output.txt", "--no-option", "arg"};
        CommandLine cmd = parser.parse(options, arguments, null, true);
        assertEquals(4, cmd.getArgs().size());
        assertEquals("--no-option", cmd.getArgs().get(2));
        assertNull(cmd.getOptionValue("unknown"));
    }

    @Test
    public void testParseWithOptionalArg() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        String[] arguments = {"input.txt", "-o", "output.txt", "--test"};
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertEquals(2, cmd.getArgs().size());
        assertNull(cmd.getOptionValue("output"));
        assertNull(cmd.getOptionValue("test"));
    }

    @Test
    public void testParseWithMultipleValues() throws ParseException {
        Options options = new Options();
        options.addOption("l", "list", true, "list of values");
        Parser parser = new DefaultParser();
        String[] arguments = {"input.txt", "--list", "val1,val2,val3"};
        CommandLine cmd = parser.parse(options, arguments, null, false);
        List values = cmd.getOptionValues("list");
        assertEquals(3, values.size());
        assertEquals("val1", values.get(0));
        assertEquals("val2", values.get(1));
        assertEquals("val3", values.get(2));
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        String[] arguments = {"input.txt", "-p", "output.txt"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments, null));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        String[] arguments = {"input.txt"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments, null));
    }

    @Test
    public void testParseWithMissingRequiredOptionInProperties() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        String[] arguments = {"input.txt", "--", "--output"};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments, properties));
    }

    @Test
    public void testParseWithInvalidArgument() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        String[] arguments = {"input.txt", "-o", "output;file.txt"};
        assertThrows(ParseException.class, () -> parser.parse(options, arguments, null));
    }

    @Test
    public void testParseWithInvalidArgumentInProperties() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        String[] arguments = {"input.txt", "--", "--output=output;file.txt"};
        assertThrows(ParseException.class, () -> parser.parse(options, arguments, properties));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group options");
        group.setRequired(true);
        group.addOption("g", "group", true, "group option");
        options.addOptionGroup(group);
        options.addRequiredOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        String[] arguments = {"input.txt", "--group", "value", "--output", "output.txt"};
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertTrue(group.isSelected());
        assertEquals("value", cmd.getOptionValue("group"));
        assertNotNull(cmd.getOptionValue("output"));
    }

    @Test
    public void testParseWithOptionGroupNotSelected() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group options");
        group.setRequired(false);
        group.addOption("g", "group", true, "group option");
        options.addOptionGroup(group);
        options.addRequiredOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        String[] arguments = {"input.txt", "--output", "output.txt"};
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertFalse(group.isSelected());
    }

    @Test
    public void testParseWithOptionGroupMixed() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group options");
        group.setRequired(true);
        group.addOption("g", "group", true, "group option");
        options.addOptionGroup(group);
        options.addRequiredOption("o", "output", true, "output file");
        Parser parser = new DefaultParser();
        String[] arguments = {"input.txt", "--group", "value", "--output", "output.txt", "--test"};
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertTrue(group.isSelected());
        assertEquals("value", cmd.getOptionValue("group"));
        assertNotNull(cmd.getOptionValue("output"));
        assertNull(cmd.getOptionValue("test"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Options options = new Options();
        options.addLongOption("long-option", true, "long option help");
        Parser parser = new DefaultParser();
        String[] arguments = {"input.txt", "--long-option", "value"};
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertEquals("value", cmd.getOptionValue("long-option"));
        assertNull(cmd.getOptionValue("output"));
    }
}