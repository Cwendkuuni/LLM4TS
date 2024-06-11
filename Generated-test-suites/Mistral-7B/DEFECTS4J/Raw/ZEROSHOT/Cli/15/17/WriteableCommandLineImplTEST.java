import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file");
        List arguments = new ArrayList();
        arguments.add("test.txt");

        WriteableCommandLine cmd = new WriteableCommandLineImpl(option, arguments);

        Assert.assertTrue(cmd instanceof WriteableCommandLine);
        Assert.assertEquals(option, cmd.getOption(0));
        Assert.assertEquals("output", cmd.getOption(0).getLongOpt());
        Assert.assertEquals(true, cmd.getOption(0).isRequired());
        Assert.assertEquals("Output file", cmd.getOption(0).getDescription());
        Assert.assertEquals("o", cmd.getOption(0).getOptString());
        Assert.assertEquals("output", cmd.getOption(0).getOptLong());
        Assert.assertEquals("Output file", cmd.getOption(0).getValueDescription());
        Assert.assertEquals("Output file", cmd.getOption(0).getValues().get(0).toString());
        Assert.assertFalse(cmd.getOption(0).isAllowedMultipleValues());
        Assert.assertNull(cmd.getOption(0).getValues().get(1));
        Assert.assertFalse(cmd.getOption(0).isValueSeparatorPresent());
        Assert.assertEquals(Option.UNLIMITED_VALUES, cmd.getOption(0).getMinimumNumericValues());
        Assert.assertEquals(Option.UNLIMITED_VALUES, cmd.getOption(0).getMaximumNumericValues());
        Assert.assertTrue(cmd.hasOption(option));
        Assert.assertFalse(cmd.hasOption(new Option("i", "input")));
        Assert.assertNull(cmd.getValue(new Option("i", "input")));
        Assert.assertEquals(0, cmd.getValues(new Option("i", "input"), null).size());
        Assert.assertNull(cmd.getSwitch(new Option("d", "debug")));
        Assert.assertEquals(Collections.EMPTY_LIST, cmd.getUndefaultedValues(option));

        cmd.addValue(option, "output.txt");
        Assert.assertEquals(1, cmd.getValues(option, null).size());
        Assert.assertEquals("output.txt", cmd.getValues(option, null).get(0).toString());

        cmd.addSwitch(option, true);
        Assert.assertTrue(cmd.getSwitch(option, false));

        cmd.addProperty(option, "property", "value");
        Assert.assertEquals("value", cmd.getProperty(option, "defaultValue"));
        Assert.assertEquals(Collections.unmodifiableSet(Collections.singleton("property")), cmd.getProperties(option));
        Assert.assertEquals(Collections.unmodifiableSet(Collections.singletonMap("property", "value")), cmd.getProperties());
        Assert.assertEquals("Output file=output.txt, property=value", cmd.toString());
        Assert.assertEquals(Collections.unmodifiableList(Collections.singleton(option)), cmd.getOptions());
    }
}