package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Test class for OptionGroup in org.apache.commons.cli package.
 */
public class OptionGroupTest
{
    @org.junit.Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optA", "Option A", false, "a");
        optionGroup.addOption(option);
        Set<String> expectedOptions = new HashSet<>(Arrays.asList("optA"));
        Set<String> actualOptions = new HashSet<>(optionGroup.getNames());
        assertEquals(expectedOptions, actualOptions);
    }

    @org.junit.Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optA", "Option A", false, "a");
        Option option2 = new Option("optB", "Option B", false, "b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Set<String> expectedOptions = new HashSet<>(Arrays.asList("optA", "optB"));
        Set<String> actualOptions = new HashSet<>(optionGroup.getNames());
        assertEquals(expectedOptions, actualOptions);
    }

    @org.junit.Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optA", "Option A", false, "a");
        Option option2 = new Option("optB", "Option B", false, "b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Option[] expectedOptions = new Option[]{option1, option2};
        Option[] actualOptions = optionGroup.getOptions().toArray(new Option[0]);
        assertArrayEquals(expectedOptions, actualOptions);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOptionGroup()
    {
        Option option = new Option("optA", "Option A", false, "a");
        OptionGroup optionGroup = null;
        optionGroup.setSelected(option);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        OptionGroup expectedException = null;
        optionGroup.setSelected(expectedException);
    }

    @org.junit.Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException
    {
        Option optionA = new Option("optA", "Option A", false, "a");
        Option optionB = new Option("optB", "Option B", false, "b");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        optionGroup.setSelected(optionA);
        optionGroup.setSelected(optionB);
    }

    @org.junit.Test
    public void testSetSelected()
    {
        Option optionA = new Option("optA", "Option A", false, "a");
        Option optionB = new Option("optB", "Option B", false, "b");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        optionGroup.setSelected(optionA);
        assertEquals(optionA.getOpt(), optionGroup.getSelected());
    }

    @org.junit.Test
    public void testIsRequired()
    {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @org.junit.Test
    public void testToString()
    {
        Option optionA = new Option("optA", "Option A", false, "a");
        Option optionB = new Option("optB", "Option B", false, "b");
        Option optionC = new Option("optC", "Option C", false, "c");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        optionGroup.addOption(optionC);
        String expectedString = "[-optA Option A, --optB Option B, --optC Option C ]";
        assertEquals(expectedString, optionGroup.toString());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testToStringNullOptionGroup()
    {
        OptionGroup optionGroup = null;
        String expectedString = null;
        assertEquals(expectedString, optionGroup.toString());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddOptionNullOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;
        optionGroup.addOption(option);
    }
}