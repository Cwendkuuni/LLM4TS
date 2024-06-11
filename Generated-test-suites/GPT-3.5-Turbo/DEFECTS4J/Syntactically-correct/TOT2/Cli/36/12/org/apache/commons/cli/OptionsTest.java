import org.junit.Test;
import org.junit.Assert;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Description 1");
        Option option2 = new Option("o2", "opt2", false, "Description 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        Assert.assertEquals(group, options.getOptionGroup(option1));
        Assert.assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Description");
        options.addOption(option);

        Assert.assertTrue(options.hasOption("o"));
        Assert.assertTrue(options.hasLongOption("opt"));
        Assert.assertTrue(options.hasShortOption("o"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Description");
        options.addOption(option);

        Assert.assertEquals(option, options.getOption("o"));
        Assert.assertEquals(option, options.getOption("opt"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("one", "opt1", false, "Description 1");
        Option option2 = new Option("onetwo", "opt2", false, "Description 2");
        Option option3 = new Option("two", "opt3", false, "Description 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Assert.assertEquals(1, options.getMatchingOptions("o").size());
        Assert.assertEquals(2, options.getMatchingOptions("one").size());
        Assert.assertEquals(0, options.getMatchingOptions("three").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Description");
        options.addOption(option);

        Assert.assertTrue(options.hasOption("o"));
        Assert.assertTrue(options.hasOption("opt"));
        Assert.assertFalse(options.hasOption("invalid"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Description");
        options.addOption(option);

        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertTrue(options.getOptions().contains(option));
    }

}