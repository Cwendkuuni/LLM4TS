package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for PosixParser with JUnit 4.
 */
public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();
        try {
            String[] result = parser.flatten(options, args, false);
            assertEquals(new String[0], result);
            assertTrue(parser.tokens.isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_SingleArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg"};
        Options options = new Options();
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = new ArrayList<>();
            expected.add("arg");
            assertArrayEquals(expected.toArray(new String[0]), result);
            assertTrue(parser.tokens.equals(expected));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_DoubleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"--no-option"};
        Options options = new Options();
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = new ArrayList<>();
            expected.add("--no-option");
            assertArrayEquals(expected.toArray(new String[0]), result);
            assertTrue(parser.tokens.equals(expected));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_SingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = new ArrayList<>();
            expected.add("-a");
            assertArrayEquals(expected.toArray(new String[0]), result);
            assertTrue(parser.tokens.equals(expected));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_OptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = new ArrayList<>();
            expected.add("-a");
            expected.add("value");
            assertArrayEquals(expected.toArray(new String[0]), result);
            assertTrue(parser.tokens.equals(expected));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_OptionWithMultipleValues() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value1", "value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = new ArrayList<>();
            expected.add("-a");
            expected.add("value1");
            expected.add("value2");
            assertArrayEquals(expected.toArray(new String[0]), result);
            assertTrue(parser.tokens.equals(expected));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--no-option", "arg"};
        Options options = new Options();
        try {
            String[] result = parser.flatten(options, args, true);
            List<String> expected = new ArrayList<>();
            expected.add("--no-option");
            assertArrayEquals(expected.toArray(new String[0]), Arrays.copyOfRange(result, 0, 1));
            assertEquals(1, result.length);
            assertTrue(parser.tokens.isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_SingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        Options options = new Options();
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = new ArrayList<>();
            expected.add("-");
            assertArrayEquals(expected.toArray(new String[0]), result);
            assertTrue(parser.tokens.equals(expected));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-invalidOption"};
        Options options = new Options();
        try {
            parser.flatten(options, args, false);
        } catch (Exception e) {
            // expected
        }
        assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testFlatten_LongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option"};
        Options options = new Options();
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = new ArrayList<>();
            expected.add("--long-option");
            assertArrayEquals(expected.toArray(new String[0]), result);
            assertTrue(parser.tokens.equals(expected));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_LongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option=value"};
        Options options = new Options();
        try {
            parser.flatten(options, args, false);
        } catch (Exception e) {
            // expected
        }
        assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testFlatten_LongOptionWithMultipleValues() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option", "value1", "value2"};
        Options options = new Options();
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = new ArrayList<>();
            expected.add("--long-option");
            expected.add("value1");
            expected.add("value2");
            assertArrayEquals(expected.toArray(new String[0]), result);
            assertTrue(parser.tokens.equals(expected));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}