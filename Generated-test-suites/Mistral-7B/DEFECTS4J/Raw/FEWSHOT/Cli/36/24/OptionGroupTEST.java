package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals("o1", optionGroup.getOptions().iterator().next().getKey());
        assertEquals("o2", optionGroup.getOptions().iterator().next(1).getKey());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Set<String> names = new HashSet<>(optionGroup.getNames());
        assertEquals(2, names.size());
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> options = new ArrayList<>(optionGroup.getOptions());
        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("o1", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        StringBuilder expected = new StringBuilder();
        expected.append("[");
        expected.append("-o1 ");
        expected.append("--o2 ");
        expected.append("]");

        assertEquals(expected.toString(), optionGroup.toString());
    }

    @Test
    public void testSelectedWithSameOptionTwice() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = Option.builder("o").build();

        optionGroup.addOption(option);
        optionGroup.addOption(option);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option);
        assertEquals("o", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    @Test
    public void testSelectedWithNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = Option.builder("o").build();

        optionGroup.addOption(option);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
}