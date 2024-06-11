package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Test method for {@link OptionGroup#OptionGroup()}.
     */
    @org.junit.Test
    public void testConstructor()
    {
        OptionGroup optionGroup = new OptionGroup();
        assertEquals(Collections.emptyMap(), optionGroup.optionMap);
        assertNull(optionGroup.selected);
        assertFalse(optionGroup.required);
    }

    /**
     * Test method for {@link OptionGroup#OptionGroup(String)}.
     */
    @org.junit.Test
    public void testConstructorWithName()
    {
        OptionGroup optionGroup = new OptionGroup("Test Group");
        assertEquals("Test Group", optionGroup.getName());
        assertEquals(Collections.emptyMap(), optionGroup.optionMap);
        assertNull(optionGroup.selected);
        assertFalse(optionGroup.required);
    }

    /**
     * Test method for {@link OptionGroup#addOption(Option)}.
     */
    @org.junit.Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-desc");
        Option option2 = new Option("o2", "option2", true, "o2-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.optionMap.size());
        assertEquals(option1, optionGroup.optionMap.get("o1"));
        assertEquals(option2, optionGroup.optionMap.get("o2"));
    }

    /**
     * Test method for {@link OptionGroup#getNames()}.
     */
    @org.junit.Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");
        Option option3 = new Option("o3", "option3", false, "o3-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<String> expectedNames = new ArrayList<>(optionGroup.getNames());
        List<String> names = Arrays.asList("o1", "o2", "o3");

        assertEquals(names, expectedNames);
    }

    /**
     * Test method for {@link OptionGroup#getOptions()}.
     */
    @org.junit.Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-desc");
        Option option2 = new Option("o2", "option2", true, "o2-desc");
        Option option3 = new Option("o3", "option3", false, "o3-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<Option> expectedOptions = new ArrayList<>(optionGroup.getOptions());
        List<Option> options = Arrays.asList(option1, option2, option3);

        assertEquals(options, expectedOptions);
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)}.
     */
    @org.junit.Test
    public void testSetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("o1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("o2", optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
    }

    /**
     * Test method for {@link OptionGroup#getSelected()}.
     */
    @org.junit.Test
    public void testGetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("o1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("o2", optionGroup.getSelected());

        assertNull(optionGroup.getSelected());
    }

    /**
     * Test method for {@link OptionGroup#setRequired(boolean)}.
     */
    @org.junit.Test
    public void testSetRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#isRequired()}.
     */
    @org.junit.Test
    public void testIsRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);

        assertFalse(optionGroup.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#toString()}.
     */
    @org.junit.Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        StringBuilder expectedString = new StringBuilder();
        expectedString.append("[");
        expectedString.append("-o1 option1 ");
        expectedString.append("o1-desc, ");
        expectedString.append("-o2 option2 ");
        expectedString.append("o2-desc]");

        assertEquals(expectedString.toString(), optionGroup.toString());
    }
}