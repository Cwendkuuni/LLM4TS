package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        String[] args = {"--foo", "--bar=baz", "-c", "-d", "-e foo", "--", "-f -g", "-h=hello", "-i", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("c", true, "A boolean option");
        options.addOption("d", false, "An option with a flag");
        options.addOption("e", true, "An option that takes an argument");
        options.addOption("f", true, "An option that takes an optional argument");
        options.addOption("h", true, "An option that takes a required argument");
        options.addOption("i", false, "An option with a short name i");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(13, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar", result[1]);
        assertEquals("baz", result[2]);
        assertEquals("-c", result[3]);
        assertEquals(true, result[4].equals("true"));
        assertEquals("-d", result[5]);
        assertEquals("foo", result[6]);
        assertEquals("--", result[7]);
        assertEquals("-f", result[8]);
        assertEquals(null, result[9]);
        assertEquals("-g", result[10]);
        assertEquals("-h", result[11]);
        assertEquals("hello", result[12]);
        assertEquals("arg1", result[13]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--foo", "--bar=baz", "-c", "-d", "-e foo", "--", "-f -g", "-h=hello", "-i", "arg1", "arg2", "nonOption"};
        Options options = new Options();
        options.addOption("c", true, "A boolean option");
        options.addOption("d", false, "An option with a flag");
        options.addOption("e", true, "An option that takes an argument");
        options.addOption("f", true, "An option that takes an optional argument");
        options.addOption("h", true, "An option that takes a required argument");
        options.addOption("i", false, "An option with a short name i");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(11, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar=baz", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("-d", result[3]);
        assertEquals("--", result[4]);
        assertEquals("foo", result[5]);
        assertEquals("nonOption", result[6]);
        assertNull(result[7]);
        assertNull(result[8]);
        assertNull(result[9]);
        assertNull(result[10]);
    }

    @Test
    public void testBurstToken() {
        String token = "--foo-bar-baz";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        assertEquals(3, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("-", parser.tokens.get(1));
        assertEquals("bar-baz", parser.tokens.get(2));

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("-");
        expected.add("bar");
        expected.add("-");
        expected.add("baz");

        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "--foo-bar-baz-nonOption";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, true);

        assertEquals(3, parser.tokens.size());
        assertEquals("--foo-nonOption", parser.tokens.get(0));
        assertEquals(null, parser.tokens.get(1));
        assertEquals("--", parser.tokens.get(2));
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("nonOption");

        assertEquals("--", parser.tokens.get(0));
        assertEquals("nonOption", parser.tokens.get(1));

        List<String> expected = Arrays.asList("--", "nonOption");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("c", true, "A boolean option");
        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.processOptionToken("-c", false);

        assertEquals("-c", parser.tokens.get(0));
        assertEquals(true, parser.tokens.get(1).equals("true"));

        List<String> expected = Arrays.asList("-c", "true");
        assertEquals(expected, parser.tokens);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertEquals(0, parser.tokens.size());
        assertFalse(parser.eatTheRest);
    }
}