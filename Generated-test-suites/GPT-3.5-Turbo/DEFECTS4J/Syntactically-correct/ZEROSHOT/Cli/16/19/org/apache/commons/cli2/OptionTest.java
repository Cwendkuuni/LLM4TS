import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator args = new ArrayList<String>().listIterator();
        Option option = new OptionImpl();
        try {
            option.process(commandLine, args);
        } catch (OptionException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new OptionImpl();
        option.defaults(commandLine);
    }

    @Test
    public void testCanProcessString() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new OptionImpl();
        boolean canProcess = option.canProcess(commandLine, "argument");
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessListIterator() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = new ArrayList<String>().listIterator();
        Option option = new OptionImpl();
        boolean canProcess = option.canProcess(commandLine, arguments);
        assertTrue(canProcess);
    }

    @Test
    public void testGetTriggers() {
        Option option = new OptionImpl();
        Set triggers = option.getTriggers();
        assertNotNull(triggers);
    }

    @Test
    public void testGetPrefixes() {
        Option option = new OptionImpl();
        Set prefixes = option.getPrefixes();
        assertNotNull(prefixes);
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new OptionImpl();
        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        Set helpSettings = new HashSet();
        Comparator comp = new ComparatorImpl();
        Option option = new OptionImpl();
        List helpLines = option.helpLines(depth, helpSettings, comp);
        assertNotNull(helpLines);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = new ComparatorImpl();
        Option option = new OptionImpl();
        option.appendUsage(buffer, helpSettings, comp);
        assertNotNull(buffer);
    }

    @Test
    public void testGetPreferredName() {
        Option option = new OptionImpl();
        String preferredName = option.getPreferredName();
        assertNotNull(preferredName);
    }

    @Test
    public void testGetDescription() {
        Option option = new OptionImpl();
        String description = option.getDescription();
        assertNotNull(description);
    }

    @Test
    public void testGetId() {
        Option option = new OptionImpl();
        int id = option.getId();
        assertTrue(id >= 0);
    }

    @Test
    public void testFindOption() {
        Option option = new OptionImpl();
        Option foundOption = option.findOption("trigger");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new OptionImpl();
        boolean isRequired = option.isRequired();
        assertFalse(isRequired);
    }

    @Test
    public void testGetParent() {
        Option option = new OptionImpl();
        Option parent = option.getParent();
        assertNull(parent);
    }

    @Test
    public void testSetParent() {
        Option option = new OptionImpl();
        Option parent = new OptionImpl();
        option.setParent(parent);
        Option newParent = option.getParent();
        assertEquals(parent, newParent);
    }
}

class OptionImpl implements Option {

    public void process(final WriteableCommandLine commandLine, final ListIterator args) throws OptionException {}

    public void defaults(final WriteableCommandLine commandLine) {}

    public boolean canProcess(final WriteableCommandLine commandLine, final String argument) {
        return true;
    }

    public boolean canProcess(final WriteableCommandLine commandLine, final ListIterator arguments) {
        return true;
    }

    public Set getTriggers() {
        return new HashSet();
    }

    public Set getPrefixes() {
        return new HashSet();
    }

    public void validate(final WriteableCommandLine commandLine) throws OptionException {}

    public List helpLines(final int depth, final Set helpSettings, final Comparator comp) {
        return new ArrayList();
    }

    public void appendUsage(final StringBuffer buffer, final Set helpSettings, final Comparator comp) {}

    public String getPreferredName() {
        return "option";
    }

    public String getDescription() {
        return "description";
    }

    public int getId() {
        return 0;
    }

    public Option findOption(final String trigger) {
        return null;
    }

    public boolean isRequired() {
        return false;
    }

    public Option getParent() {
        return null;
    }

    public void setParent(Option parent) {}
}

class WriteableCommandLine {}

class OptionException extends Exception {}

class ComparatorImpl implements Comparator {

    public int compare(Object o1, Object o2) {
        return 0;
    }
}