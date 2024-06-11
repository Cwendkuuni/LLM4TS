import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new OptionImpl();
    }

    @Test
    public void testProcess_typicalCase() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> args = List.of("argument1", "argument2").listIterator();

        try {
            option.process(commandLine, args);
        } catch (OptionException e) {
            fail("process method caused OptionException");
        }

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testProcess_emptyArgs_shouldThrowException() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> args = List.of().listIterator();

        try {
            option.process(commandLine, args);
            fail("process method did not throw OptionException for empty args");
        } catch (OptionException e) {
            // Exception expected
        }
    }

    @Test
    public void testDefaults_noDefaultsSet() {
        WriteableCommandLine commandLine = new CommandLineImpl();

        option.defaults(commandLine);

        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testCanProcess_validArgument() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        String argument = "--option";

        boolean canProcess = option.canProcess(commandLine, argument);

        assertTrue(canProcess);
    }

    @Test
    public void testCanProcess_invalidArgument() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        String argument = "argument";

        boolean canProcess = option.canProcess(commandLine, argument);

        assertFalse(canProcess);
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();

        assertNotNull(triggers);
        assertFalse(triggers.isEmpty());
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();

        assertNotNull(prefixes);
        assertFalse(prefixes.isEmpty());
    }

    @Test
    public void testValidate_validCommandLine() {
        WriteableCommandLine commandLine = new CommandLineImpl();

        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            fail("validate method caused OptionException");
        }
    }

    @Test
    public void testValidate_invalidCommandLine() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        commandLine.addError("Error Message");

        try {
            option.validate(commandLine);
            fail("validate method did not throw OptionException for invalid commandLine");
        } catch (OptionException e) {
            // Exception expected
        }
    }

    @Test
    public void testHelpLines_typicalCase() {
        int depth = 0;
        Set helpSettings = new HashSet();
        Comparator comp = null;

        List helpLines = option.helpLines(depth, helpSettings, comp);

        assertNotNull(helpLines);
        assertFalse(helpLines.isEmpty());
    }

    @Test
    public void testAppendUsage_typicalCase() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = null;

        option.appendUsage(buffer, helpSettings, comp);

        assertTrue(buffer.length() > 0);
    }

    @Test
    public void testGetPreferredName() {
        String preferredName = option.getPreferredName();

        assertNotNull(preferredName);
    }

    @Test
    public void testGetDescription() {
        String description = option.getDescription();

        assertNotNull(description);
    }

    @Test
    public void testGetId() {
        int id = option.getId();

        assertTrue(id >= 0);
    }

    @Test
    public void testFindOption_existingOption() {
        String trigger = "--option";

        Option foundOption = option.findOption(trigger);

        assertNotNull(foundOption);
        assertEquals(option, foundOption);
    }

    @Test
    public void testFindOption_nonExistingOption() {
        String trigger = "--nonexistent";

        Option foundOption = option.findOption(trigger);

        assertNull(foundOption);
    }

    @Test
    public void testIsRequired_defaultValue() {
        boolean isRequired = option.isRequired();

        assertFalse(isRequired);
    }

    @Test
    public void testParentOption_noParentOption() {
        Option parent = option.getParentOption();

        assertNull(parent);
    }

    @Test
    public void testSetParentOption_withParentOption() {
        Option parentOption = new OptionImpl();
        option.setParentOption(parentOption);

        Option parent = option.getParentOption();

        assertNotNull(parent);
        assertEquals(parentOption, parent);
    }
}