package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ParserTest {

    // Helper method to create an Options object with three options
    private Options createOptions() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");
        return options;
    }

    @Test
    public void testParseWithoutOptions() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {};
        CommandLine cmd = parser.parse(createOptions(), args);

        assertTrue(cmd.getArgList().isEmpty());
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"-a"};
        CommandLine cmd = parser.parse(createOptions(), args);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertTrue(cmd.getArgList().isEmpty());
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"-a", "-b", "value", "arg1", "arg2"};
        CommandLine cmd = parser.parse(createOptions(), args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertEquals("value", cmd.getOptionValue("b"));
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }

    @Test
    public void testParseWithMissingValue() {
        Parser parser = new DefaultParser();
        String[] args = {"-b"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(createOptions(), args);
        } catch (ParseException e) {
            assertEquals("Missing argument for option: b", e.getMessage());
        }
        assertNull(cmd);
    }

    @Test
    public void testParseWithUnknownOption() {
        Parser parser = new DefaultParser();
        String[] args = {"-d"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(createOptions(), args);
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -d", e.getMessage());
            assertEquals("-d", e.getOption());
        } catch (ParseException e) {
            // Ignore other exceptions
        }
        assertNull(cmd);
    }

    @Test
    public void testParseWithDuplicateOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] args = {"-a", "-a"};
        CommandLine cmd = parser.parse(createOptions(), args);

        assertTrue(cmd.hasOption("a"));
        assertEquals(2, cmd.getOptions().getOption("a").getValues().length);
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        Options options = createOptions();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("x", false, "option x"));
        group.addOption(new Option("y", false, "option y"));
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        String[] args = {"-x"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
    }
}