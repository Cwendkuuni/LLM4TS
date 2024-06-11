import org.apache.commons.cli2.CommandLine;
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

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        Option option = new Option("option1", "option1", false, "o1");
        commandLine.addOption(option);

        Assert.assertEquals("option1", commandLine.getOption("o1").getValue());
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch1"), new ArrayList());
        commandLine.addSwitch(new Option("switch1", true, true, "s1"), true);
        commandLine.addSwitch(new Option("switch1", false, true, "s1"));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        Option option = new Option("option1", "option1", false, "o1");
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        Option option = new Option("option1", "option1", false, "o1");
        commandLine.addOption(option);

        Assert.assertEquals(option, commandLine.getOption("o1"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        commandLine.addValue(new Option("option1"), "value1");
        commandLine.addValue(new Option("option1"), "value2");

        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");

        List actualValues = commandLine.getValues(new Option("option1"), null);

        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        commandLine.addValue(new Option("option1"), "value1");

        List actualValues = commandLine.getUndefaultedValues(new Option("option1"));

        Assert.assertEquals(Collections.singletonList("value1"), actualValues);
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch1"), new ArrayList());
        commandLine.addSwitch(new Option("switch1", true, true, "s1"), true);

        Boolean actualSwitch = commandLine.getSwitch(new Option("switch1", null, true, "s1"), null);

        Assert.assertTrue(actualSwitch);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        commandLine.addProperty(new Option("option1"), "property1", "value1");

        String actualProperty = commandLine.getProperty(new Option("option1"), "property1", null);

        Assert.assertEquals("value1", actualProperty);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        Option option = new Option("option1", "option1", false, "o1");
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property1", "value1");

        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("property1", "value1");

        Map<String, String> actualProperties = (Map<String, String>) commandLine.getOptionToProperties().get(option);

        Assert.assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        commandLine.addValue(new Option("option1"), "value1");
        commandLine.addValue(new Option("option1"), "value2");

        String expectedToString = "value1 value2";

        String actualToString = commandLine.toString();

        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        Option option = new Option("option1", "option1", false, "o1");
        commandLine.addOption(option);

        List expectedOptions = new ArrayList();
        expectedOptions.add(option);

        List actualOptions = commandLine.getOptions();

        Assert.assertEquals(expectedOptions, actualOptions);
    }
}