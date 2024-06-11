package org.apache.commons.cli;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] args = {"--foo", "-a", "-b", "--bar=value", "-c", "--baz"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List expected = Arrays.asList(new String[]{"--foo", "-a", "-b", "--bar", "value", "-c", "--baz"});
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--foo", "-a", "-b", "--bar=value", "-c", "--baz", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List expected = Arrays.asList(new String[]{"--foo", "-a", "-b", "--bar", "value", "--baz", "nonOption"});
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testProcessLongOptionWithValue() {
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo"};
        PosixParser parser = new PosixParser();

        parser.init();
        parser.flatten(options, args, false);

        Assert.assertEquals("foo", parser.currentOption.getLongOpt());
        Assert.assertTrue(parser.currentOption.isSet());
    }

    @Test
    public void testProcessLongOptionWithoutValue() {
        Options options = new Options();
        options.addOption("foo", false, "f");
        String[] args = {"--foo"};
        PosixParser parser = new PosixParser();

        parser.init();
        parser.flatten(options, args, false);

        Assert.assertEquals("foo", parser.currentOption.getLongOpt());
        Assert.assertFalse(parser.currentOption.isSet());
    }

    @Test
    public void testProcessShortOption() {
        Options options = new Options();
        options.addOption("a", true, "a");
        String[] args = {"-a"};
        PosixParser parser = new PosixParser();

        parser.init();
        parser.flatten(options, args, false);

        Assert.assertEquals("a", parser.currentOption.getOpt());
        Assert.assertTrue(parser.currentOption.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testProcessInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();

        parser.init();
        parser.flatten(options, args, false);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        PosixParser parser = new PosixParser();

        String[] args = {"-ab"};
        parser.init();
        parser.flatten(options, args, false);

        Assert.assertEquals(Arrays.asList("-a", "-b"), parser.tokens);
    }

    @Test
    public void testBurstTokenWithMultipleArguments() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        PosixParser parser = new PosixParser();

        String[] args = {"-aarg1 arg2 arg3"};
        parser.init();
        parser.flatten(options, args, false);

        Assert.assertEquals(Arrays.asList("-a", "arg1", "arg2", "arg3"), parser.tokens);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "a");
        options.addOption("b", true, "b");
        PosixParser parser = new PosixParser();

        String[] args = {"-aarg1 arg2 arg3", "nonOption"};
        parser.init();
        parser.flatten(options, args, true);

        List expected = Arrays.asList(new String[]{"-a", "arg1", "arg2", "arg3", "nonOption"});
        Assert.assertEquals(expected, parser.tokens);
    }
}