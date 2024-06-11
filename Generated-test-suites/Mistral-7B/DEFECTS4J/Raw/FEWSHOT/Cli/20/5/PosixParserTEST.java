package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmptyArgs() {
        String[] args = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[0], result);

        parser.init();
    }

    @Test
    public void testSingleArg() {
        String[] args = {"arg"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("arg", result[0]);

        parser.init();
    }

    @Test
    public void testSingleShortOption() {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOptional("a", "a", false, "Short option a");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);

        parser.init();
    }

    @Test
    public void testSingleLongOption() {
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addLongOption("long-option", true, "Long option");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--long-option", result[0]);

        parser.init();
    }

    @Test
    public void testSingleShortOptionWithArg() {
        String[] args = {"-a", "arg"};
        Options options = new Options();
        options.addOptional("a", "a", true, "Short option a with an argument");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg", result[1]);

        parser.init();
    }

    @Test
    public void testSingleLongOptionWithArg() {
        String[] args = {"--long-option", "arg"};
        Options options = new Options();
        options.addLongOption("long-option", true, "Long option with an argument");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--long-option", result[0]);
        assertEquals("arg", result[1]);

        parser.init();
    }

    @Test
    public void testSingleDash() {
        String[] args = {"-"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);

        parser.init();
    }

    @Test
    public void testDoubleDash() {
        String[] args = {"--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--", result[0]);

        parser.init();
    }

    @Test
    public void testDoubleDashWithArg() {
        String[] args = {"--arg"};
        Options options = new Options();
        options.addLongOption("arg", true, "Option with an argument");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--arg", result[0]);

        parser.init();
    }

    @Test
    public void testMultipleArgs() {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);

        parser.init();
    }

    @Test
    public void testMixedArgs() {
        String[] args = {"arg1", "--long-option", "arg3", "-a", "arg4"};
        Options options = new Options();
        options.addLongOption("long-option", true, "Long option");
        options.addOptional("a", "a", true, "Short option a");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertEquals(6, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("--long-option", result[1]);
        assertEquals("arg3", result[2]);
        assertEquals("-a", result[3]);
        assertEquals("arg4", result[4]);

        parser.init();
    }

    @Test
    public void testStopAtNonOption() {
        String[] args = {"arg1", "--non-option", "arg3"};
        Options options = new Options();
        options.addLongOption("option", true, "Option");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, true);
        assertEquals(3, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("--non-option", result[1]);
        assertEquals("arg3", result[2]);

        parser.init();
    }

    @Test
    public void testBursting() {
        String[] args = {"-abcd"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("-d", result[3]);

        parser.init();
    }

    @Test
    public void testBurstingWithStopAtNonOption() {
        String[] args = {"-abcd", "--non-option"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, true);
        assertEquals(3, result.length);
        assertEquals("-abcd", result[0]);
        assertEquals("--non-option", result[1]);

        parser.init();
    }

    @Test
    public void testBurstingWithStopAtNonOptionAfterShortOption() {
        String[] args = {"-a-abcd", "--non-option"};
        Options options = new Options();
        options.addOptional("a", "a", true, "Option a");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("-a-abcd", result[0]);
        assertEquals("--non-option", result[1]);

        parser.init();
    }

    @Test
    public void testBurstingWithStopAtNonOptionAfterLongOption() {
        String[] args = {"--long-option-abcd", "--non-option"};
        Options options = new Options();
        options.addLongOption("long-option", true, "Long option");
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("--long-option-abcd", result[0]);
        assertEquals("--non-option", result[1]);

        parser.init();
    }
}