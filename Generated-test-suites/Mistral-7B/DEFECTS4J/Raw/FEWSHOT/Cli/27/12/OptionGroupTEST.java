package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testOptionGroupEmpty() {
        OptionGroup group = new OptionGroup();
        assertEquals(Collections.emptyList(), group.getNames());
        assertEquals(Collections.emptyList(), group.getOptions());
        assertNull(group.getSelected());
        assertFalse(group.isRequired());
    }

    @Test
    public void testOptionGroupWithOptions() {
        OptionGroup group = new OptionGroup();

        Option opt1 = new OptionBuilder("opt1").withDescription("Option 1").build();
        Option opt2 = new OptionBuilder("opt2").withDescription("Option 2").build();
        Option opt3 = new OptionBuilder("opt3").withDescription("Option 3").build();

        group.addOption(opt1);
        group.addOption(opt2);
        group.addOption(opt3);

        assertEquals(Arrays.asList("opt1", "opt2", "opt3"), group.getNames());
        assertEquals(Arrays.asList(opt1, opt2, opt3), group.getOptions());
        assertNull(group.getSelected());
        assertFalse(group.isRequired());
    }

    @Test
    public void testOptionGroupWithSelectedOption() {
        OptionGroup group = new OptionGroup();

        Option opt1 = new OptionBuilder("opt1").withDescription("Option 1").build();
        Option opt2 = new OptionBuilder("opt2").withDescription("Option 2").build();
        Option opt3 = new OptionBuilder("opt3").withDescription("Option 3").build();

        group.addOption(opt1);
        group.addOption(opt2);
        group.addOption(opt3);

        group.setSelected(opt1);

        assertEquals(Arrays.asList("opt1"), group.getNames());
        assertEquals(opt1, group.getSelected());
        assertFalse(group.isRequired());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testOptionGroupWithAlreadySelectedOption() {
        OptionGroup group = new OptionGroup();

        Option opt1 = new OptionBuilder("opt1").withDescription("Option 1").build();
        Option opt2 = new OptionBuilder("opt2").withDescription("Option 2").build();

        group.addOption(opt1);
        group.setSelected(opt1);
        group.setSelected(opt2);
    }

    @Test
    public void testOptionGroupWithRequiredOption() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);

        Option opt1 = new OptionBuilder("opt1").withDescription("Option 1").build();
        Option opt2 = new OptionBuilder("opt2").withDescription("Option 2").build();

        group.addOption(opt1);
        group.addOption(opt2);

        assertTrue(group.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionGroupWithNullOption() {
        OptionGroup group = new OptionGroup();
        Option nullOption = null;
        group.addOption(nullOption);
    }
}