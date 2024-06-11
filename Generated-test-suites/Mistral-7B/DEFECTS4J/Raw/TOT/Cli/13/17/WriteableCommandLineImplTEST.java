import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.examples.WriteableCommandLineExample;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option optionA = OptionBuilder.withLongOpt("optionA")
                .withDescription("Option A")
                .isRequired(false)
                .create();

        Option optionB = OptionBuilder.withLongOpt("optionB")
                .withDescription("Option B")
                .isRequired(true)
                .create();

        Option optionC = OptionBuilder.withLongOpt("optionC")
                .withDescription("Option C")
                .isRequired(false)
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("", "test"),
                Collections.singletonList("test"));

        commandLine.addOption(optionA);
        commandLine.addOption(optionB);
        commandLine.addOption(optionC);

        // Test adding values to an option
        commandLine.addValue(optionA, "valueA");
        commandLine.addValue(optionA, "valueB");

        Assert.assertTrue(commandLine.hasOption(optionA));
        Assert.assertEquals(Arrays.asList("valueA", "valueB"), commandLine.getValues(optionA, null));

        // Test adding a switch
        commandLine.addSwitch(optionC, true);
        Assert.assertTrue(commandLine.getSwitch(optionC, false));

        // Test parsing command line arguments
        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = WriteableCommandLineExample.parse(new String[]{"-test", "--optionA", "valueC", "--optionB", "valueD", "--optionC"});
            Assert.assertEquals(commandLine, parsedCommandLine);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Test getting properties
        commandLine.addProperty("property", "value");
        Assert.assertEquals("value", commandLine.getProperty("property", "defaultValue"));

        // Test looking like an option
        Assert.assertTrue(commandLine.looksLikeOption("test--optionA"));
        Assert.assertFalse(commandLine.looksLikeOption("testOptionA"));

        // Test toString()
        Assert.assertEquals("-test --optionA valueC --optionB valueD", commandLine.toString());

        // Test getOptions()
        Assert.assertEquals(3, commandLine.getOptions().size());

        // Test getOptionTriggers()
        Assert.assertEquals(3, commandLine.getOptionTriggers().size());

        // Test setDefaultValues()
        List defaultsA = Arrays.asList("defaultA");
        commandLine.setDefaultValues(optionA, defaultsA);
        Assert.assertEquals(defaultsA, commandLine.getValues(optionA, null));

        // Test setDefaultSwitch()
        commandLine.setDefaultSwitch(optionC, true);
        Assert.assertTrue(commandLine.getSwitch(optionC, false));

        // Test getNormalised()
        Assert.assertEquals(Arrays.asList("-test", "--optionA", "valueC", "--optionB", "valueD", "--optionC"), commandLine.getNormalised());
    }
}