import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "apple", false, "apple option");
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testAlreadySelectedException() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[ -a apple, -b banana]";
        
        assertEquals(expected, optionGroup.toString());
    }

}