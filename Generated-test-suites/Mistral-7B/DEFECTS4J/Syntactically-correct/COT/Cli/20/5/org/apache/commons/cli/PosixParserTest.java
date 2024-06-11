package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");
        options.addOption("d", 3, "Option D with three arguments");

        String[] args = {"--a", "--b=value", "--c arg1 arg2", "--d arg1 arg2 arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList(new String[]{"--a", "--b", "value", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3"});
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);

        args = new String[]{"--a", "--b=value", "--c arg1 arg2", "--d arg1 arg2 arg3", "otherArg"};
        eatTheRest(parser);
        result = parser.flatten(options, args, true);
        expected = Arrays.asList(new String[]{"--a", "--b", "value", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3", "otherArg"});
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    /**
     * Test method for {@link PosixParser#processOptionToken(String, boolean)}.
     */
    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a");
        PosixParser parser = new PosixParser();

        parser.processOptionToken("--a", false);
        Assert.assertNull(parser.currentOption);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("--a", parser.tokens.get(0));

        parser.init();
        parser.processOptionToken("--a", true);
        Assert.assertEquals("--a", parser.tokens.get(0).toString());
        Assert.assertEquals(Option.BUILDER.hasOptional(), parser.currentOption.getValue().isPresent());
        Assert.assertEquals("a", parser.currentOption.getKey());

        parser.init();
        parser.processOptionToken("--no-option", false);
        Assert.assertNull(parser.currentOption);
        Assert.assertEquals("--no-option", parser.tokens.get(0).toString());
        eatTheRest(parser);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("--no-option", parser.tokens.get(0).toString());
        Assert.assertEquals("otherArg", parser.tokens.get(1).toString());
    }

    /**
     * Test method for {@link PosixParser#burstToken(String, boolean)}.
     */
    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a");
        options.addOption("b");
        options.addOption("c");

        PosixParser parser = new PosixParser();

        parser.burstToken("-ab", false);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("-a", parser.tokens.get(0).toString());
        Assert.assertEquals("-b", parser.tokens.get(1).toString());

        parser.init();
        parser.burstToken("-ab", true);
        Assert.assertEquals(3, parser.tokens.size());
        Assert.assertEquals("-a", parser.tokens.get(0).toString());
        Assert.assertEquals("-b", parser.tokens.get(1).toString());
        Assert.assertEquals("--", parser.tokens.get(2).toString());

        parser.init();
        parser.burstToken("-ac", true);
        Assert.assertEquals(3, parser.tokens.size());
        Assert.assertEquals("-a", parser.tokens.get(0).toString());
        Assert.assertEquals("-c", parser.tokens.get(1).toString());
        Assert.assertEquals("--", parser.tokens.get(2).toString());

        parser.init();
        parser.burstToken("-abc", true);
        Assert.assertEquals(4, parser.tokens.size());
        Assert.assertEquals("-a", parser.tokens.get(0).toString());
        Assert.assertEquals("-c", parser.tokens.get(1).toString());
        Assert.assertEquals("--", parser.tokens.get(2).toString());
        Assert.assertEquals("b", parser.tokens.get(3).toString());
    }

    /**
     * Helper method to simulate gobbling the remaining arguments when
     * <code>stopAtNonOption</code> is set to <code>true</code>.
     *
     * @param parser The parser to eat the rest for.
     */
    private void eatTheRest(PosixParser parser) {
        parser.init();
        parser.gobble(parser.options.commandLineArgs.iterator());
    }

}