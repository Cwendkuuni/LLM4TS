package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        OptionGroup optGroup = new OptionGroup();

        optGroup.addOption(new Option("a", "group-a", true, "Option A in group"));
        optGroup.addOption(new Option("b", "group-b", true, "Option B in group"));
        optGroup.addOption(new Option("c", "group-c", true, "Option C in group"));

        this.optionGroup = optGroup;
    }

    @Test
    public void testAddOption() {
        assertEquals(3, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        List<String> names = Arrays.asList("a", "b", "c");
        assertEquals(names, optionGroup.getNames());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = Arrays.asList(optionGroup.getOptions().iterator().next(),
                optionGroup.getOptions().iterator().next(),
                optionGroup.getOptions().iterator().next());
        assertEquals(options, optionGroup.getOptions());
    }

    @Test
    public void testSetSelectedNull() {
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSame() {
        optionGroup.setSelected(optionGroup.getOptions().iterator().next());
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(optionGroup.getOptions().iterator().next());
        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferent() {
        optionGroup.setSelected(optionGroup.getOptions().iterator().next());
        assertEquals("a", optionGroup.getSelected());

        Option optionB = optionGroup.getOptions().iterator().next();
        optionGroup.setSelected(optionB);
    }

    @Test
    public void testSetSelectedDifferentWithSameKey() {
        Option optionA = optionGroup.getOptions().iterator().next();
        Option optionB = new Option("a", null, false, "Option A with same key in group");
        optionGroup.addOption(optionB);

        optionGroup.setSelected(optionB);
        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedWithNullOption() {
        optionGroup.setSelected(null);
    }

    @Test
    public void testIsRequired() {
        optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());

        optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        StringBuilder expected = new StringBuilder("[--group, Option A in group -g, Option A in group --group-a, --group, Option B in group -b, Option B in group --group-b, --group, Option C in group -c, Option C in group --group-c, ]");
        assertEquals(expected.toString(), optionGroup.toString());
    }

    @Test
    public void testToStringEmpty() {
        OptionGroup optGroup = new OptionGroup();
        StringBuilder expected = new StringBuilder("[[]]");
        assertEquals(expected.toString(), optGroup.toString());
    }
}