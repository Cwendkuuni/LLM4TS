package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("x", "xxx", false, "Option X");
        OptionGroup returnedGroup = optionGroup.addOption(option);
        assertEquals(optionGroup, returnedGroup);
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("x", "xxx", false, "Option X");
        Option option2 = new Option("y", "yyy", false, "Option Y");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains(option1.getKey()));
        assertTrue(optionGroup.getNames().contains(option2.getKey()));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("x", "xxx", false, "Option X");
        Option option2 = new Option("y", "yyy", false, "Option Y");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("x", "xxx", false, "Option X");
        Option option2 = new Option("y", "yyy", false, "Option Y");
        try {
            optionGroup.setSelected(option1);
            assertEquals(option1.getKey(), optionGroup.getSelected());
            optionGroup.setSelected(option2);
            assertEquals(option2.getKey(), optionGroup.getSelected());
            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() throws AlreadySelectedException {
        Option option1 = new Option("x", "xxx", false, "Option X");
        Option option2 = new Option("y", "yyy", false, "Option Y");
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("x", "xxx", false, "Option X");
        Option option2 = new Option("y", "yyy", false, "Option Y");
        assertNull(optionGroup.getSelected());
        try {
            optionGroup.setSelected(option1);
            assertEquals(option1.getKey(), optionGroup.getSelected());
            optionGroup.setSelected(option2);
            assertEquals(option2.getKey(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("x", "xxx", false, "Option X");
        Option option2 = new Option("y", "yyy", false, "Option Y");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "["
                + "-" + option1.getOpt() + " " + option1.getDescription() + ", "
                + "-" + option2.getOpt() + " " + option2.getDescription()
                + "]";
        assertEquals(expected, optionGroup.toString());
    }
}