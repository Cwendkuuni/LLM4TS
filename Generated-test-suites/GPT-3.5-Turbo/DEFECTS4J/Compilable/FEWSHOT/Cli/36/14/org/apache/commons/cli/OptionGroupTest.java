package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class OptionGroupTest {
    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        assertEquals(2, options.size());
        assertEquals(option1, options.stream().filter(o -> o.getKey().equals("a")).findFirst().orElse(null));
        assertEquals(option2, options.stream().filter(o -> o.getKey().equals("b")).findFirst().orElse(null));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        assertEquals(2, names.size());
        assertEquals("a", names.stream().filter(n -> n.equals("a")).findFirst().orElse(null));
        assertEquals("b", names.stream().filter(n -> n.equals("b")).findFirst().orElse(null));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);

        assertEquals("b", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedNull() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertEquals(true, optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertEquals(false, optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-a option1, -b option2]";

        assertEquals(expected, optionGroup.toString());
    }
}