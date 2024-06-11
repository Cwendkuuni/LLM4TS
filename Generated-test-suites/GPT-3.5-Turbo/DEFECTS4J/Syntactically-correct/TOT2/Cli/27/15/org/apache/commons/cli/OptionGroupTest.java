import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "add", false, "Option A");
        Option option2 = new Option("b", "delete", false, "Option B");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);

        assertTrue(group.getOptions().contains(option1));
        assertFalse(group.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "Option A");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);

        assertTrue(group.getNames().contains("a"));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "add", false, "Option A");
        Option option2 = new Option("b", "delete", false, "Option B");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("a", group.getSelected());

        try {
            group.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // pass
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
        Option option1 = new Option("a", "add", false, "Option A");
        Option option2 = new Option("b", "delete", false, "Option B");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[ -a Option A, -b Option B ]";
        assertEquals(expected, group.toString());
    }
}