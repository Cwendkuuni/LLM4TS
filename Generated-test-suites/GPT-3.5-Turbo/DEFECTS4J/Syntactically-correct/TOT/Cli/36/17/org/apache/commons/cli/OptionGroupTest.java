
import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        Option option3 = new Option("c", "option3", false, "Option 3");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        assertEquals(3, optionGroup.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getNames().size());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getOptions().size());
    }
    
    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        
        optionGroup.addOption(option1);
        
        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
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

