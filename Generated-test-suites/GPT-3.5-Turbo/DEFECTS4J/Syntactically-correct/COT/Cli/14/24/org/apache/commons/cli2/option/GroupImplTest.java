import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class GroupImplTest {

    @Test
    public void testCanProcess() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String arg = "test";
        group.canProcess(commandLine, arg);
        verify(commandLine, times(1)).looksLikeOption(arg);
    }

    @Test
    public void testGetPrefixes() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        group.getPrefixes();
    }

    @Test
    public void testGetTriggers() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        group.getTriggers();
    }

    @Test
    public void testProcess() throws OptionException {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator arguments = mock(ListIterator.class);
        group.process(commandLine, arguments);
    }

    @Test
    public void testValidate() throws OptionException {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        group.getPreferredName();
    }

    @Test
    public void testGetDescription() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        group.getDescription();
    }

    @Test
    public void testAppendUsage() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = null;
        group.appendUsage(buffer, helpSettings, comp);
    }

    @Test
    public void testHelpLines() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        int depth = 0;
        Set helpSettings = new HashSet();
        Comparator comp = null;
        group.helpLines(depth, helpSettings, comp);
    }

    @Test
    public void testGetOptions() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        group.getOptions();
    }

    @Test
    public void testGetAnonymous() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        group.getAnonymous();
    }

    @Test
    public void testFindOption() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        String trigger = "test";
        group.findOption(trigger);
    }

    @Test
    public void testGetMinimum() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        group.getMinimum();
    }

    @Test
    public void testGetMaximum() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        group.getMaximum();
    }

    @Test
    public void testIsRequired() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        group.isRequired();
    }

    @Test
    public void testDefaults() {
        GroupImpl group = new GroupImpl(options, name, description, minimum, maximum);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        group.defaults(commandLine);
    }

}