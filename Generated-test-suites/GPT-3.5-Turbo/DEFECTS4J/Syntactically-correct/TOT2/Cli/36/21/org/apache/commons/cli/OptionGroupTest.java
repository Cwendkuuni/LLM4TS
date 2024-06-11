import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "Test option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1", false, "Test option 1");
        Option option2 = new Option("b", "option2", false, "Test option 2");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("c", "option3", false, "Test option 3");
        Option option2 = new Option("d", "option4", false, "Test option 4");
        
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("c", optionGroup.getSelected());
        
        Exception exception = assertThrows(AlreadySelectedException.class, () -> {
            optionGroup.setSelected(option2);
        });
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option = new Option("e", "option5", false, "Test option 5");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option);

        String expected = "[--option5 Test option 5]";
        assertEquals(expected, optionGroup.toString());
    }
}