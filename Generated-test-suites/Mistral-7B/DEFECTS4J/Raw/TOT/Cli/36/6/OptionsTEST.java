package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testSimpleOption() {
        Options options = new Options();
        options.addOption("a", "anOption", false, "A simple option");
        String[] args = {"-a"};

        try {
            options.parse(args);
            Assert.assertTrue(true);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionWithValue() {
        Options options = new Options();
        options.addOption("b", "anOptionWithValue", true, "An option with a value");
        String[] args = {"-b", "someValue"};

        try {
            Options parsedOptions = options.parse(args);
            Assert.assertEquals("someValue", parsedOptions.getOptionValue("b"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "A group of options");
        group.addOption(new Option("g1", "groupOption1", false, "Option 1 in group"));
        group.addOption(new Option("g2", "groupOption2", false, "Option 2 in group"));

        options.addOptionGroup(group);
        String[] args = {"--group", "-g1", "-g2"};

        try {
            Options parsedOptions = options.parse(args);
            Assert.assertTrue(parsedOptions.isSetOption("g1"));
            Assert.assertTrue(parsedOptions.isSetOption("g2"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMissingOption() {
        Options options = new Options();
        options.addOption("a", "anOption", false, "A simple option");
        String[] args = {"--notAnOption"};

        try {
            options.parse(args);
            Assert.fail("Expected ParseException, but none was thrown");
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: notAnOption", e.getMessage());
        }
    }

    @Test
    public void testMissingRequiredOption() {
        Options options = new Options();
        options.addOption("a", "anOption", true, "A required option");
        String[] args = {};

        try {
            options.parse(args);
            Assert.fail("Expected ParseException, but none was thrown");
        } catch (ParseException e) {
            Assert.assertEquals("The following required options do not have values: a", e.getMessage());
        }
    }

    @Test
    public void testInvalidOptionValue() {
        Options options = new Options();
        options.addOption("a", "anOptionWithValue", true, "An option with a value");
        String[] args = {"-a", "invalidValue"};

        try {
            options.parse(args);
            Assert.fail("Expected ParseException, but none was thrown");
        } catch (ParseException e) {
            Assert.assertEquals("Invalid value for option: anOptionWithValue. Valid values are: [invalidValue]", e.getMessage());
        }
    }
}