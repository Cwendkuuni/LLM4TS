import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "add", false, "add option");
        Option expected = optionGroup.addOption(option);
        assertSame(expected, optionGroup);
    }
    
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<String> names = optionGroup.getNames();
        
        assertTrue(names.contains("a"));
        assertTrue(names.contains("r"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<Option> options = optionGroup.getOptions();
        
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        String selected = optionGroup.getSelected();
        
        assertEquals("a", selected);
    }
    
    @Test
    public void testSetRequired() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[--add add option, --remove remove option]";
        String result = optionGroup.toString();
        
        assertEquals(expected, result);
    }    
}