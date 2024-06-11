package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOption_EmptyArguments_ReturnsEmptyArray() {
        Options options = new Options();

        String[] arguments = {};
        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(0, result.length);
    }
    
    
    @Test
    public void testFlatten_StopAtNonOption_NullOptions_ReturnsEmptyArray() {
        Options options = null;

        String[] arguments = {"--foo", "bar"};
        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_StopAtNonOption_ValidOption_ReturnsOptionValue() {
        Options options = new Options();
        Option option = new Option("f", "foo", true, "Option foo");
        options.addOption(option);

        String[] arguments = {"-f", "bar"};
        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testFlatten_StopAtNonOption_ValidLongOption_ReturnsLongOptionValue() {
        Options options = new Options();
        Option option = new Option("f", "foo", true, "Option foo");
        options.addOption(option);

        String[] arguments = {"--foo", "bar"};
        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testFlatten_StopAtNonOption_InvalidOption_ReturnsOptionWithoutValue() {
        Options options = new Options();
        Option option = new Option("f", "foo", true, "Option foo");
        options.addOption(option);

        String[] arguments = {"-b"};
        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-b", result[0]);
    }

    @Test
    public void testFlatten_StopAtNonOption_InvalidLongOption_ReturnsOptionWithoutValue() {
        Options options = new Options();
        Option option = new Option("f", "foo", true, "Option foo");
        options.addOption(option);

        String[] arguments = {"--bar"};
        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("--bar", result[0]);
    }

    @Test
    public void testFlatten_ContinueAfterNonOption_ValidOption_ReturnsOptionValue() {
        Options options = new Options();
        Option option = new Option("f", "foo", true, "Option foo");
        options.addOption(option);

        String[] arguments = {"-f", "bar1", "--", "bar2"};
        String[] result = posixParser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-f", result[0]);
        assertEquals("bar1", result[1]);
        assertEquals("--", result[2]);
        assertEquals("bar2", result[3]);
    }

    @Test
    public void testFlatten_ContinueAfterNonOption_NoValidOption_ReturnsAllArguments() {
        Options options = new Options();
        Option option = new Option("f", "foo", true, "Option foo");
        options.addOption(option);

        String[] arguments = {"-a", "--bar", "value"};
        String[] result = posixParser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--bar", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_ContinueAfterNonOption_InvalidOptionWithArg_ReturnsAllArguments() {
        Options options = new Options();
        Option option = new Option("f", "foo", true, "Option foo");
        options.addOption(option);

        String[] arguments = {"-a=value", "--bar", "value"};
        String[] result = posixParser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a=value", result[0]);
        assertEquals("--bar", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_ContinueAfterNonOption_InvalidOptionWithoutArg_ReturnsAllArguments() {
        Options options = new Options();
        Option option = new Option("f", "foo", true, "Option foo");
        options.addOption(option);

        String[] arguments = {"-a", "--", "--bar"};
        String[] result = posixParser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
    }
}

