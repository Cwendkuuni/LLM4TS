import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.apache.commons.cli2.commandline.WriteableCommandLineImplTest.createOption;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test2"));
        Assert.assertTrue(commandLine.hasOption(createOption("--test2")));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test2"));
        commandLine.addOption(createOption("--test2"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test"));
        commandLine.addValue(createOption("--test"), "value1");
        commandLine.addValue(createOption("--test"), "value2");
        List values = commandLine.getValues(createOption("--test"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test"));
        commandLine.addSwitch(createOption("--test"), true);
        Assert.assertTrue(commandLine.getSwitch(createOption("--test"), false));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test"));
        Option option = commandLine.getOption("--test");
        Assert.assertNotNull(option);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test"));
        commandLine.addValue(createOption("--test"), "value1");
        commandLine.addValue(createOption("--test"), "value2");
        List values = commandLine.getValues(createOption("--test"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test"));
        commandLine.addValue(createOption("--test"), "value1");
        List values = commandLine.getUndefaultedValues(createOption("--test"));
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test"));
        commandLine.addSwitch(createOption("--test"), true);
        Boolean switchValue = commandLine.getSwitch(createOption("--test"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test"));
        commandLine.addProperty(createOption("--test"), "property", "value");
        String propertyValue = commandLine.getProperty(createOption("--test"), "property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test"));
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(createOption("--test"), "property", properties);
        Map<String, String> propertiesMap = commandLine.getProperties(createOption("--test"));
        Assert.assertEquals(1, propertiesMap.size());
        Assert.assertEquals("value1", propertiesMap.get("property1"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test"));
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(createOption("--test"), "property", properties);
        Set propertiesSet = commandLine.getProperties(createOption("--test"));
        Assert.assertEquals(1, propertiesSet.size());
        Assert.assertTrue(propertiesSet.containsAll(properties.keySet()));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test"));
        commandLine.addValue(createOption("--test"), "value1");
        Assert.assertEquals("--test value1", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test1"));
        commandLine.addOption(createOption("--test2"));
        List options = commandLine.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertTrue(options.contains(createOption("--test")));
        Assert.assertTrue(options.contains(createOption("--test1")));
        Assert.assertTrue(options.contains(createOption("--test2")));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(createOption("--test"), null);
        commandLine.addOption(createOption("--test1"));
        commandLine.addOption(createOption("--test2"));
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("--test", "--test1", "--test2")));
    }

    private Option createOption(String longOpt) {
        return Option.builder()
                .longOpt(longOpt)
                .build();
    }
}