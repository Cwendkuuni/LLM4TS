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
    public void testFlatten() throws ParseException {
        String[] arguments = {"--foo", "-a", "-b", "-c=d", "-e", "--", "arg1", "arg2"};
        List<String> expected = Arrays.asList("--foo", "-a", "-b", "-c", "d", "--", "arg1", "arg2");
        Options options = new Options();
        options.addOption("foo", true, "f", "Option foo");
        options.addOption("c", true, "c", "Option c");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> actual = Arrays.asList(result);

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)} with stopAtNonOption set to true.
     */
    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] arguments = {"--foo", "-a", "-b", "-c=d", "-e", "--", "arg1", "arg2", "arg3"};
        List<String> expected = Arrays.asList("--foo", "-a", "-b", "-c", "d", "--", "arg1", "arg2", "arg3");
        Options options = new Options();
        options.addOption("foo", true, "f", "Option foo");
        options.addOption("c", true, "c", "Option c");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List<String> actual = Arrays.asList(result);

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)} with unknown option.
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithUnknownOption() throws ParseException {
        String[] arguments = {"--foo", "-x", "-b", "-c=d", "-e", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f", "Option foo");
        options.addOption("c", true, "c", "Option c");

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test method for {@link PosixParser#processSingleHyphen(String)} with single hyphen.
     */
    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");

        List<String> expected = Arrays.asList("-");
        List<String> actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PosixParser#processOptionToken(String, boolean)} with known option.
     */
    @Test
    public void testProcessOptionTokenWithKnownOption() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        parser.options.addOption("foo", true, "f", "Option foo");

        parser.processOptionToken("-f", false);

        List<String> expected = Arrays.asList("-f");
        List<String> actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PosixParser#processOptionToken(String, boolean)} with unknown option and stopAtNonOption set to true.
     */
    @Test
    public void testProcessOptionTokenWithUnknownOptionAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();

        parser.processOptionToken("-x", true);

        List<String> expected = Arrays.asList("--", "-x");
        List<String> actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PosixParser#burstToken(String, boolean)} with valid option.
     */
    @Test
    public void testBurstTokenWithValidOption() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();
        parser.options.addOption("foo", true, "f", "Option foo");

        parser.burstToken("-foobar", false);

        List<String> expected = Arrays.asList("-f", "-o", "o", "b", "a", "r");
        List<String> actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PosixParser#burstToken(String, boolean)} with unknown option and stopAtNonOption set to true.
     */
    @Test
    public void testBurstTokenWithUnknownOptionAndStopAtNonOption() {
        PosixParser parser = new PosixParser();
        parser.options = new Options();

        parser.burstToken("-x", true);

        List<String> expected = Arrays.asList("--", "-x");
        List<String> actual = Arrays.asList((String[]) parser.tokens.toArray(new String[parser.tokens.size()]));

        Assert.assertEquals(expected, actual);
    }

    
}