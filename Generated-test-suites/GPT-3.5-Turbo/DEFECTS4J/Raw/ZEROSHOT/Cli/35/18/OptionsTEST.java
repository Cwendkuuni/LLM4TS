import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "option1 description");
        Option option2 = new Option("b", "option2", true, "option2 description");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        options.addOption("a", "option1", false, "option1 description");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("option1"));
    }

    @Test
    public void testAddOption_withArg() {
        options.addOption("a", true, "option1 description");

        assertTrue(options.getOption("a").hasArg());
    }

    @Test
    public void testAddOption_noArg() {
        options.addOption("a", false, "option1 description");

        assertFalse(options.getOption("a").hasArg());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "option1 description");
        Option option2 = new Option("b", "option2", true, "option2 description");
        options.addOption(option1);
        options.addOption(option2);

        List<Option> optionList = options.getOptions();

        assertEquals(2, optionList.size());
        assertTrue(optionList.contains(option1));
        assertTrue(optionList.contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "option1", false, "option1 description");
        Option option2 = new Option("b", "option2", true, "option2 description");
        options.addOption(option1);
        options.addOption(option2);

        List<Option> requiredOptions = options.getOptions();

        assertEquals(2, requiredOptions.size());
        assertTrue(requiredOptions.contains(option1));
        assertTrue(requiredOptions.contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "option1", false, "option1 description");
        options.addOption(option);

        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("option1"));
    }

    @Test
    public void testGetMatchingOptions() {
        options.addOption("apple", false, "Apple option");
        options.addOption("banana", false, "Banana option");
        options.addOption("orange", false, "Orange option");

        List<String> matchingOptions = options.getMatchingOptions("a");

        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("apple"));
        assertTrue(matchingOptions.contains("orange"));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "option1", false, "option1 description");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option1"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "option1", false, "option1 description");
        options.addOption(option);

        assertTrue(options.hasLongOption("option1"));
        assertFalse(options.hasLongOption("option2"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "option1", false, "option1 description");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "option1 description");
        Option option2 = new Option("b", "option2", true, "option2 description");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("c", "option3", true, "option3 description")));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "option1 description");
        options.addOption(option1);

        assertEquals("[ Options: [ short {a=Option1} ] [ long {option1=Option1} ]", options.toString());
    }
}