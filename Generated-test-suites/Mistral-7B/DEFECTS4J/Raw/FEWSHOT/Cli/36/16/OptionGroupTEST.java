package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testOptionGroupEmpty() {
        OptionGroup group = new OptionGroup();
        assertNull(group.getSelected());
        assertFalse(group.isRequired());
        assertTrue(group.getNames().isEmpty());
        assertTrue(group.getOptions().isEmpty());
    }

    @Test
    public void testOptionGroupSingleOption() {
        Option option = new Option("f", "foo", false, "Foo Option");
        OptionGroup group = new OptionGroup();
        group.addOption(option);

        assertSame(option, group.getOptions().iterator().next());
        assertEquals(1, group.getOptions().size());
        assertSame(option, group.getOptions().toArray()[0]);
        assertNull(group.getSelected());
        assertFalse(group.isRequired());
    }

    @Test
    public void testOptionGroupMultipleOptions() {
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", false, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        assertEquals(3, group.getOptions().size());
        assertTrue(group.getOptions().containsAll(Arrays.asList(option1, option2, option3)));
        assertNull(group.getSelected());
        assertFalse(group.isRequired());
    }

    @Test
    public void testOptionGroupSingleSelectedOption() {
        Option option = new Option("f", "foo", false, "Foo Option");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        group.setSelected(option);

        assertSame(option, group.getSelected());
        assertEquals(1, group.getOptions().size());
        assertSame(option, group.getOptions().toArray()[0]);
        assertTrue(group.isSelected());
        assertFalse(group.isRequired());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testOptionGroupAlreadySelectedOption() {
        Option option = new Option("f", "foo", false, "Foo Option");
        Option option2 = new Option("bar", "optionB", false, "Option B");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        group.setSelected(option);
        group.setSelected(option2);
    }

    @Test
    public void testOptionGroupSelectedOption() {
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", false, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        group.setSelected(option1);

        assertSame(option1, group.getSelected());
        assertEquals(3, group.getOptions().size());
        assertTrue(group.getOptions().containsAll(Arrays.asList(option1, option2, option3)));
        assertTrue(group.isSelected());
        assertFalse(group.isRequired());
    }

    @Test
    public void testOptionGroupRequiredGroup() {
        Option option1 = new Option("a", "optionA", false, "Option A");
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        group.addOption(option1);

        assertSame(option1, group.getOptions().iterator().next());
        assertEquals(1, group.getOptions().size());
        assertSame(option1, group.getOptions().toArray()[0]);
        assertNull(group.getSelected());
        assertTrue(group.isRequired());
    }

    @Test
    public void testOptionGroupToString() {
        Option option1 = new Option("a", "optionA", false, "Option A");
        Option option2 = new Option("b", "optionB", false, "Option B");
        Option option3 = new Option("c", "optionC", false, "Option C");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        group.setSelected(option1);

        String expected = "[-a Option A, --optionB Option B, --optionC Option C]";
        assertEquals(expected, group.toString());
    }
}