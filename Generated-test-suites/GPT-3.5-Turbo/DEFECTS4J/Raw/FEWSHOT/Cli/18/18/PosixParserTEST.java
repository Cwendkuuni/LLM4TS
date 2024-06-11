package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();

        // add sample options
        options.addOption("a", false, "Sample option a");
        options.addOption("b", true, "Sample option b");
        options.addOption("c", "cOption", true, "Sample option c");
    }

    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = {};
        String[] flattened = parser.flatten(options, arguments, true);

        assertTrue(flattened.length == 0);
    }

    @Test
    public void testFlattenNoOptions() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertTrue(flattened.length == 3);
        assertEquals("arg1", flattened[0]);
        assertEquals("arg2", flattened[1]);
        assertEquals("arg3", flattened[2]);
    }

    @Test
    public void testFlattenWithOption() {
        String[] arguments = {"-a"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertTrue(flattened.length == 1);
        assertEquals("-a", flattened[0]);
    }

    @Test
    public void testFlattenWithOptionAndArgument() {
        String[] arguments = {"-b", "value"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertTrue(flattened.length == 2);
        assertEquals("-b", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testFlattenWithOptionAndEquals() {
        String[] arguments = {"-c=value"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertTrue(flattened.length == 2);
        assertEquals("-c", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-a", "arg1", "-b", "value"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertTrue(flattened.length == 1);
        assertEquals("-a", flattened[0]);
    }

    @Test
    public void testFlattenNotStopAtNonOption() {
        String[] arguments = {"-a", "arg1", "-b", "value"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertTrue(flattened.length == 5);
        assertEquals("-a", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value", flattened[3]);
        assertEquals(null, flattened[4]);
    }

    @Test
    public void testFlattenMultipleOptions() {
        String[] arguments = {"-a", "-b", "value", "-c=value"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertTrue(flattened.length == 5);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value", flattened[2]);
        assertEquals("-c", flattened[3]);
        assertEquals("value", flattened[4]);
    }

    @Test
    public void testFlattenMultipleOptionsMixed() {
        String[] arguments = {"-a", "arg1", "-b", "value", "-c=value", "arg2"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertTrue(flattened.length == 5);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value", flattened[2]);
        assertEquals("-c", flattened[3]);
        assertEquals("value", flattened[4]);
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] arguments = {"-"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertTrue(flattened.length == 1);
        assertEquals("-", flattened[0]);
    }
}