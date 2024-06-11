package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getNames().size());
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertEquals("a", group.getSelected());
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertEquals("a", group.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();

        group.setRequired(true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();

        group.setRequired(true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        group.addOption(option1);
        group.addOption(option2);

        assertEquals("[--option1 option1, --option2 option2]", group.toString());
    }
}