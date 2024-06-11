import org.junit.Test;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.OptionException;

public class ArgumentImplTest {

    private ArgumentImpl argumentImpl;

    @Test
    public void testProcessValues() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = Arrays.asList("value1", "value2", "value3").listIterator();
        Option option = new Option();

        try {
            argumentImpl.processValues(commandLine, arguments, option);
        } catch (OptionException e) {
            fail("ProcessValues threw unexpected OptionException");
        }

        assertEquals("value1", commandLine.getValues(option).get(0));
        assertEquals("value2", commandLine.getValues(option).get(1));
        assertEquals("value3", commandLine.getValues(option).get(2));
    }

    @Test
    public void testCanProcess() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        String arg = "value";

        assertTrue(argumentImpl.canProcess(commandLine, arg));
    }

    @Test
    public void testGetInitialSeparator() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);

        assertEquals('\0', argumentImpl.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);

        assertEquals('\0', argumentImpl.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);

        assertEquals("--", argumentImpl.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", Arrays.asList("default1", "default2"), 0);

        assertEquals("default1", argumentImpl.getDefaultValues().get(0));
        assertEquals("default2", argumentImpl.getDefaultValues().get(1));
    }

    @Test
    public void testGetValidator() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', new Validator(), "--", null, 0);

        assertNotNull(argumentImpl.getValidator());
    }

    @Test
    public void testValidate() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new Option();

        commandLine.addValue(option, "value1");

        try {
            argumentImpl.validate(commandLine, option);
        } catch (OptionException e) {
            fail("Validate threw unexpected OptionException");
        }
    }

    @Test
    public void testAppendUsage() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet<>();
        Comparator comp = null;

        argumentImpl.appendUsage(buffer, helpSettings, comp);

        assertEquals("name", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);

        assertEquals("description", argumentImpl.getDescription());
    }

    @Test
    public void testHelpLines() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);
        int depth = 0;
        Set helpSettings = new HashSet<>();
        Comparator comp = null;

        List<HelpLine> helpLines = argumentImpl.helpLines(depth, helpSettings, comp);

        assertEquals(1, helpLines.size());
        assertEquals(argumentImpl, helpLines.get(0).getArgument());
        assertEquals(0, helpLines.get(0).getDepth());
    }

    @Test
    public void testGetMaximum() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);

        assertEquals(3, argumentImpl.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);

        assertEquals(1, argumentImpl.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);
        String token = "\"value\"";

        String result = argumentImpl.stripBoundaryQuotes(token);

        assertEquals("value", result);
    }

    @Test
    public void testIsRequired() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", null, 0);

        assertTrue(argumentImpl.isRequired());
    }

    @Test
    public void testDefaults() {
        argumentImpl = new ArgumentImpl("name", "description", 1, 3, '\0', '\0', null, "--", Arrays.asList("default1", "default2"), 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        argumentImpl.defaults(commandLine);

        List values = commandLine.getValues(argumentImpl);

        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }
}