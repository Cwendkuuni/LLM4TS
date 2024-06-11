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
        option1 = new Option("a", "option A");
        option2 = new Option("b", "option B");
        option3 = new Option("c", "option C");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
    }

    @Test
    public void testAddOption() {
        assertEquals(3, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertTrue(optionGroup.getOptions().contains(option3));
    }

    @Test
    public void testGetNames() {
        assertEquals(3, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
        assertTrue(optionGroup.getNames().contains("c"));
    }

    @Test
    public void testGetOptions() {
        assertEquals(3, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertTrue(optionGroup.getOptions().contains(option3));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());

        optionGroup.setSelected(option3);
        assertEquals("c", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());

        optionGroup.setSelected(option3);
        assertEquals("c", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        assertEquals("[option A, option B, option C]", optionGroup.toString());
    }
}