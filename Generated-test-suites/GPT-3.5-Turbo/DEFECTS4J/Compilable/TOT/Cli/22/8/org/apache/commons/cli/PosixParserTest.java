package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten_withLongOption_noValue_shouldAddTokenToList() {
        Options options = new Options();
        options.addOption(new Option("foo", false, "Option foo"));
        String[] arguments = {"--foo"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("--foo", result[0]);
    }

    @Test
    public void testFlatten_withLongOption_withValue_shouldAddBothTokensToList() {
        Options options = new Options();
        options.addOption(new Option("foo", true, "Option foo"));
        String[] arguments = {"--foo=bar"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testFlatten_withLongOption_notRegistered_shouldAddTokenToList() {
        Options options = new Options();
        String[] arguments = {"--unknown"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("--unknown", result[0]);
    }

    @Test
    public void testFlatten_withMinusOption_shouldAddTokenToList() {
        Options options = new Options();
        String[] arguments = {"-"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_withShortOption_shouldAddTokenToList() {
        Options options = new Options();
        options.addOption(new Option("f", false, "Option f"));
        String[] arguments = {"-f"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlatten_withShortOption_stopAtNonOption_shouldAddRemainingTokensToList() {
        Options options = new Options();
        options.addOption(new Option("f", false, "Option f"));
        String[] arguments = {"-f", "non-option"};
        String[] result = posixParser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("non-option", result[1]);
    }

    @Test
    public void testFlatten_withShortOption_bursting_shouldAddRemainingTokensToList() {
        Options options = new Options();
        options.addOption(new Option("f", true, "Option f"));
        String[] arguments = {"-fvalue"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_stopAtNonOption_shouldAddSpecialTokenBeforeRemainingTokensToList() {
        Options options = new Options();
        options.addOption(new Option("f", false, "Option f"));
        String[] arguments = {"non-option"};
        String[] result = posixParser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("non-option", result[1]);
    }

    @Test
    public void testFlatten_withNonOption_shouldAddNonOptionTokenToList() {
        Options options = new Options();
        String[] arguments = {"non-option"};
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("non-option", result[0]);
    }
}