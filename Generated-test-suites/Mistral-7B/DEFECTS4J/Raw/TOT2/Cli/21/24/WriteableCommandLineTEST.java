import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test");
        Option option = new Option("t", "test", false, "Test flag");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("t"));
    }
}