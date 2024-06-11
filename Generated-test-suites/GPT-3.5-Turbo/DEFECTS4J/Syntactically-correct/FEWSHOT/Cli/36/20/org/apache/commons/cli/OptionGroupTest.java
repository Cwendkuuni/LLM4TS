import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
  
    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("a", "first option");
        option2 = new Option("b", "second option");
        option3 = new Option("c", "third option");
    }
  
    @Test
    public void testAddOption() {
        // Add option 1
        optionGroup.addOption(option1);
        
        assertEquals(1, optionGroup.getOptions().size());
        assertEquals(option1, optionGroup.getOptions().iterator().next());
    }

    @Test
    public void testGetNames() {
        // Add options 1, 2, and 3
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        assertEquals(3, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
        assertTrue(optionGroup.getNames().contains("c"));
    }

    @Test
    public void testGetOptions() {
        // Add options 1 and 2
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
  
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        // Add options 1 and 2
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        // Set option 1 as selected
        optionGroup.setSelected(option1);
        
        assertEquals("a", optionGroup.getSelected());
    }
  
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedThrowsAlreadySelectedException() throws AlreadySelectedException {
        // Add options 1 and 2
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        // Set option 1 as selected
        optionGroup.setSelected(option1);
        
        // Try to set option 2 as selected, which should throw an AlreadySelectedException
        optionGroup.setSelected(option2);
    }
  
    @Test
    public void testSetSelectedWithNullOptionResetsSelected() throws AlreadySelectedException {
        // Add options 1 and 2
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        // Set option 1 as selected
        optionGroup.setSelected(option1);
        
        // Set null option
        optionGroup.setSelected(null);
        
        assertNull(optionGroup.getSelected());
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
        // Add options 1, 2, and 3
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        assertEquals("[a first option, b second option, c third option]", optionGroup.toString());
    }
}