import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        try {
            optionGroup.setSelected(option1);
            assertEquals(option1.getOpt(), optionGroup.getSelected());
            optionGroup.setSelected(option2);
            assertEquals(option2.getOpt(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Failed to select option: " + e.getMessage());
        }
    }

    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        optionGroup.addOption(option1);
        try {
            optionGroup.setSelected(option1);
            assertEquals(option1.getOpt(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Failed to select option: " + e.getMessage());
        }
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
        Option option1 = new Option("a", "option1", false, "Option 1");
        optionGroup.addOption(option1);
        assertEquals("[ -a Option 1 ]", optionGroup.toString());
    }
}