import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testProcess() {
        // typical use case
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator args = new ArrayList<String>().listIterator();
        Option option = new OptionImpl();
        try {
            option.process(commandLine, args);
        } catch (OptionException e) {
            fail("Exception thrown when processing valid arguments");
        }
        // edge case: empty argument list
        commandLine = new WriteableCommandLineImpl();
        args = new ArrayList<String>().listIterator();
        option = new OptionImpl();
        try {
            option.process(commandLine, args);
        } catch (OptionException e) {
            fail("Exception thrown when processing empty argument list");
        }
        // error scenario: invalid argument
        commandLine = new WriteableCommandLineImpl();
        args = new ArrayList<String>().listIterator(Arrays.asList("invalidArg"));
        option = new OptionImpl();
        try {
            option.process(commandLine, args);
            fail("No exception thrown when processing invalid argument");
        } catch (OptionException e) {
            // success
        }
    }

    @Test
    public void testDefaults() {
        // no defaults to test
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        option.defaults(commandLine);
    }

    @Test
    public void testCanProcessWithCommandlineAndArgument() {
        // typical use case
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "validArgument";
        Option option = new OptionImpl();
        boolean result = option.canProcess(commandLine, argument);
        assertTrue(result);
        // edge case: empty commandLine
        commandLine = new WriteableCommandLineImpl();
        option = new OptionImpl();
        result = option.canProcess(commandLine, argument);
        assertTrue(result);
        // error scenario: argument not processable
        commandLine = new WriteableCommandLineImpl();
        argument = "invalidArgument";
        option = new OptionImpl();
        result = option.canProcess(commandLine, argument);
        assertFalse(result);
    }

    @Test
    public void testCanProcessWithCommandlineAndArgumentList() {
        // typical use case
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new ArrayList<String>().listIterator(Arrays.asList("validArgument"));
        Option option = new OptionImpl();
        boolean result = option.canProcess(commandLine, arguments);
        assertTrue(result);
        // edge case: empty commandLine
        commandLine = new WriteableCommandLineImpl();
        arguments = new ArrayList<String>().listIterator(Arrays.asList("validArgument"));
        option = new OptionImpl();
        result = option.canProcess(commandLine, arguments);
        assertTrue(result);
        // error scenario: argument not processable
        commandLine = new WriteableCommandLineImpl();
        arguments = new ArrayList<String>().listIterator(Arrays.asList("invalidArgument"));
        option = new OptionImpl();
        result = option.canProcess(commandLine, arguments);
        assertFalse(result);
    }

    @Test
    public void testGetTriggers() {
        // test triggers for this option
        Option option = new OptionImpl();
        Set triggers = option.getTriggers();
        assertNotNull(triggers);
        // test for no triggers
        // TODO: add test cases for triggers
    }

    @Test
    public void testGetPrefixes() {
        // test prefixes for this option
        Option option = new OptionImpl();
        Set prefixes = option.getPrefixes();
        assertNotNull(prefixes);
        // test for no prefixes
        // TODO: add test cases for prefixes
    }

    @Test
    public void testValidate() {
        // no validation required for this option
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            fail("Exception thrown during validation");
        }
    }

    @Test
    public void testHelpLines() {
        // test help lines
        int depth = 0;
        Set helpSettings = new HashSet<>();
        Comparator comp = null;
        Option option = new OptionImpl();
        List helpLines = option.helpLines(depth, helpSettings, comp);
        assertNotNull(helpLines);
        // TODO: add assertions for returned help lines
    }

    @Test
    public void testAppendUsage() {
        // test appending usage information
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet<>();
        Comparator comp = null;
        Option option = new OptionImpl();
        option.appendUsage(buffer, helpSettings, comp);
        assertNotNull(buffer.toString());
        // TODO: add assertions for appended usage information
    }

    @Test
    public void testGetPreferredName() {
        // test getting preferred name
        Option option = new OptionImpl();
        String preferredName = option.getPreferredName();
        assertNotNull(preferredName);
        // TODO: add assertions for preferred name
    }

    @Test
    public void testGetDescription() {
        // test getting description
        Option option = new OptionImpl();
        String description = option.getDescription();
        assertNotNull(description);
        // TODO: add assertions for description
    }

    @Test
    public void testGetId() {
        // test getting id
        Option option = new OptionImpl();
        int id = option.getId();
        assertNotNull(id);
        // TODO: add assertions for id
    }

    @Test
    public void testFindOption() {
        // test finding option with trigger
        String trigger = "trigger";
        Option option = new OptionImpl();
        Option foundOption = option.findOption(trigger);
        assertNull(foundOption);
        // TODO: add assertions for found option
    }

    @Test
    public void testIsRequired() {
        // test if option is required
        Option option = new OptionImpl();
        boolean isRequired = option.isRequired();
        assertFalse(isRequired);
        // TODO: add assertions for isRequired
    }

    @Test
    public void testSetParent() {
        // no parent to set for this option
        Option option = new OptionImpl();
        // TODO: add test cases for setting parent
    }

}