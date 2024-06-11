package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        Assert.assertFalse(parser.eatTheRest);
        Assert.assertEquals(0, parser.tokens.size());
        Assert.assertNull(parser.currentOption);
    }

    @Test
    public void testFlattenBasic() {
        String[] args = {"--foo", "bar", "-x", "-y", "--", "z"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("x", false, "x");
        options.addOption("y", false, "y");

        String[] expected = {"--foo", "bar", "-x", "-y", "--", "z"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"--foo", "bar", "-z", "baz"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("x", false, "x");
        options.addOption("y", false, "y");

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithArg() {
        String[] args = {"--foo", "bar", "--baz=qux"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("baz", true, "b");

        String[] expected = {"--foo", "bar", "--baz", "qux"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenSingleOption() {
        PosixParser parser = new PosixParser();
        String token = "-x";

        List<String> expected = Arrays.asList("-x");
        List<String> result = parser.burstToken(token, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenTwoCharacterOption() {
        PosixParser parser = new PosixParser();
        String token = "--foo";

        List<String> expected = Arrays.asList("-foo");
        List<String> result = parser.burstToken(token, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenLongOptionWithArg() {
        PosixParser parser = new PosixParser();
        String token = "--long-option=value";

        List<String> expected = Arrays.asList("-long-option", "value");
        List<String> result = parser.burstToken(token, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenLongOptionWithoutArg() {
        PosixParser parser = new PosixParser();
        String token = "--long-option";

        List<String> expected = Arrays.asList("-long-option");
        List<String> result = parser.burstToken(token, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenLongOptionWithMultipleArgs() {
        PosixParser parser = new PosixParser();
        String token = "--long-option=value1 value2";

        List<String> expected = Arrays.asList("-long-option", "value1", "value2");
        List<String> result = parser.burstToken(token, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenInvalidOption() {
        PosixParser parser = new PosixParser();
        String token = "--invalid";

        List<String> expected = Arrays.asList("--invalid");
        List<String> result = parser.burstToken(token, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-x";

        List<String> expected = Arrays.asList("-x");
        List<String> result = parser.burstToken(token, true);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBurstTokenStopAtNonOptionWithArgument() {
        PosixParser parser = new PosixParser();
        String token = "--foo=bar";

        List<String> expected = Arrays.asList("--foo=bar");
        List<String> result = parser.burstToken(token, true);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "-x", "-y", "--", "z"};
        parser.init();
        parser.flatten(new Options(), args, false);

        ArrayList tokens = (ArrayList) parser.tokens;
        List<String> expected = Arrays.asList("--foo", "-x", "-y", "--", "z");
        Assert.assertEquals(expected, tokens);

        ArrayList tokensAfterGobble = new ArrayList();
        parser.gobble(tokens.iterator());
        Assert.assertEquals(2, tokensAfterGobble.size());
        Assert.assertEquals("--", tokensAfterGobble.get(0));
        Assert.assertEquals("z", tokensAfterGobble.get(1));
    }
}