import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@RunWith(PowerMockRunner.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setUp() {
        option = Mockito.mock(Option.class);
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("" + option.getPreferredName()));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("" + option.getPreferredName()));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySetSwitch() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getValues(option, null);
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getValues(option, null);
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        Assert.assertNotNull(values);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        commandLine.addValue(option, "value3");
        values = commandLine.getValues(option, null);
        Assert.assertNotNull(values);
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));

        commandLine.addValue(option, "value4");
        values = commandLine.getValues(option, null);
        Assert.assertNotNull(values);
        Assert.assertEquals(4, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));
        Assert.assertEquals("value4", values.get(3));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertEquals(true, switchValue);

        commandLine.addSwitch(option, false);
        switchValue = commandLine.getSwitch(option, true);
        Assert.assertEquals(false, switchValue);

        commandLine.addSwitch(option, null);
        switchValue = commandLine.getSwitch(option, false);
        Assert.assertEquals(null, switchValue);
    }

    @Test
    public void testGetProperty() {
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty(option, "defaultValue");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Map<String, String> properties = commandLine.getProperties(option);
        Assert.assertNotNull(properties);
        Assert.assertEquals("value1", properties.get("property1"));
        Assert.assertEquals("value2", properties.get("property2"));
    }

    @Test
    public void testGetProperties() {
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Map<String, String> properties = commandLine.getProperties();
        Assert.assertNotNull(properties);
        Assert.assertEquals(2, properties.size());
    }

    @Test
    public void testToString() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        String commandLineString = commandLine.toString();
        Assert.assertEquals("-" + option.getPreferredName() + " value1", commandLineString);
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        Assert.assertNotNull(options);
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        Assert.assertNotNull(optionTriggers);
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertEquals(option.getPreferredName(), optionTriggers.iterator().next());
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getValues(option, null);
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.addOption(option);
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testGetNormalised() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> normalised = commandLine.getNormalised();
        Assert.assertNotNull(normalised);
        Assert.assertEquals(1, normalised.size());
        Assert.assertEquals("-" + option.getPreferredName() + " value1", normalised.get(0));
    }
}