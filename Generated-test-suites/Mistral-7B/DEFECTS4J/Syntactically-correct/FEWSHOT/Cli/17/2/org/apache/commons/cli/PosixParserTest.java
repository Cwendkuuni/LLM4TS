package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSimpleArgs() {
        Options options = new Options();
        String[] args = {"foo", "bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"foo", "bar"}, result);
    }

    @Test
    public void testSingleDash() {
        Options options = new Options();
        String[] args = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testDoubleDash() {
        Options options = new Options();
        String[] args = {"--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testSingleHyphenOption() {
        Options options = new Options();
        options.addOption("f", false, null, "Print Foo");
        String[] args = {"-f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-f"}, result);
    }

    @Test
    public void testDoubleHyphenOption() {
        Options options = new Options();
        options.addOption("aa", false, null, "Print AA");
        String[] args = {"--aa"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--aa"}, result);
    }

    @Test
    public void testLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "value", "Print Long Option Value");
        String[] args = {"--longOption=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testLongOptionWithoutValue() {
        Options options = new Options();
        options.addOption("longOption", false, null, "Print Long Option");
        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testLongOptionWithValueAndArg() {
        Options options = new Options();
        options.addOption("longOption", true, "value", "Print Long Option Value");
        options.addOption("arg", true, null, "Print Arg");
        String[] args = {"--longOption=value", "--arg=argValue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--longOption", "value", "--arg", "argValue"}, result);
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption("f", false, null, "Print Foo");
        options.addOption("aa", false, null, "Print AA");
        String[] args = {"-f", "--aa"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-f", "--aa"}, result);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", false, null, "Print Foo");
        String[] args = {"-f", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"-f", "nonOption"}, result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("foo", false, null, "Print Foo");
        options.addOption("bar", false, null, "Print Bar");
        String[] args = {"-fba"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-f", "-b", "-a"}, result);
    }

    @Test
    public void testBurstTokenWithValue() {
        Options options = new Options();
        options.addOption("foo", true, null, "Print Foo");
        options.addOption("bar", false, null, "Print Bar");
        String[] args = {"--foo=value", "-ba"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "value", "-b", "-a"}, result);
    }

    @Test
    public void testBurstTokenWithValueAndArg() {
        Options options = new Options();
        options.addOption("foo", true, null, "Print Foo");
        options.addOption("bar", true, null, "Print Bar");
        options.addOption("arg", true, null, "Print Arg");
        String[] args = {"--foo=value", "--bar=argValue", "-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "value", "--bar", "argValue", "-a"}, result);
    }

    @Test
    public void testBurstTokenWithLongOptions() {
        Options options = new Options();
        options.addOption("foo", false, null, "Print Foo");
        options.addOption("longOption", true, null, "Print Long Option");
        String[] args = {"--longOption=value", "--foo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--longOption", "value", "--foo"}, result);
    }

    @Test
    public void testBurstTokenWithValueAndLongOptions() {
        Options options = new Options();
        options.addOption("foo", true, null, "Print Foo");
        options.addOption("longOption", true, null, "Print Long Option");
        String[] args = {"--foo=value", "--longOption=argValue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "value", "--longOption", "argValue"}, result);
    }

    @Test
    public void testBurstTokenWithLongOptionsAndArg() {
        Options options = new Options();
        options.addOption("foo", true, null, "Print Foo");
        options.addOption("longOption", true, null, "Print Long Option");
        options.addOption("arg", true, null, "Print Arg");
        String[] args = {"--longOption=argValue", "--foo", "-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--longOption", "argValue", "--foo", "-a"}, result);
    }

    @Test
    public void testBurstTokenWithMultipleOptions() {
        Options options = new Options();
        options.addOption("foo", false, null, "Print Foo");
        options.addOption("bar", false, null, "Print Bar");
        String[] args = {"-foobar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-foo", "-bar"}, result);
    }

    @Test
    public void testBurstTokenWithMultipleOptionsAndValues() {
        Options options = new Options();
        options.addOption("foo", true, null, "Print Foo");
        options.addOption("bar", true, null, "Print Bar");
        String[] args = {"--foo=value1", "--foo=value2", "-ba"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "value1", "--foo", "value2", "-b", "-a"}, result);
    }

    @Test
    public void testBurstTokenWithLongOptionsAndValues() {
        Options options = new Options();
        options.addOption("foo", true, null, "Print Foo");
        options.addOption("longOption1", true, null, "Print Long Option 1");
        options.addOption("longOption2", true, null, "Print Long Option 2");
        String[] args = {"--foo=value1", "--longOption1=argValue1", "--longOption2=argValue2", "-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "value1", "--longOption1", "argValue1", "--longOption2", "argValue2", "-a"}, result);
    }

    @Test
    public void testBurstTokenWithLongOptionsValuesAndArg() {
        Options options = new Options();
        options.addOption("foo", true, null, "Print Foo");
        options.addOption("longOption1", true, null, "Print Long Option 1");
        options.addOption("longOption2", true, null, "Print Long Option 2");
        options.addOption("arg", true, null, "Print Arg");
        String[] args = {"--longOption1=argValue1", "--longOption2=argValue2", "--foo=value1", "-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--longOption1", "argValue1", "--longOption2", "argValue2", "--foo", "value1", "-a"}, result);
    }
}