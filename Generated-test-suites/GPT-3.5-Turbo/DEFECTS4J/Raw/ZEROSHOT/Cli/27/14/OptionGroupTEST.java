import org.junit.Test;
import java.util.Collection;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        assertTrue(optionGroup.getNames().isEmpty());

        Option option = new Option("a", "option a");
        optionGroup.addOption(option);

        Collection<String> names = optionGroup.getNames();
        assertEquals(1, names.size());
        assertTrue(names.contains("a"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        assertTrue(optionGroup.getOptions().isEmpty());

        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());

        // Selecting a different option in a required group should throw AlreadySelectedException
        Option option3 = new Option("c", "option c");
        optionGroup.addOption(option3);

        try {
            optionGroup.setSelected(option3);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Pass
        }
    }

    @Test
    public void testSetRequired() {
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

        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[-a option a, -b option b]", optionGroup.toString());
    }
}