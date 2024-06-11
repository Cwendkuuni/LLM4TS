package org.apache.commons.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    

    @Test
    public void testFlatten_BasicUsage() {
        String[] args = {"-a", "--OptionB", "-c=valueC", "--OptionD=valueD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with value");
        options.addOption("d", true, "Option D with value");

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(new String[]{"-a", "--OptionB", "-c", "valueC", "--OptionD", "valueD"});
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "--OptionB", "-c=valueC", "--OptionD=valueD", "nonOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with value");
        options.addOption("d", true, "Option D with value");

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(new String[]{"-a", "--OptionB", "-c=valueC", "--OptionD=valueD", "nonOption"});
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_SpecialTokens() {
        String[] args = {"--OptionA=valueA", "--OptionB=valueB", "--OptionC=valueC", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with value");

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(new String[]{"--OptionA=valueA", "--OptionB=valueB", "--OptionC=valueC", "--"});
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"--invalidOption"};
        Options options = new Options();

        Parser parser = new PosixParser();
        parser.flatten(options, args, false);

        // the test should fail with an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_EmptyArguments() {
        String[] args = {};
        Options options = new Options();

        Parser parser = new PosixParser();
        parser.flatten(options, args, false);

        // the test should fail with an IllegalArgumentException
    }

    
}