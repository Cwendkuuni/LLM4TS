import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        options.addOptionGroup(group);
        Assert.assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", false, "test option");
        options.addOption(option);
        Assert.assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("t", "test", false, "test option");
        options.addOption(option);
        Assert.assertEquals(option, options.getOption("t"));
        Assert.assertEquals(option, options.getOption("test"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("t1", "test1", false, "test option 1");
        Option option2 = new Option("t2", "test2", false, "test option 2");
        options.addOption(option1);
        options.addOption(option2);
        Assert.assertEquals(2, options.getMatchingOptions("t").size());
        Assert.assertEquals(1, options.getMatchingOptions("test1").size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("t", "test", false, "test option");
        options.addOption(option);
        Assert.assertTrue(options.hasOption("t"));
        Assert.assertTrue(options.hasOption("test"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("t", "test", false, "test option");
        options.addOption(option);
        Assert.assertTrue(options.hasLongOption("test"));
        Assert.assertFalse(options.hasLongOption("t"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("t", "test", false, "test option");
        options.addOption(option);
        Assert.assertTrue(options.hasShortOption("t"));
        Assert.assertFalse(options.hasShortOption("test"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("t", "test", false, "test option");
        group.addOption(option);
        options.addOptionGroup(group);
        Assert.assertEquals(group, options.getOptionGroup(option));
    }
}