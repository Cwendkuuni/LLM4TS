import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description");
        Option option2 = new Option("b", "option2", true, "description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        List<OptionGroup> optionGroups = options.getOptionGroups();
        Assert.assertEquals(1, optionGroups.size());
        Assert.assertEquals(optionGroup, optionGroups.get(0));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "option", true, "description");
        options.addOption(option);
        Collection<Option> optionCollection = options.getOptions();
        Assert.assertEquals(1, optionCollection.size());
        Assert.assertEquals(option, optionCollection.iterator().next());
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "option", true, "description");
        options.addOption(option);
        Option retrievedOption = options.getOption("a");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "option1", true, "description");
        Option option2 = new Option("b", "option2", true, "description");
        Option option3 = new Option("c", "option3", true, "description");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        List<String> matchingOptions = options.getMatchingOptions("o");
        Assert.assertEquals(3, matchingOptions.size());
        Assert.assertTrue(matchingOptions.contains("option1"));
        Assert.assertTrue(matchingOptions.contains("option2"));
        Assert.assertTrue(matchingOptions.contains("option3"));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "option", true, "description");
        options.addOption(option);
        boolean hasOption = options.hasOption("a");
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "option", true, "description");
        options.addOption(option);
        boolean hasLongOption = options.hasLongOption("option");
        Assert.assertTrue(hasLongOption);
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "option", true, "description");
        options.addOption(option);
        boolean hasShortOption = options.hasShortOption("a");
        Assert.assertTrue(hasShortOption);
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option", true, "description");
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);
        OptionGroup retrievedOptionGroup = options.getOptionGroup(option);
        Assert.assertEquals(optionGroup, retrievedOptionGroup);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", true, "description1");
        Option option2 = new Option("b", "option2", false, "description2");
        options.addOption(option1);
        options.addOption(option2);
        String expected = "[ Options: [ short {a=Option -a, long {option1=Option -a}, long {option2=Option -b} ]";
        String actual = options.toString();
        Assert.assertEquals(expected, actual);
    }
}