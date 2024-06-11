package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] expected = {};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        options.addOption("a", true, "Help");
        options.addOption("b", 1, "Number");

        String[] arguments = {"-a", "-b", "3"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "-b", "3"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Help");

        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--longOption"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "Help");

        String[] arguments = {"--longOption", "Value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--longOption", "Value"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenShortOption() {
        Options options = new Options();
        options.addOption("a", true, "Help");

        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenShortOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "Help");

        String[] arguments = {"-a", "Value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "Value"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenShortOptionWithTwoLetters() {
        Options options = new Options();
        options.addOption("aa", true, "Help");

        String[] arguments = {"-aa"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-aa"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenShortOptionWithTwoLettersAndValue() {
        Options options = new Options();
        options.addOption("aa", true, "Help");

        String[] arguments = {"-aa", "Value"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-aa", "Value"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenShortOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "Help");
        options.addOption("b", 1, "Number");

        String[] arguments = {"-a", "Value1", "-b", "3"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "Value1", "-b", "3"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("longOption", true, "Help");

        String[] arguments = {"--longOption", "Value1"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--longOption", "Value1"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenShortOptionAndLongOption() {
        Options options = new Options();
        options.addOption("a", true, "Help");
        options.addOption("longOption", true, "Help");

        String[] arguments = {"-a", "--longOption"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "--longOption"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOptionWithTwoValues() {
        Options options = new Options();
        options.addOption("longOption", true, "Help");

        String[] arguments = {"--longOption", "Value1", "--longOption", "Value2"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--longOption", "Value1", "--longOption", "Value2"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenShortOptionAndLongOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "Help");
        options.addOption("longOption", true, "Help");

        String[] arguments = {"-a", "Value1", "--longOption", "Value2"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "Value1", "--longOption", "Value2"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOptionWithArgumentAndValue() {
        Options options = new Options();
        options.addOption("longOption", true, "Help");
        options.addOption("longOption2", 1, "Number");

        String[] arguments = {"--longOption", "Value1", "--longOption2", "2"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--longOption", "Value1", "--longOption2", "2"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Help");

        String[] arguments = {"-a", "Value1", "--nonOption"};
        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "Value1"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOptionWithArgumentAndValueAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Help");
        options.addOption("longOption2", 1, "Number");

        String[] arguments = {"--longOption", "Value1", "--nonOption"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--longOption", "Value1"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithEmptyOption() {
        Options options = new Options();
        options.addOption("emptyOption", true, "Help");

        String[] arguments = {"--emptyOption"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--emptyOption"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);

        String[] emptyArguments = {};
        actual = parser.flatten(options, emptyArguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithEmptyOptionAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("emptyOption", true, "Help");

        String[] arguments = {"--emptyOption", "--nonOption"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--emptyOption"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);

        String[] emptyArguments = {};
        actual = parser.flatten(options, emptyArguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithMultiWordOption() {
        Options options = new Options();
        options.addOption("multiWordOption", true, "Help");

        String[] arguments = {"--multiWordOption", "oneWord", "--multiWordOption", "anotherWord"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--multiWordOption", "oneWord", "--multiWordOption", "anotherWord"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithMultiWordOptionAndValue() {
        Options options = new Options();
        options.addOption("multiWordOption", true, "Help");

        String[] arguments = {"--multiWordOption", "oneWordValue", "--multiWordOption", "anotherWordValue"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--multiWordOption", "oneWordValue", "--multiWordOption", "anotherWordValue"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithMultiWordOptionAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("multiWordOption", true, "Help");

        String[] arguments = {"--multiWordOption", "oneWordValue", "--nonOption"};
        PosixParser parser = new PosixParser();
        String[] expected = {"--multiWordOption", "oneWordValue"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);

        String[] emptyArguments = {};
        actual = parser.flatten(options, emptyArguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("aa", true, "Help");
        options.addOption("bb", 1, "Number");
        options.addOption("cc", 1, "Number");

        PosixParser parser = new PosixParser();

        String[] input = {"-aaa", "1", "-bb", "2", "-cc", "3"};
        List<String> output = Arrays.asList(parser.flatten(options, input, false));

        List<String> expected = Arrays.asList("-aa", "-a", "1", "-bb", "2", "-cc", "3");

        assertEquals(expected, output);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("aa", true, "Help");
        options.addOption("bb", 1, "Number");
        options.addOption("cc", 1, "Number");

        PosixParser parser = new PosixParser();

        String[] input = {"-aaa", "1", "--nonOption"};
        List<String> output = Arrays.asList(parser.flatten(options, input, true));

        List<String> expected = Arrays.asList("-aa", "-a", "1");

        assertEquals(expected, output);

        String[] emptyArguments = {};
        output = Arrays.asList(parser.flatten(options, emptyArguments, true));

        assertEquals(Arrays.asList(), output);
    }
}