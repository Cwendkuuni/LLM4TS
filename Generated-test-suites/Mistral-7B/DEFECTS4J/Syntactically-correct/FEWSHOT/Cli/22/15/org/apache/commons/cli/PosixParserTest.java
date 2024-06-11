package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmptyOptionsAndArguments() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{}, flattenedArgs);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testSimpleOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", true, "A");
        options.addOption("b", 2, "B");
        String[] arguments = {"-a", "foo", "-b", "1", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a", "true", "-b", "2", "--", "arg1", "arg2"}, flattenedArgs);
        assertEquals(7, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("true", parser.tokens.get(1));
        assertEquals("-b", parser.tokens.get(2));
        assertEquals("2", parser.tokens.get(3));
        assertEquals("--", parser.tokens.get(4));
        assertEquals("arg1", parser.tokens.get(5));
        assertEquals("arg2", parser.tokens.get(6));
    }

    @Test
    public void testLongOptionsAndArguments() {
        Options options = new Options();
        options.addOption("longOption", true, "A");
        String[] arguments = {"--longOption", "value", "foo", "-b", "1", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption", "true", "foo", "-b", "1", "--", "arg1", "arg2"}, flattenedArgs);
        assertEquals(8, parser.tokens.size());
        assertEquals("--longOption", parser.tokens.get(0));
        assertEquals("true", parser.tokens.get(1));
        assertEquals("foo", parser.tokens.get(2));
        assertEquals("-b", parser.tokens.get(3));
        assertEquals("1", parser.tokens.get(4));
        assertEquals("--", parser.tokens.get(5));
        assertEquals("arg1", parser.tokens.get(6));
        assertEquals("arg2", parser.tokens.get(7));
    }

    @Test
    public void testLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "A");
        options.addOption("longOptionWithValue", 1, "value");
        String[] arguments = {"--longOption", "value1", "--longOptionWithValue", "value2", "foo", "-b", "1", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--longOption", "value1", "--longOptionWithValue", "value2", "foo", "-b", "1", "--", "arg1", "arg2"}, flattenedArgs);
        assertEquals(11, parser.tokens.size());
        assertEquals("--longOption", parser.tokens.get(0));
        assertEquals("value1", parser.tokens.get(1));
        assertEquals("--longOptionWithValue", parser.tokens.get(2));
        assertEquals("value2", parser.tokens.get(3));
        assertEquals("foo", parser.tokens.get(4));
        assertEquals("-b", parser.tokens.get(5));
        assertEquals("1", parser.tokens.get(6));
        assertEquals("--", parser.tokens.get(7));
        assertEquals("arg1", parser.tokens.get(8));
        assertEquals("arg2", parser.tokens.get(9));
        assertEquals(null, parser.tokens.get(10));
    }

    @Test
    public void testBurstingSingleOption() {
        Options options = new Options();
        options.addOption("o", true, "O");
        String[] arguments = {"-oo", "value"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "true", "-o", "value"}, flattenedArgs);
        assertEquals(4, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("true", parser.tokens.get(1));
        assertEquals("-o", parser.tokens.get(2));
        assertEquals("value", parser.tokens.get(3));
    }

    @Test
    public void testBurstingMultipleOptions() {
        Options options = new Options();
        options.addOption("o", true, "O");
        options.addOption("oo", 2, "OO");
        String[] arguments = {"-ooo", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "true", "-o", "true", "-o", "value1", "-o", "value2"}, flattenedArgs);
        assertEquals(9, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("true", parser.tokens.get(1));
        assertEquals("-o", parser.tokens.get(2));
        assertEquals("true", parser.tokens.get(3));
        assertEquals("-o", parser.tokens.get(4));
        assertEquals("value1", parser.tokens.get(5));
        assertEquals("-o", parser.tokens.get(6));
        assertEquals("value2", parser.tokens.get(7));
        assertEquals(null, parser.tokens.get(8));
    }

    @Test
    public void testBurstingLongOption() {
        Options options = new Options();
        options.addOption("lo", true, "LO");
        String[] arguments = {"-loo", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-lo", "true", "-lo", "true", "--", "value1", "value2"}, flattenedArgs);
        assertEquals(7, parser.tokens.size());
        assertEquals("-lo", parser.tokens.get(0));
        assertEquals("true", parser.tokens.get(1));
        assertEquals("-lo", parser.tokens.get(2));
        assertEquals(null, parser.tokens.get(3));
        assertEquals("value1", parser.tokens.get(4));
        assertEquals("value2", parser.tokens.get(5));
        assertEquals("--", parser.tokens.get(6));
    }

    @Test
    public void testBurstingLongOptionWithValue() {
        Options options = new Options();
        options.addOption("lo", true, "LO");
        options.addOption("lov", 2, "LOV");
        String[] arguments = {"-loov", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-lo", "true", "-lov", "value1", "-lov", "value2"}, flattenedArgs);
        assertEquals(6, parser.tokens.size());
        assertEquals("-lo", parser.tokens.get(0));
        assertEquals("true", parser.tokens.get(1));
        assertEquals("-lov", parser.tokens.get(2));
        assertEquals("value1", parser.tokens.get(3));
        assertEquals("-lov", parser.tokens.get(4));
        assertEquals("value2", parser.tokens.get(5));
    }

    @Test
    public void testBurstingNonOption() {
        Options options = new Options();
        String[] arguments = {"foo", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"foo", "--", "arg1", "arg2"}, flattenedArgs);
        assertEquals(4, parser.tokens.size());
        assertEquals("foo", parser.tokens.get(0));
        assertEquals("--", parser.tokens.get(1));
        assertEquals("arg1", parser.tokens.get(2));
        assertEquals("arg2", parser.tokens.get(3));
    }

    @Test
    public void testBurstingNonOptionWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"foo", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"foo", "arg1", "arg2"}, flattenedArgs);
        assertEquals(3, parser.tokens.size());
        assertEquals("foo", parser.tokens.get(0));
        assertEquals("arg1", parser.tokens.get(1));
        assertEquals("arg2", parser.tokens.get(2));
    }

    @Test
    public void testMultipleNonOptions() {
        Options options = new Options();
        String[] arguments = {"foo", "bar", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"foo", "--", "bar", "--", "arg1", "arg2"}, flattenedArgs);
        assertEquals(6, parser.tokens.size());
        assertEquals("foo", parser.tokens.get(0));
        assertEquals("--", parser.tokens.get(1));
        assertEquals("bar", parser.tokens.get(2));
        assertEquals("--", parser.tokens.get(3));
        assertEquals("arg1", parser.tokens.get(4));
        assertEquals("arg2", parser.tokens.get(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstingNonExistingOption() {
        Options options = new Options();
        String[] arguments = {"-x", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testBurstingNonExistingOptionWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-x", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-x", "arg1", "arg2"}, flattenedArgs);
        assertEquals(3, parser.tokens.size());
        assertEquals("-x", parser.tokens.get(0));
        assertEquals("arg1", parser.tokens.get(1));
        assertEquals("arg2", parser.tokens.get(2));
    }

    @Test
    public void testBurstingEmptyOption() {
        Options options = new Options();
        String[] arguments = {"--", ""};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", ""}, flattenedArgs);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("", parser.tokens.get(1));
    }

    @Test
    public void testBurstingEmptyOptionWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--", ""};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--", ""}, flattenedArgs);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("", parser.tokens.get(1));
    }

    @Test
    public void testBurstingEmptyOptions() {
        Options options = new Options();
        String[] arguments = {"--", "--"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--", "--"}, flattenedArgs);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("--", parser.tokens.get(1));
    }

    @Test
    public void testBurstingEmptyOptionsWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--", "--"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--", "--"}, flattenedArgs);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("--", parser.tokens.get(1));
    }

    @Test
    public void testBurstingEmptyListOfArguments() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{}, flattenedArgs);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testBurstingEmptyListOfArgumentsWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{}, flattenedArgs);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testBurstingSingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-"}, flattenedArgs);
        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testBurstingSingleHyphenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"-"}, flattenedArgs);
        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
    }
}