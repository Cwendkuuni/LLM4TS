import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getNames().size());
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-a option a, -b option b]";
        assertEquals(expected, optionGroup.toString());
    }
}