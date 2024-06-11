package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption_AdditionalArguments() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOption_NoAdditionalArguments() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_NoStopAtNonOption_AdditionalArguments() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_NoStopAtNonOption_NoAdditionalArguments() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] arguments = {"--optionA=value"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--optionA", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();
        String[] arguments = {"--optionA"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("--optionA", result[0]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_InvalidOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] arguments = {"-b"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-b", result[0]);
    }

    @Test
    public void testFlatten_StopAtNonOption_InvalidOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] arguments = {"-b"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-abcd"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("cd", result[2]);
    }

    @Test
    public void testFlatten_BurstToken_NoStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");

        PosixParser parser = new PosixParser();
        String[] arguments = {"-abcd"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }
}