package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-help");
        Option option2 = new Option("o2", "option2", false, "o2-help");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals("o1-help", option1.getLongOpt());
        assertEquals("o2-help", option2.getLongOpt());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-help");
        Option option2 = new Option("o2", "option2", false, "o2-help");

        group.addOption(option1);
        group.addOption(option2);

        List<String> names = Arrays.asList(option1.getKey(), option2.getKey());
        assertEquals(names, group.getNames());
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-help");
        Option option2 = new Option("o2", "option2", false, "o2-help");

        group.addOption(option1);
        group.addOption(option2);

        List<Option> options = Collections.unmodifiableList(group.getOptions());
        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-help");
        Option option2 = new Option("o2", "option2", false, "o2-help");

        group.addOption(option1);
        group.addOption(option2);

        assertNull(group.getSelected());

        group.setSelected(option1);
        assertEquals(option1.getKey(), group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option1));

        group.setSelected(option2);
        assertEquals(option2.getKey(), group.getSelected());
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        assertFalse(group.isRequired());

        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "o1-help");
        Option option2 = new Option("o2", "option2", false, "o2-help");

        group.addOption(option1);
        group.addOption(option2);

        OptionGroup requiredGroup = new OptionGroup();
        requiredGroup.setRequired(true);

        Option option3 = new Option("o3", "option3", false, "o3-help");

        requiredGroup.addOption(option3);

        String expected = "[-o1, --o1-help, -o2, --o2-help, --o3-help]";
        assertEquals(expected, group.toString());

        expected = "[--o3-help]";
        assertEquals(expected, requiredGroup.toString());
    }
}