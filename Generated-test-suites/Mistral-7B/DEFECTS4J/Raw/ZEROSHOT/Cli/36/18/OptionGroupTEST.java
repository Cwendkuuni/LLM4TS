package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test for OptionGroup class.
 */
public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals(option1, optionGroup.getOptions().iterator().next());
        assertEquals(option2, optionGroup.getOptions().iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        assertEquals(2, names.size());
        assertSame(option1.getKey(), names.iterator().next());
        assertSame(option2.getKey(), names.iterator().next());
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSame() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("opt", "Option");

        optionGroup.setSelected(option);
        assertEquals(option.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(option);
        assertEquals(option.getKey(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferent() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--opt1 Option 1, --opt2 Option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}