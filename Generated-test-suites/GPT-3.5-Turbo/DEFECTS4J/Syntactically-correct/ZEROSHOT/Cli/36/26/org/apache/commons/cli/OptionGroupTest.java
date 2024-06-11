import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        Collection<Option> options = group.getOptions();

        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        Collection<String> names = group.getNames();

        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        Collection<Option> options = group.getOptions();

        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertEquals(option1.getKey(), group.getSelected());

        group.setSelected(option2);

        assertEquals(option2.getKey(), group.getSelected());

        try {
            group.setSelected(option1);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected
        }

        group.setSelected(null);

        assertNull(group.getSelected());
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
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[-a Option 1, -b Option 2]";

        assertEquals(expected, group.toString());
    }
}