import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    private Option option;
    private List arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option = new Option("test", "Test option");
        arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option newOption = new Option("new", "New option");
        commandLine.addOption(newOption);
        Assert.assertTrue(commandLine.hasOption(newOption));
    }

    @Test
    public void testAddValue() {
        String value = "test value";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Assert.assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption = commandLine.getOption("test");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        List defaultValues = new ArrayList();
        defaultValues.add("default value");
        List values = commandLine.getValues(option, defaultValues);
        Assert.assertEquals(defaultValues, values);
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Boolean defaultValue = false;
        Boolean switchValue = commandLine.getSwitch(option, defaultValue);
        Assert.assertEquals(defaultValue, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "test property";
        String value = "test value";
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddProperty() {
        String property = "test property";
        String value = "test value";
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        String property = "test property";
        String value = "test value";
        commandLine.addProperty(option, property, value);
        List properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-test";
        boolean result = commandLine.looksLikeOption(trigger);
        Assert.assertTrue(result);
    }

    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        arguments.add(arg1);
        arguments.add(arg2);
        String stringRepresentation = commandLine.toString();
        Assert.assertEquals(arg1 + " " + arg2, stringRepresentation);
    }

    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertTrue(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Set triggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, triggers.size());
        Assert.assertTrue(triggers.contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        List defaults = new ArrayList();
        defaults.add("default value");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultValue = false;
        commandLine.setDefaultSwitch(option, defaultValue);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(defaultValue, switchValue);
    }

    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(arguments, normalised);
    }
}