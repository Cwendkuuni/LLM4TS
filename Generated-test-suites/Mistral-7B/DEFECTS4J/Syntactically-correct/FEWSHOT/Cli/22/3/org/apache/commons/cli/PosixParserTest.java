package org.apache.commons.cli;

import static org.junit.Assert. assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        String[] expected = new String[0];

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] arguments = { "-a", "--b=1", "--c", "d", "--e=foo" };
        PosixParser parser = new PosixParser();
        String[] expected = { "-a", "--b", "1", "--c", "d", "--e", "foo" };

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "a help");
        String[] arguments = { "-a", "foo", "--", "bar", "-b" };
        PosixParser parser = new PosixParser();
        String[] expected = { "--", "bar", "-b" };

        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "long option help");
        String[] arguments = { "--longOption" };
        PosixParser parser = new PosixParser();
        String[] expected = { "--longOption" };

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "long option help");
        String[] arguments = { "--longOption=value" };
        PosixParser parser = new PosixParser();
        String[] expected = { "--longOption", "value" };

        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenInvalidLongOption() {
        Options options = new Options();
        String[] arguments = { "--invalidOption" };
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);

        assertTrue(parser.eatTheRest);

        List tokens = parser.tokens;
        String[] expected = { "--", "invalidOption" };
        assertArrayEquals(expected.toString().split(","), tokens.toArray(new Object[0]));
    }

    @Test
    public void testFlattenInvalidLongOptionWithValue() {
        Options options = new Options();
        String[] arguments = { "--invalidOption=value" };
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);

        assertTrue(parser.eatTheRest);

        List tokens = parser.tokens;
        String[] expected = { "--", "invalidOption", "value" };
        assertArrayEquals(expected.toString().split(","), tokens.toArray(new Object[0]));
    }

    @Test
    public void testFlattenInvalidShortOption() {
        Options options = new Options();
        String[] arguments = { "-z" };
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);

        assertFalse(parser.eatTheRest);

        List tokens = parser.tokens;
        String[] expected = { "-z" };
        assertArrayEquals(expected, tokens.toArray(new Object[0]));
    }

    @Test
    public void testFlattenInvalidShortOptionWithValue() {
        Options options = new Options();
        String[] arguments = { "-z=value" };
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);

        assertTrue(parser.eatTheRest);

        List tokens = parser.tokens;
        String[] expected = { "--", "z", "value" };
        assertArrayEquals(expected.toString().split(","), tokens.toArray(new Object[0]));
    }

    @Test
    public void testFlattenValidShortOptionWithValue() {
        Options options = new Options();
        options.addOption("z", true, "z option help");
        String[] arguments = { "-z=value" };
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = { "-z", "value" };
        assertArrayEquals(expected, result);
    }
}