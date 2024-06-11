import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "option a");
        OptionGroup result = optionGroup.addOption(option);
        assertEquals(optionGroup, result);
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[ -a option a, -b option b]";
        assertEquals(expected, optionGroup.toString());
    }
}