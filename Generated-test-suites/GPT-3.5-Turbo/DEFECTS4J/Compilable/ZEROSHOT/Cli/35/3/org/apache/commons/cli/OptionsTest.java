package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        Option option3 = new Option("o3", "option3", true, "Option 3");

        OptionGroup group1 = new OptionGroup()
                .addOption(option1)
                .addOption(option2)
                .addOption(option3);

        Options options = new Options().addOptionGroup(group1);

        Assert.assertTrue(options.hasOption("o1"));
        Assert.assertTrue(options.hasOption("o2"));
        Assert.assertTrue(options.hasOption("o3"));

        Assert.assertEquals(group1, options.getOptionGroup(option1));
        Assert.assertEquals(group1, options.getOptionGroup(option2));
        Assert.assertEquals(group1, options.getOptionGroup(option3));
    }

    @Test
    public void testAddOption() {
        Options options = new Options()
                .addOption("o1", "option1", true, "Option 1")
                .addOption("o2", "option2", false, "Option 2");

        Assert.assertTrue(options.hasOption("o1"));
        Assert.assertTrue(options.hasOption("o2"));

        Option option1 = options.getOption("o1");
        Option option2 = options.getOption("o2");

        Assert.assertEquals("o1", option1.getOpt());
        Assert.assertEquals("option1", option1.getLongOpt());
        Assert.assertEquals(true, option1.hasArg());
        Assert.assertEquals("Option 1", option1.getDescription());

        Assert.assertEquals("o2", option2.getOpt());
        Assert.assertEquals("option2", option2.getLongOpt());
        Assert.assertEquals(false, option2.hasArg());
        Assert.assertEquals("Option 2", option2.getDescription());
    }

    @Test
    public void testGetOption() {
        Options options = new Options()
                .addOption("o1", "option1", true, "Option 1");

        Option option = options.getOption("o1");

        Assert.assertEquals("o1", option.getOpt());
        Assert.assertEquals("option1", option.getLongOpt());
        Assert.assertEquals(true, option.hasArg());
        Assert.assertEquals("Option 1", option.getDescription());
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options()
                .addOption("o1", "option1", true, "Option 1")
                .addOption("o2", "option2", false, "Option 2")
                .addOption("o3", "option3", true, "Option 3");

        Assert.assertEquals(2, options.getMatchingOptions("o").size());
        Assert.assertEquals(1, options.getMatchingOptions("o1").size());
        Assert.assertEquals(0, options.getMatchingOptions("o4").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options()
                .addOption("o1", "option1", true, "Option 1");

        Assert.assertTrue(options.hasOption("o1"));
        Assert.assertFalse(options.hasOption("o2"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options()
                .addOption("o1", "option1", true, "Option 1");

        Assert.assertTrue(options.hasLongOption("option1"));
        Assert.assertFalse(options.hasLongOption("option2"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options()
                .addOption("o1", "option1", true, "Option 1");

        Assert.assertTrue(options.hasShortOption("o1"));
        Assert.assertFalse(options.hasShortOption("o2"));
    }
}