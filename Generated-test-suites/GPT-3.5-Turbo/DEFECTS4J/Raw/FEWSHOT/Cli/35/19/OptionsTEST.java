import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "option", false, "This is an option");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testAddOptionWithArgument() {
        Option option = new Option("a", "option", true, "This is an option with an argument");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("a", "option", false, "This is an option group"));
        options.addOptionGroup(optionGroup);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "This is option 1");
        Option option2 = new Option("b", "option2", false, "This is option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "option1", false, "This is option 1");
        Option option2 = new Option("b", "option2", false, "This is option 2");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getRequiredOptions().size());
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "option", false, "This is an option");
        options.addOption(option);

        Option retrievedOption = options.getOption("a");
        assertEquals(option, retrievedOption);

        retrievedOption = options.getOption("option");
        assertEquals(option, retrievedOption);

        retrievedOption = options.getOption("b");
        assertEquals(null, retrievedOption);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "option1", false, "This is option 1");
        Option option2 = new Option("ab", "option2", false, "This is option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getMatchingOptions("a").size());
        assertEquals(1, options.getMatchingOptions("ab").size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "option", false, "This is an option");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "option", false, "This is an option");
        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("b"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "option", false, "This is an option");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("a", "option", false, "This is an option group"));
        options.addOptionGroup(optionGroup);

        OptionGroup retrievedOptionGroup = options.getOptionGroup(options.getOption("a"));
        assertEquals(optionGroup, retrievedOptionGroup);

        retrievedOptionGroup = options.getOptionGroup(options.getOption("option"));
        assertEquals(optionGroup, retrievedOptionGroup);

        retrievedOptionGroup = options.getOptionGroup(options.getOption("b"));
        assertEquals(null, retrievedOptionGroup);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "This is option 1");
        Option option2 = new Option("b", "option2", false, "This is option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals("[ Options: [ short {a=option1, b=option2} ] [ long {option1=Option: a, option2=Option: b} ]", options.toString());
    }
}