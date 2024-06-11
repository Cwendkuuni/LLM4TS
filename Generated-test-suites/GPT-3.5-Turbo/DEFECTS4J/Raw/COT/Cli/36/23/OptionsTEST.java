import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        Assert.assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        Assert.assertEquals(option, options.getOption("o"));
    }

    @Test
    public void testAddOptionWithArg() {
        Options options = new Options();
        Option option = new Option("o", true, "Option with argument");
        options.addOption(option);

        Assert.assertEquals(option, options.getOption("o"));
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option with long name");
        options.addOption(option);

        Assert.assertEquals(option, options.getOption("option"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        Assert.assertTrue(options.getOptions().contains(option1));
        Assert.assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Required Option");
        option.setRequired(true);
        options.addOption(option);

        Assert.assertTrue(options.getRequiredOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        Assert.assertEquals(option, options.getOption("o"));
        Assert.assertEquals(option, options.getOption("option"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        Assert.assertEquals(1, options.getMatchingOptions("op").size());
        Assert.assertEquals(2, options.getMatchingOptions("o").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        Assert.assertTrue(options.hasOption("o"));
        Assert.assertTrue(options.hasOption("option"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        Assert.assertTrue(options.hasLongOption("option"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        Assert.assertTrue(options.hasShortOption("o"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        Assert.assertEquals(group, options.getOptionGroup(option1));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("p", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {o=Option 1, p=Option 2} ] [ long {option1=Option 1, option2=Option 2} ]";
        Assert.assertEquals(expected, options.toString());
    }
}