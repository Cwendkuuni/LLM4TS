import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest
{
    @Test 
    public void testAddOption()
    {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }
    
    @Test
    public void testGetNames()
    {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("x", "option x");
        Option option2 = new Option("y", "option y");
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getNames().contains("x"));
        assertTrue(group.getNames().contains("y"));
    }
    
    @Test
    public void testSetSelected()
    {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("x", "option x");
        Option option2 = new Option("y", "option y");
        
        group.addOption(option1);
        group.addOption(option2);
        
        try
        {
            group.setSelected(option1);
            assertEquals("x", group.getSelected());
        }
        catch(AlreadySelectedException e)
        {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testIsRequired()
    {
        OptionGroup group = new OptionGroup();
        
        assertFalse(group.isRequired());
        
        group.setRequired(true);
        assertTrue(group.isRequired());
    }
    
    @Test
    public void testToString()
    {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("x", "option x");
        Option option2 = new Option("y", "option y");
        
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[-x option x, -y option y]";
        assertEquals(expected, group.toString());
    }
}