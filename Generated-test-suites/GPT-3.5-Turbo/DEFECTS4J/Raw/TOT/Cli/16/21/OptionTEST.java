
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;

public class OptionTest {

    @Test
    public void testProcess() throws OptionException {
        Option option = new OptionImpl();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> args = new ArrayList<String>().listIterator();

        // Typical use case
        args.add("value");
        option.process(commandLine, args);
        assertTrue(commandLine.getValues(option).contains("value"));

        // Edge case: Empty argument list
        args = new ArrayList<String>().listIterator();
        try {
            option.process(commandLine, args);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // Expected exception
        }

        // Error scenario: Argument not processed
        args = new ArrayList<String>().listIterator();
        args.add("value");
        args.add("value2");
        option.process(commandLine, args);
        assertFalse(args.hasNext());
        assertTrue(commandLine.getValues(option).contains("value"));
        assertFalse(commandLine.getValues(option).contains("value2"));
    }

    @Test
    public void testDefaults() {
        Option option = new OptionImpl();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Typical use case
        option.defaults(commandLine);
        assertTrue(commandLine.getValues(option).isEmpty());

        // Edge case: No defaults set

        // Error scenario: Defaults not applied
        OptionImpl.withDefaults = true;
        option.defaults(commandLine);
        assertTrue(commandLine.getValues(option).isEmpty());
    }

    @Test
    public void testCanProcessString() {
        Option option = new OptionImpl();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Typical use case
        assertTrue(option.canProcess(commandLine, "value"));

        // Edge case: Empty argument
        assertTrue(option.canProcess(commandLine, ""));

        // Error scenario: No arguments
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcessListIterator() {
        Option option = new OptionImpl();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> args = new ArrayList<String>().listIterator();

        // Typical use case
        args.add("value");
        assertTrue(option.canProcess(commandLine, args));

        // Edge case: Empty argument list
        args = new ArrayList<String>().listIterator();
        assertFalse(option.canProcess(commandLine, args));

        // Error scenario: Argument not processed
        args = new ArrayList<String>().listIterator();
        args.add("value");
        args.add("value2");
        assertFalse(option.canProcess(commandLine, args));
        assertTrue(args.hasNext());
    }

    @Test
    public void testGetTriggers() {
        Option option = new OptionImpl();

        // Typical use case
        assertTrue(option.getTriggers().isEmpty());

        // Edge case: No triggers set

        // Error scenario: Triggers not returned
        OptionImpl.withTriggers = true;
        assertFalse(option.getTriggers().isEmpty());
    }

    @Test
    public void testGetPrefixes() {
        Option option = new OptionImpl();

        // Typical use case
        assertTrue(option.getPrefixes().isEmpty());

        // Edge case: No prefixes set

        // Error scenario: Prefixes not returned
        OptionImpl.withPrefixes = true;
        assertFalse(option.getPrefixes().isEmpty());
    }

    @Test
    public void testValidate() throws OptionException {
        Option option = new OptionImpl();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        // Typical use case
        option.validate(commandLine);

        // Edge case: No validation rules

        // Error scenario: Invalid command line
        OptionImpl.throwValidationException = true;
        try {
            option.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // Expected exception
        }
    }

    @Test
    public void testHelpLines() {
        Option option = new OptionImpl();

        // Typical use case
        List helpLines = option.helpLines(0, null, null);
        assertNotNull(helpLines);

        // Edge case: Empty help settings

        // Error scenario: Help lines not returned
        OptionImpl.withHelpLines = true;
        assertNotNull(option.helpLines(0, null, null));
    }

    @Test
    public void testAppendUsage() {
        Option option = new OptionImpl();

        // Typical use case
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, null, null);
        assertTrue(buffer.length() > 0);

        // Edge case: Empty help settings

        // Error scenario: Usage not appended
        OptionImpl.withUsage = true;
        buffer = new StringBuffer();
        option.appendUsage(buffer, null, null);
        assertTrue(buffer.length() > 0);
    }

    @Test
    public void testGetPreferredName() {
        Option option = new OptionImpl();

        // Typical use case
        assertNotNull(option.getPreferredName());

        // Edge case: No preferred name set

        // Error scenario: Preferred name not returned
        OptionImpl.withPreferredName = true;
        assertNotNull(option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Option option = new OptionImpl();

        // Typical use case
        assertNotNull(option.getDescription());

        // Edge case: No description set

        // Error scenario: Description not returned
        OptionImpl.withDescription = true;
        assertNotNull(option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new OptionImpl();

        // Typical use case
        assertNotNull(option.getId());

        // Edge case: No ID set

        // Error scenario: ID not returned
        OptionImpl.withId = true;
        assertNotNull(option.getId());
    }

    @Test
    public void testFindOption() {
        Option option = new OptionImpl();

        // Typical use case
        assertNull(option.findOption("trigger"));

        // Edge case: Option not found

        // Error scenario: Option not returned
    }

    @Test
    public void testIsRequired() {
        Option option = new OptionImpl();

        // Typical use case
        assertFalse(option.isRequired());

        // Edge case: Option is required

        // Error scenario: Option is not required
        OptionImpl.isRequired = true;
        assertTrue(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option option = new OptionImpl();

        // Typical use case
        assertNull(option.getParent());

        // Edge case: No parent set

        // Error scenario: Parent not returned
        OptionImpl.withParent = true;
        assertNotNull(option.getParent());
    }

    @Test
    public void testSetParent() {
        Option option = new OptionImpl();

        // Typical use case
        option.setParent(option.getParent());
        assertNotNull(option.getParent());

        // Edge case: No parent set

        // Error scenario: Parent not set
    }
}

class OptionImpl implements Option {

    public static boolean withDefaults = false;
    public static boolean withTriggers = false;
    public static boolean withPrefixes = false;
    public static boolean throwValidationException = false;
    public static boolean withHelpLines = false;
    public static boolean withUsage = false;
    public static boolean withPreferredName = false;
    public static boolean withDescription = false;
    public static boolean withId = false;
    public static boolean isRequired = false;
    public static boolean withParent = false;

    @Override
    public void process(final WriteableCommandLine commandLine, final ListIterator args) throws OptionException {
        if (!args.hasNext()) {
            throw new OptionException("No argument provided");
        }
        commandLine.addValue(this, args.next());
    }

    @Override
    public void defaults(final WriteableCommandLine commandLine) {
        if (withDefaults) {
            commandLine.addValue(this, "default");
        }
    }

    @Override
    public boolean canProcess(final WriteableCommandLine commandLine, final String argument) {
        return true;
    }

    @Override
    public boolean canProcess(final WriteableCommandLine commandLine, final ListIterator arguments) {
        return arguments.hasNext();
    }

    @Override
    public Set getTriggers() {
        if (withTriggers) {
            Set<String> triggers = new HashSet<>();
            triggers.add("trigger");
            return triggers;
        }
        return new HashSet<>();
    }

    @Override
    public Set getPrefixes() {
        if (withPrefixes) {
            Set<String> prefixes = new HashSet<>();
            prefixes.add("-");
            return prefixes;
        }
        return new HashSet<>();
    }

    @Override
    public void validate(final WriteableCommandLine commandLine) throws OptionException {
        if (throwValidationException) {
            throw new OptionException("Invalid command line");
        }
    }

    @Override
    public List helpLines(final int depth, final Set helpSettings, final Comparator comp) {
        if (withHelpLines) {
            List helpLines = new ArrayList();
            helpLines.add("Help Line");
            return helpLines;
        }
        return new ArrayList();
    }

    @Override
    public void appendUsage(final StringBuffer buffer, final Set helpSettings, final Comparator comp) {
        if (withUsage) {
            buffer.append("Usage");
        }
    }

    @Override
    public String getPreferredName() {
        if (withPreferredName) {
            return "Preferred Name";
        }
        return "Name";
    }

    @Override
    public String getDescription() {
        if (withDescription) {
            return "Description";
        }
        return "No Description";
    }

    @Override
    public int getId() {
        if (withId) {
            return 1;
        }
        return 0;
    }

    @Override
    public Option findOption(final String trigger) {
        return null;
    }

    @Override
    public boolean isRequired() {
        if (isRequired) {
            return true;
        }
        return false;
    }

    @Override
    public Option getParent() {
        if (withParent) {
            return new OptionImpl();
        }
        return null;
    }

    @Override
    public void setParent(Option parent) {
        if (withParent) {
            assertNotNull(parent);
        }
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {

    private Map<Option, List<String>> values = new HashMap<>();

    @Override
    public void addValue(final Option option, final Object value) {
        if (!values.containsKey(option)) {
            values.put(option, new ArrayList<>());
        }
        values.get(option).add(value.toString());
    }

    @Override
    public List<String> getValues(final Option option) {
        return values.getOrDefault(option, new ArrayList<>());
    }
}
