package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 1, "Option C");
        options.addOptionGroup(new OptionGroup("group1", "Group 1", Arrays.asList(
                options.addOption("d", false, "Option D"),
                options.addOption("e", false, "Option E")
        )));
        options.addOptionGroup(new OptionGroup("group2", "Group 2", Arrays.asList(
                options.addOption("f", false, "Option F"),
                options.addOption("g", false, "Option G")
        )));
    }

    @Test
    public void testBasicUsage() {
        String[] args = {"-a"};
        try {
            options.parse(args);
            Assert.assertTrue("Option A should be set", options.isSetOption('a'));
            Assert.assertFalse("Option B should not be set", options.isSetOption('b'));
            Assert.assertEquals("Number of options should be 1", 1, options.getOptions().size());
        } catch (ParseException e) {
            Assert.fail("No exceptions expected");
        }
    }

    @Test(expected = ParseException.class)
    public void testMissingMandatoryOption() {
        String[] args = {};
        options.parse(args);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        String[] args = {"-z"};
        options.parse(args);
    }

    @Test
    public void testOptionWithValue() {
        String[] args = {"-c", "valueC"};
        try {
            options.parse(args);
            Assert.assertTrue("Option C should be set with value 'valueC'", options.getOption("c").getValue().equals("valueC"));
        } catch (ParseException e) {
            Assert.fail("No exceptions expected");
        }
    }

    @Test
    public void testGroupOption() {
        String[] args = {"--group1"};
        try {
            options.parse(args);
            Assert.assertTrue("Option D should be set", options.isSetOption('d', true));
            Assert.assertTrue("Option E should be set", options.isSetOption('e', true));
        } catch (ParseException e) {
            Assert.fail("No exceptions expected");
        }
    }

    @Test(expected = ParseException.class)
    public void testMixedShortLongOptions() {
        String[] args = {"-a", "--b", "-c"};
        options.parse(args);
    }

    @Test
    public void testHelp() {
        String[] args = {"--help"};
        try {
            options.parse(args);
        } catch (ParseException e) {
            // expected
        }
        Assert.assertEquals("Help message", options.getHelpFormatter().printHelp("test", options, true));
    }
}