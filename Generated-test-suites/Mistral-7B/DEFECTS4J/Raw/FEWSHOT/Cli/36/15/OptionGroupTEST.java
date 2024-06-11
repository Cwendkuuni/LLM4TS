package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "opt1-desc");
        Option option2 = new Option("opt2", "opt2-desc");
        Option option3 = new Option("opt3", "opt3-desc");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        assertEquals(3, group.getOptions().size());
        assertEquals(option1, group.getOptions().iterator().next());
        assertEquals(option2, group.getOptions().iterator().next());
        assertEquals(option3, group.getOptions().iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "opt1-desc");
        Option option2 = new Option("opt2", "opt2-desc");
        Option option3 = new Option("opt3", "opt3-desc");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        assertEquals(3, group.getNames().size());
        assertEquals("opt1", group.getNames().iterator().next());
        assertEquals("opt2", group.getNames().iterator().next());
        assertEquals("opt3", group.getNames().iterator().next());
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "opt1-desc");
        Option option2 = new Option("opt2", "opt2-desc");

        try {
            group.setSelected(option1);
            assertEquals("opt1", group.getSelected());

            try {
                group.setSelected(option2);
                fail("AlreadySelectedException expected");
            } catch (AlreadySelectedException e) {
                // expected
            }

            assertNull(group.getSelected());
            group.setSelected(option1);
            assertEquals("opt1", group.getSelected());
        } catch (AlreadySelectedException e) {
            // expected - no option was selected before
        }
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        OptionGroup requiredGroup = new OptionGroup();
        Option option1 = new Option("opt1", "opt1-desc");

        group.setRequired(false);
        requiredGroup.setRequired(true);

        assertFalse(group.isRequired());
        assertTrue(requiredGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "opt1-desc");
        Option option2 = new Option("opt2", "opt2-desc");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals("[--opt1 opt1-desc, --opt2 opt2-desc]", group.toString());
    }
}