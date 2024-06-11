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
    public void testFlattenEmpty() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSimple() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "-b"};
        Options options = new Options();
        Option foo = new Option("foo", true, false, "Foo");
        options.addOption(foo);
        Option bar = new Option("bar", true, false, "Bar");
        options.addOption(bar);

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "-b"}, result);
    }

    @Test
    public void testFlattenWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo=bar", "-b=baz"};
        Options options = new Options();
        Option foo = new Option("foo", true, true, "Foo");
        options.addOption(foo);
        Option bar = new Option("bar", true, true, "Bar");
        options.addOption(bar);

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo=bar", "-b=baz"}, result);
    }

    @Test
    public void testFlattenWithLongArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option=this-is-a-long-option-value"};
        Options options = new Options();
        Option longOption = new Option("longOption", true, false, "Long option");
        options.addOption(longOption);

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--long-option=this-is-a-long-option-value"}, result);
    }

    @Test
    public void testFlattenWithNoOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--no-option", "value"};
        Options options = new Options();

        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--no-option", "value"}, result);
    }

    @Test
    public void testFlattenWithDash() {
        PosixParser parser = new PosixParser();
        String[] args = {"-", "--"};
        Options options = new Options();

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenWithDoubleDash() {
        PosixParser parser = new PosixParser();
        String[] args = {"--", "--option"};
        Options options = new Options();
        Option option = new Option("option", true, false, "Option");
        options.addOption(option);

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--", "--option"}, result);
    }

    @Test
    public void testFlattenWithDoubleDashAndArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--", "--option=value"};
        Options options = new Options();
        Option option = new Option("option", true, false, "Option");
        options.addOption(option);

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--", "--option=value"}, result);
    }

    @Test
    public void testFlattenWithLongOptionAndArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option=this-is-a-long-option-value", "value"};
        Options options = new Options();
        Option longOption = new Option("longOption", true, false, "Long option");
        options.addOption(longOption);

        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--long-option=this-is-a-long-option-value", "value"}, result);
    }

    @Test
    public void testFlattenWithMixedOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "-b=bar", "--long-option=this-is-a-long-option-value"};
        Options options = new Options();
        Option foo = new Option("foo", true, false, "Foo");
        options.addOption(foo);
        Option bar = new Option("bar", true, true, "Bar");
        options.addOption(bar);
        Option longOption = new Option("longOption", true, false, "Long option");
        options.addOption(longOption);

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--foo", "-b=bar", "--long-option=this-is-a-long-option-value");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithShortOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"-f", "-b=bar", "--long-option=this-is-a-long-option-value"};
        Options options = new Options();
        Option foo = new Option(null, true, false, "Foo");
        foo.setLongOpt("foo");
        options.addOption(foo);
        Option bar = new Option("b", true, true, "Bar");
        options.addOption(bar);
        Option longOption = new Option("longOption", true, false, "Long option");
        options.addOption(longOption);

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-f", "-b=bar", "--long-option=this-is-a-long-option-value");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithMixedShortAndLongOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"-f", "--foo", "-b=bar", "--long-option=this-is-a-long-option-value"};
        Options options = new Options();
        Option foo = new Option(null, true, false, "Foo");
        foo.setLongOpt("foo");
        options.addOption(foo);
        Option bar = new Option("b", true, true, "Bar");
        options.addOption(bar);
        Option longOption = new Option("longOption", true, false, "Long option");
        options.addOption(longOption);

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-f", "--foo", "-b=bar", "--long-option=this-is-a-long-option-value");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "nonOption", "--long-option=this-is-a-long-option-value"};
        Options options = new Options();
        Option foo = new Option("foo", true, false, "Foo");
        options.addOption(foo);
        Option longOption = new Option("longOption", true, false, "Long option");
        options.addOption(longOption);

        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("--foo", "nonOption", "--long-option=this-is-a-long-option-value");
        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testProcessOptionTokenWithShortOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option foo = new Option(null, true, false, "Foo");
        foo.setLongOpt("foo");
        options.addOption(foo);

        parser.processOptionToken("-f", false);
        assertSame(foo, parser.currentOption);
    }

    @Test
    public void testProcessOptionTokenWithLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option foo = new Option("foo", true, false, "Foo");
        options.addOption(foo);

        parser.processOptionToken("--foo", false);
        assertSame(foo, parser.currentOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessOptionTokenWithInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        parser.processOptionToken("invalidOption", false);
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option foo = new Option("foo", true, false, "Foo");
        options.addOption(foo);

        parser.processOptionToken("nonOption", true);
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenWithEmptyToken() {
        PosixParser parser = new PosixParser();
        String token = "";

        parser.burstToken(token, false);
        assertArrayEquals(new String[]{}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenWithShortOption() {
        PosixParser parser = new PosixParser();
        String token = "-f";

        parser.burstToken(token, false);
        String[] expected = new String[]{"-f"};
        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenWithLongOption() {
        PosixParser parser = new PosixParser();
        String token = "--foo";

        parser.burstToken(token, false);
        Option foo = new Option("foo", true, false, "Foo");
        assertSame(foo, parser.currentOption);
        String[] expected = new String[]{"--foo"};
        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenWithLongOptionAndArg() {
        PosixParser parser = new PosixParser();
        String token = "--foo=bar";

        parser.burstToken(token, false);
        Option foo = new Option("foo", true, true, "Foo");
        assertSame(foo, parser.currentOption);
        String[] expected = new String[]{"--foo=bar"};
        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenWithInvalidOption() {
        PosixParser parser = new PosixParser();
        String token = "invalidOption";

        parser.burstToken(token, false);
        assertArrayEquals(new String[]{}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "nonOption";

        parser.burstToken(token, true);
        assertArrayEquals(new String[]{"nonOption"}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenWithLongOptionAndArgAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "--foo=bar nonOption";

        parser.burstToken(token, true);
        assertArrayEquals(new String[]{"--foo=bar", "nonOption"}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }
}