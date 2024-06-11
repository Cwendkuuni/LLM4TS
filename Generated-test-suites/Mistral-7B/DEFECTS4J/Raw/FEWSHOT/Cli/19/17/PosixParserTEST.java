package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C with arg");
        options.addOption("d", 3, "Option D with arg");
        options.addOption("e", null, "Option E with long arg");
        options.addOption("f", null, "Option F with long arg");
    }

    @Test
    public void testFlatten_Empty() {
        String[] args = {};
        String[] expected = {};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OneArgument() {
        String[] args = {"arg1"};
        String[] expected = {"arg1"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OneOption() {
        String[] args = {"-a"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OneOptionWithArg() {
        String[] args = {"-a", "arg2"};
        String[] expected = {"-a", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = {"-a", "-b", "--d", "arg3", "-c", "arg4", "arg5"};
        String[] expected = {"-a", "-b", "--d", "arg3", "-c", "arg4", "arg5"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SpecialToken() {
        String[] args = {"--", "arg1", "-c", "arg2"};
        String[] expected = {"--", "arg1", "-c", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SpecialTokenWithArguments() {
        String[] args = {"--longOption", "arg1", "arg2"};
        String[] expected = {"--longOption", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "--", "-b", "-c", "--", "arg1"};
        String[] expected = {"-a", "--", "-b", "--", "arg1"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionWithLongOption() {
        String[] args = {"-a", "--longOption", "-c", "--", "arg1"};
        String[] expected = {"-a", "--longOption", "-c", "--", "arg1"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken() {
        String[] args = {"---abcd"};
        String[] expected = {"--", "-a", "-b", "-c", "-d"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstTokenWithStopAtNonOption() {
        String[] args = {"---abcd"};
        String[] expected = {"--", "-a"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MixedArgumentsAndOptions() {
        String[] args = {"arg1", "-a", "-b", "--d", "-c", "arg2", "--longOption"};
        String[] expected = {"arg1", "-a", "-b", "--d", "-c", "arg2", "--longOption"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessOptionToken() {
        parser = new PosixParser();
        String[] args = {"--longOption", "arg1", "arg2"};
        parser.processOptionToken("--longOption", false);
        assertEquals(options.getOption("longOption"), parser.currentOption);
        assertNull(parser.currentOption);
        List<String> result = (List<String>) parser.tokens;
        assertEquals(Arrays.asList("--longOption", "arg1", "arg2"), result);
    }

    @Test
    public void testProcessOptionToken_StopAtNonOption() {
        parser = new PosixParser();
        String[] args = {"--longOption", "arg1", "-c", "arg2"};
        parser.processOptionToken("--longOption", true);
        assertEquals(options.getOption("longOption"), parser.currentOption);
        assertNull(parser.currentOption);
        List<String> result = (List<String>) parser.tokens;
        assertEquals(Arrays.asList("--longOption", "-c"), result);
    }

    @Test
    public void testBurstToken() {
        parser = new PosixParser();
        String[] args = {"---abcd"};
        parser.burstToken("---abcd", false);
        List<String> result = (List<String>) parser.tokens;
        assertEquals(Arrays.asList("--", "-a", "-b", "-c", "-d"), result);
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        parser = new PosixParser();
        String[] args = {"---abcd"};
        parser.burstToken("---abcd", true);
        List<String> result = (List<String>) parser.tokens;
        assertEquals(Arrays.asList("--", "-a"), result);
    }
}