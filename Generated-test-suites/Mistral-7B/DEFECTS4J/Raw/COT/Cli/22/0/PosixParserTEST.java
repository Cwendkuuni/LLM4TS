package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--a", "--b", "1", "-c", "-d", "--", "e", "f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("--a", "--b", "1", "-c", "-d", "--", "e", "f");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();

        String[] args = {"--invalid", "-a"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--a", "--b", "1", "-c", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList("--a", "--b", "1", "-c");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testBurstTokenSimple() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");

        String[] args = {"-ab"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("-ab");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testBurstTokenWithArgument() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");

        String[] args = {"-ab", "x"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("-ab", "x");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenInvalidOption() {
        Options options = new Options();

        String[] args = {"-iv", "--invalid"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testBurstTokenMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-ab"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("-a", "-b");
        Assert.assertArrayEquals(expected.toArray(), result);
    }
}