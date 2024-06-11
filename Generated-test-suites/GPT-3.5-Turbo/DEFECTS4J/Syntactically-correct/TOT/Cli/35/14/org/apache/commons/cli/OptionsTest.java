import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
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
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertTrue(options.hasOption(option1.getOpt()));
        assertTrue(options.hasOption(option2.getOpt()));
        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption_ShortNameOnly() {
        Option option = new Option("a", false, "Description");

        options.addOption(option);

        assertTrue(options.hasOption(option.getOpt()));
        assertFalse(options.hasOption(option.getLongOpt()));
        assertEquals(option, options.getOption(option.getOpt()));
        assertNull(options.getOption(option.getLongOpt()));
    }

    @Test
    public void testAddOption_ShortNameWithArgument() {
        Option option = new Option("a", true, "Description");

        options.addOption(option);

        assertTrue(options.hasOption(option.getOpt()));
        assertFalse(options.hasOption(option.getLongOpt()));
        assertTrue(options.hasArg(option.getOpt()));
        assertNull(options.getOption(option.getLongOpt()));
    }

    @Test
    public void testAddOption_ShortAndLongName() {
        Option option = new Option("a", "apple", false, "Description");

        options.addOption(option);

        assertTrue(options.hasOption(option.getOpt()));
        assertTrue(options.hasOption(option.getLongOpt()));
        assertEquals(option, options.getOption(option.getOpt()));
        assertEquals(option, options.getOption(option.getLongOpt()));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", false, "Description 1");
        Option option2 = new Option("b", false, "Description 2");

        options.addOption(option1);
        options.addOption(option2);

        Collection<Option> optionList = options.getOptions();

        assertEquals(2, optionList.size());
        assertTrue(optionList.contains(option1));
        assertTrue(optionList.contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", false, "Description 1");
        Option option2 = new Option("b", false, "Description 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption(option1.getOpt()));
        assertEquals(option2, options.getOption(option2.getOpt()));
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", false, "Description 1");
        Option option2 = new Option("abc", false, "Description 2");
        Option option3 = new Option("acb", false, "Description 3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("a");

        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains(option1.getOpt()));
        assertTrue(matchingOptions.contains(option2.getLongOpt()));
        assertFalse(matchingOptions.contains(option3.getLongOpt()));

        matchingOptions = options.getMatchingOptions("ab");

        assertEquals(1, matchingOptions.size());
        assertTrue(matchingOptions.contains(option2.getLongOpt()));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", false, "Description 1");
        Option option2 = new Option("b", false, "Description 2");

        options.addOption(option1);

        assertTrue(options.hasOption(option1.getOpt()));
        assertFalse(options.hasOption(option2.getOpt()));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("apple", false, "Description 1");
        Option option2 = new Option("banana", false, "Description 2");

        options.addOption(option1);

        assertTrue(options.hasLongOption(option1.getLongOpt()));
        assertFalse(options.hasLongOption(option2.getLongOpt()));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", false, "Description 1");
        Option option2 = new Option("b", false, "Description 2");

        options.addOption(option1);

        assertTrue(options.hasShortOption(option1.getOpt()));
        assertFalse(options.hasShortOption(option2.getOpt()));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("c", "cherry")));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", false, "Description 1");
        Option option2 = new Option("b", false, "Description 2");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=a, b=b} ] [ long {apple=a, banana=b} ]]";
        assertEquals(expected, options.toString());
    }
}