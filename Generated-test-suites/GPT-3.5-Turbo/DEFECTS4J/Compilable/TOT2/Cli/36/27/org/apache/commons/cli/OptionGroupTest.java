package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;
import java.util.Collection;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        group.addOption(option1);
        assertTrue(group.getOptions().contains(option1));

        group.addOption(option2);
        assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        group.addOption(option1);
        group.addOption(option2);

        Collection<String> names = group.getNames();
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
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

        group.setSelected(option2);
        assertEquals("b", group.getSelected());

        try {
            group.setSelected(option1); // should throw exception
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected
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
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[-a option1, -b option2]";
        assertEquals(expected, group.toString());
    }
}