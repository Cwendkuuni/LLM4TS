package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testOptionGroup() {
        OptionGroup group = new OptionGroup();
        assertEquals(0, group.getOptions().size());
        assertEquals(Collections.emptySet(), group.getNames());
        assertNull(group.getSelected());
        assertFalse(group.isRequired());
    }

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(new HashSet<>(Arrays.asList("o1", "o2")), group.getNames());

        Option opt = null;
        try {
            group.setSelected(opt);
        } catch (AlreadySelectedException e) {
            opt = e.getOption();
        }
        assertNull(opt);
        assertNull(group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("o1", group.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        String expected = "[-o1 option 1, --o2 option 2 ]";
        assertEquals(expected, group.toString());
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        group.addOption(option1);
        group.addOption(option2);

        List<Option> options = new ArrayList<>(group.getOptions());
        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option 1");
        Option option2 = new Option("o2", "option 2");
        group.addOption(option1);
        group.addOption(option2);

        Set<String> names = new HashSet<>(group.getNames());
        assertEquals(2, names.size());
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
    }
}