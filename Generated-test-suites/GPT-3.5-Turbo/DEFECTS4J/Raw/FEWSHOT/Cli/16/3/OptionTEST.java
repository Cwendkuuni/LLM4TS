import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = new ArrayList<String>().listIterator();
        Option option = new OptionImpl();

        try {
            option.process(commandLine, args);
        } catch (OptionException e) {
            fail("OptionException occurred");
        }
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new OptionImpl();

        option.defaults(commandLine);
    }

    @Test
    public void testCanProcessWithStringArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new OptionImpl();
        String argument = "";

        boolean result = option.canProcess(commandLine, argument);

        assertTrue(result);
    }

    @Test
    public void testCanProcessWithListIteratorArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new OptionImpl();
        ListIterator<String> arguments = new ArrayList<String>().listIterator();

        boolean result = option.canProcess(commandLine, arguments);

        assertTrue(result);
    }

    @Test
    public void testGetTriggers() {
        Option option = new OptionImpl();

        Set result = option.getTriggers();

        assertNotNull(result);
    }

    @Test
    public void testGetPrefixes() {
        Option option = new OptionImpl();

        Set result = option.getPrefixes();

        assertNotNull(result);
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = new OptionImpl();

        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            fail("OptionException occurred");
        }
    }

    @Test
    public void testHelpLines() {
        Option option = new OptionImpl();
        int depth = 0;
        Set helpSettings = new HashSet();
        Comparator comp = new ComparatorImpl();

        List result = option.helpLines(depth, helpSettings, comp);

        assertNotNull(result);
    }

    @Test
    public void testAppendUsage() {
        Option option = new OptionImpl();
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = new ComparatorImpl();

        option.appendUsage(buffer, helpSettings, comp);

        assertNotNull(buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new OptionImpl();

        String result = option.getPreferredName();

        assertNotNull(result);
    }

    @Test
    public void testGetDescription() {
        Option option = new OptionImpl();

        String result = option.getDescription();

        assertNotNull(result);
    }

    @Test
    public void testGetId() {
        Option option = new OptionImpl();

        int result = option.getId();

        assertNotNull(result);
    }

    @Test
    public void testFindOption() {
        Option option = new OptionImpl();
        String trigger = "";

        Option result = option.findOption(trigger);

        assertNotNull(result);
    }

    @Test
    public void testIsRequired() {
        Option option = new OptionImpl();

        boolean result = option.isRequired();

        assertNotNull(result);
    }

    @Test
    public void testSetParent() {
        Option option = new OptionImpl();
        Option parent = new OptionImpl();

        option.setParent(parent);
    }

}