import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("", "", false, "");
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "t", false, "Test option");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test", "t", false, "Test option");
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "t", false, "Test option");
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test", "t", false, "Test option");
        Assert.assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test", "t", false, "Test option");
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("t");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test", "t", false, "Test option");
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getValues(option, null);
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test", "t", false, "Test option");
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test", "t", false, "Test option");
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertNotNull(switchValue);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = commandLine.getProperty("test");
        Assert.assertNull(property);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("test", "value");
        String property = commandLine.getProperty("test");
        Assert.assertEquals("value", property);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = commandLine.getProperty("test", "default");
        Assert.assertEquals("default", property);
    }

    @Test
    public void testGetPropertiesWithOption() {
        Option option = new Option("test", "t", false, "Test option");
        commandLine.addProperty(option, "test1", "value1");
        commandLine.addProperty(option, "test2", "value2");
        Assert.assertEquals(2, commandLine.getProperties(option).size());
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("test1", "value1");
        commandLine.addProperty("test2", "value2");
        Assert.assertEquals(2, commandLine.getProperties().size());
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertFalse(commandLine.looksLikeOption("test"));
    }

    @Test
    public void testToString() {
        String string = commandLine.toString();
        Assert.assertEquals("", string);
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("test", "t", false, "Test option");
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        Assert.assertNotNull(options);
        Assert.assertEquals(1, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("test", "t", false, "Test option");
        commandLine.addOption(option);
        Set<String> triggers = commandLine.getOptionTriggers();
        Assert.assertNotNull(triggers);
        Assert.assertEquals(2, triggers.size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test", "t", false, "Test option");
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        List<String> values = commandLine.getValues(option, null);
        Assert.assertNotNull(values);
        Assert.assertEquals(2, values.size());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test", "t", false, "Test option");
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertNotNull(switchValue);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        Assert.assertNotNull(normalised);
    }
}