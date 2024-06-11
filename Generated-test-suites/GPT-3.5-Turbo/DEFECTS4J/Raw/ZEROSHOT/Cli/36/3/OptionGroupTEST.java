import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("test", "Test option");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getOptions().size());
        assertEquals(option, optionGroup.getOptions().iterator().next());
    }
    
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("test1", "Test option 1");
        Option option2 = new Option("test2", "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("test1"));
        assertTrue(optionGroup.getNames().contains("test2"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("test1", "Test option 1");
        Option option2 = new Option("test2", "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("test1", "Test option 1");
        Option option2 = new Option("test2", "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());
        
        try {
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("test1", "Test option 1");
        Option option2 = new Option("test2", "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());
        
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("t", "Test option 1");
        Option option2 = new Option("test2", "Test option 2");
        option1.setDescription("Description 1");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[-t Test option 1, --test2 Test option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}