package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.junit.Assert;

import java.util.List;

public class OptionsTest {

    @org.junit.Test
    public void testSimpleOption() {
        Options options = new Options();
        options.addOption("a", "anOption", false, "A simple option");

        CommandLineParser parser = new BasicParser();
        String[] args = new String[]{"-a"};
        try {
            CommandLine cmd = parser.parse(options.toArray(), args);
            Assert.assertTrue(cmd.hasOption("a"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parsing failed.");
        }
    }

    @org.junit.Test
    public void testOptionWithValue() {
        Options options = new Options();
        options.addOption("b", "anOption", true, "An option with a value");

        CommandLineParser parser = new BasicParser();
        String[] args = new String[]{"-b", "someValue"};
        try {
            CommandLine cmd = parser.parse(options.toArray(), args);
            Assert.assertEquals("someValue", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parsing failed.");
        }
    }

    @org.junit.Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("longOption", "aLongOption", false, "A long option");

        CommandLineParser parser = new BasicParser();
        String[] args = new String[]{"--aLongOption"};
        try {
            CommandLine cmd = parser.parse(options.toArray(), args);
            Assert.assertTrue(cmd.hasOption("longOption"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parsing failed.");
        }
    }

    @org.junit.Test
    public void testOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group1", "A test group");
        group.addOption(new Option("g", "groupOption", false, "A group option"));

        options.addOptionGroup(group);

        CommandLineParser parser = new BasicParser();
        String[] args = new String[]{"-g"};
        try {
            CommandLine cmd = parser.parse(options.toArray(), args);
            Assert.assertTrue(cmd.hasOption("g"));
            Assert.assertEquals("group1", cmd.getOptionGroup("group1").getName());
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parsing failed.");
        }
    }

    @org.junit.Test
    public void testRequiredOption() {
        Options options = new Options();
        options.addOption("r", "aRequiredOption", true, "A required option");
        options.addOption("r", "anotherOption", false, "Another option");

        CommandLineParser parser = new BasicParser();
        String[] args = new String[]{"--anotherOption"};
        try {
            CommandLine cmd = parser.parse(options.toArray(), args);
            Assert.assertFalse(cmd.hasOption("r"));
            Assert.fail("A required option was not provided.");
        } catch (ParseException e) {
            // Expected
        }

        args = new String[]{"--r"};
        try {
            CommandLine cmd = parser.parse(options.toArray(), args);
            Assert.assertTrue(cmd.hasOption("r"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parsing failed.");
        }
    }

    @org.junit.Test
    public void testParseWithInvalidOption() {
        Options options = new Options();
        options.addOption("option1", true, "An option");

        CommandLineParser parser = new BasicParser();
        String[] args = new String[]{"--invalidOption"};
        try {
            parser.parse(options.toArray(), args);
            Assert.fail("Parsing should have failed.");
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: invalidOption", e.getMessage());
        }
    }
}