package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};

        String[] expectedTokens = {};
        String[] actualTokens = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"foo", "bar", "baz"};

        String[] expectedTokens = {"foo", "bar", "baz"};
        String[] actualTokens = parser.flatten(options, args, false);
        assertArrayEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testFlattenWithOptions() {
        Options options = new Options();
        options.addOption("o", "opt", false, "Option with no argument");
        options.addOption("a", "arg", true, "Option with argument");

        PosixParser parser = new PosixParser();
        String[] args = {"-o", "val1", "--arg=val2", "-a", "val3"};

        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("-o");
        expectedTokens.add("val1");
        expectedTokens.add("--arg");
        expectedTokens.add("val2");
        expectedTokens.add("-a");
        expectedTokens.add("val3");

        List<String> actualTokens = new ArrayList<>(Arrays.asList(parser.flatten(options, args, false)));
        assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), actualTokens.toArray(new String[expectedTokens.size()]));
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("l", "long-option", false, "Long option");

        PosixParser parser = new PosixParser();
        String[] args = {"--long-option"};

        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("--long-option");
        expectedTokens.add("long-option");

        List<String> actualTokens = new ArrayList<>(Arrays.asList(parser.flatten(options, args, false)));
        assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), actualTokens.toArray(new String[expectedTokens.size()]));
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        Options options = new Options();
        options.addOption("l", "long-option", true, "Long option with value");

        PosixParser parser = new PosixParser();
        String[] args = {"--long-option=value"};

        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("--long-option");
        expectedTokens.add("value");

        List<String> actualTokens = new ArrayList<>(Arrays.asList(parser.flatten(options, args, false)));
        assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), actualTokens.toArray(new String[expectedTokens.size()]));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "opt", false, "Option with no argument");
        options.addOption("a", "arg", true, "Option with argument");

        PosixParser parser = new PosixParser();
        String[] args = {"foo", "-o", "val1", "--arg=val2", "bar"};

        String[] expectedTokens = {"foo", "-o", "val1", "--arg", "val2", "bar"};
        String[] actualTokens = parser.flatten(options, args, true);
        assertArrayEquals(expectedTokens, actualTokens);
    }
}