package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
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
        Option option = new Option("f", "foo", true, "Test option");
        optionGroup.addOption(option);

        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("f", "foo", true, "Test option1");
        optionGroup.addOption(option1);

        Option option2 = new Option("b", "bar", true, "Test option2");
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains(option1.getOpt()));
        assertTrue(optionGroup.getNames().contains(option2.getOpt()));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "foo", true, "Test option1");
        optionGroup.addOption(option1);

        Option option2 = new Option("b", "bar", true, "Test option2");
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option = new Option("f", "foo", true, "Test option");
        optionGroup.addOption(option);

        optionGroup.setSelected(option);

        assertEquals(option.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedReset() throws AlreadySelectedException {
        Option option = new Option("f", "foo", true, "Test option");
        optionGroup.addOption(option);

        optionGroup.setSelected(option);
        assertEquals(option.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "foo", true, "Test option1");
        optionGroup.addOption(option1);

        Option option2 = new Option("b", "bar", true, "Test option2");
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option = new Option("f", "foo", true, "Test option");
        optionGroup.addOption(option);

        optionGroup.setSelected(option);

        assertEquals(option.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("f", "foo", true, "Test option1");
        Option option2 = new Option("b", "bar", true, "Test option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-" + option1.getOpt() + " " + option1.getDescription() + ", -" + option2.getOpt()
                + " " + option2.getDescription() + "]";
        assertEquals(expected, optionGroup.toString());
    }
}