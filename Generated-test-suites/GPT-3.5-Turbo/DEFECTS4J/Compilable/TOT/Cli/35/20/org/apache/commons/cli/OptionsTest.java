package org.apache.commons.cli;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOption() {
        // create Options object
        Options options = new Options();

        // create Option objects
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", true, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");

        // add options to Options object
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        // assert options are added correctly
        Assert.assertEquals(option1, options.getOption("a"));
        Assert.assertEquals(option2, options.getOption("b"));
        Assert.assertEquals(option3, options.getOption("c"));
    }

    @Test
    public void testAddOptionGroup() {
        // create Options object
        Options options = new Options();

        // create Option objects
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", true, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");
        Option option4 = new Option("d", "optionD", true, "Option D");

        // create OptionGroup object
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option2);
        optionGroup.addOption(option4);

        // add options to Options object
        options.addOption(option1);
        options.addOptionGroup(optionGroup);
        options.addOption(option3);

        // assert option group is added correctly
        Assert.assertEquals(optionGroup, options.getOptionGroup(option2));
    }

    @Test
    public void testGetOptions() {
        // create Options object
        Options options = new Options();

        // create Option objects
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", true, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");

        // add options to Options object
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        // assert options are retrieved correctly
        Assert.assertTrue(options.getOptions().contains(option1));
        Assert.assertTrue(options.getOptions().contains(option2));
        Assert.assertTrue(options.getOptions().contains(option3));
    }

    @Test
    public void testGetRequiredOptions() {
        // create Options object
        Options options = new Options();

        // create Option objects
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", true, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");

        // set options as required
        option1.setRequired(true);
        option3.setRequired(true);

        // add options to Options object
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        // assert required options are retrieved correctly
        Assert.assertTrue(options.getRequiredOptions().contains(option1));
        Assert.assertFalse(options.getRequiredOptions().contains(option2));
        Assert.assertTrue(options.getRequiredOptions().contains(option3));
    }

    @Test
    public void testGetOption() {
        // create Options object
        Options options = new Options();

        // create Option objects
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", true, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");

        // add options to Options object
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        // assert options are retrieved correctly
        Assert.assertEquals(option1, options.getOption("a"));
        Assert.assertEquals(option2, options.getOption("b"));
        Assert.assertEquals(option3, options.getOption("c"));
    }

    @Test
    public void testHasOption() {
        // create Options object
        Options options = new Options();

        // create Option objects
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", true, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");

        // add options to Options object
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        // assert options are checked correctly
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("b"));
        Assert.assertTrue(options.hasOption("c"));
        Assert.assertFalse(options.hasOption("d"));
    }

    @Test
    public void testHasLongOption() {
        // create Options object
        Options options = new Options();

        // create Option objects
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", true, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");

        // add options to Options object
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        // assert options are checked correctly
        Assert.assertTrue(options.hasLongOption("optionA"));
        Assert.assertTrue(options.hasLongOption("optionB"));
        Assert.assertTrue(options.hasLongOption("optionC"));
        Assert.assertFalse(options.hasLongOption("optionD"));
    }

    @Test
    public void testHasShortOption() {
        // create Options object
        Options options = new Options();

        // create Option objects
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", true, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");

        // add options to Options object
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        // assert options are checked correctly
        Assert.assertTrue(options.hasShortOption("a"));
        Assert.assertTrue(options.hasShortOption("b"));
        Assert.assertTrue(options.hasShortOption("c"));
        Assert.assertFalse(options.hasShortOption("d"));
    }

    @Test
    public void testGetOptionGroup() {
        // create Options object
        Options options = new Options();

        // create Option objects
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", true, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");
        Option option4 = new Option("d", "optionD", true, "Option D");

        // create OptionGroup object
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option2);
        optionGroup.addOption(option4);

        // add options to Options object
        options.addOption(option1);
        options.addOptionGroup(optionGroup);
        options.addOption(option3);

        // assert option group is retrieved correctly
        Assert.assertEquals(optionGroup, options.getOptionGroup(option2));
        Assert.assertNull(options.getOptionGroup(option1));
        Assert.assertNull(options.getOptionGroup(option3));
    }

    @Test
    public void testGetMatchingOptions() {
        // create Options object
        Options options = new Options();

        // create Option objects
        Option option1 = new Option("apple", false, "Apple option");
        Option option2 = new Option("banana", false, "Banana option");
        Option option3 = new Option("grape", false, "Grape option");

        // add options to Options object
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        // assert matching options are retrieved correctly
        Assert.assertEquals(2, options.getMatchingOptions("a").size());
        Assert.assertEquals(1, options.getMatchingOptions("b").size());
        Assert.assertEquals(1, options.getMatchingOptions("g").size());
        Assert.assertEquals(0, options.getMatchingOptions("d").size());
    }

}