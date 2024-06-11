package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1= new Option("a", "option1");
        Option option2= new Option("b", "option2");
        assertEquals(optionGroup, optionGroup.addOption(option1));
        assertEquals(optionGroup, optionGroup.addOption(option2));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1= new Option("a", "option1");
        Option option2= new Option("b", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(optionGroup.getNames().size(), 2);
        assertEquals(optionGroup.getNames().contains("a"), true);
        assertEquals(optionGroup.getNames().contains("b"), true);
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1= new Option("a", "option1");
        Option option2= new Option("b", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(optionGroup.getOptions().size(), 2);
        assertEquals(optionGroup.getOptions().contains(option1), true);
        assertEquals(optionGroup.getOptions().contains(option2), true);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1= new Option("a", "option1");
        Option option2= new Option("b", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1= new Option("a", "option1");
        Option option2= new Option("b", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals(optionGroup.getSelected(), option1.getKey());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertEquals(optionGroup.isRequired(), true);
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1= new Option("a", "option1");
        Option option2= new Option("b", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(optionGroup.toString(), "[ -a, -b]");
    }
}