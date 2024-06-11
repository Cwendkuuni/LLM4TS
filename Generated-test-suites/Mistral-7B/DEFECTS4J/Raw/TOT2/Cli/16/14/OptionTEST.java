import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator<String> args = mock(ListIterator.class);

        Option option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                commandLine.setValue("optionValue");
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setValue("defaultValue");
            }

            // other methods omitted for brevity
        };

        option.process(commandLine, args);

        verify(commandLine, times(1)).setValue("optionValue");
    }
}