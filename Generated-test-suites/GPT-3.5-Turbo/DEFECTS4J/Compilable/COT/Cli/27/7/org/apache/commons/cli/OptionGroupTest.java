package org.apache.commons.cli;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);

        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertFalse(options.contains(option2));

        optionGroup.addOption(option2);
        options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
        assertFalse(names.contains("c"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedException() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

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
        Option option1 = new Option("a", "option1");
        option1.setDescription("Description 1");
        Option option2 = new Option("b", "option2");
        option2.setDescription("Description 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-a Description 1, -b Description 2]";
        assertEquals(expected, optionGroup.toString());
    }
}
