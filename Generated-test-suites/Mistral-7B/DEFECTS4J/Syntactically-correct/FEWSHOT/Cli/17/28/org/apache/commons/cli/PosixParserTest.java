package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "-" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "--" }, result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption(new Option("a", null, false, "An option")).setRequired(false);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "-a" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "-a" }, result);
    }

    @Test
    public void testFlattenSingleOptionWithValue() {
        Options options = new Options();
        options.addOption(new Option("a", true, false, "An option with value")).setRequired(false);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "-a", "value" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "-a", "value" }, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption(new Option("a", null, false, "An option A")).setRequired(false);
        options.addOption(new Option("b", null, false, "An option B")).setRequired(false);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "-a", "-b" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "-a", "-b" }, result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption(new Option("long-option", null, false, "A long option")).setRequired(false);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "--long-option" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "--long-option" }, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption(new Option("long-option", true, false, "A long option with value")).setRequired(false);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "--long-option", "value" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "--long-option", "value" }, result);
    }

    @Test
    public void testFlattenLongOptionAndSingleShortOption() {
        Options options = new Options();
        options.addOption(new Option("a", null, false, "An option A")).setRequired(false);
        options.addOption(new Option("long-option", true, false, "A long option")).setRequired(false);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "--long-option", "-a" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "--long-option", "-a" }, result);
    }

    @Test
    public void testFlattenLongOptionAndMultipleShortOptions() {
        Options options = new Options();
        options.addOption(new Option("a", null, false, "An option A")).setRequired(false);
        options.addOption(new Option("b", null, false, "An option B")).setRequired(false);
        options.addOption(new Option("long-option", true, false, "A long option")).setRequired(false);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "--long-option", "-a", "-b" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "--long-option", "-a", "-b" }, result);
    }

    @Test
    public void testFlattenGroupOption() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "A group");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption(new Option("a", null, false, "An option A")).setRequired(false);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "--group", "-a" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "--group", "-a" }, result);
    }

    @Test
    public void testFlattenLongOptionWithArgument() {
        Options options = new Options();
        Option longOption = new Option("long-option", true, true, "A long option with argument");
        longOption.setArgName("ARG");
        options.addOption(longOption);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "--long-option=ARG1", "--long-option=ARG2" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "--long-option", "ARG1", "--long-option", "ARG2" }, result);
    }

    @Test
    public void testFlattenLongOptionWithArgumentAndSingleShortOption() {
        Options options = new Options();
        Option longOption = new Option("long-option", true, true, "A long option with argument");
        longOption.setArgName("ARG");
        options.addOption(longOption);
        options.addOption(new Option("a", null, false, "An option A")).setRequired(false);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "--long-option=ARG1", "-a" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "--long-option", "ARG1", "-a" }, result);
    }

    @Test
    public void testFlattenLongOptionWithArgumentAndMultipleShortOptions() {
        Options options = new Options();
        Option longOption = new Option("long-option", true, true, "A long option with argument");
        longOption.setArgName("ARG");
        options.addOption(longOption);
        options.addOption(new Option("a", null, false, "An option A")).setRequired(false);
        options.addOption(new Option("b", null, false, "An option B")).setRequired(false);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "--long-option=ARG1", "-a", "-b" };

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[] { "--long-option", "ARG1", "-a", "-b" }, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a", null, false, "An option A")).setRequired(false);
        PosixParser parser = new PosixParser();
        String[] arguments = new String[] { "-a", "non-option" };

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(new String[] { "-a", "non-option" }, result);
    }

    @Test
    public void testBurstTokenSingleCharacter() {
        PosixParser parser = new PosixParser();
        String result = parser.burstToken("-a", false);

        assertEquals(Arrays.asList("-a").toString(), result);
    }

    @Test
    public void testBurstTokenMultipleCharactersValidOption() {
        PosixParser parser = new PosixParser();
        Option option = new Option("a", "opt", true, "An option with argument");
        String result = parser.burstToken("-aoptARG", false);

        List expected = Arrays.asList("-a", "opt", "ARG");
        assertEquals(expected.toString(), result);
    }

    @Test
    public void testBurstTokenMultipleCharactersInvalidOption() {
        PosixParser parser = new PosixParser();
        String result = parser.burstToken("-nonoptionARG", false);

        assertEquals(Arrays.asList("--", "nonoption", "ARG").toString(), result);
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String result = parser.burstToken("-a", true);

        assertEquals(Arrays.asList("--", "a").toString(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenNullToken() {
        PosixParser parser = new PosixParser();
        String result = parser.burstToken(null, false);
    }
}