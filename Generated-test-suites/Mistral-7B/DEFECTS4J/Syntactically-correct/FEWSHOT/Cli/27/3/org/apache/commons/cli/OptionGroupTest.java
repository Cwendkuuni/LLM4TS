package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "foo", false, "foovalue");
        Option option2 = new Option("b", "bar", false, "barvalue");

        group.addOption(option);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals("foo", group.getOptions().toArray()[0].getLongOpt());
        assertEquals("bar", group.getOptions().toArray()[1].getLongOpt());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "foovalue");
        Option option2 = new Option("b", "bar", false, "barvalue");
        Option option3 = new Option("o", "opt", false, "optvalue");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        Iterator<Option> names = group.getNames().iterator();
        assertEquals("f", names.next().getKey());
        assertEquals("b", names.next().getKey());
        assertEquals("o", names.next().getKey());
        assertFalse(names.hasNext());
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "foovalue");
        Option option2 = new Option("b", "bar", false, "barvalue");

        group.addOption(option1);
        group.addOption(option2);

        assertNull(group.getSelected());

        group.setSelected(option1);
        assertEquals("f", group.getSelected());

        group.setSelected(option2);
        assertEquals("b", group.getSelected());

        group.setSelected(option1);
        assertEquals("f", group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testAlreadySelectedException() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "foovalue");
        Option option2 = new Option("b", "bar", false, "barvalue");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        OptionGroup requiredGroup = new OptionGroup();
        requiredGroup.setRequired(true);

        assertFalse(group.isRequired());
        assertTrue(requiredGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "foovalue");
        Option option2 = new Option("b", "bar", false, "barvalue");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[-f foo, --foo foo, -b bar, --bar bar]";
        assertEquals(expected, group.toString());
    }
}