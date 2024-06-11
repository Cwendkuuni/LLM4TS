package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_NullOptions() {
        String[] args = {"arg1", "--foo", "-b", "--bar=val", "-c", "--baz=qux"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_EmptyArgs() {
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_SimpleOptions() {
        String[] args = {"--foo", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        assertArrayEquals(new String[]{"--foo", "-b"}, result);
    }

    @Test
    public void testFlatten_LongOptions() {
        String[] args = {"--foo=bar", "--bar=baz"};
        Options options = new Options();
        options.addOption("foo", false, "f", "Set foo");
        options.addOption("bar", true, "b", "Set bar");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo=bar", "--bar=baz"}, result);
    }

    @Test
    public void testFlatten_LongOptionsWithValues() {
        String[] args = {"--foo=bar1", "--foo=bar2"};
        Options options = new Options();
        options.addOption("foo", true, "f", "Set foo");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo=bar1", "--foo=bar2"}, result);
    }

    @Test
    public void testFlatten_ShortOptions() {
        String[] args = {"-f", "-b"};
        Options options = new Options();
        options.addOption("foo", false, "f", "Set foo");
        options.addOption("bar", false, "b", "Set bar");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-f", "-b"}, result);
    }

    @Test
    public void testFlatten_ShortOptionsWithValues() {
        String[] args = {"-f", "arg1", "-b", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f", "Set foo");
        options.addOption("bar", true, "b", "Set bar");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-f=arg1", "-b=arg2"}, result);
    }

    @Test
    public void testFlatten_LongAndShortOptions() {
        String[] args = {"--foo", "-b", "--bar=qux"};
        Options options = new Options();
        options.addOption("foo", false, "f", "Set foo");
        options.addOption("bar", true, "b", "Set bar");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "-b", "--bar=qux"}, result);
    }

    @Test
    public void testFlatten_MixedLongAndShortOptions() {
        String[] args = {"--foo", "-b", "--bar=qux", "-c"};
        Options options = new Options();
        options.addOption("foo", false, "f", "Set foo");
        options.addOption("bar", true, "b", "Set bar");
        options.addOption("c", false, "c", "Set c");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "-b", "--bar=qux", "-c"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"--foo", "-b", "--bar=qux", "nonOption"};
        Options options = new Options();
        options.addOption("foo", false, "f", "Set foo");
        options.addOption("bar", true, "b", "Set bar");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo", "-b"}, result);
    }

    @Test
    public void testFlatten_BurstedTokens() {
        String[] args = {"--foo-bar", "--baz-qux"};
        Options options = new Options();
        options.addOption("foo", true, null, "Set foo");
        options.addOption("baz", true, null, "Set baz");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "--bar", "--baz", "--qux"}, result);
    }

    @Test
    public void testFlatten_BurstedTokensWithValues() {
        String[] args = {"--foo-bar=ba", "--baz-qux=qu"};
        Options options = new Options();
        options.addOption("foo", true, null, "Set foo");
        options.addOption("baz", true, null, "Set baz");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo=ba", "--baz=qu"}, result);
    }

    @Test
    public void testFlatten_BurstedTokensWithStopAtNonOption() {
        String[] args = {"--foo-bar=ba", "--baz-qux=qu", "nonOption"};
        Options options = new Options();
        options.addOption("foo", true, null, "Set foo");
        options.addOption("baz", true, null, "Set baz");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo=ba"}, result);
    }

    @Test
    public void testProcessOptionToken() {
        String[] args = {"-f", "--foo"};
        PosixParser parser = new PosixParser();
        parser.processOptionToken("-f", false);
        assertEquals(Options.OPTION_DIRECT, parser.getOption().getOptionType());
        assertEquals("f", parser.getOption().getLongOpt());
        assertEquals(false, parser.getOption().hasArg());
        assertEquals(null, parser.getOption().getValue());

        parser.init();
        parser.processOptionToken("--foo", false);
        assertEquals(Options.LONG_OPTION, parser.getOption().getOptionType());
        assertEquals("foo", parser.getOption().getLongOpt());
        assertEquals(false, parser.getOption().hasArg());
        assertEquals(null, parser.getOption().getValue());
    }

    @Test
    public void testProcessNonOptionToken() {
        String[] args = {"arg1", "--foo", "--bar=val"};
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("arg1");
        assertNull(parser.getOption());

        parser.init();
        parser.processNonOptionToken("--foo");
        assertNull(parser.getOption());

        parser.init();
        parser.processNonOptionToken("--bar=val");
        assertEquals("bar", parser.getOption().getLongOpt());
        assertEquals(true, parser.getOption().hasArg());
        assertEquals("val", parser.getOption().getValue());
    }
}