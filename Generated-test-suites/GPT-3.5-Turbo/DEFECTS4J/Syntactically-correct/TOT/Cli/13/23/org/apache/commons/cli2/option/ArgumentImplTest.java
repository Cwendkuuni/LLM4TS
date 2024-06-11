import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setup() {
        argument = new ArgumentImpl("arg", "Description", 1, 3, '-', ',', null, "--", null, 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('-', argument.getInitialSeparator());
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
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testStripBoundaryQuotes() {
        String tokenWithQuotes = "\"test\"";
        String tokenWithoutQuotes = "test";
        assertEquals(tokenWithoutQuotes, argument.stripBoundaryQuotes(tokenWithQuotes));
    }

    @Test
    public void testProcessValues_withSubsequentSplit() throws Exception {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = Arrays.asList("value1,value2,value3").listIterator();

        argument.processValues(commandLine, arguments, argument);

        List<String> values = commandLine.getValues(argument);
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testProcessValues_withConsumeRemaining() throws Exception {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = Arrays.asList("--", "value1", "value2", "value3").listIterator();

        argument.processValues(commandLine, arguments, argument);

        List<String> values = commandLine.getValues(argument);
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testProcessValues_noSplit() throws Exception {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = Arrays.asList("value1").listIterator();

        argument.processValues(commandLine, arguments, argument);

        List<String> values = commandLine.getValues(argument);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test(expected = OptionException.class)
    public void testProcessValues_invalidValue() throws Exception {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = Arrays.asList("value1,value2").listIterator();

        argument.processValues(commandLine, arguments, argument);
    }

    @Test
    public void testCanProcess_shouldReturnTrue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String arg = "value";

        assertTrue(argument.canProcess(commandLine, arg));
    }

    @Test
    public void testGetPrefixes_shouldReturnEmptySet() {
        assertTrue(argument.getPrefixes().isEmpty());
    }

    @Test
    public void testGetTriggers_shouldReturnEmptySet() {
        assertTrue(argument.getTriggers().isEmpty());
    }

    @Test
    public void testValidate_validValues() throws Exception {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_missingValues() throws Exception {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_unexpectedValue() throws Exception {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");

        argument.validate(commandLine);
    }
}