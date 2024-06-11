package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
    }

    @Test
    public void testFlatten_noArguments_returnEmpty() {
        String[] arguments = new String[0];

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_singleOption_returnOption() {
        String[] arguments = new String[]{"-a"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_optionWithValue_returnOptionAndValue() {
        String[] arguments = new String[]{"-b", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_multipleOptions_returnOptions() {
        String[] arguments = new String[]{"-a", "-b", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_stopAtNonOption_returnOptionsUntilNonOption() {
        String[] arguments = new String[]{"-a", "-b", "value", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_ignoreNonOption_returnAllArguments() {
        String[] arguments = new String[]{"-a", "-b", "value", "nonOption"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("nonOption", result[3]);
    }

    @Test
    public void testFlatten_burstToken_returnBurstTokens() {
        String[] arguments = new String[]{"-abc", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("value", result[3]);
    }

    @Test
    public void testFlatten_burstToken_stopAtNonOption_returnBurstTokensUntilNonOption() {
        String[] arguments = new String[]{"-abc", "value", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("value", result[3]);
    }

    @Test
    public void testFlatten_burstToken_ignoreNonOption_returnBurstTokensAndNonOption() {
        String[] arguments = new String[]{"-abc", "value", "nonOption"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("value", result[3]);
        assertEquals("nonOption", result[4]);
    }
}