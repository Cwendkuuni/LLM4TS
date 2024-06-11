import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        
        group.addOption(option);
        
        assertTrue(group.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        Option option3 = new Option("c", "check", false, "Check option");
        
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("r"));
        assertTrue(group.getNames().contains("c"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        Option option3 = new Option("c", "check", false, "Check option");
        
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);
        
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
        assertTrue(group.getOptions().contains(option3));
    }
    
    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        try {
            group.setSelected(option1);
            assertEquals(option1.getOpt(), group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
        
        try {
            group.setSelected(option2);
            assertEquals(option2.getOpt(), group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testSelectedNull() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        try {
            group.setSelected(option1);
            assertEquals(option1.getOpt(), group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException");
        }
        
        group.setSelected(null);
        assertNull(group.getSelected());
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
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[--add Add option, --remove Remove option]";
        assertEquals(expected, group.toString());
    }
}