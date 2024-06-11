import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(new DefaultParser(), new String[0]);
        Option option = new Option("o", "option", true, "Option description");
        commandLine.addOption(option);
        assertNotNull(commandLine.getOption("option"));
    }
}