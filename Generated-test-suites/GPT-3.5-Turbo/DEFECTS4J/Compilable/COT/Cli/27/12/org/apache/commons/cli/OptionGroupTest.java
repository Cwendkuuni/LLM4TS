package org.apache.commons.cli;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "option1", false, "Option 1");
        option2 = new Option("b", "option2", false, "Option 2");
    }

    @Test
    public void testAddOption() {
        OptionGroup updatedOptionGroup = optionGroup.addOption(option1);
        assertTrue(updatedOptionGroup.getOptions().contains(option1));
    }

    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedException() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-a Option 1, -b Option 2]";
        assertEquals(expected, optionGroup.toString());
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testAddOptionDuplicateName() {
        optionGroup.addOption(option1);
        Option updatedOption = new Option("a", "updatedOption", false, "Updated Option");
        optionGroup.addOption(updatedOption);
        assertEquals(1, optionGroup.getOptions().size());
        assertEquals(updatedOption, optionGroup.getOptions().iterator().next());
    }

    @Test
    public void testAddOptionDuplicateOption() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option1);
        assertEquals(1, optionGroup.getOptions().size());
        assertEquals(option1, optionGroup.getOptions().iterator().next());
    }

    @Test
    public void testGetNamesEmpty() {
        Collection<String> names = optionGroup.getNames();
        assertTrue(names.isEmpty());
    }

    @Test
    public void testGetOptionsEmpty() {
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.isEmpty());
    }

    @Test
    public void testSetSelectedNull() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSameOption() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedDifferentOption() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOptionException() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
        optionGroup.setSelected(option1);
    }
}

