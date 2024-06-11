import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        
        Option option = new Option("a", "option a");
        group.addOption(option);
        
        Collection options = group.getOptions();
        
        assertTrue(options.contains(option));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Collection names = group.getNames();
        
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        group.addOption(option1);
        group.addOption(option2);
        
        Collection options = group.getOptions();
        
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        group.addOption(option1);
        group.addOption(option2);
        
        try {
            group.setSelected(option1);
            assertEquals(option1.getOpt(), group.getSelected());
            
            group.setSelected(option2);
            assertEquals(option2.getOpt(), group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedException() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        group.addOption(option1);
        group.addOption(option2);
        
        try {
            group.setSelected(option1);
            assertEquals(option1.getOpt(), group.getSelected());
            
            group.setSelected(option2);
            assertEquals(option2.getOpt(), group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        
        group.setRequired(true);
        assertTrue(group.isRequired());
        
        group.setRequired(false);
        assertFalse(group.isRequired());
    }
    
    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        
        group.setRequired(true);
        assertTrue(group.isRequired());
        
        group.setRequired(false);
        assertFalse(group.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[-[option a], --[option b]]";
        assertEquals(expected, group.toString());
    }
}