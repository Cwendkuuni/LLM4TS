import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("test1", "test option 1");
        Option option2 = new Option("test2", "test option 2");
        
        assertEquals(0, group.getOptions().size());
        
        group.addOption(option1);
        assertEquals(1, group.getOptions().size());
        
        group.addOption(option2);
        assertEquals(2, group.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("test1", "test option 1");
        Option option2 = new Option("test2", "test option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("test1"));
        assertTrue(group.getNames().contains("test2"));
    }
    
    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("test1", "test option 1");
        Option option2 = new Option("test2", "test option 2");
        
        group.addOption(option1);
        group.addOption(option2);
        
        // Select an option
        group.setSelected(option1);
        assertEquals("test1", group.getSelected());
        
        // Reselecting the same option should not cause an exception
        group.setSelected(option1);
        
        // Selecting a different option should throw an exception
        Exception exception = assertThrows(AlreadySelectedException.class, () -> {
            group.setSelected(option2);
        });
        assertEquals("Option 'test2' can't be selected because option 'test1' has already been selected in this group", exception.getMessage());
    }
    
    // Add more test cases for other public methods
    
}