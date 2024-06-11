package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "first option");
        option2 = new Option("b", "second option");
        option3 = new Option("c", "third option");
    }

    @Test
    public void testAddOption() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getNames().contains(option1.getKey()));
        assertTrue(optionGroup.getNames().contains(option2.getKey()));
    }

    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> {
            optionGroup.setSelected(option2);
        });
    }

    @Test
    public void testGetSelected() {
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[" +
                "-" + option1.getOpt() + " " + option1.getDescription() + ", " +
                "-" + option2.getOpt() + " " + option2.getDescription() +
                "]";
        assertEquals(expected, optionGroup.toString());
    }
}