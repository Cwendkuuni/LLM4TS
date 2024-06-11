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
        Option option1 = new Option("a", "aaaa", false, "option 1");
        Option option2 = new Option("b", "bbbb", false, "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertTrue(options.getOptionGroups().contains(optionGroup));
        assertTrue(options.getRequiredOptions().contains(optionGroup));

        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        assertNull(options.getOption("c"));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "aaaa", false, "option");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.getRequiredOptions().contains("a"));
        assertFalse(options.getRequiredOptions().contains("b"));

        assertEquals(option, options.getOption("a"));
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetOption() {
        options.addOption("a", "aaaa", false, "option");

        Option option = options.getOption("a");

        assertEquals("a", option.getOpt());
        assertEquals("aaaa", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("option", option.getDescription());
    }

    @Test
    public void testGetMatchingOptions() {
        options.addOption("apple", "Apple", false, "apple option");
        options.addOption("orange", "Orange", false, "orange option");
        options.addOption("banana", "Banana", false, "banana option");

        List<String> matchingOptions = options.getMatchingOptions("a");

        assertEquals(1, matchingOptions.size());
        assertEquals("apple", matchingOptions.get(0));
    }

    @Test
    public void testHasOption() {
        options.addOption("a", "aaaa", false, "option");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("aaaa"));
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasOption("b"));
        assertFalse(options.hasLongOption("bbbb"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "aaaa", false, "option 1");
        Option option2 = new Option("b", "bbbb", false, "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertNotEquals(optionGroup, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("c", "cccc", false, "option 3")));
    }

    @Test
    public void testToString() {
        options.addOption("a", "aaaa", false, "option 1");
        options.addOption("b", "bbbb", false, "option 2");

        String expected = "[ Options: [ short {a=Option(a:aaaa:false:option 1), b=Option(b:bbbb:false:option 2)} ] [ long {} ]";

        assertEquals(expected, options.toString());
    }
}