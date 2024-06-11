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

public class OptionGroupTest
{
    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp()
    {
        optionGroup = new OptionGroup();
        option1 = new Option("o1", "option 1", false, 'o', "opt1");
        option2 = new Option("o2", "option 2", false, 'o', "opt2");
        option3 = new Option("o3", "option 3", false, 'o', "opt3");
    }

    @Test
    public void testAddOption()
    {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<Option> options = new ArrayList<>(optionGroup.getOptions());

        assertEquals(3, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
        assertEquals(option3, options.get(2));
    }

    @Test
    public void testGetNames()
    {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        List<String> names = new ArrayList<>(optionGroup.getNames());

        assertEquals(3, names.size());
        assertEquals(option1.getOpt(), names.get(0));
        assertEquals(option2.getOpt(), names.get(1));
        assertEquals(option3.getOpt(), names.get(2));
    }

    @Test
    public void testSetSelectedNull()
    {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSameOption()
    {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(option1);

        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption()
    {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired()
    {
        optionGroup.setRequired(false);

        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String actual = optionGroup.toString();

        String expected = "[-o opt1 option 1,\n" +
                "  -o opt2 option 2,\n" +
                "  --opt3 option 3 ]";

        assertEquals(expected, actual);
    }
}