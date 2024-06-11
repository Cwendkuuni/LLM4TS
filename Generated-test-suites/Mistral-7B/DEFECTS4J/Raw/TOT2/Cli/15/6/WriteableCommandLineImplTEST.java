import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), Collections.emptyList());
        Option option = new Option("testOption", true);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
}