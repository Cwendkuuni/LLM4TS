import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        commandLine.addOption(new Option("optionB"));
        Assert.assertTrue(commandLine.hasOption(new Option("optionB")));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        commandLine.addSwitch(new Option("option"), true);
        commandLine.addSwitch(new Option("option"), false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        commandLine.addOption(new Option("option"));
        Assert.assertTrue(commandLine.hasOption(new Option("option")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        Option option = new Option("option");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        commandLine.addValue(new Option("option"), "value1");
        commandLine.addValue(new Option("option"), "value2");
        List values = commandLine.getValues(new Option("option"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        commandLine.addValue(new Option("option"), "value1");
        List values = commandLine.getUndefaultedValues(new Option("option"));
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        commandLine.addSwitch(new Option("option"), true);
        Boolean switchValue = commandLine.getSwitch(new Option("option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        commandLine.addProperty(new Option("option"), "property", "value");
        Map<String, String> properties = commandLine.getProperties(new Option("option"));
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value", properties.get("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        Assert.assertTrue(commandLine.looksLikeOption("option"));
        Assert.assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        commandLine.addValue(new Option("option"), "value1");
        commandLine.addValue(new Option("option"), "value2");
        Assert.assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        commandLine.addOption(new Option("optionB"));
        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertTrue(options.get(0) instanceof Option);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        commandLine.addOption(new Option("optionA"));
        commandLine.addOption(new Option("optionB"));
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("optionA"));
        Assert.assertTrue(optionTriggers.contains("optionB"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        List defaultValues = new ArrayList();
        defaultValues.add("value1");
        defaultValues.add("value2");
        commandLine.setDefaultValues(new Option("option"), defaultValues);
        List values = commandLine.getValues(new Option("option"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        commandLine.setDefaultSwitch(new Option("option"), true);
        Boolean switchValue = commandLine.getSwitch(new Option("option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        commandLine.addValue(new Option("option"), "value1");
        commandLine.addValue(new Option("option"), "value2");
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertTrue(normalised.get(0) instanceof String);
        Assert.assertTrue(normalised.get(1) instanceof String);
    }
}