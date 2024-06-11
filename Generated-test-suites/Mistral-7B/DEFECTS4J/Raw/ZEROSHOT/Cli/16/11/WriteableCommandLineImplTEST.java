import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "output file");
        Option rootOption = new Option("t", "test", 0, "run tests");

        List arguments = new ArrayList();
        arguments.add("test.java");
        arguments.add("--output");
        arguments.add("output.txt");
        arguments.add("--test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Assert.assertEquals("--output output.txt --test", writeableCommandLine.toString());
        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
        Assert.assertEquals(2, writeableCommandLine.getOptionTriggers().size());

        Option optionFromCommandLine = writeableCommandline.getOption("output");
        Assert.assertEquals("output", optionFromCommandLine.getLongOpt());
        Assert.assertEquals(true, optionFromCommandLine.isValuePresent());
        Assert.assertEquals("output.txt", optionFromCommandLine.getValue());

        Option testOption = writeableCommandLine.getOption("test");
        Assert.assertEquals("test", testOption.getLongOpt());
        Assert.assertEquals(0, testOption.getValueCount());
        Assert.assertFalse(testOption.isValuePresent());

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals("output.txt", values.get(0));

        Boolean switchValue = writeableCommandLine.getSwitch(testOption, Boolean.FALSE);
        Assert.assertTrue(switchValue);

        List undefaultedValues = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(Collections.emptyList(), undefaultedValues);

        Option propertyOption = new Option("p", "prefix", true, "prefix for properties");
        writeableCommandLine.addProperty(propertyOption, "prefix.property", "value");
        Map<String, String> properties = writeableCommandLine.getProperties(propertyOption);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value", properties.get("prefix.property"));
        Assert.assertEquals(Collections.singleton("prefix.property"), properties.keySet());
    }
}