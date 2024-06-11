package org.junit.tests;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1", false, "o1");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
    }

    @Test(expected = AlreadySelectedException.class)
    public void testAddOptionTwice() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1", false, "o1");
        optionGroup.addOption(option);
        optionGroup.setSelected(option); // select the option
        optionGroup.addOption(option); // try to add it again
    }

    @Test
    public void testGetOptionsEmptyList() {
        OptionGroup optionGroup = new OptionGroup();
        Collection<Option> options = optionGroup.getOptions();
        assertEquals(0, options.size());
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelectedOption());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertEquals(true, optionGroup.isRequired());
    }

    @Test
    public void testSetRequiredFalse() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        assertEquals(false, optionGroup.isRequired());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSelectedNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
    }
}