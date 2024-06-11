package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = new String[]{"-a", "--optionB", "valueB", "--optionC=valueC", "-d", "argD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertFalse(cmd.hasOption("unknown"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertTrue(cmd.hasOption("d"));
        Assert.assertArrayEquals(new String[]{"valueB"}, cmd.getOptionValues("b"));
        Assert.assertArrayEquals(new String[]{"valueC"}, cmd.getOptionValues("c"));
        Assert.assertArrayEquals(new String[]{"argD"}, cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        String[] args = new String[]{"-a", "--optionB", "valueB", "--optionE=valueE"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();
        parser.parse(options, args);

        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A", true, "This option is required");

        Parser parser = new DefaultParser();
        parser.parse(options, args);

        Assert.fail();
    }

    @Test
    public void testProcessProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB", "valueB");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, null, properties);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertArrayEquals(new String[]{"valueA"}, cmd.getOptionValues("a"));
        Assert.assertArrayEquals(new String[]{"valueB"}, cmd.getOptionValues("b"));
    }

    @Test(expected = ParseException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "Option B requires an argument");

        Parser parser = new DefaultParser();
        parser.parse(options, args);

        Assert.fail();
    }
}

class DefaultParser extends Parser {
    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return Arrays.asList(arguments).toArray(new String[0]);
    }
}