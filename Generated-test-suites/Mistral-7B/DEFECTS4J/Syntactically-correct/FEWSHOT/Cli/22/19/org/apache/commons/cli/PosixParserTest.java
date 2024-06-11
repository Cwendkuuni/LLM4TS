package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        String[] arguments = new String[] { "--foo", "-a", "-b", "-c", "--", "-d", "e", "f" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(new String[] { "--foo", "-a", "-b", "-c--", "-d", "e", "f" });
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[] { "--foo", "-a", "-b", "--", "-c", "d" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        List<String> expected = Arrays.asList(new String[] { "--foo", "-a", "--", "-c", "d" });
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("foo", true, "f");

        String[] arguments = new String[] { "--foo", "true", "-a", "-b" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(new String[] { "--foo=true", "-a", "-b" });
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");

        String[] arguments = new String[] { "--foo", "true", "--bar", "testing" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(new String[] { "--foo=true", "--bar=testing" });
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenNoOption() {
        Options options = new Options();

        String[] arguments = new String[] { "arg1", "arg2" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(new String[] { "--", "arg1", "arg2" });
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstTokenSingleOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");

        String[] arguments = new String[] { "-f" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false)[0];

        assertEquals("-foo", result);
    }

    @Test
    public void testBurstTokenLongOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");

        String[] arguments = new String[] { "--foo" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false)[0];

        assertEquals("--foo", result);
    }

    @Test
    public void testBurstTokenLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");

        String[] arguments = new String[] { "--foo=arg1", "--bar=arg2" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false)[0];

        assertEquals("--foo=arg1", result);
    }

    @Test
    public void testBurstTokenLongOptionWithArgumentAndValue() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");

        String[] arguments = new String[] { "--foo=arg1", "--bar=arg2=val" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false)[0];

        assertEquals("--foo=arg1", result);
    }

    @Test
    public void testBurstTokenInvalidOption() {
        Options options = new Options();

        String[] arguments = new String[] { "-x" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false)[0];

        assertNull(result);
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        Options options = new Options();

        String[] arguments = new String[] { "-a", "-b", "-" };

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true)[0];

        assertEquals("--", result);
    }
}