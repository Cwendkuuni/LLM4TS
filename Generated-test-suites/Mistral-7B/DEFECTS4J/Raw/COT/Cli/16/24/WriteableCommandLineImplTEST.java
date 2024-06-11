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
        Option option = new Option();
        commandLine.addOption(option);

        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addOption(option);

        boolean hasOption = commandLine.hasOption(option);
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("trigger");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
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
        Option option = new Option();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");

        String propertyValue = commandLine.getProperty(option, "defaultValue");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option();
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        commandLine.addOption(option);
        commandLine.addProperty(option, properties);

        Map<String, String> propertiesMap = commandLine.getProperties(option);
        Assert.assertEquals(1, propertiesMap.size());
        Assert.assertEquals("value", propertiesMap.get("property"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        commandLine.addOption(new Option());
        commandLine.addOption(new Option());

        String commandLineString = commandLine.toString();
        String expected = "\"option1\" option2";
        Assert.assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option();
        Option option2 = new Option();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option();
        Option option2 = new Option();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList(option1.getPreferredName(), option2.getPreferredName())));
    }
}