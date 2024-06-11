package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

import java.util.Collection;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("o1", "option1", false, "First option");
        option2 = new Option("o2", "option2", false, "Second option");
    }

    @Test
    public void testAddOption() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection names = optionGroup.getNames();

        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
    }

    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection options = optionGroup.getOptions();

        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        try {
            optionGroup.setSelected(option1);
            assertEquals("o1", optionGroup.getSelected());

            optionGroup.setSelected(option2);
            assertEquals("o2", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }

    @Test
    public void testSetSelectedException() {
        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option1);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected
        }
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
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[ -o1 First option, -o2 Second option]";
        assertEquals(expected, optionGroup.toString());
    }
}