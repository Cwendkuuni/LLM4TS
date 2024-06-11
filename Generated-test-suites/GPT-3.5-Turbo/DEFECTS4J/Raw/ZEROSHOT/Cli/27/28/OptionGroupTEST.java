import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        assertEquals(optionGroup.addOption(option), optionGroup);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection options = optionGroup.getNames();
        assertTrue(options.contains("a"));
        assertTrue(options.contains("r"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals(optionGroup.getSelected(), "a");
        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option2));
    }

    @Test
    public void testSetSelectedWithNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        assertEquals(optionGroup.getSelected(), "a");
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        assertFalse(optionGroup.isSelected());
        optionGroup.setSelected(option);
        assertTrue(optionGroup.isSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(optionGroup.toString(), "[--add Add option, --remove Remove option]");
    }
}