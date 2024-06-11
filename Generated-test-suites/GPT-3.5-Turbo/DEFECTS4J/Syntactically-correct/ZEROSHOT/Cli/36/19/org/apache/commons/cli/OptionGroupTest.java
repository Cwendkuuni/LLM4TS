import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "option1", false, "Option 1");
        option2 = new Option("b", "option2", false, "Option 2");
        option3 = new Option("c", "option3", false, "Option 3");
    }

    @Test
    public void testAddOption() {
        OptionGroup result = optionGroup.addOption(option1);
        
        assertEquals(optionGroup, result);        
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getNames().contains("a"));        
    }

    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertTrue(optionGroup.getNames().contains("a"));  
        assertTrue(optionGroup.getNames().contains("b"));  
        assertFalse(optionGroup.getNames().contains("c"));  
    }

    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertTrue(optionGroup.getOptions().contains(option1));  
        assertTrue(optionGroup.getOptions().contains(option2));  
        assertFalse(optionGroup.getOptions().contains(option3));  
    }

    @Test
    public void testSetSelected() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            fail("Caught an exception when no option was selected");
        }
        
        assertEquals(option1.getKey(), optionGroup.getSelected()); 
        
        try {
            optionGroup.setSelected(option2);
            fail("Failed to catch AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }

        assertEquals(option1.getKey(), optionGroup.getSelected());
        
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() {
        assertNull(optionGroup.getSelected());
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        
        assertEquals(option1.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testIsRequired() {
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        assertEquals("[a, b, c]", optionGroup.toString());
    }
}