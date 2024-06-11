import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    // Test case for addOption()
    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "add option");
        OptionGroup resultOptionGroup = optionGroup.addOption(option);

        assertNotNull(resultOptionGroup); // Check if the returned option group is not null
        assertEquals(option, optionGroup.getOptions().iterator().next()); // Check if the added option is present in the option group
    }

    // Test case for getNames()
    @Test
    public void testGetNames() {
        Option option = new Option("a", "add", false, "add option");
        optionGroup.addOption(option);

        assertEquals(1, optionGroup.getNames().size()); // Check if the number of names is correct
        assertTrue(optionGroup.getNames().contains("a")); // Check if the specific name is present
    }

    // Test case for getOptions()
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        Option option3 = new Option("c", "clear", false, "clear option");
        optionGroup.addOption(option1).addOption(option2).addOption(option3);

        assertEquals(3, optionGroup.getOptions().size()); // Check if the number of options is correct
        assertTrue(optionGroup.getOptions().contains(option1)); // Check if the specific option is present
        assertTrue(optionGroup.getOptions().contains(option2)); // Check if the specific option is present
        assertTrue(optionGroup.getOptions().contains(option3)); // Check if the specific option is present
    }

    // Test case for setSelected()
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1).addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected()); // Check if the selected option is correct

        AlreadySelectedException exception = assertThrows(AlreadySelectedException.class, () -> {
            optionGroup.setSelected(option2); // Try selecting an already selected option
        });

        assertEquals("The option 'a' has already been selected", exception.getMessage()); // Check if the correct exception is thrown

        optionGroup.setSelected(null); // Test the case when null is passed

        assertNull(optionGroup.getSelected()); // Check if the selected option is null
    }

    // Test case for getSelected()
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1).addOption(option2);

        assertNull(optionGroup.getSelected()); // Check if the selected option is null initially

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected()); // Check if the selected option is correct

        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected()); // Check if the selected option is null
    }

    // Test case for setRequired()
    @Test
    public void testSetRequired() {
        assertFalse(optionGroup.isRequired()); // Check if the initial value of required is false

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired()); // Check if the required value is set correctly to true
    }

    // Test case for isRequired()
    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired()); // Check if the initial value of required is false

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired()); // Check if the value of required is true
    }

    // Test case for toString()
    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1).addOption(option2);

        assertEquals("[--add add option, --remove remove option]", optionGroup.toString()); // Check if the string representation of the option group is correct
    }
}