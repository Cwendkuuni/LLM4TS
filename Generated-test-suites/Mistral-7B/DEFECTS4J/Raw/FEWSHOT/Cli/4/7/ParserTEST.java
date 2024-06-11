package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");
        options.addOption("f", "foo", "Option F");
        options.addOption("g", null, "Option G", "group");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--a", "--b", "1", "--c=3", "--d=4", "--e=5", "--f=foo", "-g", "--group" };
        CommandLine commandLine = parser.parse(options, args);

        assertArrayEquals(new String[] { "--a", "--b", "1", "--c", "--d", "--e", "--f=foo" }, commandLine.getArgs());
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
        assertTrue(commandLine.hasOption("d"));
        assertTrue(commandLine.hasOption("e"));
        assertTrue(commandLine.hasOption("f"));
        assertSame(options.getOption("f"), commandLine.getOption("f"));
        assertFalse(commandLine.hasOption("g"));
        assertNull(commandLine.getOptionValue("g"));
        assertTrue(commandLine.getOptions().isRequired());
        assertSame(options.getOptionGroup("group"), commandLine.getOptionGroup());
        assertSame(options.getOption("group"), commandLine.getOption("group"));
        assertSame(options.getOption("group"), commandLine.getOption("g"));
    }

    @Test
    public void testParseWithHelp() throws ParseException {
        Options options = new Options();
        options.addOption(true, "help", "Print this help message.");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--help" };
        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("help"));
        assertTrue(commandLine.getValues("help").length > 0);
    }

    @Test
    public void testParseWithHelpAndOptions() throws ParseException {
        Options options = new Options();
        options.addOption(true, "help", "Print this help message.");
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--help", "--a" };
        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("help"));
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testParseWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--x" };

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithRequiredOptionMissing() {
        Options options = new Options();
        options.addOption(true, "required", "Required option");
        options.addOptions(Collections.singletonList(new OptionGroup("group", "Group options")));
        options.getOptionGroup("group").addOption(new Option("g", "group", true, "Group option"));

        Parser parser = new DefaultParser();

        String[] args = new String[0];

        assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithRequiredGroupOptionMissing() {
        Options options = new Options();
        options.addOption(true, "required", "Required option");
        options.addOptions(Collections.singletonList(new OptionGroup("group", "Group options")));
        options.getOptionGroup("group").addOption(new Option("g", "group", true, "Group option"));

        Parser parser = new DefaultParser();

        String[] args = new String[0];

        assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithOptionalGroupOptionMissing() {
        Options options = new Options();
        options.addOption(true, "optional", "Optional option");
        options.addOptions(Collections.singletonList(new OptionGroup("group", "Group options")));
        options.getOptionGroup("group").addOption(new Option("g", "group", false, "Group option"));

        Parser parser = new DefaultParser();

        String[] args = new String[0];

        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("optional"));
        assertFalse(commandLine.hasOption("g"));
    }

    @Test
    public void testParseWithOptionalGroupOptionPresent() {
        Options options = new Options();
        options.addOption(true, "optional", "Optional option");
        options.addOptions(Collections.singletonList(new OptionGroup("group", "Group options")));
        options.getOptionGroup("group").addOption(new Option("g", "group", false, "Group option"));

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--g" };

        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("optional"));
        assertTrue(commandLine.hasOption("g"));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--a", "--b=1" };
        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption("c"));
        assertEquals(1, commandLine.getValues("b").length);
        assertEquals("1", commandLine.getValues("b")[0]);
    }

    @Test
    public void testParseWithMultipleOptionsWithArg() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with arg");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--a", "--b=value" };
        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption("c"));
        assertEquals(1, commandLine.getValues("b").length);
        assertEquals("value", commandLine.getValues("b")[0]);
    }

    @Test
    public void testParseWithMultipleOptionsWithArgAndValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with arg");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--a", "--b=value1,value2" };
        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption("c"));
        assertEquals(2, commandLine.getValues("b").length);
        assertArrayEquals(new String[] { "value1", "value2" }, commandLine.getValues("b"));
    }

    @Test
    public void testParseWithOptionalOptionWithArg() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with arg");

        Parser parser = new DefaultParser();

        String[] args = new String[0];
        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertNull(commandLine.getOptionValue("b"));
    }

    @Test
    public void testParseWithOptionalOptionWithArgAndValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with arg");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--b=value" };
        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.isOptionIndicated("b"));
        assertEquals("value", commandLine.getOptionValue("b"));
    }

    @Test
    public void testParseWithOptionalOptionWithArgAndMultipleValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with arg");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--b=value1,value2" };
        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.isOptionIndicated("b"));
        assertArrayEquals(new String[] { "value1", "value2" }, commandLine.getValues("b"));
    }

    @Test
    public void testParseWithMixedOptionsAndValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with arg");
        options.addOption("c", null, "Option C with arg");
        options.addOption("d", null, "Option D with arg");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--a", "--b=value1", "--c=value2", "--d", "--d=value3" };
        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption("c"));
        assertFalse(commandLine.hasOption("d"));
        assertNull(commandLine.getOptionValue("c"));
        assertEquals(2, commandLine.getValues("d").length);
        assertArrayEquals(new String[] { "value3", "value3" }, commandLine.getValues("d"));
    }

    @Test
    public void testParseWithMixedOptionsAndValuesAndMissingArg() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with arg");
        options.addOption("c", null, "Option C with arg");
        options.addOption("d", null, "Option D with arg");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--a", "--b=value1", "--c=value2", "--d" };
        CommandLine commandLine = parser.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption("c"));
        assertFalse(commandLine.hasOption("d"));
        assertNull(commandLine.getOptionValue("c"));
        assertArrayEquals(new String[] { "value3" }, commandLine.getValues("d"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with arg");

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "value1");
        properties.setProperty("b", "value2");

        Parser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, null, properties);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.isOptionIndicated("b"));
        assertEquals(2, commandLine.getValues("b").length);
        assertArrayEquals(new String[] { "value1", "value2" }, commandLine.getValues("b"));
    }

    @Test
    public void testParseWithPropertiesAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with arg");

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "value1");

        Parser parser = new DefaultParser();

        String[] args = new String[] { "--c=value3" };
        CommandLine commandLine = parser.parse(options, args, properties);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.isOptionIndicated("b"));
        assertEquals(1, commandLine.getValues("b").length);
        assertEquals("value1", commandLine.getValues("b")[0]);
        assertEquals("value3", commandLine.getOptionValue("c"));
    }
}