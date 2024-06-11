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
        Option option = new Option("a", "add", false, "add option");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("r"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Failed to set selected option");
        }

        try {
            optionGroup.setSelected(option2);
            assertEquals("r", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Failed to set selected option");
        }

        try {
            optionGroup.setSelected(option2);
            fail("Should have thrown AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Exception is expected
        }
    }

    @Test
    public void testSetSelectedNull() {
        Option option1 = new Option("a", "add", false, "add option");
        optionGroup.addOption(option1);

        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Failed to set selected option");
        }

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsSelected() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertFalse(optionGroup.isSelected(option1));
        assertFalse(optionGroup.isSelected(option2));

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            fail("Failed to set selected option");
        }

        assertTrue(optionGroup.isSelected(option1));
        assertFalse(optionGroup.isSelected(option2));
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
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[--add add option, --remove remove option]";
        assertEquals(expected, optionGroup.toString());
    }
}