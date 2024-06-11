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
        // Test case 1
        Option option1 = new Option("a", "add", false, "add option");
        optionGroup.addOption(option1);
        assertEquals(1, optionGroup.getOptions().size());

        // Test case 2
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        // Test case 1
        Option option1 = new Option("a", "add", false, "add option");
        optionGroup.addOption(option1);
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getNames().size());
    }

    @Test
    public void testGetOptions() {
        // Test case 1
        Option option1 = new Option("a", "add", false, "add option");
        optionGroup.addOption(option1);
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testSetSelected() {
        // Test case 1
        Option option1 = new Option("a", "add", false, "add option");
        optionGroup.addOption(option1);
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        // Test case 2
        optionGroup.setSelected(null);
        assertEquals(null, optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() {
        // Test case 1
        Option option1 = new Option("a", "add", false, "add option");
        optionGroup.addOption(option1);
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        // Test case 2
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        // Test case 1
        Option option1 = new Option("a", "add", false, "add option");
        optionGroup.addOption(option1);
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        // Test case 2
        optionGroup.setSelected(null);
        assertEquals(null, optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        // Test case 1
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        // Test case 2
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        // Test case 1
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        // Test case 2
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        // Test case 1
        Option option1 = new Option("a", "add", false, "add option");
        optionGroup.addOption(option1);
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option2);
        String expectedOutput = "[-a add option, -r remove option]";
        assertEquals(expectedOutput, optionGroup.toString());

        // Test case 2
        optionGroup = new OptionGroup();
        String expectedOutput2 = "[]";
        assertEquals(expectedOutput2, optionGroup.toString());
    }
}