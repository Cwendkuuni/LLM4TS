package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserTest {

    private static final String[] ARGUMENTS = {"-a", "--option", "-b", "--option2", "-c", "--option3", "--"};
    private static final String[] ARGUMENTS_WITH_LONG_OPTIONS = {"--option", "--another-option", "--option3"};
    private static final String[] ARGUMENTS_WITH_SHORT_OPTIONS = {"-a", "-b", "-c"};
    private static final String[] ARGUMENTS_WITH_REQUIRED_OPTIONS = {"--option1", "--option2"};
    private static final String[] ARGUMENTS_WITH_MISSING_OPTIONS = {"--option3", "-x"};

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", true, "Option C");
        options.addOption("d", "optionD", false, "Option D");
        options.addOption("e", "optionE", false, "Option E");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, ARGUMENTS, null, false);

        assertEquals(3, commandLine.getArgs().length);
        assertEquals("--option", commandLine.getOption("option"));
        assertEquals("Option A", commandLine.getOptionValue("optionA"));
        assertSame(commandLine.getOption("optionB"), options.getOption("b"));
        assertNull(commandLine.getOptionValue("optionB"));
        assertSame(commandLine.getOption("optionC"), options.getOption("c"));
        assertTrue(commandLine.getOption("optionC").isSet());
        assertFalse(commandLine.getOption("optionD").isSet());
        assertFalse(commandLine.getOption("optionE").isSet());
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        options.addOption("c", "optionC", true, "Option C");

        List<Option> requiredOptions = Arrays.asList(
                options.getOption("optionA"),
                options.getOption("optionB")
        );

        Parser parser = new DefaultParser();
        parser.parse(options, ARGUMENTS_WITH_MISSING_OPTIONS, null, false);
        throw new AssertionError("ParseException expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", true, "Option C");

        Parser parser = new DefaultParser();
        parser.parse(options, ARGUMENTS, null, false);

        parser = new DefaultParser();
        parser.parse(options, Collections.singletonList("--unknownOption"), null, false);
        throw new AssertionError("UnrecognizedOptionException expected");
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("option", true, "o", "Option");
        options.addOption("another-option", false, null, "Another option");
        options.addOption("option3", false, null, "Option 3");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, ARGUMENTS_WITH_LONG_OPTIONS, null, false);

        assertEquals(3, commandLine.getArgs().length);
        assertEquals("Option", commandLine.getOptionValue("option"));
        assertNull(commandLine.getOptionValue("another-option"));
        assertTrue(commandLine.hasOption("option3"));
    }

    @Test
    public void testParseWithShortOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "a", "Option A");
        options.addOption("b", false, "b", "Option B");
        options.addOption("c", true, "c", "Option C");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, ARGUMENTS_WITH_SHORT_OPTIONS, null, false);

        assertEquals(0, commandLine.getArgs().length);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, null, "Option 1");
        options.addOption("option2", true, null, "Option 2");

        List<Option> requiredOptions = Arrays.asList(
                options.getOption("option1"),
                options.getOption("option2")
        );

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, ARGUMENTS_WITH_REQUIRED_OPTIONS, null, false);

        assertEquals(0, commandLine.getArgs().length);
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, null, "Option 1");
        options.addOption("option2", true, null, "Option 2");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, ARGUMENTS, null, false);

        throw new AssertionError("MissingOptionException expected");
    }

    @Test
    public void testParseWithOptionalGroup() throws ParseException {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group");
        optionGroup.setRequired(false);
        optionGroup.addOption("g1", false, null, "Option 1");
        optionGroup.addOption("g2", false, null, "Option 2");
        options.addOptionGroup(optionGroup);
        options.addOption("o", false, null, "Option");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, ARGUMENTS, null, false);

        assertTrue(commandLine.hasOption("g1"));
        assertTrue(commandLine.hasOption("g2"));
        assertTrue(commandLine.hasOption("o"));
        assertFalse(optionGroup.isSelected());
    }

    @Test
    public void testParseWithSelectedGroup() throws ParseException {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group");
        optionGroup.setRequired(false);
        optionGroup.addOption("g1", false, null, "Option 1");
        optionGroup.addOption("g2", false, null, "Option 2");
        options.addOptionGroup(optionGroup);
        options.getOption("g1").setSelected(true);

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, ARGUMENTS, null, false);

        assertTrue(commandLine.hasOption("g1"));
        assertFalse(commandLine.hasOption("g2"));
        assertTrue(optionGroup.isSelected());
    }

    @Test
    public void testParseWithMixedOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, null, "Option A");
        options.addOption("b", false, null, "Option B");
        options.addOption("c", true, null, "Option C");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, ARGUMENTS, null, false);

        assertEquals(3, commandLine.getArgs().length);
        assertEquals("--option", commandLine.getOption("option").getValue());
        assertEquals("Option A", commandLine.getOptionValue("optionA"));
        assertNull(commandLine.getOptionValue("optionB"));
        assertTrue(commandLine.getOption("optionC").isSet());
    }
}