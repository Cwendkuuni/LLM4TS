import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option optionA = new Option("a", "option-a")
                .requiredArg()
                .longOpt("long-option-a")
                .valueSeparator('=')
                .desc("Option A description");

        Option optionB = new Option("b", "option-b")
                .noArg()
                .longOpt("long-option-b")
                .desc("Option B description");

        Option rootOption = new Option("root", "root")
                .requiredArg()
                .desc("Root option description")
                .args(optionA, optionB);

        Options options = new Options();
        options.addOption(rootOption);

        String[] args = {"root", "arg1", "--option-a=value1", "--long-option-a=value2", "--option-b"};

        CommandLine parsed = null;
        try {
            parsed = options.parse(args);
        } catch (ParseException e) {
            Assert.fail("Error parsing command line: " + e.getMessage());
        }

        WriteableCommandLine writeableCommandLine = (WriteableCommandLine) parsed;

        Assert.assertEquals("arg1", writeableCommandLine.getArgument(0));
        Assert.assertEquals("value1", writeableCommandLine.getValues(optionA).get(0));
        Assert.assertEquals("value2", writeableCommandLine.getValues(optionA, null).get(1));
        Assert.assertTrue(writeableCommandLine.hasOption(optionB));
        Assert.assertFalse(writeableCommandLine.hasOption(new Option("c", "c")));
        Assert.assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(optionB, Boolean.FALSE));
        Assert.assertEquals(Boolean.FALSE, writeableCommandLine.getSwitch(optionB, null));

        // Test getProperties
        Properties properties = writeableCommandLine.getProperty(optionA);
        Assert.assertEquals("value1=value2", properties.getProperty("option-a"));
        Assert.assertEquals("value1", properties.getProperty("option-a.value"));

        // Test getProperty
        String property = writeableCommandLine.getProperty(optionA, "default");
        Assert.assertEquals("value1=value2", property);

        // Test setDefaultValues
        writeableCommandLine.setDefaultValues(optionA, Arrays.asList("defaultValue1", "defaultValue2"));
        properties = writeableCommandLine.getProperty(optionA);
        Assert.assertEquals("defaultValue1=defaultValue2", properties.getProperty("option-a"));

        // Test setDefaultSwitch
        writeableCommandLine.setDefaultSwitch(optionB, Boolean.TRUE);
        Assert.assertTrue(writeableCommandLine.getSwitch(optionB, null));
    }
}