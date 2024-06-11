import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    // Test case for addOption method
    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "apple", false, "description");
        Option option2 = new Option("b", "banana", false, "description");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    // Test case for getNames method
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "description");
        optionGroup.addOption(option1);

        assertTrue(optionGroup.getNames().contains("a"));
    }

    // Test case for setSelected method with valid option
    @Test
    public void testSetSelectedValidOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "apple", false, "description");

        optionGroup.setSelected(option);

        assertEquals("a", optionGroup.getSelected());
    }

    // Test case for setSelected method with invalid option
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedInvalidOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "description");
        Option option2 = new Option("b", "banana", false, "description");

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    // Test case for setRequired method
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    // Test case for toString method
    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "description");
        Option option2 = new Option("b", "banana", false, "description");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[ -a apple, -b banana ]";
        assertEquals(expected, optionGroup.toString());
    }
}