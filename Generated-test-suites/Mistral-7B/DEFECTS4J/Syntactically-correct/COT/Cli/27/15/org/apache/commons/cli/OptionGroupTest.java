package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals(option1, optionGroup.getOptions().toArray()[0]);
        assertEquals(option2, optionGroup.getOptions().toArray()[1]);
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> expectedNames = new ArrayList<>();
        expectedNames.add(option1.getOpt());
        expectedNames.add(option2.getOpt());

        List<String> actualNames = new ArrayList<>(optionGroup.getNames());

        assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testSetSelected_null()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals(option1, optionGroup.getSelectedOption());
    }

    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedToString = "[--o1 option 1, --o2 option 2]";

        assertEquals(expectedToString, optionGroup.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSelected_nullOption() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(null);
    }

    @Test
    public void testIsRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testSetRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOption_nullOption()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.addOption(null);
    }
}