import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.option.ArgumentImpl;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("testArg", "Test Argument", 1, 3, '\0', '\0', null, "--", new ArrayList(), 0);
    }

    @Test
    public void getPreferredName() {
        assertEquals("testArg", argument.getPreferredName());
    }

    @Test
    public void canProcess() {
        WriteableCommandLine commandLine = null;
        String arg = "testArgValue";
        assertTrue(argument.canProcess(commandLine, arg));
    }

    @Test
    public void getPrefixes() {
        assertTrue(argument.getPrefixes().isEmpty());
    }

    @Test
    public void getInitialSeparator() {
        assertEquals('\0', argument.getInitialSeparator());
    }

    @Test
    public void getSubsequentSeparator() {
        assertEquals('\0', argument.getSubsequentSeparator());
    }

    @Test
    public void getTriggers() {
        assertTrue(argument.getTriggers().isEmpty());
    }

    @Test
    public void getConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void getDefaultValues() {
        assertTrue(argument.getDefaultValues().isEmpty());
    }

    @Test
    public void getValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void processValues() {
        WriteableCommandLine commandLine = null;
        ListIterator arguments = null;
        Option option = null;
        assertDoesNotThrow(() -> argument.processValues(commandLine, arguments, option));
    }

    @Test
    public void process() {
        WriteableCommandLine commandLine = null;
        ListIterator args = null;
        assertDoesNotThrow(() -> argument.process(commandLine, args));
    }

    @Test
    public void validate() {
        WriteableCommandLine commandLine = null;
        Option option = null;
        assertDoesNotThrow(() -> argument.validate(commandLine, option));
    }

    @Test
    public void appendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, new HashSet<>(), null);
        assertEquals("testArg", buffer.toString());
    }

    @Test
    public void getDescription() {
        assertEquals("Test Argument", argument.getDescription());
    }

    @Test
    public void helpLines() {
        List helpLines = argument.helpLines(0, new HashSet<>(), null);
        assertEquals(1, helpLines.size());
        assertTrue(helpLines.get(0) instanceof HelpLineImpl);
    }

    @Test
    public void getMaximum() {
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void getMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void stripBoundaryQuotes() {
        String token = "\"test\"";
        String result = argument.stripBoundaryQuotes(token);
        assertEquals("test", result);
    }

    @Test
    public void isRequired() {
        assertFalse(argument.isRequired());
    }

    @Test
    public void defaults() {
        WriteableCommandLine commandLine = null;
        assertDoesNotThrow(() -> argument.defaults(commandLine));
    }

    @Test
    public void defaultValues() {
        WriteableCommandLine commandLine = null;
        Option option = null;
        assertDoesNotThrow(() -> argument.defaultValues(commandLine, option));
    }
}