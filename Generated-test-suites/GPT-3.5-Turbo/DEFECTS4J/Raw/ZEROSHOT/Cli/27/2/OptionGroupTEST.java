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
        Option option = new Option("t", "test", false, "Test Option");
        OptionGroup result = optionGroup.addOption(option);
        assertEquals(optionGroup, result);
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("t", "test", false, "Test Option 1");
        Option option2 = new Option("u", "unit", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getNames().contains("t"));
        assertTrue(optionGroup.getNames().contains("u"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("t", "test", false, "Test Option 1");
        Option option2 = new Option("u", "unit", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("t", "test", false, "Test Option 1");
        Option option2 = new Option("u", "unit", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        try {
            optionGroup.setSelected(option1);
            assertEquals("t", optionGroup.getSelected());
            optionGroup.setSelected(option2);
            assertEquals("u", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("t", "test", false, "Test Option 1");
        Option option2 = new Option("u", "unit", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("t", "test", false, "Test Option 1");
        Option option2 = new Option("u", "unit", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        try {
            optionGroup.setSelected(option1);
            assertEquals("t", optionGroup.getSelected());
            optionGroup.setSelected(option2);
            assertEquals("u", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("t", "test", false, "Test Option 1");
        Option option2 = new Option("u", "unit", false, "Test Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[--test Test Option 1, --unit Test Option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}