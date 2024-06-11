package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"--foo", "--bar=value", "-a", "-b", "-c", "arg1", "--", "arg2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        List expected = Arrays.asList(new String[]{"--foo", "--bar", "value", "-a", "-b", "arg1", "--", "arg2"});

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        // test with stopAtNonOption = true
        args = new String[]{"--foo", "--bar=value", "-a", "-b", "-c", "arg1", "--", "arg2", "-d"};
        expected = Arrays.asList(new String[]{"--foo", "--bar", "value", "-a", "-b", "arg1", "--", "arg2", "-d"});
        result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("foo", false, null, "Option 'foo'");
        String[] args = {"-f", "--foo"};

        parser.processOptionToken("-f", false);
        Assert.assertNull(parser.currentOption);
        List<String> tokens = parser.tokens;
        Assert.assertEquals(Arrays.asList("--", "-f"), tokens);

        parser.init();
        parser.processOptionToken("--foo", false);
        Option option = options.getOption("foo");
        Assert.assertEquals(option, parser.currentOption);
        tokens = parser.tokens;
        Assert.assertEquals(Arrays.asList("-", "-f"), tokens);
    }

    @Test(expected = ParseException.class)
    public void testProcessOptionTokenInvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"--invalidOption"};

        parser.processOptionToken("--invalidOption", false);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"-abcd"};

        parser.burstToken("-abcd", false);
        List<String> tokens = parser.tokens;
        Assert.assertEquals(Arrays.asList("-a", "-b", "-c", "-d"), tokens);

        tokens.clear();
        parser.init();
        parser.burstToken("-abcd", true);
        tokens = parser.tokens;
        Assert.assertEquals(Arrays.asList("--abcd"), tokens);
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenNoOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"nonOption"};

        parser.burstToken("nonOption", false);
    }
}