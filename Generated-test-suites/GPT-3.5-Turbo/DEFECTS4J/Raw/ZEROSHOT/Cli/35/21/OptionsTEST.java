import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "description");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("option1"));
        assertTrue(options.hasOption("o2"));
        assertTrue(options.hasOption("option2"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", true, "description2");
        options.addOption(option1);
        options.addOption(option2);

        List<Option> optionList = new ArrayList<>(options.getOptions());

        assertEquals(optionList.size(), 2);
        assertEquals(optionList.get(0), option1);
        assertEquals(optionList.get(1), option2);
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", true, "description2");
        option1.setRequired(true);
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        List<String> requiredOptions = options.getRequiredOptions();

        assertEquals(requiredOptions.size(), 2);
        assertTrue(requiredOptions.contains("o1"));
        assertTrue(requiredOptions.contains("o2"));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true, "description");
        options.addOption(option);

        Option retrievedOption1 = options.getOption("o");
        assertNotNull(retrievedOption1);
        assertEquals(retrievedOption1, option);

        Option retrievedOption2 = options.getOption("option");
        assertNotNull(retrievedOption2);
        assertEquals(retrievedOption2, option);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("option1", false, "description1");
        Option option2 = new Option("option2", false, "description2");
        Option option3 = new Option("other", false, "description3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("option");

        assertEquals(matchingOptions.size(), 2);
        assertTrue(matchingOptions.contains("option1"));
        assertTrue(matchingOptions.contains("option2"));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "description");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("o", "option", false, "description");
        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("o", "option", false, "description");
        options.addOption(option);

        assertTrue(options.hasShortOption("o"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);

        OptionGroup retrievedGroup = options.getOptionGroup(option1);

        assertNotNull(retrievedGroup);
        assertEquals(retrievedGroup, optionGroup);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", true, "description2");
        options.addOption(option1);
        options.addOption(option2);

        String optionsString = options.toString();

        assertTrue(optionsString.contains("[ Options: [ short {o1=Option [ opt=o1, longOpt=option1, hasArg=false, description=description1 ]}"));
        assertTrue(optionsString.contains("[ long {option1=Option [ opt=o1, longOpt=option1, hasArg=false, description=description1 ]}"));
    }
}