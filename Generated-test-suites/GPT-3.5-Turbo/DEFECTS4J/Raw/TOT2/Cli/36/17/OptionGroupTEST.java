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
        Option option1 = new Option("a", "apple", false, "Description for option 1");
        Option option2 = new Option("b", "banana", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "apple", false, "Description for option 1");
        Option option2 = new Option("b", "banana", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "apple", false, "Description for option 1");
        Option option2 = new Option("b", "banana", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option2));
    }

    @Test
    public void testSetRequired() {
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "Description for option 1");
        Option option2 = new Option("b", "banana", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedToString = "[ -a apple Description for option 1, -b banana Description for option 2]";
        assertEquals(expectedToString, optionGroup.toString());
    }
}