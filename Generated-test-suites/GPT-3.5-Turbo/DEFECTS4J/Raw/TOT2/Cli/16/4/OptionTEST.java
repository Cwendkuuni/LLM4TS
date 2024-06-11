import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

public class OptionTest {

    @Test
    public void testProcess() {
        // Test basic functionality of process method
        WriteableCommandLine mockCommandLine = mock(WriteableCommandLine.class);
        ListIterator mockArgs = mock(ListIterator.class);
        
        Option option = new OptionImpl();
        Exception exception = assertThrows(OptionException.class,
            () -> option.process(mockCommandLine, mockArgs));
        
        assertEquals("Exception message", "Test exception message", exception.getMessage());
    }

    @Test
    public void testDefaults() {
        // Test applying defaults for an option
        WriteableCommandLine mockCommandLine = mock(WriteableCommandLine.class);
        
        Option option = new OptionImpl();
        option.defaults(mockCommandLine);
        
        verify(mockCommandLine, times(1)).setOptionValue(eq("defaultKey"), eq("defaultValue"));
    }

    // Additional test cases for other methods should be added here

    private class OptionImpl implements Option {

        @Override
        public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
            throw new OptionException("Test exception message");
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            commandLine.setOptionValue("defaultKey", "defaultValue");
        }

        // Implementations of other Option interface methods
    }
}