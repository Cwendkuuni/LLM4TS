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
    public void testFlattenSimple() {
        Options options = new Options();
        String[] arguments = {"--foo", "-a", "-b", "-c=value", "--bar=arg", "-d", "--", "arg1", "arg2"};
        String[] expected = {"--foo", "-a", "--", "-b", "-c", "value", "--bar", "arg", "-d", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--foo", "-a", "-b", "-c=value", "--bar=arg", "-d", "--", "arg1", "arg2"};
        String[] expected = {"--foo", "-a", "-b", "--", "value", "--bar", "arg", "d", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "l");
        String[] arguments = {"--longOption", "--foo", "-a", "-b", "-c=value", "--bar=arg", "-d", "--", "arg1", "arg2"};
        String[] expected = {"--longOption", "--foo", "--", "-a", "--", "-b", "--", "value", "--bar", "arg", "--", "d", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        String[] expected2 = {"--longOption", "--foo", "--", "-a", "--", "--", "value", "--bar", "arg", "--", "d", "--", "arg1", "arg2"};
        PosixParser parser2 = new PosixParser();
        String[] result2 = parser2.flatten(options, arguments, true);
        assertArrayEquals(expected2, result2);
    }

    @Test
    public void testFlattenWithOptionValue() {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] arguments = {"--option=value", "--foo", "-a", "-b", "-c=value2", "--bar=arg", "-d", "--", "arg1", "arg2"};
        String[] expected = {"--option", "value", "--foo", "--", "-a", "--", "-b", "--", "value2", "--bar", "arg", "--", "d", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithShortOption() {
        Options options = new Options();
        options.addOption("o", true, "o");
        String[] arguments = {"--foo", "--o", "value", "-a", "-b", "-c=value2", "--bar=arg", "-d", "--", "arg1", "arg2"};
        String[] expected = {"--foo", "--o", "--", "value", "--", "-a", "--", "-b", "--", "value2", "--bar", "arg", "--", "d", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        String[] expected2 = {"--foo", "--o", "--", "value", "--foo", "--", "-a", "--", "-b", "--", "value2", "--bar", "arg", "--", "d", "--", "arg1", "arg2"};
        PosixParser parser2 = new PosixParser();
        String[] result2 = parser2.flatten(options, arguments, true);
        assertArrayEquals(expected2, result2);
    }

    @Test
    public void testFlattenWithOptionArgument() {
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("option2", "arg", "o2");
        String[] arguments = {"--foo", "--o=value", "--option2=argValue", "-a", "-b", "-c=value2", "--bar=arg", "-d", "--", "arg1", "arg2"};
        String[] expected = {"--foo", "--o", "value", "--option2", "argValue", "--", "-a", "--", "-b", "--", "value2", "--bar", "arg", "--", "d", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionArgument() {
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("option2", "arg", "o2");
        String[] arguments = {"--foo", "--o=value", "--option2=longArgValue", "-a", "-b", "-c=value2", "--bar=arg", "-d", "--", "arg1", "arg2"};
        String[] expected = {"--foo", "--o", "value", "--option2", "longArgValue", "--", "-a", "--", "-b", "--", "value2", "--bar", "arg", "--", "d", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMixedOptions() {
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("option2", "arg", "o2");
        String[] arguments = {"--foo", "--o=value", "--option2=longArgValue", "-a", "--bar", "arg2", "-c=value2", "-d", "--", "arg1", "arg2"};
        String[] expected = {"--foo", "--o", "value", "--option2", "longArgValue", "--bar", "arg2", "--", "-a", "--", "-c", "--", "value2", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionBeforeShortOption() {
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("o", true, "o2");
        String[] arguments = {"--option=value", "--o", "value2", "-a", "-b", "-c=value2", "--bar=arg", "-d", "--", "arg1", "arg2"};
        String[] expected = {"--option", "value", "--o", "--", "value2", "--", "-a", "--", "-b", "--", "value2", "--bar", "arg", "--", "d", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken() {
        String token = "-foobar";
        PosixParser parser = new PosixParser();
        List<String> expected = new ArrayList<>();
        expected.add("-f");
        expected.add("oo");
        expected.add("bar");
        parser.burstToken(token, false);
        assertArrayEquals(expected.toArray(new String[expected.size()]), parser.tokens.toArray(new String[parser.tokens.size()]));

        List<String> expected2 = new ArrayList<>();
        expected2.add("--");
        expected2.add("foo");
        expected2.add("bar");
        parser.reset();
        parser.burstToken(token, true);
        assertArrayEquals(expected2.toArray(new String[expected2.size()]), parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenWithLongOption() {
        String token = "--foo-bar";
        PosixParser parser = new PosixParser();
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("bar");
        parser.burstToken(token, false);
        assertArrayEquals(expected.toArray(new String[expected.size()]), parser.tokens.toArray(new String[parser.tokens.size()]));

        List<String> expected2 = new ArrayList<>();
        expected2.add("--foo");
        expected2.add("--");
        expected2.add("bar");
        parser.reset();
        parser.burstToken(token, true);
        assertArrayEquals(expected2.toArray(new String[expected2.size()]), parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenWithLongOptionArgument() {
        String token = "--foo=longArg";
        PosixParser parser = new PosixParser();
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("longArg");
        parser.burstToken(token, false);
        assertArrayEquals(expected.toArray(new String[expected.size()]), parser.tokens.toArray(new String[parser.tokens.size()]));

        List<String> expected2 = new ArrayList<>();
        expected2.add("--foo");
        expected2.add("longArg");
        expected2.add("--");
        parser.reset();
        parser.burstToken(token, true);
        assertArrayEquals(expected2.toArray(new String[expected2.size()]), parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenWithLongOptionAndShortOption() {
        String token = "--foo-o";
        PosixParser parser = new PosixParser();
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("o");
        parser.burstToken(token, false);
        assertArrayEquals(expected.toArray(new String[expected.size()]), parser.tokens.toArray(new String[parser.tokens.size()]));

        List<String> expected2 = new ArrayList<>();
        expected2.add("--foo");
        expected2.add("--");
        expected2.add("o");
        parser.reset();
        parser.burstToken(token, true);
        assertArrayEquals(expected2.toArray(new String[expected2.size()]), parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenWithLongOptionArgumentWithValue() {
        String token = "--foo=value-o";
        PosixParser parser = new PosixParser();
        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("value");
        expected.add("o");
        parser.burstToken(token, false);
        assertArrayEquals(expected.toArray(new String[expected.size()]), parser.tokens.toArray(new String[parser.tokens.size()]));

        List<String> expected2 = new ArrayList<>();
        expected2.add("--foo");
        expected2.add("value");
        expected2.add("--");
        expected2.add("o");
        parser.reset();
        parser.burstToken(token, true);
        assertArrayEquals(expected2.toArray(new String[expected2.size()]), parser.tokens.toArray(new String[parser.tokens.size()]));
    }

}