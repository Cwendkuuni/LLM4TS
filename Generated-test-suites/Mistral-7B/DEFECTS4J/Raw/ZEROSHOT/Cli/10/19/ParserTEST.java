package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @Test
    public void testParse() throws Exception {
        // Create a new Options object
        Options options = new Options();

        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", false, "Option C");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("d", "optionD", true, "Option D")
                .addOption("e", "optionE", false, "Option E")
                .addOption("f", "optionF", false, "Option F"));
        options.addOptionGroup(new OptionGroup("group2", "Group 2")
                .addOption("g", "optionG", true, "Option G"));

        // Create a new Parser instance
        Parser parser = new DefaultParser();

        // Test parsing with no arguments
        try {
            parser.parse(options, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing with incorrect arguments
        try {
            parser.parse(options, new String[]{"--optional-arg", "--incorrect-option"});
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: incorrect-option", e.getMessage());
        }

        // Test parsing with required options
        String[] args = new String[]{"--optionA", "--optionD"};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("optionA"));
        Assert.assertTrue(cmd.hasOption("optionD"));
        Assert.assertFalse(cmd.hasOption("optionB"));
        Assert.assertFalse(cmd.hasOption("optionC"));
        Assert.assertFalse(cmd.hasOption("optionE"));
        Assert.assertFalse(cmd.hasOption("optionF"));
        Assert.assertFalse(cmd.hasOption("optionG"));

        // Test parsing with optional options
        args = new String[]{"--optionB", "--optionC", "--optionE", "--group1", "--optionF", "--group2", "--optionG"};
        cmd = parser.parse(options, args);
        Assert.assertEquals(6, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("optionA"));
        Assert.assertTrue(cmd.hasOption("optionB"));
        Assert.assertTrue(cmd.hasOption("optionC"));
        Assert.assertTrue(cmd.hasOption("optionD"));
        Assert.assertTrue(cmd.hasOption("optionE"));
        Assert.assertTrue(cmd.hasOption("optionF"));
        Assert.assertTrue(cmd.hasOption("optionG"));
        Assert.assertEquals(Arrays.asList("group1", "group2"), cmd.getOptionGroupNames());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     */
    @Test
    public void testParseWithProperties() throws Exception {
        // Create a new Options object
        Options options = new Options();

        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");

        // Test parsing with no arguments
        try {
            Parser parser = new DefaultParser();
            parser.parse(options, new String[0], new Properties());
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing with incorrect arguments
        Properties properties = new Properties();
        properties.setProperty("options.arg", "incorrect-option");
        try {
            Parser parser = new DefaultParser();
            parser.parse(options, new String[]{"--optional-arg"}, properties);
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: incorrect-option", e.getMessage());
        }

        // Test parsing with required options and properties
        properties.setProperty("options.arg.optionA", "valueA");
        String[] args = new String[]{"--optionA"};
        CommandLine cmd = parser.parse(options, args, properties);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("optionA"));
        Assert.assertEquals("valueA", cmd.getOptionValue("optionA"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        // Create a new Options object
        Options options = new Options();

        options.addOption("a", "optionA", true, "Option A");

        // Test parsing with no arguments
        try {
            Parser parser = new DefaultParser();
            parser.parse(options, new String[0], false);
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }

        // Test parsing with required options and stopAtNonOption=true
        String[] args = new String[]{"--optionA", "non-option"};
        try {
            Parser parser = new DefaultParser();
            parser.parse(options, args, true);
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertTrue(e.getMessage().contains("Missing required option"));
        }

        // Test parsing with no required options and stopAtNonOption=true
        args = new String[]{"--non-option"};
        CommandLine cmd = parser.parse(options, args, true);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertFalse(cmd.hasOption("optionA"));
    }
}