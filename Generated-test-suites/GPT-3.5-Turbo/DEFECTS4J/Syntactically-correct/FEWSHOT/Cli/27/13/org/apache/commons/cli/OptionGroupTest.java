
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");

        OptionGroup result = optionGroup.addOption(option1);
        assertEquals(optionGroup, result);
        assertEquals(1, optionGroup.getOptions().size());
        assertEquals(option1, optionGroup.getOptions().iterator().next());

        result = optionGroup.addOption(option2);
        assertEquals(optionGroup, result);
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("r"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("r", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_ThrowsAlreadySelectedException() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelected_NullOption() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        Option option3 = new Option("l", "list", false, "list option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expected = "[a add, r remove, l list]";
        assertEquals(expected, optionGroup.toString());
    }
}
