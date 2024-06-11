import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OptionTest {

    @Test
    public void testProcess_Success() {
        WriteableCommandLine mockCommandLine = mock(WriteableCommandLine.class);
        ListIterator<String> mockArgs = mock(ListIterator.class);
        
        Option option = new OptionImpl();

        try {
            option.process(mockCommandLine, mockArgs);
        } catch (OptionException e) {
            fail("Exception should not be thrown for valid input");
        }

        verify(mockArgs).next();
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine mockCommandLine = mock(WriteableCommandLine.class);
        
        Option option = new OptionImpl();
        
        option.defaults(mockCommandLine);

        verify(mockCommandLine).addDefault(anyString(), any());
    }

    // Add more test cases for other methods...

}

class OptionImpl implements Option {
    // Implement methods from Option interface...
}