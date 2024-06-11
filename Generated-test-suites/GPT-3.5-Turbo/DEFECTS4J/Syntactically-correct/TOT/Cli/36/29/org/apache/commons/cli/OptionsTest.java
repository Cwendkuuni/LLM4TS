import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");
        Option option3 = new Option("c", "charlie", false, "Option Charlie");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        options.addOptionGroup(optionGroup);

        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "alpha", false, "Option Alpha");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testAddOptionShortNameDescription() {
        String shortName = "a";
        String description = "Option Alpha";
        options.addOption(shortName, description);

        assertTrue(options.hasOption(shortName));
    }

    @Test
    public void testAddOptionShortNameHasArgDescription() {
        String shortName = "a";
        boolean hasArg = true;
        String description = "Option Alpha";
        options.addOption(shortName, hasArg, description);

        assertTrue(options.hasOption(shortName));
    }

    @Test
    public void testAddOptionShortAndLongNameHasArgDescription() {
        String shortName = "a";
        String longName = "alpha";
        boolean hasArg = true;
        String description = "Option Alpha";
        options.addOption(shortName, longName, hasArg, description);

        assertTrue(options.hasOption(shortName));
        assertTrue(options.hasOption(longName));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");
        options.addOption(option1);
        options.addOption(option2);
        List<Option> optionList = options.getOptions();

        assertEquals(optionList.size(), 2);
        assertTrue(optionList.contains(option1));
        assertTrue(optionList.contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        List<Option> requiredOptionList = options.getRequiredOptions();

        assertEquals(requiredOptionList.size(), 1);
        assertTrue(requiredOptionList.contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "alpha", false, "Option Alpha");
        options.addOption(option);

        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("--alpha"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "apple", false, "Option Apple");
        Option option2 = new Option("b", "banana", false, "Option Banana");
        options.addOption(option1);
        options.addOption(option2);

        List<String> matchingOptions1 = options.getMatchingOptions("a");
        List<String> matchingOptions2 = options.getMatchingOptions("b");

        assertEquals(matchingOptions1.size(), 1);
        assertEquals(matchingOptions2.size(), 1);
        assertTrue(matchingOptions1.contains("apple"));
        assertTrue(matchingOptions2.contains("banana"));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "alpha", false, "Option Alpha");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("--alpha"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "alpha", false, "Option Alpha");
        options.addOption(option);

        assertTrue(options.hasLongOption("alpha"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "alpha", false, "Option Alpha");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option Alpha, b=Option Beta} ] [ long {alpha=Option Alpha, beta=Option Beta} ]";
        assertEquals(expected, options.toString());
    }
}