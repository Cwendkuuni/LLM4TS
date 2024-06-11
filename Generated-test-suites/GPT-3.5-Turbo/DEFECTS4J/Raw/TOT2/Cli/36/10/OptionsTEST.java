import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {
    
    @Test
    public void testAddOptionGroup() {
        // Test adding an option group
        Options options = new Options();
        Option option = new Option("o", "option", false, "Sample option");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        
        assertEquals(options.addOptionGroup(group), options);
    }
    
    @Test
    public void testAddOption() {
        // Test adding a basic option without argument
        Options options = new Options();
        
        assertEquals(options.addOption("o", "Sample option"), options);
    }
    
    @Test
    public void testGetOption() {
        // Test getting an option by key
        Options options = new Options();
        Option option = new Option("o", "option", false, "Sample option");
        options.addOption(option);
        
        assertEquals(options.getOption("o"), option);
    }
    
    @Test
    public void testHasOption() {
        // Test checking if an option exists
        Options options = new Options();
        Option option = new Option("o", "option", false, "Sample option");
        options.addOption(option);
        
        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("x"));
    }
    
    @Test
    public void testAddOptionWithArgument() {
        // Test adding an option with argument
        Options options = new Options();
        
        assertEquals(options.addOption("o", true, "Sample option with argument"), options);
    }
    
    @Test
    public void testGetMatchingOptions() {
        // Test getting matching options by partial name
        Options options = new Options();
        options.addOption("help", false, "Display help");
        options.addOption("host", true, "Host address");
        
        assertEquals(options.getMatchingOptions("h"), Arrays.asList("help", "host"));
    }
    
    @Test
    public void testRemovingOption() {
        // Test removing an option
        Options options = new Options();
        Option option = new Option("o", "option", false, "Sample option");
        options.addOption(option);
        
        options.removeOption("o");
        
        assertNull(options.getOption("o"));
    }
    
    @Test
    public void testOptionGroup() {
        // Test adding options to group and checking if they are required
        Options options = new Options();
        Option option1 = new Option("o", "option", false, "Sample option 1");
        Option option2 = new Option("s", "sample", false, "Sample option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.setRequired(true);
        
        options.addOptionGroup(group);
        
        assertTrue(group.isRequired());
    }
    
    @Test
    public void testToString() {
        // Test converting Options object to string
        Options options = new Options();
        Option option = new Option("o", "option", false, "Sample option");
        options.addOption(option);
        
        assertEquals(options.toString(), "[ Options: [ short {o=option} ] [ long {option=Option{ opt='o', longOpt='option', arg=false, description='Sample option'} } ]");
    }
}