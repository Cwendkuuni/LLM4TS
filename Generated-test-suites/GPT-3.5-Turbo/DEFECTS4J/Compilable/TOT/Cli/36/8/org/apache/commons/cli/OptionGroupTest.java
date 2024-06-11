package org.apache.commons.cli;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import java.util.Iterator;
import static org.junit.Assert.*;

public class OptionGroupTest {

    OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @After
    public void tearDown() {
        optionGroup = null;
    }

    @Test
    public void testAddOption() {
        // Test adding an option to the option group
        Option option = new Option("a", "addOptionTest", false, "Test adding option to option group");
        OptionGroup result = optionGroup.addOption(option);
        assertTrue(result.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        // Test getting names of options in the group
        Option option1 = new Option("a", "getNamesTest", false, "Test getting names of options in group");
        Option option2 = new Option("b", "getNamesTest", false, "Test getting names of options in group");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains(option1.getKey()));
        assertTrue(names.contains(option2.getKey()));
        assertEquals(2, names.size());
    }

    @Test
    public void testGetOptions() {
        // Test getting the options in the group
        Option option1 = new Option("a", "getOptionsTest1", false, "Test getting options in the group");
        Option option2 = new Option("b", "getOptionsTest2", false, "Test getting options in the group");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertEquals(2, options.size());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_withAlreadySelectedOption() throws AlreadySelectedException{
        // Test setting the selected option with an already selected option
        Option option1 = new Option("a", "setSelectedTest1", false, "Test setting selected option in the group");
        Option option2 = new Option("b", "setSelectedTest2", false, "Test setting selected option in the group");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelected_withNullOption() throws AlreadySelectedException{
        // Test setting the selected option with null option
        Option option = new Option("a", "setSelectedTest", false, "Test setting selected option in the group");
        optionGroup.addOption(option);
        optionGroup.setSelected(null);
        assertEquals(null, optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        // Test getting the selected option
        Option option1 = new Option("a", "getSelectedTest1", false, "Test getting the selected option");
        Option option2 = new Option("b", "getSelectedTest2", false, "Test getting the selected option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        // Test setting the option group as required
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        // Test checking if the option group is required
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        // Test getting the stringified representation of the option group
        Option option1 = new Option("a", "toStringTest1", false, "Test getting stringified representation of the group");
        Option option2 = new Option("b", "toStringTest2", false, "Test getting stringified representation of the group");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-" + option1.getOpt() + " " + option1.getDescription() + ", --" + option2.getLongOpt() + " " + option2.getDescription() + "]";
        assertEquals(expected, optionGroup.toString());
    }
}