package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = Option.builder("option1").build();
        Option option2 = Option.builder("option2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals("option1", optionGroup.getOptions().stream().filter(o -> o.getKey().equals("option1")).findFirst().get().getKey());
        assertEquals("option2", optionGroup.getOptions().stream().filter(o -> o.getKey().equals("option2")).findFirst().get().getKey());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = Option.builder("option1").build();
        Option option2 = Option.builder("option2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(Arrays.asList("option1", "option2"), optionGroup.getNames().spliterator().collect(Collectors.toList()));
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = Option.builder("option").build();

        optionGroup.setSelected(option);
        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedOptionExists() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = Option.builder("option1").build();
        Option option2 = Option.builder("option2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("option1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("option2", optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("option1", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedOptionAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = Option.builder("option1").build();
        Option option2 = Option.builder("option2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequiredFalse() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequiredTrue() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = Option.builder("option1").build();
        Option option2 = Option.builder("option2").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[--option1 option1 , --option2 option2 ]", optionGroup.toString());
    }
}