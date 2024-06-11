package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    /**
     * Test method for {@link OptionGroup#OptionGroup()}.
     */
    @org.junit.Test
    public void testConstructor()
    {
        OptionGroup optionGroup = new OptionGroup();
        assertNotNull(optionGroup);
    }

    /**
     * Test method for {@link OptionGroup#addOption(Option)}.
     */
    @org.junit.Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1").build();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> options = new LinkedList<>(optionGroup.getOptions());
        List<Option> expectedOptions = Arrays.asList(option1, option2);

        assertEquals(expectedOptions, options);
    }

    /**
     * Test method for {@link OptionGroup#getNames()}.
     */
    @org.junit.Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1").build();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Iterator<String> namesIterator = optionGroup.getNames().iterator();
        List<String> expectedNames = Arrays.asList("option1", "option2");

        List<String> names = new LinkedList<>();
        while (namesIterator.hasNext())
        {
            names.add(namesIterator.next());
        }

        assertEquals(expectedNames, names);
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)}.
     */
    @org.junit.Test
    public void testSetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1").build();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals(option1.getLongOpt(), optionGroup.getSelected());

        // Test exception when trying to set a selected option
        // that has already been selected
        optionGroup.setSelected(option1);
        assertThrows(AlreadySelectedException.class, () -> {
            optionGroup.setSelected(option1);
        });

        optionGroup.setSelected(option2);
        assertEquals(option2.getLongOpt(), optionGroup.getSelected());
    }

    /**
     * Test method for {@link OptionGroup#isRequired()}.
     */
    @org.junit.Test
    public void testIsRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#toString()}.
     */
    @org.junit.Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1").build();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedString = "[--option1 Option 1, --option2 Option 2 ]";
        String string = optionGroup.toString();

        assertEquals(expectedString, string);
    }

    /**
     * Test method for {@link OptionGroup#OptionGroup(String)}.
     */
    @org.junit.Test
    public void testConstructorWithName()
    {
        OptionGroup optionGroup = new OptionGroup("My Option Group");
        assertNotNull(optionGroup);
    }

    /**
     * Test method for {@link OptionGroup#OptionGroup(String, boolean)}.
     */
    @org.junit.Test
    public void testConstructorWithNameAndRequired()
    {
        OptionGroup optionGroup = new OptionGroup("My Option Group", true);
        assertNotNull(optionGroup);
        assertTrue(optionGroup.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#setSelected(String)}.
     */
    @org.junit.Test
    public void testSetSelectedWithString()
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1").build();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected("option1");
        assertEquals(option1.getLongOpt(), optionGroup.getSelected());

        optionGroup.setSelected("option2");
        assertEquals(option2.getLongOpt(), optionGroup.getSelected());

        // Test exception when trying to set a non-existent option
        assertThrows(ParseException.class, () -> {
            optionGroup.setSelected("non-existent-option");
        });
    }
}