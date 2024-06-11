import static org.junit.Assert.*;
import org.junit.Test;

public class ArgumentImplTest {
    
    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, "", "", null, "", null, 0);
        assertEquals("name", argument.getPreferredName());
    }
    
    @Test
    public void testProcessValues() {
        // Can't test this method without dependencies
        
        // ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, "", "", null, "", null, 0);
        // WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        // ListIterator arguments = new ArrayList<String>().listIterator();
        // Option option = new OptionImpl(0, false);
        // argument.processValues(commandLine, arguments, option);
    }
    
    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, "", "", null, "", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(argument.canProcess(commandLine, "arg"));
    }
    
    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '-', "", null, "", null, 0);
        assertEquals('-', argument.getInitialSeparator());
    }
    
    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '-', '|', null, "", null, 0);
        assertEquals('|', argument.getSubsequentSeparator());
    }
    
    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '-', '|', null, "--", null, 0);
        assertEquals("--", argument.getConsumeRemaining());
    }
    
    @Test
    public void testGetDefaultValues() {
        List<String> defaults = new ArrayList<String>();
        defaults.add("default1");
        defaults.add("default2");
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '-', '|', null, "", defaults, 0);
        assertEquals(defaults, argument.getDefaultValues());
    }
    
    @Test
    public void testGetValidator() {
        Validator validator = new ValidatorImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '-', '|', validator, "", null, 0);
        assertEquals(validator, argument.getValidator());
    }
    
    @Test
    public void testValidate() {
        // Can't test this method without dependencies
        
        // ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '-', '|', null, "", null, 0);
        // WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        // argument.validate(commandLine);
    }
    
    @Test
    public void testAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, '-', '|', null, "", null, 0);
        String expected = "name1 name2 name3";
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, new HashSet<DisplaySetting>(), null);
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '-', '|', null, "", null, 0);
        assertEquals("description", argument.getDescription());
    }
    
    @Test
    public void testHelpLines() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '-', '|', null, "", null, 0);
        List<HelpLine> helpLines = argument.helpLines(0, new HashSet<DisplaySetting>(), null);
        assertEquals(1, helpLines.size());
        assertEquals(argument, helpLines.get(0).getOption());
    }
    
    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, '-', '|', null, "", null, 0);
        assertEquals(3, argument.getMaximum());
    }
    
    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, '-', '|', null, "", null, 0);
        assertEquals(1, argument.getMinimum());
    }
    
    @Test
    public void testIsRequired() {
        ArgumentImpl argument1 = new ArgumentImpl("name", "description", 1, 3, '-', '|', null, "", null, 0);
        assertTrue(argument1.isRequired());
        ArgumentImpl argument2 = new ArgumentImpl("name", "description", 0, 3, '-', '|', null, "", null, 0);
        assertFalse(argument2.isRequired());
    }
    
    @Test
    public void testDefaults() {
        // Can't test this method without dependencies
        
        // ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 3, '-', '|', null, "", null, 0);
        // WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        // argument.defaults(commandLine);
    }
    
}