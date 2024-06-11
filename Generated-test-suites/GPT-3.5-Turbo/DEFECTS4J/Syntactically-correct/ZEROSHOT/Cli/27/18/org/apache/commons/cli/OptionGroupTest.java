import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "test option");
        optionGroup.addOption(option);
        assertTrue(optionGroup.getOptions().contains(option));
        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        Option option3 = new Option("c", "option3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
        assertTrue(optionGroup.getNames().contains("c"));
        assertEquals(3, optionGroup.getNames().size());
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        Option option3 = new Option("c", "option3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertTrue(optionGroup.getOptions().contains(option3));
        assertEquals(3, optionGroup.getOptions().size());
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description 1");
        Option option2 = new Option("b", "option2", false, "description 2");
        Option option3 = new Option("c", "option3", false, "description 3");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        assertEquals("[ -a description 1,  -b description 2,  -c description 3]", optionGroup.toString());
    }
}