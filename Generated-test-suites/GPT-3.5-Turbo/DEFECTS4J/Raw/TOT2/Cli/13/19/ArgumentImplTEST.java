import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("testArg", "Description", 1, 3, ' ', ',', null, "--", null, 1);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("testArg", argument.getPreferredName());
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
    public void testIsValid() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        assertFalse(commandLine.isValid(argument));
    }
    
    // Edge case testing
    @Test
    public void testMinimumValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        assertTrue(commandLine.isValid(argument));
    }

    @Test
    public void testMaximumValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");
        assertTrue(commandLine.isValid(argument));
    }

    @Test
    public void testTooFewValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        assertFalse(commandLine.isValid(argument));
    }

    @Test
    public void testTooManyValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");
        commandLine.addValue(argument, "value4");
        assertFalse(commandLine.isValid(argument));
    }

    @Test
    public void testUnexpectedValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");
        commandLine.addValue(argument, "value4");
        assertFalse(commandLine.isValid(argument));
    }

}