package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;
    private Options options;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-a", "value1", "--arg=value2", "nonOption"};
        options.addOption("a", true, "Option A");

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--arg=value2", result[2]);
    }

    @Test
    public void testFlattenDontStopAtNonOption() {
        String[] arguments = {"-a", "value1", "--arg=value2", "nonOption"};
        options.addOption("a", true, "Option A");

        String[] result = posixParser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--arg", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("nonOption", result[4]);
    }

    @Test
    public void testFlattenNoOptions() {
        String[] arguments = {"-a", "value1", "--arg=value2"};
        options.addOption("b", true, "Option B");

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--arg=value2", result[2]);
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] arguments = {"-"};
        options.addOption("a", true, "Option A");

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlattenOptionToken() {
        String[] arguments = {"-a", "value1"};
        options.addOption("a", true, "Option A");

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlattenOptionTokenStopAtNonOption() {
        String[] arguments = {"-a", "value1", "nonOption"};
        options.addOption("a", true, "Option A");

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("nonOption", result[2]);
    }

    @Test
    public void testFlattenOptionTokenMultipleArgs() {
        String[] arguments = {"-a", "value1", "-b", "value2"};
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
    }

    @Test
    public void testFlattenBurstToken() {
        String[] arguments = {"-abc"};
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testFlattenBurstTokenStopAtNonOption() {
        String[] arguments = {"-abc", "nonOption"};
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("nonOption", result[3]);
    }

    @Test
    public void testFlattenBurstTokenNoStopAtNonOption() {
        String[] arguments = {"-abc", "nonOption"};
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        String[] result = posixParser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("nonOption", result[3]);
    }

}