package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");
        options.addOption("h", "help", false, "print help message");
    }

    @Test
    public void testFlattenBasic() {
        String[] args = {"--input", "input.txt", "-o", "output.txt", "--", "-h"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--input", "input.txt", "--o", "output.txt", "--", "--help");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"--input", "input.txt", "--", "-o", "output.txt", "--help"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("--input", "input.txt", "--o", "output.txt", "--help");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenBurstToken() {
        String[] args = {"-io", "input.txt", "output.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-i", "-o", "input.txt", "output.txt");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenBurstTokenWithArg() {
        String[] args = {"-io", "input.txt", "output.txt", "file.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-i", "-o", "input.txt", "output.txt", "--", "file.txt");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenBurstTokenWithStopAtNonOption() {
        String[] args = {"-io", "input.txt", "output.txt", "file.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("-i", "-o", "input.txt", "output.txt", "file.txt");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        List<String> tokens = parser.processSingleHyphen("-");
        List<String> expected = Arrays.asList("-");
        Assert.assertEquals(expected, tokens);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        List<String> tokens = parser.process("--help");
        List<String> expected = Arrays.asList("--help");
        Assert.assertEquals(expected, tokens);

        tokens = parser.process("--input file.txt");
        expected = Arrays.asList("--input", "file.txt");
        Assert.assertEquals(expected, tokens);

        tokens = parser.process("input.txt");
        Assert.assertEquals(Arrays.asList("--", "input.txt"), tokens);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        List<String> tokens = parser.processOptionToken("-i", false);
        List<String> expected = Arrays.asList("-i");
        Assert.assertEquals(expected, tokens);

        tokens = parser.processOptionToken("-io", false);
        expected = Arrays.asList("-i", "-o");
        Assert.assertEquals(expected, tokens);

        tokens = parser.processOptionToken("--input", false);
        expected = Arrays.asList("--input");
        Assert.assertEquals(expected, tokens);

        tokens = parser.processOptionToken("-i", true);
        expected = Arrays.asList("-i", "");
        Assert.assertEquals(expected, tokens);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        List<String> tokens = parser.burstToken("-io", false);
        List<String> expected = Arrays.asList("-i", "-o");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("-io", true);
        expected = Arrays.asList("-i", "-o");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("--input file.txt", false);
        expected = Arrays.asList("--input", "file.txt");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("--input file.txt", true);
        expected = Arrays.asList("--input", "file.txt");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("-i file.txt", false);
        expected = Arrays.asList("-i", "file.txt");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("-i file.txt", true);
        expected = Arrays.asList("-i", "file.txt");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("-i file.txt arg", false);
        expected = Arrays.asList("-i", "file.txt", "--", "arg");
        Assert.assertEquals(expected, tokens);

        tokens = parser.burstToken("-i file.txt arg", true);
        expected = Arrays.asList("-i", "file.txt", "arg");
        Assert.assertEquals(expected, tokens);
    }
}