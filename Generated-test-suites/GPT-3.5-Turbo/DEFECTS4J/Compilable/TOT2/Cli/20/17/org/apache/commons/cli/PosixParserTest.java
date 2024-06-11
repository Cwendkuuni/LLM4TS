package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_NoOptions_ReturnsEmptyArray() {
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_SingleOption_NoArg_ReturnsToken() {
        options.addOption("a", false, "Option a");
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_SingleOption_WithArg_ReturnsTokenAndValue() {
        options.addOption("b", true, "Option b");
        String[] arguments = {"-b", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_UnknownOption_StopAtNonOption_ReturnsSpecialToken() {
        String[] arguments = {"-c"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-c", result[1]);
    }

    @Test
    public void testFlatten_KnownOption_UnknownSubOption_Burst_ReturnsBurstTokens() {
        options.addOption("d", false, "Option d");
        options.addOption("e", true, "Option e");
        String[] arguments = {"-devalue"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(4, result.length);
        assertEquals("-d", result[0]);
        assertEquals("-e", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-value", result[3]);
    }

}