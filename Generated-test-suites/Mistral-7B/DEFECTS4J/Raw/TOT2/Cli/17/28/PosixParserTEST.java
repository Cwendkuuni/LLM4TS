package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"--foo", "-o", "--bar=value", "-c", "arg1", "-d", "-e", "--"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option with value");
        options.addOption("c", "c", "C option");
        options.addOption("d", "d", "D option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "-o", "--bar=value", "-c", "arg1", "-d", "-e", "--"}, parsedArgs);

        // Test with stopAtNonOption
        parsedArgs = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--foo", "-o", "--bar=value", "-c", "arg1", "-d", "-e"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws Exception {
        String[] args = {"--invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testBurstToken() {
        String[] args = {"-foobar", "--foo=value", "--bar=value2"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option with value");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-f", "-o", "-o", "b", "-a", "r", "--foo=value", "--bar=value2"}, parsedArgs);
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        String[] args = {"-foobar", "--foo=value", "--bar=value2"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option with value");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"-f", "-o", "o", "b", "a", "r", "--foo=value", "--bar=value2"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenInvalidOption() {
        String[] args = {"-invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}