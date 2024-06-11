import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option option1 = new Option("opt1", "Option 1");
        Argument argument1 = new Argument("arg1", "Argument 1");
        commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList(argument1));
    }

    @Test
    public void testAddOption() {
        Option option2 = new Option("opt2", "Option 2");
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("opt", "Option");
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("opt", "Option");
        commandLine.addSwitch(option, true);
        Boolean value = commandLine.getSwitch(option, null);
        Assert.assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("opt1", "Option 1");
        boolean present = commandLine.hasOption(option);
        Assert.assertTrue(present);
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("opt1");
        Assert.assertEquals("opt1", option.getPreferredName());
        Assert.assertEquals("Option 1", option.getDescription());
    }

    @Test
    public void testGetValues() {
        Option option = new Option("opt", "Option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("opt", "Option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("opt", "Option");
        commandLine.addSwitch(option, true);
        Boolean value = commandLine.getSwitch(option, null);
        Assert.assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testGetProperty() {
        String property = commandLine.getProperty("property");
        Assert.assertNull(property);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("prop", "value");
        String property = commandLine.getProperty("prop");
        Assert.assertEquals("value", property);
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = commandLine.getProperty("property", "default");
        Assert.assertEquals("default", property);
    }

    @Test
    public void testGetProperties() {
        Set<String> properties = commandLine.getProperties();
        Assert.assertNotNull(properties);
        Assert.assertTrue(properties.isEmpty());
    }

    @Test
    public void testGetPropertiesForOption() {
        Option option = new Option("opt", "Option");
        commandLine.addProperty(option, "prop1", "value1");
        commandLine.addProperty(option, "prop2", "value2");
        Set<String> properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("prop1"));
        Assert.assertTrue(properties.contains("prop2"));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = commandLine.getNormalised();
        Assert.assertNotNull(arguments);
        Assert.assertEquals(1, arguments.size());
        Assert.assertEquals("arg1", arguments.get(0));
    }
}