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
    }

    @Test
    public void testFlattenNoOptions() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(arguments.length, result.length);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlattenSingleOption() {
        options.addOption("a", false, "Option a");
        String[] arguments = {"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(arguments.length, result.length);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        
        String[] arguments = {"-a", "arg1", "-b", "value1", "--", "-c", "arg2"};
        String[] expected = {"-a", "arg1", "-b", "value1", "--", "-c", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOption() {
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        
        String[] arguments = {"--a", "arg1", "--b=value1", "--c", "arg2"};
        String[] expected = {"--a", "arg1", "--b", "value1", "--c", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        
        String[] arguments = {"-a", "arg1", "-b", "value1", "non-option"};
        String[] expected = {"-a", "arg1", "-b", "value1", "non-option"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNoStopAtNonOption() {
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        
        String[] arguments = {"-a", "arg1", "-b", "value1", "non-option"};
        String[] expected = {"-a", "arg1", "-b", "value1", "non-option"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result);
    }
}