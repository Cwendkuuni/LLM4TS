import static org.junit.Assert.*;

import org.junit.Test;
import org.apache.commons.cli.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        group.addOption(option1);

        assertTrue(group.getOptions().contains(option1));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        group.addOption(option1);

        assertTrue(group.getNames().contains("a"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        group.addOption(option1);

        assertTrue(group.getOptions().contains(option1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        
        try {
            group.setSelected(option1);
            assertEquals("a", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }

    @Test
    public void testSetSelectedException() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");

        group.setSelected(option1);

        try {
            group.setSelected(option2);
            fail("Failed to throw AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
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
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[ -a apple, -b banana]";
        assertEquals(expected, group.toString());
    }
}