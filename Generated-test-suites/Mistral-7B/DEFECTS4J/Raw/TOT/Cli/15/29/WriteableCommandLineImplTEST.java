import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.cli2.commandline.WriteableCommandLineImpl.WriteableCommandLine;

public class WriteableCommandLineImplTest {

    @Test
    public void testBasicUsage() {
        Options options = new Options();
        Option option = options.option("o", "output", true, "Output file");
        option.required(false);

        WriteableCommandLine cmd = new WriteableCommandLineImpl(option, new String[]{"-o", "test.txt"});

        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, cmd.getArguments(), true);
        } catch (ParseException e) {
            Assert.fail("Failed to parse command line: " + e.getMessage());
        }

        Assert.assertEquals("test.txt", cmd.getOptionValue("output"));
        Assert.assertEquals(1, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testMissingOption() {
        Options options = new Options();
        Option option = options.option("o", "output", true, "Output file");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new String[]{});

        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, cmd.getArguments(), true);
        } catch (ParseException e) {
            // Expected
        }

        Assert.assertEquals(0, cmd.getOptions().size());
        Assert.assertFalse(cmd.hasOption(option));
    }

    @Test
    public void testInvalidOption() {
        Options options = new Options();
        Option option = options.option("o", "output", true, "Output file");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new String[]{"-x"});

        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, cmd.getArguments(), true);
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: -x", e.getMessage());
        }

        Assert.assertEquals(0, cmd.getOptions().size());
        Assert.assertFalse(cmd.hasOption(option));
    }

    @Test
    public void testSwitchOption() {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "Print verbose output");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new String[]{});
        cmd.addSwitch(option, true);

        Assert.assertEquals(1, cmd.getOptions().size());
        Assert.assertEquals(true, cmd.getSwitch(option, false));
    }

    @Test
    public void testPropertyOption() {
        Options options = new Options();
        Option propertyOption = new Option("p", "prefix", true, "Prefix for output files")
                .required(false)
                .valueSeparator(':');

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(propertyOption, new String[]{"-p", "my"});
        cmd.addProperty(propertyOption, "prefix", "test");

        Assert.assertEquals(1, cmd.getOptions().size());
        Assert.assertEquals("test", cmd.getProperty("prefix"));
        Assert.assertEquals(1, cmd.getProperties().size());
    }

    @Test
    public void testMultipleValueOption() {
        Options options = new Options();
        Option option = options.option("i", "inputs", true, "Input files");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new String[]{"-i", "file1.txt", "-i", "file2.txt"});

        List valueList = cmd.getValues(option, new ArrayList());
        Assert.assertEquals(2, valueList.size());
        Assert.assertEquals("file1.txt", valueList.get(0));
        Assert.assertEquals("file2.txt", valueList.get(1));
    }

    @Test
    public void testMissingValueOption() {
        Options options = new Options();
        Option option = options.option("i", "inputs", true, "Input files");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new String[]{"-i"});

        List valueList = cmd.getValues(option, new ArrayList());
        Assert.assertEquals(0, valueList.size());
    }
}