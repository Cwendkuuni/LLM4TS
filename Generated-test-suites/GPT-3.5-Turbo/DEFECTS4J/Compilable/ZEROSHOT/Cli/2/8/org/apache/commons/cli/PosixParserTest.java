package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption_True() {
        String[] arguments = {"-a", "value1", "-b", "value2", "nonOption"};
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("nonOption", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        String[] arguments = {"-a", "value1", "-b", "value2", "nonOption"};
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("nonOption", result[4]);
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] arguments = {"arg1", "arg2", "arg3"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_LongOption() {
        String[] arguments = {"--option1", "arg1", "--option2=value2", "arg2"};

        options.addOption("option1", false, "Option 1");
        options.addOption("option2", true, "Option 2");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("--option2", result[2]);
        assertEquals("value2", result[3]);
    }

    @Test
    public void testFlatten_BurstToken() {
        String[] arguments = {"-abc", "value1", "-def=value2", "arg1"};

        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        options.addOption("e", false, "Option E");
        options.addOption("f", true, "Option F");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
        assertEquals("value1", result[3]);
        assertEquals("-d", result[4]);
        assertEquals("-e", result[5]);
        assertEquals("value2", result[6]);
    }

    @Test
    public void testFlatten_IgnoreUnknownOption() {
        String[] arguments = {"-a", "-b", "-c", "-d"};

        options.addOption("a", false, "Option A");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_AddNonOption() {
        String[] arguments = {"-a", "-b", "-c", "-d"};

        options.addOption("a", false, "Option A");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("-d", result[4]);
    }
}