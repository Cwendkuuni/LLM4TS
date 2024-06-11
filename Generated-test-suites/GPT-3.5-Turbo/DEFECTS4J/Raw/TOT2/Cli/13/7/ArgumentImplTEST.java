import static org.junit.Assert.*;
import org.junit.Test;

public class ArgumentImplTest {
    
    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("testName", "Test Description", 1, 1, ' ', ',', null, "--", null, 0);
        assertEquals("testName", argument.getPreferredName());
    }
    
    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("testName", "Test Description", 2, 3, ' ', ',', null, "--", null, 0);
        assertEquals(2, argument.getMinimum());
    }
    
    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("testName", "Test Description", 2, 3, ' ', ',', null, "--", null, 0);
        assertEquals(3, argument.getMaximum());
    }
    
    @Test
    public void testValidateWithValidator() {
        Validator validator = new SomeValidator();
        ArgumentImpl argument = new ArgumentImpl("testName", "Test Description", 1, 1, ' ', ',', validator, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            fail("Should not throw exception with valid input");
        }
    }
    
    @Test(expected = OptionException.class)
    public void testValidateWithInvalidInput() throws OptionException {
        Validator validator = new SomeValidator();
        ArgumentImpl argument = new ArgumentImpl("testName", "Test Description", 1, 1, ' ', ',', validator, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "invalidValue");
        argument.validate(commandLine);
    }
    
    @Test
    public void testAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("testName", "Test Description", 1, 3, ' ', ',', null, "--", null, 0);
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("testName testName2 testName3", buffer.toString());
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("testName", "Test Description", 1, 1, ':', ',', null, "--", null, 0);
        assertEquals(':', argument.getInitialSeparator());
    }
    
    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("testName", "Test Description", 1, 1, ':', ',', null, "--consume", null, 0);
        assertEquals("--consume", argument.getConsumeRemaining());
    }

    @Test
    public void testStripBoundaryQuotes() {
        ArgumentImpl argument = new ArgumentImpl("testName", "Test Description", 1, 1, ' ', ',', null, "--", null, 0);
        String token = "\"quotedToken\"";
        assertEquals("quotedToken", argument.stripBoundaryQuotes(token));
    }
}