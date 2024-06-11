package org.apache.commons.cli;

import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {
    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", false, "Test option");
        optionGroup.addOption(option);

        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getNames().contains("t"));
        assertTrue(optionGroup.getNames().contains("u"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("t", optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option2);

        assertEquals("u", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[t, u]", optionGroup.toString());
    }
}
