import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Option;
import java.util.Collection;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {
    
    // test addOption method
    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("t", "test", false, "Test option");
        group.addOption(option);
        
        Collection<Option> options = group.getOptions();
        
        assertEquals(1, options.size());
        assertTrue(options.contains(option));
    }
    
    // test getNames method
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test", false, "Test option");
        Option option2 = new Option("v", "verbose", false, "Verbose option");
        group.addOption(option1);
        group.addOption(option2);
        
        Collection<String> names = group.getNames();
        
        assertEquals(2, names.size());
        assertTrue(names.contains("t"));
        assertTrue(names.contains("v"));
    }
    
    // test getOptions method
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test", false, "Test option");
        Option option2 = new Option("v", "verbose", false, "Verbose option");
        group.addOption(option1);
        group.addOption(option2);
        
        Collection<Option> options = group.getOptions();
        
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    // test setSelected method when option is null
    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();
        group.setSelected(null);
        
        assertNull(group.getSelected());
    }
    
    // test setSelected method when option is selected first time
    @Test
    public void testSetSelectedFirstTime() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("t", "test", false, "Test option");
        group.addOption(option);
        
        try {
            group.setSelected(option);
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }
        
        assertEquals("t", group.getSelected());
    }
    
    // test setSelected method when option is selected second time
    @Test
    public void testSetSelectedSecondTime() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test", false, "Test option 1");
        Option option2 = new Option("v", "verbose", false, "Verbose option");
        group.addOption(option1);
        group.addOption(option2);
        
        try {
            group.setSelected(option1);
            group.setSelected(option2);
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }
        
        assertEquals("v", group.getSelected());
    }
    
    // test setSelected method when option is already selected
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test", false, "Test option 1");
        Option option2 = new Option("v", "verbose", false, "Verbose option");
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option1);
    }
    
    // test getSelected method
    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("t", "test", false, "Test option");
        group.addOption(option);
        group.setSelected(option);
        
        assertEquals("t", group.getSelected());
    }
    
    // test setRequired method
    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        
        assertTrue(group.isRequired());
    }
    
    // test toString method
    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("t", "test", false, "Test option");
        Option option2 = new Option("v", "verbose", false, "Verbose option");
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[-t Test option, -v Verbose option]";
        String actual = group.toString();
        
        assertEquals(expected, actual);
    }

}