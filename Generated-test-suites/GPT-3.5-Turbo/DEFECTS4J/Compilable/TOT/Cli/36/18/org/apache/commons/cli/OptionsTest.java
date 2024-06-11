package org.apache.commons.cli;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @After
    public void tearDown() {
        options = null;
    }

    @Test
    public void testAddOption() {
        Option option = new Option("O", "option", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasOption("O"));
        assertFalse(options.hasOption("A"));
    }

    @Test
    public void testAddOption_withArguments() {
        Option option = new Option("O", "option", true, "Test option with argument");
        options.addOption(option);
        assertTrue(options.hasOption("O"));
        assertFalse(options.hasOption("A"));
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("O", "option1", false, "Test option 1");
        Option option2 = new Option("A", "option2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertTrue(options.hasOption("O"));
        assertTrue(options.hasOption("A"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("O", "option1", false, "Test option 1");
        Option option2 = new Option("A", "option2", false, "Test option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("O", "option1", false, "Test option 1");
        Option option2 = new Option("A", "option2", false, "Test option 2");

        option1.setRequired(true);
        option2.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getRequiredOptions().size());
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("O", "option1", false, "Test option 1");
        Option option2 = new Option("A", "option2", false, "Test option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("O"));
        assertEquals(option2, options.getOption("A"));
        assertNull(options.getOption("B"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("O", "option1", false, "Test option 1");
        Option option2 = new Option("A", "option2", false, "Test option 2");
        Option option3 = new Option("B", "option3", false, "Test option 3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(2, options.getMatchingOptions("o").size());
        assertEquals(1, options.getMatchingOptions("b").size());
        assertEquals(0, options.getMatchingOptions("c").size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("O", "option", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasOption("O"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("A"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("O", "option", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("O"));
        assertFalse(options.hasLongOption("A"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("O", "option", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasShortOption("O"));
        assertFalse(options.hasShortOption("option"));
        assertFalse(options.hasShortOption("A"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("O", "option1", false, "Test option 1");
        Option option2 = new Option("A", "option2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("B", "option3", false, "Test option 3")));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("O", "option1", false, "Test option 1");
        Option option2 = new Option("A", "option2", false, "Test option 2");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {O=Option: O, option1, false, Test option 1, required:false, requiresArg:false, argName:null, type:java.lang.String, values:null, numberOfArgs:-1} } [ long {option1=Option: O, option1, false, Test option 1, required:false, requiresArg:false, argName:null, type:java.lang.String, values:null, numberOfArgs:-1} ]";
        assertEquals(expected, options.toString());
    }
}