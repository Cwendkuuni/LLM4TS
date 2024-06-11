import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.StringTokenizer;

import static org.junit.Assert.*;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "description", 0, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, Mockito.mock(Validator.class),
                ArgumentImpl.DEFAULT_CONSUME_REMAINING, Collections.emptyList(), 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals(ArgumentImpl.DEFAULT_CONSUME_REMAINING, argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(Collections.emptyList(), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertNotNull(argument.getValidator());
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(Mockito.mock(WriteableCommandLine.class), "arg"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testProcessValues() throws Exception {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        ListIterator<String> arguments = Arrays.asList("arg1", "arg2").listIterator();
        Option option = Mockito.mock(Option.class);

        argument.processValues(commandLine, arguments, option);

        Mockito.verify(commandLine).addValue(option, "arg1");
        Mockito.verify(commandLine).addValue(option, "arg2");
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testProcess() throws Exception {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        ListIterator<String> args = Arrays.asList("arg").listIterator();

        argument.process(commandLine, args);

        Mockito.verify(commandLine).addValue(argument, "arg");
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        assertEquals("arg", argument.stripBoundaryQuotes("\"arg\""));
    }

    @Test
    public void testIsRequired() {
        assertFalse(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);

        argument.defaults(commandLine);

        Mockito.verify(commandLine).setDefaultValues(argument, Collections.emptyList());
    }
}