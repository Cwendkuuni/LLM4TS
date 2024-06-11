package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
        assertEquals(2, optionGroup.getNames().size());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedException() throws AlreadySelectedException {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-a Apple option, -b Banana option]";
        assertEquals(expected, optionGroup.toString());
    }
}