import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        String name = "arg";
        String description = null;
        int minimum = 0;
        int maximum = 0;
        char initialSeparator = '\0';
        char subsequentSeparator = '\0';
        Validator validator = null;
        String consumeRemaining = null;
        List valueDefaults = null;
        int id = 0;

        ArgumentImpl argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, valueDefaults, id);

        String result = argument.getPreferredName();

        assertEquals(name, result);
    }

    @Test
    public void testProcessValues() {
        // TODO: implement test method
    }

    @Test
    public void testCanProcess() {
        // TODO: implement test method
    }

    @Test
    public void testGetPrefixes() {
        // TODO: implement test method
    }

    @Test
    public void testProcess() {
        // TODO: implement test method
    }

    @Test
    public void testGetInitialSeparator() {
        // TODO: implement test method
    }

    @Test
    public void testGetSubsequentSeparator() {
        // TODO: implement test method
    }

    @Test
    public void testGetTriggers() {
        // TODO: implement test method
    }

    @Test
    public void testGetConsumeRemaining() {
        // TODO: implement test method
    }

    @Test
    public void testGetDefaultValues() {
        // TODO: implement test method
    }

    @Test
    public void testGetValidator() {
        // TODO: implement test method
    }

    @Test
    public void testValidate() {
        // TODO: implement test method
    }

    @Test
    public void testValidateWithCommandLineAndOption() {
        // TODO: implement test method
    }

    @Test
    public void testAppendUsage() {
        // TODO: implement test method
    }

    @Test
    public void testGetDescription() {
        // TODO: implement test method
    }

    @Test
    public void testHelpLines() {
        // TODO: implement test method
    }

    @Test
    public void testGetMaximum() {
        // TODO: implement test method
    }

    @Test
    public void testGetMinimum() {
        // TODO: implement test method
    }

    @Test
    public void testStripBoundaryQuotes() {
        // TODO: implement test method
    }

    @Test
    public void testIsRequired() {
        // TODO: implement test method
    }

    @Test
    public void testDefaults() {
        // TODO: implement test method
    }

    @Test
    public void testDefaultValues() {
        // TODO: implement test method
    }
}