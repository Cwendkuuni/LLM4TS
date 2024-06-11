package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1");
        Option option2 = new Option("opt2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals("opt1", optionGroup.getOptions().iterator().next().getKey());
        assertEquals("opt2", optionGroup.getOptions().iterator().next(1).getKey());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1");
        Option option2 = new Option("opt2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getNames().size());
        assertEquals("opt1", optionGroup.getNames().iterator().next());
        assertEquals("opt2", optionGroup.getNames().iterator().next(1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1");
        Option option2 = new Option("opt2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelectedOption());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
        optionGroup.setSelected(option2);
        assertEquals(option2, optionGroup.getSelectedOption());
    }

    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1");
        Option option2 = new Option("opt2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelectedOption());

        optionGroup.setSelected(option2);
        assertEquals(option2, optionGroup.getSelectedOption());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1");
        Option option2 = new Option("opt2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[[-opt1 option 1, --opt1 option 1], [-opt2 option 2, --opt2 option 2]]", optionGroup.toString());
    }

    private OptionGroup.AlreadySelectedException getException() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1");
        Option option2 = new Option("opt2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        return assertThrows(OptionGroup.AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
    }

    @Test
    public void testAlreadySelectedExceptionMessage() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1");
        Option option2 = new Option("opt2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertThrows(OptionGroup.AlreadySelectedException.class, () -> optionGroup.setSelected(option1));

        assertEquals("Option [" + Option.class.getName() + " name=opt1] is already selected in OptionGroup", getException().getMessage());
    }
}