import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        final Option rootOption = new Option("o", "output", true, "Output directory");
        final List arguments = new ArrayList();
        arguments.add("outputDir");
        arguments.add("--output");
        arguments.add("--dir");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // When
        writeableCommandLine.addOption(rootOption);

        final Option option1 = new Option("i", "input", true, "Input file");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addValue(option1, "inputFile.txt");

        final Option option2 = new Option("l", "list", false, "List files");
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addSwitch(option2, false);

        final Option option3 = new Option("h", "help", false, "Print help");
        writeableCommandLine.addOption(option3);
        writeableCommandLine.addSwitch(option3, true);

        writeableCommandLine.addProperty("user.home", System.getProperty("user.home"));

        // Then
        Assert.assertEquals("outputDir --output --dir inputFile.txt", writeableCommandLine.toString());
        Assert.assertEquals(3, writeableCommandLine.getOptions().size());
        Assert.assertEquals("output", writeableCommandLine.getOption(0).getLongOpt());
        Assert.assertEquals("output", writeableCommandLine.getOption(0).getOpt());
        Assert.assertEquals("Output directory", writeableCommandLine.getOption(0).getDescription());
        Assert.assertEquals(true, writeableCommandLine.getOption(0).isRequired());
        Assert.assertEquals("input", writeableCommandLine.getOption(1).getLongOpt());
        Assert.assertEquals("input", writeableCommandLine.getOption(1).getOpt());
        Assert.assertEquals("Input file", writeableCommandLine.getOption(1).getDescription());
        Assert.assertEquals(true, writeableCommandLine.getOption(1).isRequired());
        Assert.assertEquals("inputFile.txt", writeableCommandLine.getValues(writeableCommandLine.getOption(1), null).get(0));
        Assert.assertEquals(false, writeableCommandLine.getSwitch(option2, null));
        Assert.assertEquals(false, writeableCommandLine.hasOption(option2));
        Assert.assertEquals(false, writeableCommandLine.getSwitch(option3, null));
        Assert.assertEquals("--help", writeableCommandLine.getOption(2).getLongOpt());
        Assert.assertEquals("help", writeableCommandLine.getOption(2).getOpt());
        Assert.assertEquals("Print help", writeableCommandLine.getOption(2).getDescription());
        Assert.assertEquals(false, writeableCommandLine.getOption(2).isRequired());
        Assert.assertEquals(true, writeableCommandLine.getSwitch(option3, true));
        Assert.assertEquals(true, writeableCommandLine.hasOption(option3));
        Assert.assertEquals("user.home", writeableCommandLine.getProperty("user.home"));
        Assert.assertEquals("C:\\Users\\user", writeableCommandLine.getProperty("user.home", "C:\\"));
        Assert.assertEquals(4, writeableCommandLine.getProperties().size());
    }
}