package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedNull() throws AlreadySelectedException {
        Option option1 = new Option("a", "apple", false, "Apple option");

        optionGroup.addOption(option1);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[-a Apple option, -b Banana option]", optionGroup.toString());
    }
}