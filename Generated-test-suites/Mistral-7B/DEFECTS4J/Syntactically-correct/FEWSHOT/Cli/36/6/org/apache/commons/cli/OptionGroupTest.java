package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOptionAndGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        assertTrue(names.containsAll(Arrays.asList("opt1", "opt2")));
    }

    @Test
    public void testSetAndGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            String selected = optionGroup.getSelected();
            assertSame(option1.getKey(), selected);
        } catch (AlreadySelectedException e) {
            // expected
        }

        try {
            optionGroup.setSelected(option2);
            String selected = optionGroup.getSelected();
            assertSame(option2.getKey(), selected);
        } catch (AlreadySelectedException e) {
            // expected
        }

        try {
            optionGroup.setSelected(option1);
            String selected = optionGroup.getSelected();
            assertSame(option1.getKey(), selected);
        } catch (AlreadySelectedException e) {
            // not expected
            fail("Should not have thrown an AlreadySelectedException.");
        }
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");

        optionGroup.addOption(option1);

        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSelectedNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");

        optionGroup.addOption(option1);

        optionGroup.setSelected(null);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedOption() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-opt1 Option 1, --opt2 Option 2]";
        String actual = optionGroup.toString();
        assertEquals(expected, actual);
    }
}