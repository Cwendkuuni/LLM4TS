import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("optionName", true, "o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
}