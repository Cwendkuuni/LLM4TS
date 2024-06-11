package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        OptionGroup expectedGroup = new OptionGroup();
        expectedGroup.addOption(option1);
        expectedGroup.addOption(option2);

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(expectedGroup, group);
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);
        Option option3 = new Option("o3", "Option 3", false);

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        List<String> names = Arrays.asList(option1.getKey(), option2.getKey(), option3.getKey());
        assertEquals(names, group.getNames());
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);
        Option option3 = new Option("o3", "Option 3", false);

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        List<Option> options = Arrays.asList(option1, option2, option3);
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);

        group.setSelected(option1);

        assertNull(group.getSelected());

        group.setSelected(null);

        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedSame() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);

        group.setSelected(option1);

        assertEquals(option1, group.getSelected());

        group.setSelected(option1);

        assertEquals(option1, group.getSelected());
    }

    @Test
    public void testSetSelectedDifferent() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        group.setSelected(option1);

        assertEquals(option1, group.getSelected());

        try {
            group.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected
        }
    }

    @Test
    public void testGetSelectedNull() {
        OptionGroup group = new OptionGroup();

        assertNull(group.getSelected());
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        group.setSelected(option1);

        assertEquals(option1, group.getSelected());
    }

    @Test
    public void testSetRequiredFalse() {
        OptionGroup group = new OptionGroup();

        group.setRequired(false);

        assertFalse(group.isRequired());
    }

    @Test
    public void testSetRequiredTrue() {
        OptionGroup group = new OptionGroup();

        group.setRequired(true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        group.addOption(option1);
        group.addOption(option2);

        String expectedString = "[-o1 Option 1, --o2 Option 2 ]";

        assertEquals(expectedString, group.toString());
    }

    @Test
    public void testToStringEmpty() {
        OptionGroup group = new OptionGroup();

        String expectedString = "[ ]";

        assertEquals(expectedString, group.toString());
    }

    @Test
    public void testToStringWithSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);

        group.setSelected(option1);

        String expectedString = "[--o1 Option 1 ]";

        assertEquals(expectedString, group.toString());
    }

    @Test
    public void testToStringWithSelectedAndOtherOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);

        String expectedString = "[--o1 Option 1, --o2 Option 2 ]";

        assertEquals(expectedString, group.toString());
    }
}