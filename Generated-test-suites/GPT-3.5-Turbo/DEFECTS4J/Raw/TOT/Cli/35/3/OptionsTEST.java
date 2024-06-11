import org.junit.Test;
import org.junit.Assert;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "backup", false, "backup option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);

        Assert.assertEquals(optionGroup, options.getOptionGroup(option1));
        Assert.assertEquals(optionGroup, options.getOptionGroup(option2));
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("add"));
        Assert.assertTrue(options.hasOption("b"));
        Assert.assertTrue(options.hasOption("backup"));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("t", "testing", false, "testing option");
        options.addOption(option);

        Assert.assertEquals(option, options.getOption("t"));
        Assert.assertEquals(option, options.getOption("testing"));
        Assert.assertTrue(options.hasOption("t"));
        Assert.assertTrue(options.hasOption("testing"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "backup", false, "backup option");
        options.addOption(option1);
        options.addOption(option2);

        Assert.assertTrue(options.getOptions().contains(option1));
        Assert.assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "backup", false, "backup option");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        Assert.assertEquals(1, options.getRequiredOptions().size());
        Assert.assertTrue(options.getRequiredOptions().contains(option1));
        Assert.assertFalse(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("t", "testing", false, "testing option");
        options.addOption(option);

        Assert.assertEquals(option, options.getOption("t"));
        Assert.assertEquals(option, options.getOption("testing"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("abc", "abcd", false, "abcd option");
        options.addOption(option1);
        options.addOption(option2);

        Assert.assertEquals(2, options.getMatchingOptions("a").size());
        Assert.assertTrue(options.getMatchingOptions("a").contains("add"));
        Assert.assertTrue(options.getMatchingOptions("a").contains("abcd"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("t", "testing", false, "testing option");
        options.addOption(option);

        Assert.assertTrue(options.hasOption("t"));
        Assert.assertTrue(options.hasOption("testing"));
        Assert.assertFalse(options.hasOption("a"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("t", "testing", false, "testing option");
        options.addOption(option);

        Assert.assertTrue(options.hasLongOption("testing"));
        Assert.assertFalse(options.hasLongOption("t"));
        Assert.assertFalse(options.hasLongOption("a"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("t", "testing", false, "testing option");
        options.addOption(option);

        Assert.assertTrue(options.hasShortOption("t"));
        Assert.assertFalse(options.hasShortOption("testing"));
        Assert.assertFalse(options.hasShortOption("a"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option = new Option("t", "testing", false, "testing option");
        options.addOption(option);

        String expected = "[ Options: [ short {t=Option: { shortOpt: 't', longOpt: 'testing', hasArg: false, description: 'testing option' }} ] [ long {}]";
        Assert.assertEquals(expected, options.toString());
    }
}