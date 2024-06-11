package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    private OptionGroup optionGroup;

    @Before
    public void setUp()
    {
        // create an OptionGroup instance
        optionGroup = new OptionGroup();

        // create some Options for the OptionGroup
        Option optA = new Option("a", "option A");
        Option optB = new Option("b", "option B");
        Option optC = new Option("c", "option C");

        // add the Options to the OptionGroup
        optionGroup.addOption(optA);
        optionGroup.addOption(optB);
        optionGroup.addOption(optC);
    }

    @Test
    public void testGetNames()
    {
        // test the getNames() method
        List<String> expected = Arrays.asList("a", "b", "c");
        List<String> actual = new ArrayList<>();
        for (Object name : optionGroup.getNames())
        {
            actual.add((String) name);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOptions()
    {
        // test the getOptions() method
        List<Option> expected = Arrays.asList(optA, optB, optC);
        List<Option> actual = new ArrayList<>();
        for (Object option : optionGroup.getOptions())
        {
            actual.add((Option) option);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testSetSelected()
    {
        // test the setSelected() method with valid Option
        optionGroup.setSelected(optA);
        assertEquals(optA.getOpt(), optionGroup.getSelected());

        // test the setSelected() method with null Option
        assertNull(optionGroup.getSelected());
        optionGroup.setSelected(null);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected()
    {
        // test the setSelected() method with already selected Option
        optionGroup.setSelected(optA);
        optionGroup.setSelected(optA);
    }

    @Test
    public void testIsRequired()
    {
        // test the isRequired() method with the default value
        assertFalse(optionGroup.isRequired());

        // set the required flag and test it
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        // test the toString() method
        String expected = "[-a option A, -b option B, --option-C option C ]";
        String actual = optionGroup.toString();
        assertEquals(expected, actual);
    }
}