
import org.junit.Test;
import org.junit.Assert;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "option A"));
        group.addOption(new Option("b", "option B"));
        options.addOptionGroup(group);

        // Check if the group is added to the Options
        Assert.assertTrue(options.getOptionGroups().contains(group));

        // Check if the options in the group are added to the Options
        Assert.assertTrue(options.getOptions().contains(new Option("a", "option A")));
        Assert.assertTrue(options.getOptions().contains(new Option("b", "option B")));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "option A");
        options.addOption(option);

        // Check if the option is added to the shortOpts map
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Options options = new Options();
        Option option = new Option("a", "option A", true, "Description");
        options.addOption(option);

        // Check if the option is added to the longOpts map
        Assert.assertTrue(options.hasLongOption("option A"));
        Assert.assertEquals(option, options.getOption("option A"));
    }

    @Test
    public void testAddOptionWithoutArg() {
        Options options = new Options();
        Option option = new Option("a", "option A");
        options.addOption(option);

        // Check if the option is added without arg
        Assert.assertFalse(option.hasArg());
    }

    @Test
    public void testAddOptionWithArg() {
        Options options = new Options();
        Option option = new Option("a", "option A", true, "Description");
        options.addOption(option);

        // Check if the option is added with arg
        Assert.assertTrue(option.hasArg());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        options.addOption(option1);
        options.addOption(option2);

        // Check if all options are returned
        Assert.assertTrue(options.getOptions().contains(option1));
        Assert.assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        option1.setRequired(true);
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        // Check if all required options are returned
        Assert.assertTrue(options.getRequiredOptions().contains(option1));
        Assert.assertTrue(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "option A");
        options.addOption(option);

        // Check if the correct option is returned
        Assert.assertEquals(option, options.getOption("a"));
        Assert.assertEquals(option, options.getOption("option A"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("aa", "option AA");
        options.addOption(option1);
        options.addOption(option2);

        // Check if the matching options are returned
        Assert.assertEquals(1, options.getMatchingOptions("a").size());
        Assert.assertEquals("option A", options.getMatchingOptions("a").get(0));
        Assert.assertEquals(2, options.getMatchingOptions("aa").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "option A");
        options.addOption(option);

        // Check if the hasOption method works correctly
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("option A"));
        Assert.assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "option A");
        options.addOption(option);

        // Check if the hasLongOption method works correctly
        Assert.assertTrue(options.hasLongOption("option A"));
        Assert.assertFalse(options.hasLongOption("option B"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option A");
        options.addOption(option);

        // Check if the hasShortOption method works correctly
        Assert.assertTrue(options.hasShortOption("a"));
        Assert.assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "option A");
        group.addOption(option);
        options.addOptionGroup(group);

        // Check if the correct option group is returned
        Assert.assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        options.addOption(option1);
        options.addOption(option2);

        // Check if the toString method returns the correct string
        Assert.assertEquals("[ Options: [ short {a=option A, b=option B} ] [ long {}]", options.toString());
    }
}

