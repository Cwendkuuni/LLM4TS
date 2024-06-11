import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.OptionException;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class OptionTest {
    @Test
    public void testProcessOption() throws OptionException {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator args = mock(ListIterator.class);

        when(option.canProcess(commandLine, args)).thenReturn(true);
        option.process(commandLine, args);

        // Check if the method was called correctly
        verify(option).process(commandLine, args);
        // Check if the argument was processed correctly
        verifyNoMoreInteractions(args);
    }

    @Test(expected = OptionException.class)
    public void testProcessOptionWithInvalidArgument() throws OptionException {
        Option option = mock(Option.class);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator args = mock(ListIterator.class);

        when(option.canProcess(commandLine, args)).thenReturn(false);
        option.process(commandLine, args);

        // The method should throw an OptionException when an argument can't be processed
        option.process(commandLine, args);
    }
}