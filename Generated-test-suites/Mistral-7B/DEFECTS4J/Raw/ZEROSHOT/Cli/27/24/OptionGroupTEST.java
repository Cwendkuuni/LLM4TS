package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp()
    {
        optionGroup = new OptionGroup();
        option1 = new Option("o1", "option1", true, "Option 1");
        option2 = new Option("o2", "option2", true, "Option 2");
    }

    /**
     * Tests method addOption.
     */
    @Test
    public void testAddOption()
    {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> options = new ArrayList<>(optionGroup.getOptions());

        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    /**
     * Tests method getNames.
     */
    @Test
    public void testGetNames()
    {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> names = new ArrayList<>(optionGroup.getNames());

        assertEquals(2, names.size());
        assertTrue(names.contains(option1.getOpt()));
        assertTrue(names.contains(option2.getOpt()));
    }

    /**
     * Tests method getSelected.
     */
    @Test
    public void testGetSelected()
    {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);

        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    /**
     * Tests method setSelected.
     */
    @Test
    public void testSetSelected()
    {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertThrows(NullPointerException.class, () -> optionGroup.setSelected(null));

        optionGroup.setSelected(option1);

        assertEquals(option1.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option2);

        assertEquals(option2.getOpt(), optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
    }

    /**
     * Tests method isRequired.
     */
    @Test
    public void testIsRequired()
    {
        optionGroup = new OptionGroup();
        optionGroup.setRequired(false);

        assertFalse(optionGroup.isRequired());

        optionGroup = new OptionGroup();
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    /**
     * Tests method toString.
     */
    @Test
    public void testToString()
    {
        Map<String, Option> optionsMap = new HashMap<>();
        optionsMap.put(option1.getOpt(), option1);
        optionsMap.put(option2.getOpt(), option2);
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setOptions(optionsMap.values());

        String expected = "[-o1 Option 1, -o2 Option 2]";

        assertEquals(expected, optionGroup.toString());
    }
}