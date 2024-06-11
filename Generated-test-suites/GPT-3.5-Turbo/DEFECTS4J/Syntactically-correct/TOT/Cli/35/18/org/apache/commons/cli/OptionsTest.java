import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "Test option");
        options.addOption(option);
        assertTrue("Option is not added", options.hasOption("o"));
        assertTrue("Option is not added", options.hasOption("option"));
        assertEquals("Wrong option returned", option, options.getOption("o"));
    }

    @Test
    public void testAddOption_withArg() {
        Option option = new Option("o", "option", true, "Test option with argument");
        options.addOption(option);
        assertTrue("Option is not added", options.hasOption("o"));
        assertTrue("Option is not added", options.hasOption("option"));
        assertEquals("Wrong option returned", option, options.getOption("o"));
    }

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("o", "option1", false, "Test option 1");
        Option option2 = new Option("p", "option2", false, "Test option 2");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        assertTrue("OptionGroup is not added", options.hasOption("o"));
        assertTrue("OptionGroup is not added", options.hasOption("p"));
        assertTrue("Option is not added to OptionGroup", optionGroup.getOptions().contains(option1));
        assertTrue("Option is not added to OptionGroup", optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o", "option1", false, "Test option 1");
        options.addOption(option1);
        Option option2 = new Option("p", "option2", true, "Test option 2");
        options.addOption(option2);
        List<Option> optionList = options.getOptions();
        assertTrue("Option 1 is not included in the list", optionList.contains(option1));
        assertTrue("Option 2 is not included in the list", optionList.contains(option2));
        assertEquals("Wrong number of options in the list", 2, optionList.size());
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("o", "option1", false, "Test option 1");
        option1.setRequired(true);
        options.addOption(option1);
        Option option2 = new Option("p", "option2", true, "Test option 2");
        options.addOption(option2);
        List requiredOptions = options.getRequiredOptions();
        assertTrue("Required option is not included in the list", requiredOptions.contains(option1.getKey()));
        assertEquals("Wrong number of required options in the list", 1, requiredOptions.size());
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("o", "option1", false, "Test option 1");
        options.addOption(option1);
        Option option2 = new Option("p", "option2", true, "Test option 2");
        options.addOption(option2);
        assertEquals("Wrong option returned", option1, options.getOption("o"));
        assertEquals("Wrong option returned", option2, options.getOption("p"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o", "option", false, "Test option 1");
        options.addOption(option1);
        Option option2 = new Option("op", "option2", true, "Test option 2");
        options.addOption(option2);
        List<String> matchingOptions = options.getMatchingOptions("op");
        assertTrue("Matching option is not included in the list", matchingOptions.contains("op"));
        assertEquals("Wrong number of matching options in the list", 1, matchingOptions.size());
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("o", "option1", false, "Test option 1");
        options.addOption(option1);
        Option option2 = new Option("p", "option2", true, "Test option 2");
        options.addOption(option2);
        assertTrue("Option is not found", options.hasOption("o"));
        assertTrue("Option is not found", options.hasOption("option1"));
        assertTrue("Option is not found", options.hasOption("p"));
        assertTrue("Option is not found", options.hasOption("option2"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("o", "option", false, "Test option");
        options.addOption(option);
        assertTrue("Long option is not found", options.hasLongOption("option"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("o", "option", false, "Test option");
        options.addOption(option);
        assertTrue("Short option is not found", options.hasShortOption("o"));
    }

    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("o", "option1", false, "Test option 1");
        Option option2 = new Option("p", "option2", false, "Test option 2");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        assertEquals("OptionGroup is not found", optionGroup, options.getOptionGroup(option1));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o", "option1", false, "Test option 1");
        options.addOption(option1);
        Option option2 = new Option("p", "option2", true, "Test option 2");
        options.addOption(option2);
        String optionsString = options.toString();
        assertTrue("Options are not included in the string", optionsString.contains(option1.toString()));
        assertTrue("Options are not included in the string", optionsString.contains(option2.toString()));
    }

}