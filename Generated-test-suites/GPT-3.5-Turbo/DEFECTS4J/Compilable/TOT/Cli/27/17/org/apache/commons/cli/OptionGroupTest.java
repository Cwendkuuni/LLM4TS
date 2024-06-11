package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "option1", true, "Option 1");
        option2 = new Option("b", "option2", false, "Option 2");
        option3 = new Option("c", "option3", true, "Option 3");
    }

    @Test
    public void testAddOption() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        assertEquals(3, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
        assertTrue(optionGroup.getNames().contains("c"));
    }

    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertTrue(optionGroup.getOptions().contains(option3));
    }

    @Test
    public void testSetSelected_validOption() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelectedOption() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option3);
    }

    @Test
    public void testSetSelected_nullOption() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        try {
            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }

    @Test
    public void testGetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        try {
            optionGroup.setSelected(option3);
            assertEquals("c", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testIsNotRequired() {
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        assertEquals("[-option1 Option 1, -option2 Option 2, -option3 Option 3]", optionGroup.toString());
    }
}
