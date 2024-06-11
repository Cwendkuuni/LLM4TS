package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

import java.util.Collection;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "option1", false, "First Option");
        option2 = new Option("b", "option2", false, "Second Option");
        option3 = new Option("c", "option3", false, "Third Option");
    }

    @Test
    public void testAddOption() {
        optionGroup.addOption(option1);

        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains("a"));
    }

    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<String> names = optionGroup.getNames();
        assertEquals(3, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
        assertTrue(names.contains("c"));
    }

    @Test
    public void testGetOptions() {
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
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());

        // Trying to set an already selected option should throw AlreadySelectedException
        try {
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException to be thrown");
        } catch (AlreadySelectedException e) {
            // Expected exception to be thrown
        }
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertNull(optionGroup.getSelected());

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
    }

    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expected = "[ -a First Option, -b Second Option, -c Third Option ]";
        assertEquals(expected, optionGroup.toString());
    }
}