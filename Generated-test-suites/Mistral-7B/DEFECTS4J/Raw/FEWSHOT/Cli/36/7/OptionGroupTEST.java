package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("opt", "opt description");
        Option option2 = new Option("opt2", "opt2 description");

        optionGroup.addOption(option);
        optionGroup.addOption(option2);

        Map<String, Option> optionMap = optionGroup.optionMap;

        assertEquals(2, optionMap.size());
        assertEquals(option, optionMap.get("opt"));
        assertEquals(option2, optionMap.get("opt2"));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("opt", "opt description");
        Option option2 = new Option("opt2", "opt2 description");

        optionGroup.addOption(option);
        optionGroup.addOption(option2);

        List<String> names = optionGroup.getNames();

        assertEquals(2, names.size());
        assertTrue(names.contains("opt"));
        assertTrue(names.contains("opt2"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("opt", "opt description");
        Option option2 = new Option("opt2", "opt2 description");

        optionGroup.addOption(option);
        optionGroup.addOption(option2);

        List<Option> options = optionGroup.getOptions();

        assertEquals(2, options.size());
        assertSame(option, options.get(0));
        assertSame(option2, options.get(1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description");
        Option option2 = new Option("opt2", "opt2 description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("opt1", optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> {
            optionGroup.setSelected(option1);
        });

        optionGroup.setSelected(option2);
        assertEquals("opt2", optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        OptionGroup requiredOptionGroup = new OptionGroup();
        OptionGroup optionalOptionGroup = new OptionGroup();

        optionGroup.setRequired(false);
        requiredOptionGroup.setRequired(true);
        optionalOptionGroup.setRequired(false);

        assertFalse(optionGroup.isRequired());
        assertTrue(requiredOptionGroup.isRequired());
        assertFalse(optionalOptionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1 description");
        Option option2 = new Option("opt2", "opt2 description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-opt, opt description, --opt2, opt2 description]";
        String actual = optionGroup.toString();

        assertEquals(expected, actual);
    }
}