package org.apache.commons.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

import org.junit.Before;
import org.junit.Test;

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
        Option option = new Option("o", "option", false, "Test option");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("o1", "option1", false, "Test option 1");
        Option option2 = new Option("o2", "option2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains(option1.getKey()));
        assertTrue(names.contains(option2.getKey()));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "Test option 1");
        Option option2 = new Option("o2", "option2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option = new Option("o", "option", false, "Test option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        assertEquals(option.getKey(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        Option option1 = new Option("o1", "option1", false, "Test option 1");
        Option option2 = new Option("o2", "option2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option = new Option("o", "option", false, "Test option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        assertEquals(option.getKey(), optionGroup.getSelected());
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
        Option option1 = new Option("o1", "option1", false, "Test option 1");
        Option option2 = new Option("o2", "option2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[--option1 Test option 1, --option2 Test option 2]";
        assertEquals(expected, optionGroup.toString());
    }

}
