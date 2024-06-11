import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.cli.ArgumentImpl;
import org.apache.commons.cli.OptionException;
import org.apache.commons.cli.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ArgumentImplTest {
    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("name", "description", 1, 3, ' ', '\0', null, "--", null, 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("name", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator arguments = mock(ListIterator.class);
        Option option = mock(Option.class);

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        when(arguments.hasNext()).thenReturn(true, true, false);
        when(arguments.next()).thenReturn("value1", "value2");
        when(arguments.previous()).thenReturn(true);

        argument.processValues(commandLine, arguments, option);

        verify(commandLine).addValue(option, "value1");
        verify(commandLine).addValue(option, "value2");
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        String arg = "arg";

        assertEquals(true, argument.canProcess(commandLine, arg));
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals('\0', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
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
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        List<String> values = new ArrayList<>();
        values.add("value1");

        when(commandLine.getValues(option)).thenReturn(values);

        argument.validate(commandLine, option);

        verify(commandLine, times(1)).getValues(option);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalid() throws OptionException {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = mock(Option.class);

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        values.add("value3");

        when(commandLine.getValues(option)).thenReturn(values);

        argument.validate(commandLine, option);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);
        assertEquals("name", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testIsRequired() {
        assertEquals(true, argument.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);

        argument.defaults(commandLine);

        verify(commandLine, times(1)).setDefaultValues(any(), any());
    }
}