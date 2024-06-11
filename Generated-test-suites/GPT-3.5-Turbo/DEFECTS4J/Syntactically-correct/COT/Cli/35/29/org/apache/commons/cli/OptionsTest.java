import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Options options = new Options();
        options.addOptionGroup(group);

        Assert.assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "file", true, "Specifies the file");
        Options options = new Options();
        options.addOption(option);

        Assert.assertTrue(options.getOptions().contains(option));
        Assert.assertTrue(options.hasOption("f"));
        Assert.assertTrue(options.hasOption("file"));
        Assert.assertTrue(options.hasLongOption("file"));
        Assert.assertTrue(options.hasShortOption("f"));
    }

    @Test
    public void testAddOption_multipleTimes() {
        Option option1 = new Option("f", "file", true, "Specifies the file");
        Option option2 = new Option("l", "list", false, "Lists the files");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        Assert.assertTrue(options.getOptions().contains(option1));
        Assert.assertTrue(options.getOptions().contains(option2));
        Assert.assertTrue(options.hasOption("f"));
        Assert.assertTrue(options.hasOption("l"));
        Assert.assertTrue(options.hasOption("file"));
        Assert.assertTrue(options.hasOption("list"));
        Assert.assertTrue(options.hasLongOption("file"));
        Assert.assertTrue(options.hasLongOption("list"));
        Assert.assertTrue(options.hasShortOption("f"));
        Assert.assertTrue(options.hasShortOption("l"));
    }

    @Test
    public void testAddOption_noArg() {
        Option option = new Option("v", "version", false, "Prints the version");
        Options options = new Options();
        options.addOption(option);

        Assert.assertTrue(options.getOptions().contains(option));
        Assert.assertTrue(options.hasOption("v"));
        Assert.assertTrue(options.hasOption("version"));
        Assert.assertTrue(options.hasLongOption("version"));
        Assert.assertTrue(options.hasShortOption("v"));
    }

    @Test
    public void testAddOption_withArg() {
        Option option = new Option("p", "path", true, "Specifies the path");
        Options options = new Options();
        options.addOption(option);

        Assert.assertTrue(options.getOptions().contains(option));
        Assert.assertTrue(options.hasOption("p"));
        Assert.assertTrue(options.hasOption("path"));
        Assert.assertTrue(options.hasLongOption("path"));
        Assert.assertTrue(options.hasShortOption("p"));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("f", "file", true, "Specifies the file");
        Options options = new Options();
        options.addOption(option);

        Assert.assertEquals(option, options.getOption("f"));
        Assert.assertEquals(option, options.getOption("file"));
    }

    @Test
    public void testGetOption_nonExisting() {
        Options options = new Options();

        Assert.assertNull(options.getOption("f"));
        Assert.assertNull(options.getOption("file"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("f", "file", true, "Specifies the file");
        Option option2 = new Option("l", "list", false, "Lists the files");
        Option option3 = new Option("m", "merge", false, "Merges the files");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Assert.assertEquals(2, options.getMatchingOptions("f").size());
        Assert.assertEquals(2, options.getMatchingOptions("l").size());
        Assert.assertEquals(1, options.getMatchingOptions("m").size());
        Assert.assertEquals(0, options.getMatchingOptions("x").size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("f", "file", true, "Specifies the file");
        Options options = new Options();
        options.addOption(option);

        Assert.assertTrue(options.hasOption("f"));
        Assert.assertTrue(options.hasOption("file"));
    }

    @Test
    public void testHasOption_nonExisting() {
        Options options = new Options();

        Assert.assertFalse(options.hasOption("f"));
        Assert.assertFalse(options.hasOption("file"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("f", "file", true, "Specifies the file");
        Options options = new Options();
        options.addOption(option);

        Assert.assertTrue(options.hasLongOption("file"));
    }

    @Test
    public void testHasLongOption_nonExisting() {
        Options options = new Options();

        Assert.assertFalse(options.hasLongOption("file"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("f", "file", true, "Specifies the file");
        Options options = new Options();
        options.addOption(option);

        Assert.assertTrue(options.hasShortOption("f"));
    }

    @Test
    public void testHasShortOption_nonExisting() {
        Options options = new Options();

        Assert.assertFalse(options.hasShortOption("f"));
    }

    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("f", "file", true, "Specifies the file");
        Option option2 = new Option("l", "list", false, "Lists the files");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        Options options = new Options();
        options.addOptionGroup(group);

        Assert.assertEquals(group, options.getOptionGroup(option1));
        Assert.assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testGetOptionGroup_noGroup() {
        Option option = new Option("f", "file", true, "Specifies the file");
        Options options = new Options();
        options.addOption(option);

        Assert.assertNull(options.getOptionGroup(option));
    }
    
}