import static org.junit.Assert.*;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.OptionException;
import org.junit.Test;


public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", null, 0);
        assertEquals("testArg", argument.getPreferredName());
    }
    
    @Test
    public void testProcessValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = Arrays.asList("value1").listIterator();
        Option option = new OptionImpl(0, false);
        
        argument.processValues(commandLine, arguments, option);
        
        assertTrue(commandLine.getValues(option).contains("value1"));
    }
    
    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        String arg = "value1";
        
        assertTrue(argument.canProcess(commandLine, arg));
    }
    
    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", null, 0);
        assertEquals('\0', argument.getInitialSeparator());
    }
    
    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", null, 0);
        assertEquals('\0', argument.getSubsequentSeparator());
    }
    
    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "--", null, 0);
        assertEquals("--", argument.getConsumeRemaining());
    }
    
    @Test
    public void testGetDefaultValues() {
        List defaultValues = Arrays.asList("default");
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", defaultValues, 0);
        assertEquals(defaultValues, argument.getDefaultValues());
    }
    
    @Test
    public void testGetValidator() {
        Validator validator = new ValidatorImpl();
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', validator, "", null, 0);
        assertEquals(validator, argument.getValidator());
    }
    
    @Test
    public void testValidate() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new OptionImpl(0, false);
        commandLine.addValue(option, "value1");
        
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            fail("Should not have thrown OptionException");
        }
    }
    
    @Test
    public void testAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 2, '\0', '\0', null, "", null, 0);
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Collections.EMPTY_SET;
        Comparator comp = null;
        argument.appendUsage(buffer, helpSettings, comp);
        
        assertEquals("testArg testArg2", buffer.toString());
    }
    
    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", null, 0);
        assertEquals("Test Argument", argument.getDescription());
    }
    
    @Test
    public void testHelpLines() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", null, 0);
        int depth = 0;
        Set helpSettings = Collections.EMPTY_SET;
        Comparator comp = null;
        
        List helpLines = argument.helpLines(depth, helpSettings, comp);
        
        assertEquals(1, helpLines.size());
        assertTrue(helpLines.get(0) instanceof HelpLine);
    }
    
    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 2, '\0', '\0', null, "", null, 0);
        assertEquals(2, argument.getMaximum());
    }
    
    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 2, 2, '\0', '\0', null, "", null, 0);
        assertEquals(2, argument.getMinimum());
    }
    
    @Test
    public void testStripBoundaryQuotes() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", null, 0);
        String token = "\"value\"";
        assertEquals("value", argument.stripBoundaryQuotes(token));
    }
    
    @Test
    public void testIsRequired() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", null, 0);
        assertTrue(argument.isRequired());
    }
    
    @Test
    public void testDefaults() {
        ArgumentImpl argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '\0', '\0', null, "", Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        argument.defaults(commandLine);
        
        assertTrue(commandLine.hasOption(0));
        assertTrue(commandLine.getValues(0).contains("default"));
    }
}