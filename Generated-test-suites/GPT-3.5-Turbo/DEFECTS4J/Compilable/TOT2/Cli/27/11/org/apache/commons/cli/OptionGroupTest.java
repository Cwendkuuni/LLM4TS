package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "test", false, "test option");
        group.addOption(option);
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("b", "sample", false, "sample option");
        group.addOption(option);
        assertTrue(group.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("c", "example", false, "example option");
        group.addOption(option);
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("d", "demo", false, "demo option");

        try {
            group.setSelected(option);
            assertEquals("d", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedFail() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("e", "error", false, "error option1");
        Option option2 = new Option("f", "fail", false, "fail option2");

        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("x", "description", false, "description option1");
        Option option2 = new Option("y", "stringify", false, "stringify option2");
        group.addOption(option1);
        group.addOption(option2);

        assertEquals("[x description, y stringify]", group.toString());
    }
}