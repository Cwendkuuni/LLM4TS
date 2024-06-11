package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Options.
 */
public class OptionsTest {

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} .
     */
    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = options.addOption("a", "a", false, "Option A");
        Assert.assertNotNull(option);
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} with argument.
     */
    @Test
    public void testAddOptionWithArgument() {
        Options options = new Options();
        Option option = options.addOption("b", "b", true, "Option B requires an argument");
        Assert.assertNotNull(option);
    }

    /**
     * Test method for {@link Options#addOption(String, String)} with short option.
     */
    @Test
    public void testAddOptionWithShortOption() {
        Options options = new Options();
        Option option = options.addOption("c", "Option C");
        Assert.assertNotNull(option);
    }

    /**
     * Test method for {@link Options#addOption(String, String)} with long option.
     */
    @Test
    public void testAddOptionWithLongOption() {
        Options options = new Options();
        Option option = options.addOption("d", "Option D");
        Assert.assertNotNull(option);
    }

    /**
     * Test method for {@link Options#addOptionGroup(OptionGroup)} .
     */
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("testGroup", "Test Group");
        options.addOptionGroup(group);
        Assert.assertNotNull(group);
    }

    /**
     * Test method for {@link Options#getOptions()} .
     */
    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option = options.addOption("a", "a", false, "Option A");
        List<Option> optionsList = options.getOptions();
        Assert.assertFalse(optionsList.isEmpty());
        Assert.assertTrue(optionsList.contains(option));
    }

    /**
     * Test method for {@link Options#getRequiredOptions()} .
     */
    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = options.addOption("r", "Option R", true, "Required Option");
        List<Object> requiredOptions = options.getRequiredOptions();
        Assert.assertFalse(requiredOptions.isEmpty());
        Assert.assertTrue(requiredOptions.contains(option.getKey()));
    }

    /**
     * Test method for {@link Options#hasOption(String)} with short option.
     */
    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = options.addOption("o", "Option O");
        Assert.assertTrue(options.hasOption("o"));
    }

    /**
     * Test method for {@link Options#hasOption(String)} with long option.
     */
    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = options.addOption("p", "Option P", false, "Option P");
        Assert.assertTrue(options.hasOption("--option-p"));
    }

    /**
     * Test method for {@link Options#hasOption(String)} with invalid option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testHasInvalidOption() {
        Options options = new Options();
        Assert.assertFalse(options.hasOption("i"));
    }

    /**
     * Test method for {@link Options#parse(String[], String)} .
     */
    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptions() {
        Options options = new Options();
        String[] args = {"--invalid"};
        options.parse(args, null);
    }

    /**
     * Test method for {@link Options#parse(String[], String)} with valid options.
     */
    @Test
    public void testParseWithValidOptions() {
        Options options = new Options();
        String[] args = {"--option-a", "value-a", "--option-b", "value-b"};
        try {
            options.parse(args, null);
        } catch (ParseException e) {
            Assert.fail("parse should not throw exception with valid options");
        }
    }
}