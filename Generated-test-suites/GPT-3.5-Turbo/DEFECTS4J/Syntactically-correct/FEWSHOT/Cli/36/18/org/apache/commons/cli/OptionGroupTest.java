package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("name1", "description1");
        Option option2 = new Option("name2", "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();

        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("name1", "description1");
        Option option2 = new Option("name2", "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();

        assertTrue(names.contains("name1"));
        assertTrue(names.contains("name2"));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("name1", "description1");
        Option option2 = new Option("name2", "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("name1", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        Option option1 = new Option("name1", "description1");
        Option option2 = new Option("name2", "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedNull() {
        Option option1 = new Option("name1", "description1");

        optionGroup.addOption(option1);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("name1", "description1");
        option1.setOpt("n");

        Option option2 = new Option("name2", "description2");
        option2.setLongOpt("name");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String result = optionGroup.toString();

        assertEquals("[ -n, --name1 description1, --name2 description2]", result);
    }
}