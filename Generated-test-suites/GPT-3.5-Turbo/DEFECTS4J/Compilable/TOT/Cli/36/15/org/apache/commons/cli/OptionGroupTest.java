package org.apache.commons.cli;
import org.junit.Test;
import java.util.Collection;

import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "add", false, "Add an element");
        Option option2 = new Option("d", "delete", false, "Delete an element");
        Option option3 = new Option("u", "update", false, "Update an element");

        OptionGroup optionGroup = new OptionGroup();
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
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "Add an element");
        Option option2 = new Option("d", "delete", false, "Delete an element");
        Option option3 = new Option("u", "update", false, "Update an element");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<String> names = optionGroup.getNames();
        assertEquals(3, names.size());
        assertTrue(names.contains(option1.getKey()));
        assertTrue(names.contains(option2.getKey()));
        assertTrue(names.contains(option3.getKey()));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "Add an element");
        Option option2 = new Option("d", "delete", false, "Delete an element");
        Option option3 = new Option("u", "update", false, "Update an element");

        OptionGroup optionGroup = new OptionGroup();
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
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add an element");
        Option option2 = new Option("d", "delete", false, "Delete an element");
        Option option3 = new Option("u", "update", false, "Update an element");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(option3);
        assertEquals(option3.getKey(), optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add an element");
        Option option2 = new Option("d", "delete", false, "Delete an element");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "Add an element");
        Option option2 = new Option("d", "delete", false, "Delete an element");
        Option option3 = new Option("u", "update", false, "Update an element");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expected = "[-a add, -d delete, -u update]";
        assertEquals(expected, optionGroup.toString());
    }
}