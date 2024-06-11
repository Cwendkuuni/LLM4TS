package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", true, "Option description");
        Option addedOption = optionGroup.addOption(option);
        assertEquals(option, addedOption);
        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(null);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        Option option3 = new Option("o3", "option3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<String> names = optionGroup.getNames();
        assertEquals(3, names.size());
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
        assertTrue(names.contains("o3"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        Option option3 = new Option("o3", "option3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<Option> options = optionGroup.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }

    @Test
    public void testSetAndGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // no option is selected initially
        assertNull(optionGroup.getSelected());

        // set the first option as selected
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());

        // try to set the same option again
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());

        // set the second option as selected
        optionGroup.setSelected(option2);
        assertEquals(option2.getKey(), optionGroup.getSelected());

        // try to set the first option again after setting the second
        optionGroup.setSelected(option1);
        assertEquals(option2.getKey(), optionGroup.getSelected());

        // try to set a null option
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetAlreadySelectedOption() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup1 = new OptionGroup();
        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup1.setRequired(true);
        optionGroup2.setRequired(false);

        assertTrue(optionGroup1.isRequired());
        assertFalse(optionGroup2.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1");
        Option option2 = new Option("o2", "option2");
        Option option3 = new Option("o3", "option3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals("[--option1, --option2, --option3]", optionGroup.toString());
    }
}