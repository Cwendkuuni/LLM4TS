
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("-a", "description1");
        Option option2 = new Option("-b", "description2");

        optionGroup.addOption(option1);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertFalse(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("-a", "description1");
        Option option2 = new Option("-b", "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getNames().contains("-a"));
        assertTrue(optionGroup.getNames().contains("-b"));
        assertFalse(optionGroup.getNames().contains("-c"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("-a", "description1");
        Option option2 = new Option("-b", "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("-a", "description1");
        Option option2 = new Option("-b", "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("-a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("-b", optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelectedException() {
        Option option1 = new Option("-a", "description1");
        Option option2 = new Option("-b", "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsSelected() {
        Option option1 = new Option("-a", "description1");
        Option option2 = new Option("-b", "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertTrue(optionGroup.isSelected(option1));

        optionGroup.setSelected(option2);
        assertTrue(optionGroup.isSelected(option2));

        assertFalse(optionGroup.isSelected(null));
        assertFalse(optionGroup.isSelected(new Option("-c", "description3")));
    }

    @Test
    public void testSetRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("-a", "description1");
        Option option2 = new Option("-b", "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[-a description1, -b description2]", optionGroup.toString());
    }
}
