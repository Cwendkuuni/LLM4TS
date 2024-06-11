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
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", true, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", true, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("f", optionGroup.getSelected());

        optionGroup.setSelected(option2);

        assertEquals("b", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", true, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", true, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }

        assertEquals("f", optionGroup.getSelected());
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
        Option option1 = new Option("f", "foo", false, "Option 1");
        Option option2 = new Option("b", "bar", true, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[ -f Option 1, -b Option 2 ]", optionGroup.toString());
    }
}