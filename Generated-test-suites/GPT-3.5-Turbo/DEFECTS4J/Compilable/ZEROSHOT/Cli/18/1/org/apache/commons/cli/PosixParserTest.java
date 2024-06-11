package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-a", "value1", "nonOption", "-b", "value2", "--option=value3"};
        options.addOption(new Option("a", true, ""));
        options.addOption(new Option("b", true, ""));
        options.addOption(new Option("option", true, ""));

        String[] result = parser.flatten(options, arguments, true);

        List<String> expected = Arrays.asList("-a", "value1", "nonOption", "-b", "value2", "--", "option", "value3");
        Assert.assertEquals(expected, Arrays.asList(result));
    }

    @Test
    public void testFlatten_NoStopAtNonOption() {
        String[] arguments = {"-a", "value1", "nonOption", "-b", "value2", "--option=value3"};
        options.addOption(new Option("a", true, ""));
        options.addOption(new Option("b", true, ""));
        options.addOption(new Option("option", true, ""));

        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("-a", "value1", "nonOption", "-b", "value2", "--option=value3");
        Assert.assertEquals(expected, Arrays.asList(result));
    }

    @Test
    public void testFlatten_HyphenAsArgument() {
        String[] arguments = {"-a", "-", "--option=value"};
        options.addOption(new Option("a", true, ""));
        options.addOption(new Option("option", true, ""));

        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("-a", "-", "--option=value");
        Assert.assertEquals(expected, Arrays.asList(result));
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};
        options.addOption(new Option("a", false, ""));
        options.addOption(new Option("option", true, ""));

        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList("-");
        Assert.assertEquals(expected, Arrays.asList(result));
    }

}