package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

import java.util.Collection;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection options = optionGroup.getOptions();

        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection names = optionGroup.getNames();

        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection options = optionGroup.getOptions();

        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertEquals(null, optionGroup.getSelected());

        boolean exceptionThrown = false;
        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            exceptionThrown = true;
        }
        
        assertTrue(exceptionThrown);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "option1");
        option1.setDescription("Description of option1");

        Option option2 = new Option("b", "option2");
        option2.setDescription("Description of option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-a Description of option1, -b Description of option2]";
        assertEquals(expected, optionGroup.toString());
    }
}
