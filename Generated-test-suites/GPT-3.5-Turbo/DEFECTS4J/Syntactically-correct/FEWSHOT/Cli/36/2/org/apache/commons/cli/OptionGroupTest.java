
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
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("f", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelected_NullOption() throws AlreadySelectedException {
        Option option1 = new Option("f", "foo", false, "foo option");

        optionGroup.addOption(option1);

        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsSelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertTrue(optionGroup.isSelected(option1));
        assertFalse(optionGroup.isSelected(option2));
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);

        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", false, "bar option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[ -f, --bar ]", optionGroup.toString());
    }
}
