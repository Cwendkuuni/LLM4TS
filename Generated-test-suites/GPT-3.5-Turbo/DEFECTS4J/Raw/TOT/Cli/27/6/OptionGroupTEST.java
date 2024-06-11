import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2); // This should throw AlreadySelectedException
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option2);

        assertEquals("b", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[-a Option 1, -b Option 2]", optionGroup.toString());
    }
}