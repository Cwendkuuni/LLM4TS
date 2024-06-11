package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;

import java.util.Collection;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();

        option1 = new Option("a", "option1", false, "Option 1");
        option2 = new Option("b", "option2", false, "Option 2");
        option3 = new Option("c", "option3", false, "Option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
    }

    @After
    public void tearDown() {
        optionGroup = null;
        option1 = null;
        option2 = null;
        option3 = null;
    }

    @Test
    public void testAddOption() {
        OptionGroup newOptionGroup = optionGroup.addOption(new Option("d", "option4", false, "Option 4"));
        assertTrue(newOptionGroup.getOptions().contains(option1));
        assertTrue(newOptionGroup.getOptions().contains(option2));
        assertTrue(newOptionGroup.getOptions().contains(option3));
        assertTrue(newOptionGroup.getOptions().contains(new Option("d", "option4", false, "Option 4")));
    }

    @Test
    public void testGetNames() {
        Collection names = optionGroup.getNames();
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
        assertTrue(names.contains("c"));
    }

    @Test
    public void testGetOptions() {
        Collection options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }

    @Test
    public void testSetSelected() {
        try {
            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());

            optionGroup.setSelected(option1);
            assertEquals(option1.getOpt(), optionGroup.getSelected());

            optionGroup.setSelected(option2);
            assertEquals(option2.getOpt(), optionGroup.getSelected());

            optionGroup.setSelected(option3);
            assertEquals(option3.getOpt(), optionGroup.getSelected());

            optionGroup.setSelected(option3);
            assertEquals(option3.getOpt(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("AlreadySelectedException should not be thrown");
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        assertNull(optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
            assertEquals(option1.getOpt(), optionGroup.getSelected());

            optionGroup.setSelected(option2);
            assertEquals(option2.getOpt(), optionGroup.getSelected());

            optionGroup.setSelected(option3);
            assertEquals(option3.getOpt(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("AlreadySelectedException should not be thrown");
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
    public void testIsRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        String expected = "[--option1 Option 1, --option2 Option 2, --option3 Option 3]";
        assertEquals(expected, optionGroup.toString());
    }
}