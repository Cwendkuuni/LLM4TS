import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new DefaultParser());
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value", new DefaultParser());
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addValue(option, "value");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(values.size(), 1);
        Assert.assertEquals(values.get(0), "value");
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value1,value2", new DefaultParser());
        Option option = Option.builder("t").longOpt("test").build();
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values.get(0), "value1");
        Assert.assertEquals(values.get(1), "value2");
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new DefaultParser());
        Option option = Option.builder("t").longOpt("test").build();
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(values.size(), 0);
        Assert.assertEquals(commandLine.getValue("test"), defaultValues.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchThrowsException() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new DefaultParser());
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new DefaultParser());
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("test"));
        Assert.assertTrue(commandLine.getOption("test").getValue().equals("true"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test property=value", new DefaultParser());
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals(commandLine.getProperty("test.property"), "value");
    }

    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new DefaultParser());
        commandLine.addProperty("test.property", "defaultValue");
        Assert.assertEquals(commandLine.getProperty("test.property"), "defaultValue");
    }

    @Test
    public void testLooksLikeOptionFalse() {
        Assert.assertFalse(WriteableCommandLine.looksLikeOption("--test"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        Assert.assertTrue(WriteableCommandLine.looksLikeOption("--test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetCurrentOptionThrowsException() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new DefaultParser());
        commandLine.setCurrentOption(null);
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new DefaultParser());
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.setCurrentOption(option);
        Assert.assertEquals(commandLine.getCurrentOption(), option);
    }
}