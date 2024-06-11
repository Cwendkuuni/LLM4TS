package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "Test option");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "Test option 1");
        Option option2 = new Option("r", "remove", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains("a"));
        assertTrue(names.contains("r"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "Test option 1");
        Option option2 = new Option("r", "remove", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option = new Option("a", "add", false, "Test option");
        try {
            optionGroup.setSelected(option);
            assertEquals(option.getKey(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSetSelectedWithAlreadySelectedOption() {
        Option option1 = new Option("a", "add", false, "Test option 1");
        Option option2 = new Option("r", "remove", false, "Test option 2");
        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            assertEquals(optionGroup, e.getOptionGroup());
            assertEquals(option2, e.getOption());
        }
    }

    @Test
    public void testSetSelectedWithNullOption() {
        Option option = new Option("a", "add", false, "Test option");
        try {
            optionGroup.setSelected(option);
            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetSelected() {
        Option option = new Option("a", "add", false, "Test option");
        try {
            optionGroup.setSelected(option);
            assertEquals(option.getKey(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "Test option 1");
        Option option2 = new Option("r", "remove", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[--add Test option 1, --remove Test option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}