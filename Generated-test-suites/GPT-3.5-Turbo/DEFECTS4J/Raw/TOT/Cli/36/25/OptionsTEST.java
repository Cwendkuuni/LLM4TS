import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Option opt1 = new Option("a", "test1", false, "Test Option 1");
        Option opt2 = new Option("b", "test2", false, "Test Option 2");
        Option opt3 = new Option("c", "test3", false, "Test Option 3");
        
        OptionGroup group1 = new OptionGroup();
        group1.addOption(opt1);
        group1.addOption(opt2);
        group1.setRequired(true);
        
        Options options = new Options();
        options.addOptionGroup(group1);
        
        List<OptionGroup> optionGroups = options.getOptionGroups();
        Assert.assertEquals(1, optionGroups.size());
        Assert.assertTrue(optionGroups.contains(group1));
        
        Assert.assertEquals(group1, options.getOptionGroup(opt1));
        Assert.assertEquals(group1, options.getOptionGroup(opt2));
        Assert.assertNull(options.getOptionGroup(opt3));
    }

    @Test
    public void testAddOption() {
        Option opt1 = new Option("a", "test1", true, "Test Option 1");
        Option opt2 = new Option("b", "test2", false, "Test Option 2");

        Options options = new Options();
        options.addOption(opt1);
        options.addOption(opt2);

        Collection<Option> optionCollection = options.getOptions();
        List<Option> optionList = new ArrayList<Option>(optionCollection);

        Assert.assertEquals(2, optionList.size());
        Assert.assertTrue(optionList.contains(opt1));
        Assert.assertTrue(optionList.contains(opt2));

        Assert.assertTrue(options.hasOption("a"));
        Assert.assertEquals(opt1, options.getOption("a"));

        Assert.assertFalse(options.hasOption("c"));
        Assert.assertNull(options.getOption("c"));

        Assert.assertFalse(options.hasShortOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option opt1 = new Option("f", "test1", true, "Test Option 1");
        Option opt2 = new Option("foo", "test2", false, "Test Option 2");
        Option opt3 = new Option("food", "test3", false, "Test Option 3");

        Options options = new Options();
        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);

        List<String> matchingOpts1 = options.getMatchingOptions("f");
        Assert.assertEquals(1, matchingOpts1.size());
        Assert.assertEquals("f", matchingOpts1.get(0));

        List<String> matchingOpts2 = options.getMatchingOptions("foo");
        Assert.assertEquals(1, matchingOpts2.size());
        Assert.assertEquals("foo", matchingOpts2.get(0));

        List<String> matchingOpts3 = options.getMatchingOptions("food");
        Assert.assertEquals(1, matchingOpts3.size());
        Assert.assertEquals("food", matchingOpts3.get(0));

        List<String> matchingOpts4 = options.getMatchingOptions("test");
        Assert.assertEquals(3, matchingOpts4.size());
        Assert.assertTrue(matchingOpts4.contains("test1"));
        Assert.assertTrue(matchingOpts4.contains("test2"));
        Assert.assertTrue(matchingOpts4.contains("test3"));

        List<String> matchingOpts5 = options.getMatchingOptions("t");
        Assert.assertEquals(3, matchingOpts5.size());
        Assert.assertTrue(matchingOpts5.contains("test1"));
        Assert.assertTrue(matchingOpts5.contains("test2"));
        Assert.assertTrue(matchingOpts5.contains("test3"));

        List<String> matchingOpts6 = options.getMatchingOptions("z");
        Assert.assertEquals(0, matchingOpts6.size());
    }

    @Test
    public void testHasOption() {
        Option opt1 = new Option("a", "test1", true, "Test Option 1");
        Option opt2 = new Option("b", "test2", false, "Test Option 2");

        Options options = new Options();
        options.addOption(opt1);
        options.addOption(opt2);

        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("test1"));
        Assert.assertTrue(options.hasOption("b"));
        Assert.assertTrue(options.hasOption("test2"));
        Assert.assertFalse(options.hasOption("c"));

        Assert.assertTrue(options.hasLongOption("test1"));
        Assert.assertTrue(options.hasLongOption("test2"));
        Assert.assertFalse(options.hasLongOption("test3"));

        Assert.assertTrue(options.hasShortOption("a"));
        Assert.assertTrue(options.hasShortOption("b"));
        Assert.assertFalse(options.hasShortOption("c"));
    }

    @Test
    public void testGetRequiredOptions() {
        Option opt1 = new Option("a", "test1", true, "Test Option 1");
        Option opt2 = new Option("b", "test2", false, "Test Option 2");
        opt1.setRequired(true);

        Options options = new Options();
        options.addOption(opt1);
        options.addOption(opt2);

        List<Object> requiredOptions = options.getRequiredOptions();
        Assert.assertEquals(1, requiredOptions.size());
        Assert.assertTrue(requiredOptions.contains(opt1));
        Assert.assertFalse(requiredOptions.contains(opt2));
    }

}