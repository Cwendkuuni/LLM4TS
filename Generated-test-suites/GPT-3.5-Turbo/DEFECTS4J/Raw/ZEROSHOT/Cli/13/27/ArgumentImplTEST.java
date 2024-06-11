import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {
    
    private ArgumentImpl argument;
    
    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", null, 0);
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
        assertEquals(' ', argument.getSubsequentSeparator());
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
    public void testGetMinimum() {
        assertEquals(0, argument.getMinimum());
    }
    
    @Test
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }
    
    @Test
    public void testStripBoundaryQuotes() {
        String result = argument.stripBoundaryQuotes("\"test\"");
        assertEquals("test", result);
        
        result = argument.stripBoundaryQuotes("test");
        assertEquals("test", result);
    }
    
    @Test
    public void testIsRequired() {
        assertFalse(argument.isRequired());
    }
    
    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        argument.defaults(commandLine);
        assertNull(commandLine.getDefaultValues(argument));
    }
}