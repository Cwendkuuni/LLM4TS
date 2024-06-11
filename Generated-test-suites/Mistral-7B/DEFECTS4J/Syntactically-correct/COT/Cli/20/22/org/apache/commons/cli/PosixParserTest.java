package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlattenWithoutOptions() {
        String[] arguments = new String[]{"appName", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(new Options(), arguments, false);
        List expected = Arrays.asList("appName", "arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlattenWithOptions() {
        String[] arguments = new String[]{"--option1", "value1", "--option2", "value2", "arg"};
        Options options = new Options();
        options.addOption("option1", true, "o1", "Option 1");
        options.addOption("option2", true, "o2", "Option 2");
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
        List expected = Arrays.asList("--option1", "true", "--option2", "true", "arg");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = new String[]{"--option1", "value1", "-a", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o1", "Option 1");
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);
        List expected = Arrays.asList("--option1", "value1", "-a", "arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        String[] arguments = new String[]{"--invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);
    }

    @Test
    public void testBurstToken() {
        String token = "-foo-bar";
        PosixParser parser = new PosixParser();
        List expected = Arrays.asList("-foo", "--bar");
        String[] flattened = parser.burstToken(token, false);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "-foo-bar-arg";
        PosixParser parser = new PosixParser();
        List expected = Arrays.asList("--", "--foo", "--arg");
        String[] flattened = parser.burstToken(token, true);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), flattened);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        List expected = Collections.singletonList("--");
        parser.init();
        parser.process("--");
        List tokens = parser.getTokens();
        Assert.assertEquals(expected, tokens);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "o", "Option");
        parser.init();
        parser.processOptionToken("option", false);
        List tokens = parser.getTokens();
        List expected = Arrays.asList("-o", "true");
        Assert.assertEquals(expected, tokens);
    }

    @Test
    public void testProcessOptionTokenWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option", true, "o", "Option");
        parser.init();
        parser.processOptionToken("option", true);
        List tokens = parser.getTokens();
        List expected = Arrays.asList("-o");
        Assert.assertEquals(expected, tokens);
    }

    @Test
    public void testProcessWithoutOption() {
        PosixParser parser = new PosixParser();
        List expected = Arrays.asList("--", "arg1", "arg2");
        parser.init();
        parser.process("arg1");
        parser.process("arg2");
        List tokens = parser.getTokens();
        Assert.assertEquals(expected, tokens);
    }
}