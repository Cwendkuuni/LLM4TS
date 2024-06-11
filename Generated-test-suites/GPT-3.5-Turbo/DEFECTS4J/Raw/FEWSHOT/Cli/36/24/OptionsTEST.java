import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "apple", false, "apple option");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("apple"));
        assertFalse(options.hasShortOption("apple"));

        Option addedOption = options.getOption("a");
        assertEquals("a", addedOption.getOpt());
        assertEquals("apple", addedOption.getLongOpt());
        assertFalse(addedOption.isRequired());
        assertEquals("apple option", addedOption.getDescription());
    }

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("apple"));
        assertFalse(options.hasShortOption("apple"));

        Option addedOption = options.getOption("a");
        assertEquals("a", addedOption.getOpt());
        assertEquals("apple", addedOption.getLongOpt());
        assertFalse(addedOption.isRequired());
        assertEquals("apple option", addedOption.getDescription());

        assertTrue(options.hasOption("b"));
        assertTrue(options.hasLongOption("banana"));
        assertFalse(options.hasShortOption("banana"));

        Option addedOption2 = options.getOption("b");
        assertEquals("b", addedOption2.getOpt());
        assertEquals("banana", addedOption2.getLongOpt());
        assertFalse(addedOption2.isRequired());
        assertEquals("banana option", addedOption2.getDescription());
    }

    @Test
    public void testAddOption_noLongOpt() {
        Option option = new Option("a", false, "apple option");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertFalse(options.hasLongOption("apple"));
        assertTrue(options.hasShortOption("apple"));

        Option addedOption = options.getOption("a");
        assertEquals("a", addedOption.getOpt());
        assertNull(addedOption.getLongOpt());
        assertFalse(addedOption.isRequired());
        assertEquals("apple option", addedOption.getDescription());
    }

    @Test
    public void testAddOption_withShortAndLongOpt() {
        Option option = new Option("a", "apple", false, "apple option");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("apple"));
        assertTrue(options.hasShortOption("apple"));

        Option addedOption = options.getOption("a");
        assertEquals("a", addedOption.getOpt());
        assertEquals("apple", addedOption.getLongOpt());
        assertFalse(addedOption.isRequired());
        assertEquals("apple option", addedOption.getDescription());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        options.addOption(option1);
        options.addOption(option2);

        List<Option> optionList = options.getOptions();
        assertEquals(2, optionList.size());

        Option retrievedOption1 = optionList.get(0);
        assertEquals("a", retrievedOption1.getOpt());
        assertEquals("apple", retrievedOption1.getLongOpt());
        assertFalse(retrievedOption1.isRequired());
        assertEquals("apple option", retrievedOption1.getDescription());

        Option retrievedOption2 = optionList.get(1);
        assertEquals("b", retrievedOption2.getOpt());
        assertEquals("banana", retrievedOption2.getLongOpt());
        assertFalse(retrievedOption2.isRequired());
        assertEquals("banana option", retrievedOption2.getDescription());
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        Option option3 = new Option("c", "carrot", false, "carrot option");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("a");
        assertEquals(1, matchingOptions.size());
        assertEquals("apple", matchingOptions.get(0));

        matchingOptions = options.getMatchingOptions("ap");
        assertEquals(1, matchingOptions.size());
        assertEquals("apple", matchingOptions.get(0));

        matchingOptions = options.getMatchingOptions("ba");
        assertEquals(1, matchingOptions.size());
        assertEquals("banana", matchingOptions.get(0));

        matchingOptions = options.getMatchingOptions("c");
        assertEquals(1, matchingOptions.size());
        assertEquals("carrot", matchingOptions.get(0));

        matchingOptions = options.getMatchingOptions("d");
        assertEquals(0, matchingOptions.size());
    }

    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        OptionGroup retrievedGroup = options.getOptionGroup(option1);
        assertEquals(group, retrievedGroup);

        OptionGroup retrievedGroup2 = options.getOptionGroup(option2);
        assertEquals(group, retrievedGroup2);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        options.addOption(option1);
        options.addOption(option2);

        String expectedString = "[ Options: [ short {a=apple, b=banana} ] [ long {apple}, juice banana]";

        assertEquals(expectedString, options.toString());
    }
}