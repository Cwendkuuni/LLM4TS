package org.apache.commons.cli;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setup() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));
        options.addOption(new Option("c", false, "Option c"));

        String[] args = { "-a", "valueA", "-b", "valueB", "nonOption", "-c" };
        String[] result = parser.flatten(options, args, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("valueA", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("valueB", result[3]);
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));
        options.addOption(new Option("c", false, "Option c"));

        String[] args = { "-a", "valueA", "-b", "valueB", "nonOption", "-c" };
        String[] result = parser.flatten(options, args, false);

        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("valueA", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("valueB", result[3]);
        assertEquals("nonOption", result[4]);
        assertEquals("-c", result[5]);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));
        options.addOption(new Option("c", false, "Option c"));

        String[] args = { "-a", "-b", "valueB", "-c" };
        String[] result = parser.flatten(options, args, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("valueB", result[2]);
        assertEquals("-c", result[3]);
    }

    @Test
    public void testFlattenWithLongOption() {
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));
        options.addOption(new Option("c", false, "Option c"));

        String[] args = { "--a", "--b=valueB", "--c" };
        String[] result = parser.flatten(options, args, true);

        assertEquals(4, result.length);
        assertEquals("--a", result[0]);
        assertEquals("--b", result[1]);
        assertEquals("valueB", result[2]);
        assertEquals("--c", result[3]);
    }

    @Test
    public void testFlattenWithBursting() {
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));
        options.addOption(new Option("c", false, "Option c"));

        String[] args = { "-abvalueB", "-c" };
        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("valueB", result[2]);
    }

    @Test
    public void testFlattenWithNonOptionToken() {
        options.addOption(new Option("a", false, "Option a"));
        options.addOption(new Option("b", true, "Option b"));
        options.addOption(new Option("c", false, "Option c"));

        String[] args = { "-a", "-c", "nonOption", "-bvalueB" };
        String[] result = parser.flatten(options, args, true);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-c", result[1]);
        assertEquals("--", result[2]);
        assertEquals("nonOption", result[3]);
        assertEquals("-bvalueB", result[4]);
    }

    @Test
    public void testFlattenEmptyArguments() {
        options.addOption(new Option("a", false, "Option a"));

        String[] args = {};
        String[] result = parser.flatten(options, args, true);

        assertEquals(0, result.length);
    }
}