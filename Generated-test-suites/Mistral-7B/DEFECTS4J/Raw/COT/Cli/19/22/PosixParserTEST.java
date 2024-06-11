package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenBasic() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionB", "valueB", "--optionC=valueC", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionB", true, "Option B");
        options.addOption("optionC", true, "Option C");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "--optionB", "valueB", "--optionC=valueC", "--", "arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption"};
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--a", "-b", "--optionB", "valueB", "--", "--optionX", "OptionX"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionB", true, "Option B");

        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("--a", "-b", "--optionB", "valueB", "--optionX", "OptionX");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenBursting() {
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "with", "value", "and", "another", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");

        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-longOption", "-with", "-another", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testBurstingInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption-with-value"};
        Options options = new Options();

        parser.burstToken("--invalidOption-with-value", false);
    }
}