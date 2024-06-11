import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optGroup = new OptionGroup();
        Option opt1 = new Option("a", "apple", false, "description");
        Option opt2 = new Option("b", "banana", false, "description");
        Option opt3 = new Option("c", "cherry", false, "description");
        optGroup.addOption(opt1);
        optGroup.addOption(opt2);
        optGroup.addOption(opt3);

        options.addOptionGroup(optGroup);

        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        Assert.assertTrue(optionGroups.contains(optGroup));
    }

    @Test
    public void testAddOption() {
        Option opt = new Option("a", "apple", false, "description");

        options.addOption(opt);

        Collection<Option> optionList = options.getOptions();
        Assert.assertTrue(optionList.contains(opt));
    }

    @Test
    public void testGetOption() {
        Option opt = new Option("a", "apple", false, "description");

        options.addOption(opt);

        Option retrievedOpt = options.getOption("a");
        Assert.assertEquals(opt, retrievedOpt);
    }

    @Test
    public void testGetMatchingOptions() {
        Option opt1 = new Option("a", "apple", false, "description");
        Option opt2 = new Option("b", "banana", false, "description");

        options.addOption(opt1);
        options.addOption(opt2);

        List<String> matchingOpts = options.getMatchingOptions("a");
        Assert.assertTrue(matchingOpts.contains("apple"));
        Assert.assertTrue(matchingOpts.contains("banana"));
    }

    @Test
    public void testHasOption() {
        Option opt = new Option("a", "apple", false, "description");
        options.addOption(opt);

        boolean hasOpt = options.hasOption("a");
        Assert.assertTrue(hasOpt);
    }

    @Test
    public void testHasLongOption() {
        Option opt = new Option("a", "apple", false, "description");
        options.addOption(opt);

        boolean hasLongOpt = options.hasLongOption("apple");
        Assert.assertTrue(hasLongOpt);
    }

    @Test
    public void testHasShortOption() {
        Option opt = new Option("a", "apple", false, "description");
        options.addOption(opt);

        boolean hasShortOpt = options.hasShortOption("a");
        Assert.assertTrue(hasShortOpt);
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optGroup = new OptionGroup();
        Option opt1 = new Option("a", "apple", false, "description");
        optGroup.addOption(opt1);

        options.addOptionGroup(optGroup);

        OptionGroup retrievedGroup = options.getOptionGroup(opt1);
        Assert.assertEquals(optGroup, retrievedGroup);
    }

    @Test
    public void testToString() {
        Option opt1 = new Option("a", "apple", false, "description");
        Option opt2 = new Option("b", "banana", false, "description");
        options.addOption(opt1);
        options.addOption(opt2);

        String str = options.toString();
        String expectedStr = "[ Options: [ short {a=Option{opt='a', longOpt='apple', hasArg=false, description='description'}} ] [ long {apple=Option{opt='a', longOpt='apple', hasArg=false, description='description'}, banana=Option{opt='b', longOpt='banana', hasArg=false, description='description'}} ]";
        Assert.assertEquals(expectedStr, str);
    }
}