import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArgumentImplTest {
    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, (char) 0, (char) 0, null, "", null, 0);
        assertEquals("name", argument.getPreferredName());
    }
    
    @Test
    public void testProcessValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, (char) 0, (char) 0, null, "", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1", "value2").listIterator();
        Option option = new OptionImpl(0, false);
        
        argument.processValues(commandLine, arguments, option);
        
        assertEquals("value1", commandLine.getValues(option).get(0));
        assertEquals(1, commandLine.getValues(option).size());
    }
    
    @Test
    public void testProcess() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, (char) 0, (char) 0, null, "", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value").listIterator();
        
        argument.process(commandLine, arguments);
        
        assertEquals("value", commandLine.getValues(argument).get(0));
        assertEquals(1, commandLine.getValues(argument).size());
    }
    
    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '-', (char) 0, null, "", null, 0);
        assertEquals('-', argument.getInitialSeparator());
    }
    
    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, (char) 0, ',', null, "", null, 0);
        assertEquals(',', argument.getSubsequentSeparator());
    }
    
    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, (char) 0, (char) 0, null, "--", null, 0);
        assertEquals("--", argument.getConsumeRemaining());
    }
    
    @Test
    public void testGetDefaultValues() {
        List<String> defaultValues = Arrays.asList("default1", "default2");
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, (char) 0, (char) 0, null, "", defaultValues, 0);
        assertEquals(defaultValues, argument.getDefaultValues());
    }
    
    @Test
    public void testGetValidator() {
        Validator validator = new ValidatorImpl();
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, (char) 0, (char) 0, validator, "", null, 0);
        assertEquals(validator, argument.getValidator());
    }
    
    @Test
    public void testValidate() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 2, (char) 0, (char) 0, null, "", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        
        argument.validate(commandLine);
    }
    
    @Test(expected = OptionException.class)
    public void testValidate_Invalid() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 2, (char) 0, (char) 0, null, "", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        
        argument.validate(commandLine);
    }
    
    @Test
    public void testAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 2, (char) 0, (char) 0, null, "", null, 0);
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, new HashSet<>(), null);
        
        assertEquals("name", buffer.toString());
    }
    
    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 2, (char) 0, (char) 0, null, "", null, 0);
        assertEquals("description", argument.getDescription());
    }
    
    @Test
    public void testIsRequired() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 2, (char) 0, (char) 0, null, "", null, 0);
        assertTrue(argument.isRequired());
    }
    
    @Test
    public void testDefaults() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, (char) 0, (char) 0, null, "", Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        argument.defaults(commandLine);
        
        assertEquals("default", commandLine.getValues(argument).get(0));
    }
}