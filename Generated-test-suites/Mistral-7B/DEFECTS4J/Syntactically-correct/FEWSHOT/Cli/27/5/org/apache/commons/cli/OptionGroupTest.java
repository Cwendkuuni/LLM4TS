package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testOptionGroup_Empty() {
        OptionGroup optionGroup = new OptionGroup();
        assertEquals(Collections.emptyMap(), optionGroup.getOptionMap());
        assertNull(optionGroup.getSelected());
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = Option.builder("o1").longOpt("l1").build();
        Option option2 = Option.builder("o2").longOpt("l2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Map<String, Option> optionMap = optionGroup.getOptionMap();
        assertEquals(2, optionMap.size());
        assertEquals(option1, optionMap.get("o1"));
        assertEquals(option2, optionMap.get("o2"));

        List<Option> options = new ArrayList<>(optionMap.values());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));

        String selected = optionGroup.getSelected();
        assertNull(selected);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = Option.builder("o1").longOpt("l1").build();
        Option option2 = Option.builder("o2").longOpt("l2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            assertFalse(true); // Expected AlreadySelectedException
        } catch (AlreadySelectedException e) {
            // Expected
        }
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup1 = new OptionGroup();
        assertFalse(optionGroup1.isRequired());

        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.setRequired(true);
        assertTrue(optionGroup2.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = Option.builder("o1").longOpt("l1").build();
        Option option2 = Option.builder("o2").longOpt("l2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-o1 Option 1 [-l1 Option 1]\n" +
                " -o2 Option 2 [-l2 Option 2]]";
        assertEquals(expected, optionGroup.toString());
    }
}