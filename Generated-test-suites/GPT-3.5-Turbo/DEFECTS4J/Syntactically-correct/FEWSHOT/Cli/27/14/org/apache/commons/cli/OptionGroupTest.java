
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collection;
import java.util.Iterator;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        assertNull(optionGroup.getSelected());

        optionGroup.addOption(option1);
        assertEquals(1, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));

        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option2));
        
        Collection<String> names = optionGroup.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains(option1.getOpt()));
        assertTrue(names.contains(option2.getOpt()));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        
        assertNull(optionGroup.getSelected());

        try {
            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals(option1.getOpt(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option2);
            assertEquals(option2.getOpt(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option1);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            assertNotNull(e.getOptionGroup());
            assertNotNull(e.getOption());
            assertEquals(optionGroup, e.getOptionGroup());
            assertEquals(option1, e.getOption());
        }
    }

    @Test
    public void testIsSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        option1.setDescription("Add option description");
        Option option2 = new Option("r", "remove", false, "Remove option");
        option2.setDescription("Remove option description");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[[-a Add option description, --add Add option description], [-r Remove option description, --remove Remove option description]]";
        assertEquals(expected, optionGroup.toString());
    }
}
