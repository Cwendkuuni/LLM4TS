package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    @Test
    public void testParseWithNoOptions() throws ParseException {
        String[] args = new String[0];

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, null);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertNull(cmd.getOptionValues("help"));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        String[] args = new String[]{"-a"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, null);

        Option optionA = new Option("a", true, false, "Option A");
        optionA.setRequired(false);

        Options options = new Options();
        options.addOption(optionA);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(new String[]{"Option A"}, cmd.getOptionValues("a"));
    }

    @Test
    public void testParseWithSingleValueOption() throws ParseException {
        String[] args = new String[]{"-b", "value"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, null);

        Option optionB = new Option("b", true, false, "Option B");
        optionB.setRequired(false);

        Options options = new Options();
        options.addOption(optionB);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(new String[]{"value"}, cmd.getOptionValues("b"));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        String[] args = new String[]{"-a", "-b", "-c"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, null);

        Option optionA = new Option("a", true, false, "Option A");
        optionA.setRequired(false);

        Option optionB = new Option("b", true, false, "Option B");
        optionB.setRequired(false);

        Option optionC = new Option("c", true, false, "Option C");
        optionC.setRequired(false);

        Options options = new Options();
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(3, cmd.getOptions().size());
        Assert.assertEquals(new String[]{"Option A"}, cmd.getOptionValues("a"));
        Assert.assertEquals(new String[]{"Option B"}, cmd.getOptionValues("b"));
        Assert.assertEquals(new String[]{"Option C"}, cmd.getOptionValues("c"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        String[] args = new String[]{"--long-option-a", "value"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, null);

        Option optionLongOptionA = new Option("long-option-a", true, false,  "Long option A");
        optionLongOptionA.setRequired(false);

        Options options = new Options();
        options.addOption(optionLongOptionA);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(new String[]{"value"}, cmd.getOptionValues("long-option-a"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] args = new String[]{"-a"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, null);

        Option optionA = new Option("a", true, true, "Option A");
        optionA.setRequired(true);

        Options options = new Options();
        options.addOption(optionA);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(new String[0], cmd.getOptionValues("a"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));

        try {
            cmd = parser.parse(options, args, null);
        } catch (ParseException e) {
            Assert.assertEquals("Missing required option 'a'", e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionGroups() throws ParseException {
        String[] args = new String[]{"-g1", "-g2", "-a"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, null);

        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption(new Option("a", true, false, "Option A"));

        Option optionG1 = new Option("g1", false, false, "Group 1");
        optionG1.setLongOpt("group1");
        group1.addOption(optionG1);

        Option optionG2 = new Option("g2", false, false, "Group 2");
        optionG2.setLongOpt("group2");
        group1.addOption(optionG2);

        Options options = new Options();
        options.addOptionGroup(group1);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(3, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("g1"));
        Assert.assertTrue(cmd.hasOption("g2"));
        Assert.assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithInvalidOptions() throws ParseException {
        String[] args = new String[]{"--invalid-option"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, null);

        Options options = new Options();
        options.addOption(new Option("valid-option", true, false, "Valid option"));

        try {
            parser.parse(options, args, null);
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: invalid-option", e.getMessage());
        }
    }

    @Test
    public void testParseWithMixedOptionsAndArgs() throws ParseException {
        String[] args = new String[]{"-a", "--long-option-b=value", "-c", "arg1", "arg2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, null);

        Option optionA = new Option("a", true, false, "Option A");
        optionA.setRequired(false);

        Option optionLongOptionB = new Option("long-option-b", true, false, "Long option B");
        optionLongOptionB.setRequired(false);

        Options options = new Options();
        options.addOption(optionA);
        options.addOption(optionLongOptionB);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertEquals(new String[]{"Option A"}, cmd.getOptionValues("a"));
        Assert.assertEquals(new String[]{"value"}, cmd.getOptionValues("long-option-b"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("long-option-b"));
    }
}