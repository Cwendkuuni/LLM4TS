package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
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
        Option option1 = new Option("a", "option1", false, "Option 1 description");
        Option option2 = new Option("b", "option2", false, "Option 2 description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption_shortName() {
        String opt = "a";
        String description = "Option description";
        options.addOption(opt, description);
        assertTrue(options.hasOption(opt));
        Option option = options.getOption(opt);
        assertNotNull(option);
        assertEquals(opt, option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
    }

    @Test
    public void testAddOption_shortName_hasArg() {
        String opt = "a";
        String description = "Option description";
        options.addOption(opt, true, description);
        assertTrue(options.hasOption(opt));
        Option option = options.getOption(opt);
        assertNotNull(option);
        assertEquals(opt, option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddOption_shortAndLongName_hasArg() {
        String opt = "a";
        String longOpt = "optionA";
        String description = "Option description";
        options.addOption(opt, longOpt, true, description);
        assertTrue(options.hasOption(opt));
        assertTrue(options.hasOption(longOpt));
        Option option = options.getOption(opt);
        assertNotNull(option);
        assertEquals(opt, option.getOpt());
        assertEquals(longOpt, option.getLongOpt());
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "optionA", true, "Option description");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("optionA"));
        assertSame(option, options.getOption("a"));
        assertSame(option, options.getOption("optionA"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1 description");
        Option option2 = new Option("b", "option2", true, "Option 2 description");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1 description");
        Option option2 = new Option("b", "option2", true, "Option 2 description");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option1));
        assertFalse(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "optionA", true, "Option description");
        options.addOption(option);
        assertSame(option, options.getOption("a"));
        assertSame(option, options.getOption("optionA"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1 description");
        Option option2 = new Option("b", "option2", true, "Option 2 description");
        Option option3 = new Option("ab", "option3", false, "Option 3 description");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        assertEquals(2, options.getMatchingOptions("o").size());
        assertEquals(2, options.getMatchingOptions("opt").size());
        assertEquals(1, options.getMatchingOptions("option1").size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "optionA", true, "Option description");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("optionA"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "optionA", true, "Option description");
        options.addOption(option);
        assertTrue(options.hasLongOption("optionA"));
        assertFalse(options.hasLongOption("optionB"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "optionA", true, "Option description");
        options.addOption(option);
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "optionA", true, "Option description");
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);
        assertSame(optionGroup, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "Option 1 description");
        Option option2 = new Option("b", "option2", true, "Option 2 description");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        String expected = "[ Options: [ short {a=Option -a: -a}, {b=Option -b [*]} ] [ long {option1=Option -a, --option1 [*]}, {option2=Option -b [*]} ]";
        assertEquals(expected, options.toString());
    }
}