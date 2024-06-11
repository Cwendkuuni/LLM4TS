package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class PosixParserTest {

    @Test
    public void testFlattenBasic() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "bar", "--", "arg1", "arg2", "-x", "--baz"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("baz", null, "b");

        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--foo", "bar", "--", "-x", "--baz"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "bar", "--", "arg1", "arg2", "-x", "--baz", "-z"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("baz", null, "b");

        String[] result = parser.flatten(options, args, true);
        String[] expected = {"--foo", "bar", "--", "arg1", "arg2", "-x", "--baz", "-z"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"foo"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        String[] result = parser.flatten(options, args, false);
        String[] expected = {"foo"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo=bar"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--foo", "bar"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValueAndArgument() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo=bar baz"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.getOption("foo").hasArg(true);

        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--foo", "bar", "baz"};
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalid"};
        Options options = new Options();

        parser.flatten(options, args, false);
        fail("Expected ParseException but it didn't occur.");
    }

    @Test
    public void testFlattenInvalidOptionWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalid", "arg1", "arg2"};
        Options options = new Options();

        String[] result = parser.flatten(options, args, true);
        String[] expected = {"--invalid", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertFalse(parser.eatTheRest);
        assertNull(parser.tokens);
        assertNull(parser.currentOption);
    }
}