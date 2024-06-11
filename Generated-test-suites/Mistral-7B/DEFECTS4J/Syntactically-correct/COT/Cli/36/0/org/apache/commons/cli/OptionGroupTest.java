package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.AlreadySelectedException;

/**
 * Tests for {@link OptionGroup} class.
 */
public class OptionGroupTest
{
    @org.junit.Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals(option1, optionGroup.getOptions().iterator().next());
        assertEquals(option2, optionGroup.getOptions().iterator().next());
    }

    @org.junit.Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> names = new ArrayList<>(optionGroup.getNames());

        assertTrue(names.contains("opt1"));
        assertTrue(names.contains("opt2"));
    }

    @org.junit.Test
    public void testGetOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> options = new ArrayList<>(optionGroup.getOptions());

        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @org.junit.Test
    public void testSetSelectedNull()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected());
    }

    @org.junit.Test
    public void testSetSelectedSameOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    @org.junit.Test
    public void testSetSelectedDifferentOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option2);

        assertEquals(option2.getKey(), optionGroup.getSelected());
    }

    @org.junit.Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @org.junit.Test
    public void testIsRequiredFalse()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(false);

        assertFalse(optionGroup.isRequired());
    }

    @org.junit.Test
    public void testIsRequiredTrue()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @org.junit.Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--opt1 opt1 description, --opt2 opt2 description]";

        assertEquals(expected, optionGroup.toString());
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseExceptionNull()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.parse(null);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseExceptionEmpty()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.parse(new String[0]);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseExceptionUnknownOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);

        optionGroup.addOption(option1);

        String[] args = {"unknownOption"};

        optionGroup.parse(args);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseExceptionRequiredOptionMissing()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);

        optionGroup.setRequired(true);
        optionGroup.addOption(option1);

        String[] args = {};

        optionGroup.parse(args);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseExceptionMixedOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String[] args = {"--opt1", "unknownOption"};

        optionGroup.parse(args);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseExceptionMixedOptions2()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String[] args = {"unknownOption", "--opt1"};

        optionGroup.parse(args);
    }

    @org.junit.Test
    public void testParseCorrectly()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String[] args = {"--opt1", "--opt2"};

        optionGroup.parse(args);

        assertTrue(optionGroup.isSet("opt1"));
        assertTrue(optionGroup.isSet("opt2"));
    }

    @org.junit.Test
    public void testParseCorrectlySelectedOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        String[] args = {"--opt1"};

        optionGroup.parse(args);

        assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    @org.junit.Test
    public void testParseCorrectlyNoSelectedOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String[] args = {"--opt2"};

        optionGroup.parse(args);

        assertNull(optionGroup.getSelected());
    }

    @org.junit.Test
    public void testParseCorrectlyMixedOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String[] args = {"--opt2", "--opt1"};

        optionGroup.parse(args);

        assertEquals(option2.getKey(), optionGroup.getSelected());
    }

    @org.junit.Test
    public void testParseCorrectlyTwoSelectedOptions()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description", true);
        Option option2 = new Option("opt2", "opt2 description", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);

        String[] args = {"--opt1", "--opt2"};

        optionGroup.parse(args);

        assertEquals(option1.getKey(), optionGroup.getSelected());
        assertEquals(option2.getKey(), optionGroup.getSelected());
    }
}