import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {
    
    ArgumentImpl argument;
    WriteableCommandLine commandLine;
    
    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "argument", 1, 3, '\0', '\0', null, "--", null, 0);
        commandLine = new WriteableCommandLineImpl();
    }
    
    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }
    
    @Test
    public void testGetInitialSeparator() {
        assertEquals('\0', argument.getInitialSeparator());
    }
    
    @Test
    public void testGetSubsequentSeparator() {
        assertEquals('\0', argument.getSubsequentSeparator());
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
        assertTrue(argument.canProcess(commandLine, "arg1"));
    }
    
    @Test
    public void testProcess() throws OptionException {
        ListIterator<String> args = Arrays.asList("arg1").listIterator();
        argument.process(commandLine, args);
        assertEquals("arg1", commandLine.getValues(argument).get(0));
    }
    
    @Test
    public void testValidate() throws OptionException {
        commandLine.addValue(argument, "arg1");
        commandLine.addValue(argument, "arg2");
        argument.validate(commandLine, argument);
    }
    
    @Test(expected = OptionException.class)
    public void testValidateMissingValues() throws OptionException {
        argument.validate(commandLine, argument);
    }
    
    @Test(expected = OptionException.class)
    public void testValidateExceedsMax() throws OptionException {
        commandLine.addValue(argument, "arg1");
        commandLine.addValue(argument, "arg2");
        argument.validate(commandLine, argument);
    }
    
    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = null;
        argument.appendUsage(buffer, helpSettings, comp);
        assertEquals("arg", buffer.toString());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("argument", argument.getDescription());
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
    public void testStripBoundaryQuotes() {
        String token = argument.stripBoundaryQuotes("\"arg\"");
        assertEquals("arg", token);
    }
    
    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }
    
    @Test
    public void testDefaults() {
        argument.defaults(commandLine);
        assertNull(commandLine.getDefaultValues(argument));
    }
}