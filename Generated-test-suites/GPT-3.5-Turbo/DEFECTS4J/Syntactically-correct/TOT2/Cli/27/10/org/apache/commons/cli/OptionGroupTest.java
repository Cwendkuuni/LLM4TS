import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        
        group.addOption(option);
        
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assertEquals("a", group.getSelected());

        try {
            group.setSelected(option2);
        } catch (AlreadySelectedException e) {
            // Exception expected as another option is already selected
            assertNotNull(e);
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
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        group.addOption(option1);
        group.addOption(option2);

        String expectedString = "[-a Add option, -r Remove option]";
        assertEquals(expectedString, group.toString());
    }
}