package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        Option option1 = new Option("o", "opt1", false, "desc1");
        Option option2 = new Option("o", "opt2", false, "desc2");
        Option option3 = new Option("l", "longopt", false, "desc3");
        Option option4 = new Option("l", "longopt2", false, "desc4");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        optionGroup.addOption(option4);
    }

    @Test
    public void testGetNames() {
        Collection names = optionGroup.getNames();
        assertEquals(4, names.size());
        assertTrue(names.containsAll(Collections.singletonList("o")));
        assertTrue(names.containsAll(Collections.singletonList("l")));
    }

    @Test
    public void testGetOptions() {
        Collection options = optionGroup.getOptions();
        assertEquals(4, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
        assertTrue(options.contains(option4));
    }

    @Test
    public void testSetAndGetSelected() {
        Option option1 = new Option("o", "opt1", false, "desc1");
        Option option2 = new Option("o", "opt2", false, "desc2");

        optionGroup.setSelected(option1);
        assertEquals("opt1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("opt2", optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        Option option1 = new Option("o", "opt1", false, "desc1");
        Option option2 = new Option("o", "opt2", false, "desc2");

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        assertEquals("[--desc1 -opt1, --desc2 -o, --desc3 -l, --desc4 -l]", optionGroup.toString());
    }
}