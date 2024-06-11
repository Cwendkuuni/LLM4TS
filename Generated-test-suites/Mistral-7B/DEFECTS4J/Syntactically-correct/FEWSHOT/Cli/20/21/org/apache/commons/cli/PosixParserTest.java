package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmpty() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testSimpleOptions() {
        Options options = new Options();
        String[] args = {"-a", "--foo", "--bar=baz"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "--foo", "--bar", "baz");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testLongOptions() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", 1, "b", "B");
        String[] args = {"--foo", "--bar=42", "-f", " Value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--foo", "--bar", "42", "-f", " Value");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testLongOptionsWithArgument() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", 1, "b", "B");
        String[] args = {"--foo", "--bar=abc", "-f", "--bar=def"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--foo", "--bar", "abc", "--bar", "def");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testMixedShortLongOptions() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", 1, "b");
        options.addOption("c", 1, "c", "C");
        String[] args = {"-a", "--b=123", "-c", "Value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "--b", "123", "-c", "Value");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBursting() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"-foo-bar", "-foobar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-foo", "--bar", "-foo", "bar");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstingWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"-foo-bar", "-foobar", "nonoption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("-foo-bar", "-foobar", "nonoption");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstingWithEmptyInput() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testBurstingWithEmptyInputAndStopAtNonOption() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertNull(result);
    }

    @Test
    public void testBurstingWithInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalid"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--invalid");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstingWithInvalidOptionAndStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--invalid", "nonoption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("--invalid", "nonoption");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstingWithStopAtNonOptionAndTwoValidOptions() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        String[] args = {"--a", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("--a", "-b");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstingWithStopAtNonOptionAndTwoInvalidOptions() {
        Options options = new Options();
        String[] args = {"--invalid1", "--invalid2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("--invalid1", "--invalid2");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstingWithStopAtNonOptionAndMixedValidAndInvalidOptions() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        String[] args = {"--a", "--invalid", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("--a", "--invalid", "-b");
        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstingWithStopAtNonOptionAndEmptyInput() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertNull(result);
    }
}