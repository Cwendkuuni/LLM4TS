package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineParser;
import org.apache.commons.cli2.builder.*;
import org.apache.commons.cli2.parser.DefaultParser;
import org.apache.commons.cli2.utils.Arguments;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OptionImplTest {
    private OptionParser parser;

    @Before
    public void setUp() {
        parser = new DefaultParser();
    }

    @Test
    public void testCreateOption() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .isRequired()
                .withDescription("this is an option")
                .create();

        assertEquals("o", option.getOpt());
        assertEquals("option-long", option.getLongOpt());
        assertEquals("this is an option", option.getDescription());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
    }

    @Test
    public void testCanProcess() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        final List<String> arguments = Arrays.asList("--option-long", "value");
        final ListIterator<String> argumentList = arguments.listIterator();

        assertFalse(option.canProcess(commandLine, argumentList));

        final WriteableCommandLine parsedCommandLine = parser.parse(commandLine, arguments);

        assertTrue(option.canProcess(parsedCommandLine, argumentList));
    }

    @Test
    public void testCanProcessWithNoArgument() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        final List<String> arguments = Arrays.asList("--option-long");
        final ListIterator<String> argumentList = arguments.listIterator();

        assertFalse(option.canProcess(commandLine, argumentList));

        final WriteableCommandLine parsedCommandLine = parser.parse(commandLine, arguments);

        assertTrue(option.canProcess(parsedCommandLine, argumentList));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcessWithMultipleArguments() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        final List<String> arguments = Arrays.asList("--option-long", "value1", "value2");
        final ListIterator<String> argumentList = arguments.listIterator();

        assertFalse(option.canProcess(commandLine, argumentList));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcessWithNoOption() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        final List<String> arguments = Arrays.asList("value");
        final ListIterator<String> argumentList = arguments.listIterator();

        assertFalse(option.canProcess(commandLine, argumentList));
    }

    @Test
    public void testFindOption() {
        final Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withShortOpt("o1")
                .create();

        final Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withShortOpt("o2")
                .create();

        final Option option3 = new OptionBuilder()
                .withLongOpt("option3")
                .withShortOpt("o3")
                .create();

        final List<Option> options = Arrays.asList(option1, option2, option3);

        assertEquals(option1, option1.findOption("o1"));
        assertEquals(option2, option2.findOption("o2"));
        assertEquals(option3, option3.findOption("o3"));
        assertNull(option1.findOption("o2"));
    }

    @Test
    public void testFindOptionWithLongOpt() {
        final Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withShortOpt("o1")
                .create();

        final Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withShortOpt("o2")
                .create();

        final Option option3 = new OptionBuilder()
                .withLongOpt("option3")
                .withShortOpt("o3")
                .create();

        final List<Option> options = Arrays.asList(option1, option2, option3);

        assertEquals(option1, option1.findOption("option1"));
        assertEquals(option2, option2.findOption("option2"));
        assertEquals(option3, option3.findOption("option3"));
        assertNull(option1.findOption("option4"));
    }

    @Test
    public void testEquals() {
        final Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withShortOpt("o1")
                .create();

        final Option option2 = new OptionBuilder()
                .withLongOpt("option1")
                .withShortOpt("o1")
                .create();

        final Option option3 = new OptionBuilder()
                .withLongOpt("option2")
                .withShortOpt("o2")
                .create();

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
    }

    @Test
    public void testHashCode() {
        final Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withShortOpt("o1")
                .create();

        final Option option2 = new OptionBuilder()
                .withLongOpt("option1")
                .withShortOpt("o1")
                .create();

        final Option option3 = new OptionBuilder()
                .withLongOpt("option2")
                .withShortOpt("o2")
                .create();

        assertEquals(option1.hashCode(), option1.hashCode());
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testToString() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        assertEquals("--option-long, -o", option.toString());
    }

    @Test
    public void testId() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        assertEquals(0, option.getId());
    }

    @Test
    public void testIsRequired() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        assertFalse(option.isRequired());

        final Option requiredOption = new OptionBuilder()
                .withLongOpt("required-option")
                .withShortOpt("r")
                .isRequired()
                .create();

        assertTrue(requiredOption.isRequired());
    }

    @Test
    public void testCheckPrefixesWithEmptyPrefixes() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        final Set<String> emptyPrefixes = Set.of();

        option.checkPrefixes(emptyPrefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidPrefix() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        final Set<String> invalidPrefixes = Set.of("invalid-prefix");

        option.checkPrefixes(invalidPrefixes);
    }

    @Test
    public void testCheckPrefixesWithValidPrefixes() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        final Set<String> validPrefixes = Set.of("o", "option");

        option.checkPrefixes(validPrefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidTrigger() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        final Set<String> prefixes = Set.of("invalid-trigger");

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithValidTrigger() {
        final Option option = new OptionBuilder()
                .withLongOpt("option-long")
                .withShortOpt("o")
                .create();

        final Set<String> prefixes = Set.of("o", "option");

        option.checkPrefixes(prefixes);
    }
}