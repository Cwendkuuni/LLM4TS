import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setup() {
        option1 = new Option("o1", "option1", "option 1");
        option2 = new Option("o2", "option2", "option 2");
        option3 = new Option("o3", "option3", "option 3");

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        Assert.assertTrue(commandLine.hasOption(option2));
        Assert.assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        List<String> values = commandLine.getValues(option1, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        Assert.assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testHasOption() {
        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption = commandLine.getOption("o1");
        Assert.assertEquals(option1, retrievedOption);
    }

    @Test
    public void testGetValues() {
        List<String> values = commandLine.getValues(option1, null);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetUndefaultedValues() {
        List<String> values = commandLine.getUndefaultedValues(option1);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Boolean switchValue = commandLine.getSwitch(option1, false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        String propertyValue = commandLine.getProperty("property");
        Assert.assertNull(propertyValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option1, "property", "value");
        String propertyValue = commandLine.getProperty(option1, "property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(option1, "property", "value");
        Assert.assertEquals(1, commandLine.getProperties(option1).size());
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertFalse(commandLine.looksLikeOption("arg1"));
        Assert.assertTrue(commandLine.looksLikeOption("-o1"));
    }

    @Test
    public void testToString() {
        String commandLineString = commandLine.toString();
        Assert.assertEquals("arg1 arg2", commandLineString);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option1, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Assert.assertEquals(1, commandLine.getOptionTriggers().size());
        Assert.assertTrue(commandLine.getOptionTriggers().contains("o1"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option1, defaults);
        Assert.assertEquals(2, commandLine.getValues(option1, null).size());
        Assert.assertEquals(defaults, commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        Assert.assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("arg1", normalised.get(0));
        Assert.assertEquals("arg2", normalised.get(1));
    }
}