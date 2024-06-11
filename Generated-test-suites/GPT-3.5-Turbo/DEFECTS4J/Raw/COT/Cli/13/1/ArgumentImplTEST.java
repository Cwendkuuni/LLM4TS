
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.validation.InvalidArgumentException;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "description", 1, 1, '\0', '\0', null, null, null, 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('\0', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals('\0', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals(null, argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(null, argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertEquals(null, argument.getValidator());
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        String arg = "test";
        assertEquals(true, argument.canProcess(commandLine, arg));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testProcessValues() {
        WriteableCommandLine commandLine = null;
        ListIterator arguments = new ArrayList<String>().listIterator();
        Option option = null;
        try {
            argument.processValues(commandLine, arguments, option);
        } catch (OptionException e) {
        }
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = null;
        ListIterator args = new ArrayList<String>().listIterator();
        try {
            argument.process(commandLine, args);
        } catch (OptionException e) {
        }
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = null;
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
        }
    }

    @Test
    public void testValidate2() {
        WriteableCommandLine commandLine = null;
        Option option = null;
        try {
            argument.validate(commandLine, option);
        } catch (OptionException e) {
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet<DisplaySetting>();
        Comparator comp = null;
        argument.appendUsage(buffer, helpSettings, comp);
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        int depth = 1;
        Set helpSettings = new HashSet<DisplaySetting>();
        Comparator comp = null;
        assertEquals(Collections.singletonList(new HelpLineImpl(argument, depth)), argument.helpLines(depth, helpSettings, comp));
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        String token = "\"test\"";
        assertEquals("test", argument.stripBoundaryQuotes(token));
    }

    @Test
    public void testIsRequired() {
        assertEquals(true, argument.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;
        argument.defaults(commandLine);
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = null;
        Option option = null;
        argument.defaultValues(commandLine, option);
    }
}
