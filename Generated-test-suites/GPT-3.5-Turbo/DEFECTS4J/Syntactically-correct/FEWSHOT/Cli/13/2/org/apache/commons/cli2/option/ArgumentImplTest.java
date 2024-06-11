import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {
    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;
    
    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "Argument", 1, 3, ' ', ',', null, "--", null, 0);
        commandLine = new WriteableCommandLineImpl();
    }
    
    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }
    
    @Test
    public void testGetInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }
    
    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(',', argument.getSubsequentSeparator());
    }
    
    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }
    
    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }
    
    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }
    
    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "argValue"));
    }
    
    @Test
    public void testProcessValues() {
        ListIterator<String> arguments = new ArrayList<String>().listIterator();
        arguments.add("value1");
        arguments.add("value2");
        
        argument.processValues(commandLine, arguments, argument);
        
        assertEquals("value1", commandLine.getValues(argument).get(0));
        assertEquals("value2", commandLine.getValues(argument).get(1));
    }
    
    @Test
    public void testProcess() {
        ListIterator<String> arguments = new ArrayList<String>().listIterator();
        arguments.add("value1");
        arguments.add("value2");
        
        argument.process(commandLine, arguments);
        
        assertEquals("value1", commandLine.getValues(argument).get(0));
        assertEquals("value2", commandLine.getValues(argument).get(1));
    }
    
    @Test
    public void testValidate() throws OptionException {
        List<String> values = new ArrayList<String>();
        values.add("value1");
        values.add("value2");
        values.add("value3");
        
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");
        
        argument.validate(commandLine);
        
        commandLine.getValues(argument).clear();
        
        values.remove(2);
        values.remove(1);
        
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        
        argument.validate(commandLine);
        
        commandLine.getValues(argument).clear();
        commandLine.addValue(argument, "value1");
        
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            assertEquals(ResourceConstants.ARGUMENT_MISSING_VALUES, e.getMessage());
        }
    }
    
    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);
        assertEquals("arg", buffer.toString());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("Argument", argument.getDescription());
    }
    
    @Test
    public void testHelpLines() {
        List<String> helpLines = new ArrayList<String>();
        helpLines.add("arg : Argument");
        assertEquals(helpLines, argument.helpLines(0, null, null));
    }
    
    @Test
    public void testGetMaximum() {
        assertEquals(3, argument.getMaximum());
    }
    
    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }
    
    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }
    
    @Test
    public void testDefaults() {
        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        
        argument.defaultValues(commandLine, argument);
        
        assertEquals(defaultValues, commandLine.getDefaultValues(argument));
    }
}