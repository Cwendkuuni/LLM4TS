import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cli2CmdLineParser().parse("--test");
        Option option = new Option("test", "testOption");
        commandLine.addOption(option);
        assertNotNull(commandLine.getOption("test"));
    }
}