import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.OptionException;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {
  
    private ArgumentImpl argument;
  
    @Before
    public void setup() {
        argument = new ArgumentImpl("arg", "description", 1, 3, '\0', '\0', null, "--", null, 0);
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
    public void testGetTriggers() {
        assertTrue(argument.getTriggers().isEmpty());
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
    public void testProcessValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(argument);
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        
        // Testing processValues() method does not throw OptionException
        try {
            argument.processValues(commandLine, commandLine.getValuesIterator(argument), argument);
        } catch (OptionException e) {
            e.printStackTrace();
        }
        
        assertEquals(2, commandLine.getValues(argument).size());
        assertEquals("value1", commandLine.getValues(argument).get(0));
        assertEquals("value2", commandLine.getValues(argument).get(1));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        // Testing canProcess() method always returns true
        assertTrue(argument.canProcess(commandLine, "value"));
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(argument);
        
        // Testing validate() method does not throw OptionException
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            e.printStackTrace();
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
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        assertEquals(1, argument.helpLines(1, null, null).size());
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
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 1, '\0', '\0', null, "--", null, 0);
        assertEquals("value", argument.stripBoundaryQuotes("\"value\""));
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(argument);
        
        argument.defaults(commandLine);
        
        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("arg", commandLine.getValues(argument).get(0));
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addOption(argument);
        
        argument.defaultValues(commandLine, argument);
        
        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("arg", commandLine.getValues(argument).get(0));
    }
}