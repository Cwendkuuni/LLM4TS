package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "option1", true, "Option 1");
        option2 = new Option("b", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
    }

    @Test
    public void testAddOption() {
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option1);
    }

    @Test
    public void testSetSelectedWithNullOption() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertEquals(null, optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        assertEquals(false, optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertEquals(true, optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        String expected = "[a Option 1, b Option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}