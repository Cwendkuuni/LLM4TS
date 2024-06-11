package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "foo", false, "option foo");
        optionGroup.addOption(option);

        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        Option option = new Option("f", "foo", false, "option foo");
        optionGroup.addOption(option);

        assertEquals(1, optionGroup.getNames().size());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("f", "foo", false, "option foo");
        optionGroup.addOption(option);

        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "foo", false, "option foo");
        Option option2 = new Option("b", "bar", false, "option bar");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("f", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("f", "foo", false, "option foo");
        Option option2 = new Option("b", "bar", false, "option bar");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedNull() throws AlreadySelectedException {
        Option option1 = new Option("f", "foo", false, "option foo");
        Option option2 = new Option("b", "bar", false, "option bar");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);

        assertEquals(null, optionGroup.getSelected());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("f", "foo", false, "option foo");
        Option option2 = new Option("b", "bar", false, "option bar");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-f option foo, -b option bar]";
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertEquals(true, optionGroup.isRequired());
    }
}