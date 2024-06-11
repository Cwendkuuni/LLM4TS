package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        String[] args = new String[]{"--foo", "-h", "--bar=baz", "-a", "-b"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("help", false, "h");
        options.addOption("a", false, "a");

        String[] result = new PosixParser().flatten(options, args, false);

        List<String> expected = Arrays.asList("--foo", "-h", "--bar", "baz", "-a", "-b");
        assertEquals(expected.size(), result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = new String[]{"--foo", "-h", "--bar=baz", "-a", "-b", "arg"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("help", false, "h");
        options.addOption("a", false, "a");

        String[] result = new PosixParser().flatten(options, args, true);

        List<String> expected = Arrays.asList("--foo", "-h", "--bar", "baz", "-a", "-b", "arg");
        assertEquals(expected.size(), result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
    }

    @Test
    public void testFlattenEmptyArgs() {
        String[] args = new String[0];
        Options options = new Options();
        String[] result = new PosixParser().flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testFlattenNullArgs() {
        String[] args = null;
        Options options = new Options();
        String[] result = new PosixParser().flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testFlattenNoOptions() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        Options options = new Options();
        String[] result = new PosixParser().flatten(options, args, false);
        List<String> expected = Arrays.asList("arg1", "arg2", "arg3");
        assertEquals(expected.size(), result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
    }

    @Test
    public void testFlattenLongOptionsWithoutArg() {
        String[] args = new String[]{"--longOption1", "--longOption2"};
        Options options = new Options();
        options.addOption("longOption1", true, "l");
        options.addOption("longOption2", true, "o");
        String[] result = new PosixParser().flatten(options, args, false);
        List<String> expected = Arrays.asList("--longOption1", "--longOption2");
        assertEquals(expected.size(), result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
    }

    @Test
    public void testFlattenLongOptionsWithArg() {
        String[] args = new String[]{"--longOption1=arg1", "--longOption2=arg2"};
        Options options = new Options();
        options.addOption("longOption1", true, "l");
        options.addOption("longOption2", true, "o");
        String[] result = new PosixParser().flatten(options, args, false);
        List<String> expected = Arrays.asList("--longOption1", "arg1", "--longOption2", "arg2");
        assertEquals(expected.size(), result.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
    }

    @Test
    public void testBurstToken() {
        String token = "-abcd";
        List<String> expected = Arrays.asList("-a", "-b", "-c", "-d");
        List<String> result = new PosixParser().burstToken(token, false);
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        String token = "-abcd";
        List<String> expected = Arrays.asList("--", "-d");
        List<String> result = new PosixParser().burstToken(token, true);
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }
    }

    @Test
    public void testBurstTokenEmptyToken() {
        String token = "";
        List<String> result = new PosixParser().burstToken(token, false);
        assertNull(result);
    }

    @Test
    public void testBurstTokenNullToken() {
        String token = null;
        List<String> result = new PosixParser().burstToken(token, false);
        assertNull(result);
    }
}