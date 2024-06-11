package org.apache.commons.cli;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "optionA", false, "Option A");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames() {
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        Collection<String> names = optionGroup.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        Collection<Option> options = optionGroup.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(optionA));
        assertTrue(options.contains(optionB));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");

        // Select option A
        optionGroup.setSelected(optionA);
        assertEquals("a", optionGroup.getSelected());

        // Select option B
        optionGroup.setSelected(optionB);
        assertEquals("b", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");

        // Select option A
        optionGroup.setSelected(optionA);
        assertEquals("a", optionGroup.getSelected());

        // Select option B (should throw AlreadySelectedException)
        optionGroup.setSelected(optionB);
    }

    @Test
    public void testSetSelected_NullOption() throws AlreadySelectedException {
        Option optionA = new Option("a", "optionA", false, "Option A");

        // Select option A
        optionGroup.setSelected(optionA);
        assertEquals("a", optionGroup.getSelected());

        // Select null option (reset)
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");

        // No option selected
        assertNull(optionGroup.getSelected());

        // Select option A
        optionGroup.setSelected(optionA);
        assertEquals("a", optionGroup.getSelected());

        // Select option B
        optionGroup.setSelected(optionB);
        assertEquals("b", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);

        String expected = "[-a Option A, -b Option B]";
        assertEquals(expected, optionGroup.toString());
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test(expected = NoSuchElementException.class)
    public void testGetNames_EmptyOptionGroup() {
        optionGroup.getNames().iterator().next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetOptions_EmptyOptionGroup() {
        optionGroup.getOptions().iterator().next();
    }
}
