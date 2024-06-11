package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("f", "foo", false, "Option 1");
        option2 = new Option("b", "bar", false, "Option 2");
    }

    @Test
    public void testAddOption() {
        optionGroup.addOption(option1);
        assertTrue(optionGroup.getOptions().contains(option1));
    }

    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedThrowsAlreadySelectedException() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test
    public void testGetSelected() {
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals("[f Option 1, b Option 2]", optionGroup.toString());
    }
}