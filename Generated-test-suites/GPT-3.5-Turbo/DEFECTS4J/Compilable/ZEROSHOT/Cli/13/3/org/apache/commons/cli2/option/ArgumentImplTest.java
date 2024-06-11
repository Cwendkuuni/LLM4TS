package org.apache.commons.cli2.option;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.option.ArgumentImpl;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "A test argument", 1, 3, '\0', '\0', null, "--", null, 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        String arg = "test";
        assertTrue(argument.canProcess(commandLine, arg));
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
        assertEquals(null, argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertEquals(null, argument.getValidator());
    }

    @Test
    public void testProcessValues() throws OptionException {
        WriteableCommandLine commandLine = null;
        ArrayList<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        assertTrue(values.equals(commandLine.getValues(argument)));
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = null;
        argument.validate(commandLine);
        assertTrue(argument.getMinimum() > 0);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);
        assertEquals("arg", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("A test argument", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        assertEquals("arg: A test argument", argument.helpLines(1, null, null).get(0).toString());
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
        WriteableCommandLine commandLine = null;
        argument.defaults(commandLine);
        assertEquals(null, commandLine.getValues(argument));
    }
}