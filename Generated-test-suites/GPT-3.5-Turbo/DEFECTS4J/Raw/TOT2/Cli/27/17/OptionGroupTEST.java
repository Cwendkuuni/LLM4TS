import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        try {
            group.setSelected(option1);
            assertEquals("a", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw exception");
        }

        try {
            group.setSelected(option2);
            assertEquals("b", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw exception");
        }

        try {
            group.setSelected(option1);
            fail("Should throw AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // success
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
        Option option1 = new Option("a", "test option 1");
        Option option2 = new Option("b", "test option 2");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        assertEquals("[-a test option 1, -b test option 2]", group.toString());
    }

}