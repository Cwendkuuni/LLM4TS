import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        // Test case for addOption method
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "optionA", false, "Option A");
        
        // Call the addOption method
        group.addOption(option);
        
        // Assert that the option has been added to the group
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        // Test case for getNames method
        OptionGroup group = new OptionGroup();
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        
        // Add options to the group
        group.addOption(optionA);
        group.addOption(optionB);
        
        // Call the getNames method
        Collection<String> names = group.getNames();
        
        // Assert that the names collection contains the correct names
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        // Test case for getOptions method
        OptionGroup group = new OptionGroup();
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        
        // Add options to the group
        group.addOption(optionA);
        group.addOption(optionB);
        
        // Call the getOptions method
        Collection<Option> options = group.getOptions();
        
        // Assert that the options collection contains the correct options
        assertTrue(options.contains(optionA));
        assertTrue(options.contains(optionB));
    }

    @Test
    public void testSetSelected() {
        // Test case for setSelected method
        OptionGroup group = new OptionGroup();
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        
        // Add options to the group
        group.addOption(optionA);
        group.addOption(optionB);
        
        // Call the setSelected method with optionA
        group.setSelected(optionA);
        
        // Assert that optionA is selected and optionB is not selected
        assertEquals("a", group.getSelected());
        
        // Call the setSelected method with optionB
        group.setSelected(optionB);
        
        // Assert that optionB is selected and optionA is not selected
        assertEquals("b", group.getSelected());
        
        // Call the setSelected method with null
        group.setSelected(null);
        
        // Assert that no option is selected
        assertNull(group.getSelected());
    }

    @Test
    public void testIsRequired() {
        // Test case for isRequired method
        OptionGroup group = new OptionGroup();
        
        // By default, the group is not required
        assertFalse(group.isRequired());
        
        // Set the group as required
        group.setRequired(true);
        
        // Assert that the group is required
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        // Test case for toString method
        OptionGroup group = new OptionGroup();
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        
        // Add options to the group
        group.addOption(optionA);
        group.addOption(optionB);
        
        // Call the toString method
        String result = group.toString();
        
        // Assert that the result string has the correct format
        assertEquals("[-a Option A, -b Option B]", result);
    }
}