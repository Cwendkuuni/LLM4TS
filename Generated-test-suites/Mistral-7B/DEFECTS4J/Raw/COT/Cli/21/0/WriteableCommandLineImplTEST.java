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
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);

        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);

        boolean hasOption = commandLine.hasOption(option);
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);

        Option getOption = commandLine.getOption("opt");
        Assert.assertEquals(option, getOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "propertyValue");

        String propertyValue = commandLine.getProperty(option, "property");
        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property1", "propertyValue1");
        commandLine.addProperty(option, properties);

        Map propertiesMap = commandLine.getProperties(option);
        Assert.assertEquals(1, propertiesMap.size());
        Assert.assertEquals("property1", propertiesMap.keySet().iterator().next());
        Assert.assertEquals("propertyValue1", propertiesMap.get("property1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option("option1", "opt1", false, "Option 1 description");
        option1.setArgName("arg1");
        commandLine.addOption(option1);

        Option option2 = new Option("option2", "opt2", true, "Option 2 description");
        commandLine.addOption(option2);

        commandLine.addValue(option1, "arg1Value1");
        commandLine.addValue(option1, "arg1Value2");
        commandLine.addSwitch(option2, true);

        String expectedOutput = "option1[arg1=arg1Value1, arg1=arg1Value2] opt1 opt1 opt2";
        String actualOutput = commandLine.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option("option1", "opt1", false, "Option 1 description");
        commandLine.addOption(option1);

        Option option2 = new Option("option2", "opt2", true, "Option 2 description");
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option("option1", "opt1", false, "Option 1 description");
        commandLine.addOption(option1);

        Option option2 = new Option("option2", "opt2", true, "Option 2 description");
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("opt1"));
        Assert.assertTrue(optionTriggers.contains("opt2"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);

        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option description");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option("option1", "opt1", false, "Option 1 description");
        option1.setArgName("arg1");
        commandLine.addOption(option1);

        Option option2 = new Option("option2", "opt2", true, "Option 2 description");
        commandLine.addOption(option2);

        commandLine.addValue(option1, "arg1Value1");
        commandLine.addValue(option1, "arg1Value2");
        commandLine.addSwitch(option2, true);

        List normalised = commandLine.getNormalised();
        Assert.assertEquals(4, normalised.size());
        Assert.assertEquals("arg1Value1", normalised.get(0));
        Assert.assertEquals("arg1Value2", normalised.get(1));
        Assert.assertEquals("opt1", normalised.get(2));
        Assert.assertEquals("opt2", normalised.get(3));
    }
}