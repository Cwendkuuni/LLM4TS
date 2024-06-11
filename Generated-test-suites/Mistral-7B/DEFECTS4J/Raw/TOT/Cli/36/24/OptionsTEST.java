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

        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertFalse(options.hasOption("b"));
        Assert.assertFalse(options.hasLongOption("anOption"));
        Assert.assertFalse(options.hasLongOption("anotherOption"));

        Assert.assertNull(options.getOption("b"));
        Assert.assertNotNull(options.getOption("a"));

        try {
            options.parse("a");
        } catch (ParseException e) {
            Assert.assertEquals("No option 'b'", e.getMessage());
        }

        try {
            options.parse("a");
            Assert.assertTrue(true);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "anArgument", "An option with an argument");

        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.getOption("a").hasArg());

        try {
            options.parse("a value");
            Assert.assertEquals("value", options.getOptionValue("a"));
        } catch (ParseException e) {
            Assert.fail();
        }

        try {
            options.parse("a");
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("The option 'a' requires an argument.", e.getMessage());
        }
    }

    @Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "lo", "A long option");

        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertTrue(options.hasOption("longOption"));
        Assert.assertFalse(options.hasOption("lo"));
        Assert.assertTrue(options.getOption("longOption").hasArg());

        try {
            options.parse("--longOption");
            Assert.assertTrue(true);
        } catch (ParseException e) {
            Assert.fail();
        }

        try {
            options.parse("--longOption value");
            Assert.assertEquals("value", options.getOptionValue("longOption"));
        } catch (ParseException e) {
            Assert.fail();
        }

        try {
            options.parse("--longOption");
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("The option 'longOption' requires an argument.", e.getMessage());
        }
    }

    @Test
    public void testShortAndLongOption() {
        Options options = new Options();
        options.addOption("a", true, "a", "An option with a short name and an argument");
        options.addOption("l", "longOption", false, "A long option");

        Assert.assertEquals(2, options.getOptions().size());
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.getOption("a").hasArg());
        Assert.assertTrue(options.hasOption("l"));
        Assert.assertFalse(options.getOption("l").hasArg());

        try {
            options.parse("a value");
            Assert.assertEquals("value", options.getOptionValue("a"));
            Assert.assertNull(options.getOptionValue("l"));
        } catch (ParseException e) {
            Assert.fail();
        }

        try {
            options.parse("--longOption");
            Assert.assertTrue(true);
            Assert.assertTrue(options.getOption("l").isSet());
        } catch (ParseException e) {
            Assert.fail();
        }

        try {
            options.parse("--longOption value");
            Assert.assertEquals("value", options.getOptionValue("longOption"));
            Assert.assertNull(options.getOptionValue("a"));
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testRequiredOption() {
        Options options = new Options();
        options.addOption("a", true, "a", "A required option");
        options.addOptionGroup(new OptionGroup("group", "A group", new Option[]{}));
        options.addOptionGroup(new OptionGroup("requiredGroup", "A required group", new Option[]{"a"}));

        try {
            options.parse("");
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("Required option 'a' is missing", e.getMessage());
        }
    }

    @Test
    public void testOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "A group", new Option[]{"a", "b"});
        options.addOptionGroup(group);

        Assert.assertEquals(2, options.getOptions().size());
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("b"));
        Assert.assertFalse(options.hasOption("c"));

        try {
            options.parse("--group a");
            Assert.assertEquals("a", options.getOptionValue("a"));
            Assert.assertNull(options.getOptionValue("b"));
        } catch (ParseException e) {
            Assert.fail();
        }

        try {
            options.parse("--group b");
            Assert.assertEquals("b", options.getOptionValue("b"));
            Assert.assertNull(options.getOptionValue("a"));
        } catch (ParseException e) {
            Assert.fail();
        }

        try {
            options.parse("--group c");
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("The option 'c' is not recognized.", e.getMessage());
        }
    }

    @Test
    public void testParseMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "a", "An option with an argument");
        options.addOption("b", true, "b", "Another option with an argument");

        try {
            options.parse("--a valueA --b valueB");
            Assert.assertEquals("valueA", options.getOptionValue("a"));
            Assert.assertEquals("valueB", options.getOptionValue("b"));
        } catch (ParseException e) {
            Assert.fail();
        }

        try {
            options.parse("--a valueA");
            Assert.assertEquals("valueA", options.getOptionValue("a"));
            Assert.assertNull(options.getOptionValue("b"));
        } catch (ParseException e) {
            Assert.fail();
        }

        try {
            options.parse("--b valueB");
            Assert.assertNull(options.getOptionValue("a"));
            Assert.assertEquals("valueB", options.getOptionValue("b"));
        } catch (ParseException e) {
            Assert.fail();
        }

        try {
            options.parse("--a valueA --b valueB --c valueC");
            Assert.assertEquals("valueA", options.getOptionValue("a"));
            Assert.assertEquals("valueB", options.getOptionValue("b"));
            Assert.assertEquals("valueC", options.getOptionValue("c"));
            Assert.assertTrue(options.getOption("c").isSet());
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testParseWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "a", "An option with an argument");

        try {
            options.parse("--invalidOption");
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("Invalid option: 'invalidOption'", e.getMessage());
        }
    }
}