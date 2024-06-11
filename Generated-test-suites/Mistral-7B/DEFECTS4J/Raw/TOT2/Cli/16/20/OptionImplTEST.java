import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class OptionImplTest {
    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, false);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = commandLine.getParsedArgs().listIterator();

        arguments.add("argument1");
        arguments.add("--option1");

        assertTrue(option.canProcess(commandLine, arguments));
    }
}