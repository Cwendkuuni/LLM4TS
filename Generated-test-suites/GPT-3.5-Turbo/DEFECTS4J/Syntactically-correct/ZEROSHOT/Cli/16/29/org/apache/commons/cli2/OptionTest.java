import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator args = new ArrayList<String>().listIterator();
        Option option = new OptionImpl();

        try {
            option.process(commandLine, args);
        } catch (OptionException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();

        option.defaults(commandLine);
    }

    @Test
    public void testCanProcessStringArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        String argument = "test";

        boolean result = option.canProcess(commandLine, argument);

        assertTrue(result);
    }

    @Test
    public void testCanProcessListIteratorArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        ListIterator arguments = new ArrayList<String>().listIterator();

        boolean result = option.canProcess(commandLine, arguments);

        assertTrue(result);
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
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();

        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testHelpLines() {
        Option option = new OptionImpl();
        int depth = 0;
        Set helpSettings = new HashSet();
        Comparator comp = Comparator.naturalOrder();

        List helpLines = option.helpLines(depth, helpSettings, comp);

        assertNotNull(helpLines);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Option option = new OptionImpl();
        Set helpSettings = new HashSet();
        Comparator comp = Comparator.naturalOrder();

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
        String trigger = "test";

        Option foundOption = option.findOption(trigger);

        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new OptionImpl();

        boolean isRequired = option.isRequired();

        assertFalse(isRequired);
    }

}

class OptionImpl implements Option {

    public void process(
        final WriteableCommandLine commandLine,
        final ListIterator args)
        throws OptionException {
        
    }

    public void defaults(final WriteableCommandLine commandLine) {
        
    }

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

    public void validate(final WriteableCommandLine commandLine)
        throws OptionException {
        
    }

    public List helpLines(
        final int depth,
        final Set helpSettings,
        final Comparator comp) {
        
        return new ArrayList();
    }

    public void appendUsage(
        final StringBuffer buffer,
        final Set helpSettings,
        final Comparator comp) {
        
    }

    public String getPreferredName() {
        return "";
    }

    public String getDescription() {
        return "";
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

}