import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
        Assert.assertNotNull(commandLine.getOption("test"));;
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test value");
        Option option = Option.builder("test").build();
        commandLine.addValue(option, "value");
        Assert.assertEquals("value", commandLine.getValueForOption("test").get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueWithExistingOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test value");
        Option option = Option.builder("test").build();
        commandLine.addValue(option, "value2");
    }

    @Test
    public void testGetValuesForOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test value1 value2");
        Option option = Option.builder("test").build();
        List<String> values = commandLine.getValuesForOption(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("");
        Option option = Option.builder("test").build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getValuesForOption(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("");
        Option option = Option.builder("test").build();
        commandLine.setDefaultSwitch(option, false);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getValueForOptionAsType(option, Boolean.TYPE).get());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingSwitch() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("");
        Option option = Option.builder("test").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("");
        Option option = Option.builder("test").build();
        commandLine.setDefaultSwitch(option, false);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getValueForOptionAsType(option, Boolean.TYPE).get());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("");
        commandLine.addProperty("test", "propertyValue");
        Assert.assertEquals("propertyValue", commandLine.getValueForName("test"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        Assert.assertFalse(WriteableCommandLine.class.newInstance().looksLikeOption("argument"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        Assert.assertTrue(commandLine.looksLikeOption("test"));
    }
}