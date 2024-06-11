package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "option1", false, "Option 1");
        option2 = new Option("b", "option2", false, "Option 2");
        option3 = new Option("c", "option3", false, "Option 3");
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
        optionGroup.addOption(option3);

        assertEquals(3, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains(option1.getKey()));
        assertTrue(optionGroup.getNames().contains(option2.getKey()));
        assertTrue(optionGroup.getNames().contains(option3.getKey()));
    }

    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals(3, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertTrue(optionGroup.getOptions().contains(option3));
    }

    @Test
    public void testSetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }

        assertEquals(option1.getKey(), optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            fail("AlreadySelectedException should be thrown");
        } catch (AlreadySelectedException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertNull(optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }

        assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals("[a, --option2, --option3]", optionGroup.toString());
    }
}