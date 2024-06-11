import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Parser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the WriteableCommandLineImpl class.
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testBasicUsage() {
        // Create a command line parser
        Parser parser = new Parser();
        Option option = new Option("o", "output", true, "Path to output file")
                .requiredArg()
                .longOpt("output-file")
                .desc("Specify output file path.");

        // Set up the command line options
        parser.parse(new String[]{"-o", "test.txt"});
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        // Test the parsed options
        Assert.assertEquals("test.txt", cmd.getValues(option, null).get(0));
    }

    @Test
    public void testMissingOption() {
        // Create a command line parser
        Parser parser = new Parser();
        Option option = new Option("o", "output", true, "Path to output file")
                .requiredArg()
                .longOpt("output-file")
                .desc("Specify output file path.");

        // Set up the command line options
        parser.parse(new String[0]);
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        // Test the parsed options
        Assert.assertNull(cmd.getValues(option, null));
        Assert.assertFalse(cmd.hasOption(option));
    }

    @Test
    public void testSwitch() {
        // Create a command line parser
        Parser parser = new Parser();
        Option option = new Option("v", "verbose", false, "Verbose logging")
                .longOpt("verbose")
                .desc("Enable verbose logging.");

        // Set up the command line options
        parser.parse(new String[]{"-v"});
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        // Test the parsed options
        Assert.assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testMissingSwitch() {
        // Create a command line parser
        Parser parser = new Parser();
        Option option = new Option("v", "verbose", false, "Verbose logging")
                .longOpt("verbose")
                .desc("Enable verbose logging.");

        // Set up the command line options
        parser.parse(new String[0]);
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        // Test the parsed options
        Assert.assertFalse(cmd.getSwitch(option, false));
    }

    @Test
    public void testProperties() {
        // Create a command line parser
        Parser parser = new Parser();
        Option option = new Option("p", "prefix", true, "Prefix for properties")
                .longOpt("prefix")
                .desc("Specify prefix for properties.");

        // Set up the command line options
        parser.parse(new String[]{"-p=test"});
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        // Test the parsed properties
        Assert.assertEquals("test", cmd.getProperty("prefix", "default"));
    }

    @Test
    public void testMissingProperty() {
        // Create a command line parser
        Parser parser = new Parser();
        Option option = new Option("p", "prefix", true, "Prefix for properties")
                .longOpt("prefix")
                .desc("Specify prefix for properties.");

        // Set up the command line options
        parser.parse(new String[0]);
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        // Test the parsed properties
        Assert.assertEquals("", cmd.getProperty("prefix", "default"));
    }
}