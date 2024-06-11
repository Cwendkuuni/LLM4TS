package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");

        group.addOption(option);

        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("n", "name", false, "Name option");
        group.addOption(option);

        assertTrue(group.getNames().contains(option.getKey()));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        try {
            group.setSelected(option1);
            assertEquals(option1.getKey(), group.getSelected());

            group.setSelected(null);
            assertNull(group.getSelected());

            group.setSelected(option2); // selecting different option should throw AlreadySelectedException
            fail("Expected AlreadySelectedException was not thrown");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();

        group.setRequired(true);
        assertTrue(group.isRequired());

        group.setRequired(false);
        assertFalse(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t1", "test1", false, "Test 1");
        Option option2 = new Option("t2", "test2", false, "Test 2");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[-t1 Test 1, -t2 Test 2]";
        assertEquals(expected, group.toString());
    }
}