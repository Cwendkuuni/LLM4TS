import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        String[] args = new String[]{"-a", "--optionA"};
        CommandLine cmd = new CommandLineParser().parse(new Options(), args);

        // Test with valid option
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("optionA"));

        // Test with non-existent option
        Assert.assertFalse(cmd.hasOption("nonExistentOption"));
    }
}