package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1", false, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        Option option3 = new Option("opt3", "option 3", false, "o3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        this.optionGroup = optionGroup;
    }

    @Test
    public void testGetNames() {
        Set<String> names = new HashSet<String>(Arrays.asList("opt1", "opt2", "opt3"));
        assertEquals(names, optionGroup.getNames());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = Collections.unmodifiableList(Arrays.asList(option1, option2, option3));
        assertEquals(options, optionGroup.getOptions());
    }

    @Test
    public void testSetSelectedNull() {
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSame() {
        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedDifferent() {
        optionGroup.setSelected(option1);
        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option2));
        assertSame(option1, optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedDifferentThenSame() {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
        assertEquals(option2, optionGroup.getSelected());
    }

    @Test
    public void testGetSelectedNull() {
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testGetSelectedSet() {
        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelected());
    }

    @Test
    public void testIsRequiredFalseByDefault() {
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequiredSetTrue() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testIsRequiredSetFalse() {
        optionGroup.setRequired(true);
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        assertEquals("[ -o1 option 1, -o2 option 2, -o3 option 3 ]", optionGroup.toString());
    }
}