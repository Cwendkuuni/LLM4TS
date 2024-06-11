package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;



public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        Assert.assertNull(parser.getCurrentOption());
        Assert.assertEquals(0, parser.getTokens().size());
        Assert.assertFalse(parser.isEatTheRest());

        parser.init();

        Assert.assertNull(parser.getCurrentOption());
        Assert.assertEquals(0, parser.getTokens().size());
        Assert.assertFalse(parser.isEatTheRest());
    }

    @Test
    public void testFlattenEmptyArgs() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        String[] args = {};
        String[] result = parser.flatten(parser.getOptions(), args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSimpleArgs() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        String[] args = {"--foo", "--bar", "--baz"};
        String[] result = parser.flatten(parser.getOptions(), args, false);
        List<String> expected = Arrays.asList("--foo", "--bar", "--baz");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenSingleArg() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        String[] args = {"--foo"};
        String[] result = parser.flatten(parser.getOptions(), args, false);
        Assert.assertArrayEquals(new String[]{"--foo"}, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        String[] args = {"-"};
        String[] result = parser.flatten(parser.getOptions(), args, false);
        Assert.assertArrayEquals(new String[]{"-"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullArgs() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        String[] args = null;
        parser.flatten(parser.getOptions(), args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        String[] args = {"--foo", "-a", "--bar", "-b", "--baz"};
        String[] result = parser.flatten(parser.getOptions(), args, true);
        List<String> expected = Arrays.asList("--foo", "-a", "--bar", "-b");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testBurstTokenSingleArg() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        String token = "-a";

        parser.burstToken(token, false);
        List<String> expected = Arrays.asList("-a");
        Assert.assertEquals(expected, parser.getTokens());

        parser.init();
        parser.burstToken(token, true);
        expected = Arrays.asList("--", "-a");
        Assert.assertEquals(expected, parser.getTokens());
    }

    @Test
    public void testBurstTokenOptionWithArg() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        parser.addOption("a", true, "description");

        String token = "-a";
        String arg = "value";

        parser.burstToken(token, false);
        parser.process(arg);

        List<String> expected = Arrays.asList("-a", "value");
        Assert.assertEquals(expected, parser.getTokens());

        parser.init();
        parser.burstToken(token, true);
        expected = Arrays.asList("--a", "value");
        Assert.assertEquals(expected, parser.getTokens());
    }

    @Test
    public void testBurstTokenOptionWithoutArg() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        parser.addOption("a", false, "description");

        String token = "-a";

        parser.burstToken(token, false);
        List<String> expected = Arrays.asList("-a");
        Assert.assertEquals(expected, parser.getTokens());

        parser.init();
        parser.burstToken(token, true);
        expected = Arrays.asList("--a");
        Assert.assertEquals(expected, parser.getTokens());
    }

    @Test
    public void testBurstTokenInvalidOption() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        String token = "-x";

        parser.burstToken(token, false);
        List<String> expected = Arrays.asList("--", "-x");
        Assert.assertEquals(expected, parser.getTokens());

        parser.init();
        parser.burstToken(token, true);
        expected = Arrays.asList("--", "-x");
        Assert.assertEquals(expected, parser.getTokens());
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        parser.addOption("a", true, "description");

        String token = "--bar";

        parser.process(token);
        List<String> expected = Arrays.asList("--bar");
        Assert.assertEquals(expected, parser.getTokens());

        parser.init();
        parser.process(token);
        expected = Arrays.asList("--", "--bar");
        Assert.assertEquals(expected, parser.getTokens());
    }

    @Test
    public void testProcessOptionWithArg() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        parser.addOption("a", true, "description");

        String token = "--a=value";

        parser.process(token);
        List<String> expected = Arrays.asList("--a", "value");
        Assert.assertEquals(expected, parser.getTokens());

        parser.init();
        parser.process(token);
        expected = Arrays.asList("--", "--a=value");
        Assert.assertEquals(expected, parser.getTokens());
    }

    @Test
    public void testProcessOptionWithoutArg() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        parser.addOption("a", false, "description");

        String token = "--a";

        parser.process(token);
        List<String> expected = Arrays.asList("--a");
        Assert.assertEquals(expected, parser.getTokens());

        parser.init();
        parser.process(token);
        expected = Arrays.asList("--", "--a");
        Assert.assertEquals(expected, parser.getTokens());
    }

    @Test
    public void testProcessInvalidToken() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        String token = "--invalid";

        parser.process(token);
        List<String> expected = Arrays.asList("--", "--invalid");
        Assert.assertEquals(expected, parser.getTokens());

        parser.init();
        parser.process(token);
        expected = Arrays.asList("--", "--invalid");
        Assert.assertEquals(expected, parser.getTokens());
    }

    @Test
    public void testProcessOptionWithMultipleArgs() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());

        parser.addOption("a", true, "description");

        String token = "--a=value1 value2";

        parser.process(token);
        List<String> expected = Arrays.asList("--a", "value1", "value2");
        Assert.assertEquals(expected, parser.getTokens());

        parser.init();
        parser.process(token);
        expected = Arrays.asList("--", "--a=value1 value2");
        Assert.assertEquals(expected, parser.getTokens());
    }

    private PosixParser getParser() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.setOptions(new Options());
        return parser;
    }

    private List<String> getTokens(PosixParser parser) {
        return (List<String>) parser.getTokens().toArray(new String[parser.getTokens().size()]);
    }
}
