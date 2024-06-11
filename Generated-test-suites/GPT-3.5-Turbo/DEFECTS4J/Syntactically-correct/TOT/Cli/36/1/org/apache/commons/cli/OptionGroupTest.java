import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {

    // Test the addOption() method
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "backup", false, "Backup option");
        Option option3 = new Option("r", "remove", false, "Remove option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        assertEquals(3, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertTrue(optionGroup.getOptions().contains(option3));
    }
    
    // Test the getNames() method
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "backup", false, "Backup option");
        Option option3 = new Option("r", "remove", false, "Remove option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        assertEquals(3, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
        assertTrue(optionGroup.getNames().contains("r"));
    }
    
    // Test the getOptions() method
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "backup", false, "Backup option");
        Option option3 = new Option("r", "remove", false, "Remove option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        assertEquals(3, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertTrue(optionGroup.getOptions().contains(option3));
    }
    
    // Test the setSelected() method
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "backup", false, "Backup option");
        Option option3 = new Option("r", "remove", false, "Remove option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
        
        // Try selecting an option from another group
        Option option4 = new Option("c", "copy", false, "Copy option");
        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.addOption(option4);
        
        try {
            optionGroup2.setSelected(option1);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Exception should be thrown
        }
    }
    
    // Test the isRequired() method
    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
        
        optionGroup.setRequired(false);
        
        assertFalse(optionGroup.isRequired());
    }
    
    // Test the toString() method
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "backup", false, "Backup option");
        Option option3 = new Option("r", "remove", false, "Remove option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        String expected = "[add, backup, remove]";
        assertEquals(expected, optionGroup.toString());
    }
}