package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws Exception {
        // Test case for parsing options with no arguments
        String[] arguments = new String[]{"-a", "-b", "--optionC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertFalse("Option C should not have a value", cmd.hasOption("c", true));
        Assert.assertTrue("Option A should be present", cmd.hasOption("a"));
        Assert.assertTrue("Option B should be present", cmd.hasOption("b"));
        Assert.assertEquals("Option A value should be true", true, cmd.getOptionValue("a"));
        Assert.assertEquals("Option B value should be true", true, cmd.getOptionValue("b"));
        Assert.assertNull("Option C value should be null", cmd.getOptionValue("c"));

        // Test case for parsing options with arguments
        String[] argumentsWithArgs = new String[]{"-a", "argA", "-b", "argB", "--optionC=argC"};
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        cmd = parser.parse(options, argumentsWithArgs, true);

        Assert.assertEquals("Option A value should be argA", "argA", cmd.getOptionValue("a"));
        Assert.assertEquals("Option B value should be argB", "argB", cmd.getOptionValue("b"));
        Assert.assertEquals("Option C value should be argC", "argC", cmd.getOptionValue("c"));

        // Test case for parsing options with no arguments and errors
        String[] argumentsWithErrors = new String[]{"-x", "-y", "--optionC"};
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        try {
            cmd = parser.parse(options, argumentsWithErrors, true);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java", options);
            System.out.println(e.getMessage());
            throw e;
        }

        Assert.fail("Should have thrown ParseException");
    }

    @Test
    public void testParseWithProperties() throws Exception {
        // Test case for parsing options with properties
        String[] arguments = new String[]{"--optionA=valueA", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true, new Properties());

        Assert.assertEquals("Option A value should be valueA", "valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("Option B value should be valueB", "valueB", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        // Test case for parsing options with stopAtNonOption=true
        String[] arguments = new String[]{"-a", "argA", "-", "value", "--optionC=argC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("c", true, "Option C");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true, new Properties(), true);

        Assert.assertEquals("Option A value should be argA", "argA", cmd.getOptionValue("a"));
        Assert.assertEquals("Option C value should be argC", "argC", cmd.getOptionValue("c"));
        Assert.assertArrayEquals("Args should be parsed correctly", new String[]{"value"}, cmd.getArgs());
    }
}