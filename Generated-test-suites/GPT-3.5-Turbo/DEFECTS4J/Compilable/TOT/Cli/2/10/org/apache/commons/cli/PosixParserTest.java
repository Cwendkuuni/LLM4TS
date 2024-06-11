package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption() {
        // Test with stopAtNonOption = true
        String[] arguments = { "-a", "arg1", "--option1", "arg2", "--", "arg3" };
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--option1", result[2]);
        assertEquals("arg2", result[3]);
        assertEquals("--", result[4]);
    }

    @Test
    public void testFlatten_NotStopAtNonOption() {
        // Test with stopAtNonOption = false
        String[] arguments = { "-a", "arg1", "--option1", "arg2", "--", "arg3" };
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--option1", result[2]);
        assertEquals("arg2", result[3]);
        assertEquals("--", result[4]);
        assertEquals("arg3", result[5]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        // Test with single hyphen as argument
        String[] arguments = { "-" };
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_NoOption() {
        // Test with no option
        String[] arguments = { "arg1", "arg2" };
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlatten_InvalidOption() {
        // Test with invalid option
        String[] arguments = { "-x", "arg1", "arg2" };
        Options options = new Options();
        options.addOption("a", false, "Option a");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-x", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_BurstToken() {
        // Test with bursting token
        String[] arguments = { "-abc", "arg1", "arg2" };
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("arg1", result[3]);
    }
}