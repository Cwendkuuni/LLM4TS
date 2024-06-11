import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArgumentImplTest {
    
    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, null, null, 0);
        assertEquals("name", argument.getPreferredName());
    }
    
    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, null, null, 0);
        assertEquals('\0', argument.getInitialSeparator());
    }
    
    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, null, null, 0);
        assertEquals('\0', argument.getSubsequentSeparator());
    }
    
    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, "--", null, 0);
        assertEquals("--", argument.getConsumeRemaining());
    }
    
    @Test
    public void testGetDefaultValues() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, null, null, 0);
        assertNull(argument.getDefaultValues());
    }
    
    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, null, null, 0);
        assertTrue(argument.canProcess(null, "arg"));
    }
    
    @Test
    public void testGetTriggers() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, null, null, 0);
        assertTrue(argument.getTriggers().isEmpty());
    }
    
    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, null, null, 0);
        assertEquals("description", argument.getDescription());
    }
    
    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, null, null, 0);
        assertEquals(1, argument.getMaximum());
    }
    
    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, null, null, 0);
        assertEquals(0, argument.getMinimum());
    }
    
    @Test
    public void testDefaultValues() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 0, 1, '\0', '\0', null, null, null, 0);
        WriteableCommandLine commandLine = new DefaultCommandLine();
        argument.defaultValues(commandLine, argument);
        assertTrue(commandLine.getValues(argument).isEmpty());
    }
    
}