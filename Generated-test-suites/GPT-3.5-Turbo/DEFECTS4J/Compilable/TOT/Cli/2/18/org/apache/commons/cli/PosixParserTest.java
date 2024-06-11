package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "input.txt"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("input.txt", result[1]);
    }

    @Test
    public void testFlatten_WithoutStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "input.txt"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("input.txt", result[1]);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("input", true, "Input file");

        PosixParser parser = new PosixParser();
        String[] args = {"--input=input.txt"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("--input", result[0]);
        assertEquals("input.txt", result[1]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_SingleHyphenWithoutOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-", result[1]);
    }

    @Test
    public void testFlatten_MultipleShortOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-ab"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithArgs() {
        Options options = new Options();
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-aarg1", "-barg2"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithArgsStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");

        PosixParser parser = new PosixParser();
        String[] args = {"-aarg1", "nonOption", "-barg2"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("nonOption", result[2]);
        assertEquals("--", result[3]);
        assertEquals("-barg2", result[4]);
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-ax"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-ax", result[0]);
    }

    @Test
    public void testFlatten_MultipleShortOptionsWithInvalidOptionStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        PosixParser parser = new PosixParser();
        String[] args = {"-ax", "nonOption"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-ax", result[0]);
        assertEquals("--", result[1]);
        assertEquals("nonOption", result[2]);
    }
}