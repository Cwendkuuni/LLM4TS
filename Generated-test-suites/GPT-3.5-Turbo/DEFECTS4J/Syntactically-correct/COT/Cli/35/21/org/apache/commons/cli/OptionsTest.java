import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", true, "Test option 1");
        Option option2 = new Option("b", "option2", false, "Test option 2");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("b"));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("c", "option1", true, "Test option 1");
        Option option2 = new Option("d", "option2", false, "Test option 2");

        options.addOption(option1);
        options.addOption(option2);

        Assert.assertTrue(options.hasOption("c"));
        Assert.assertTrue(options.hasOption("d"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("e", "option1", true, "Test option 1");
        Option option2 = new Option("f", "option2", false, "Test option 2");

        options.addOption(option1);
        options.addOption(option2);

        Assert.assertEquals(2, options.getOptions().size());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("g", "option1", true, "Test option 1");
        Option option2 = new Option("h", "option2", false, "Test option 2");

        option1.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        Assert.assertEquals(1, options.getRequiredOptions().size());
        Assert.assertTrue(options.getRequiredOptions().contains("g"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("i", "option1", true, "Test option 1");
        Option option2 = new Option("j", "option2", false, "Test option 2");

        options.addOption(option1);
        options.addOption(option2);

        Option result = options.getOption("i");
        Assert.assertEquals(option1, result);

        result = options.getOption("j");
        Assert.assertEquals(option2, result);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("k", "option1", true, "Test option 1");
        Option option2 = new Option("ka", "option2", false, "Test option 2");
        Option option3 = new Option("kl", "option3", false, "Test option 3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> result = options.getMatchingOptions("k");
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains("k"));
        Assert.assertTrue(result.contains("ka"));
        Assert.assertTrue(result.contains("kl"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("l", "option1", true, "Test option 1");
        Option option2 = new Option("m", "option2", false, "Test option 2");

        options.addOption(option1);
        options.addOption(option2);

        Assert.assertTrue(options.hasOption("l"));
        Assert.assertTrue(options.hasOption("m"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("n", "option1", true, "Test option 1");
        Option option2 = new Option("o", "option2", false, "Test option 2");

        options.addOption(option1);
        options.addOption(option2);

        Assert.assertTrue(options.hasLongOption("option1"));
        Assert.assertTrue(options.hasLongOption("option2"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("p", "option1", true, "Test option 1");
        Option option2 = new Option("q", "option2", false, "Test option 2");

        options.addOption(option1);
        options.addOption(option2);

        Assert.assertTrue(options.hasShortOption("p"));
        Assert.assertTrue(options.hasShortOption("q"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("r", "option1", true, "Test option 1");
        Option option2 = new Option("s", "option2", false, "Test option 2");

        option1.setRequired(true);

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        OptionGroup result = options.getOptionGroup(option1);
        Assert.assertEquals(optionGroup, result);
    }
}