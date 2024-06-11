import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", true, "an option");
        cmdLine.addOption(option);
        Assert.assertEquals(option, cmdLine.getOption("o"));
        Assert.assertEquals(1, cmdLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", true, "an option");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        List values = cmdLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", false, "an option");
        cmdLine.addOption(option);
        cmdLine.addSwitch(option, true);
        Boolean switchValue = cmdLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithSwitchSet() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", false, "an option");
        cmdLine.addOption(option);
        cmdLine.addSwitch(option, true);
        cmdLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", true, "an option");
        cmdLine.addOption(option);
        Assert.assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", true, "an option");
        cmdLine.addOption(option);
        Assert.assertEquals(option, cmdLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", true, "an option");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        List values = cmdLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", true, "an option");
        cmdLine.addOption(option);
        List values = cmdLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", false, "an option");
        cmdLine.addOption(option);
        cmdLine.addSwitch(option, true);
        Boolean switchValue = cmdLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
        switchValue = cmdLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", false, "an option");
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        cmdLine.addOption(option);
        cmdLine.addProperty(option, "property1", "value1");
        String propertyValue = cmdLine.getProperty(option, "defaultValue");
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", false, "an option");
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        cmdLine.addOption(option);
        cmdLine.addProperty(option, "property1", "value1");
        Properties optionProperties = cmdLine.getProperties(option);
        Assert.assertEquals(1, optionProperties.size());
        Assert.assertEquals("value1", optionProperties.getProperty("property1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option = new Option("o", "opt", true, "an option");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        String cmdLineString = cmdLine.toString();
        String expected = "value1 value2";
        Assert.assertEquals(expected, cmdLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("o1", "opt1", true, "an option1");
        Option option2 = new Option("o2", "opt2", false, "an option2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        List options = cmdLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.get(0) instanceof Option);
        Assert.assertTrue(options.get(1) instanceof Option);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        Option option1 = new Option("o1", "opt1", true, "an option1");
        Option option2 = new Option("o2", "opt2", false, "an option2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        Set optionTriggers = cmdLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("o1"));
        Assert.assertTrue(optionTriggers.contains("o2"));
    }
}