import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Option("option");

        commandLine.addOption(option);

        Assert.assertTrue("Option should be added", commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Argument();
        Object value = "value";

        commandLine.addValue(option, value);

        List values = commandLine.getValues(option, null);
        Assert.assertEquals("Value should be added", value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Option("switch");

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue("Switch value should be true", switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Option("option");

        commandLine.addOption(option);

        Assert.assertTrue("Option should be present", commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Option("option");

        commandLine.addOption(option);

        Option result = commandLine.getOption("option");
        Assert.assertEquals("Should retrieve correct option", option, result);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Argument();
        Object value = "value";

        commandLine.addValue(option, value);

        List values = commandLine.getValues(option, null);
        Assert.assertEquals("Should retrieve correct values", value, values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Argument();
        Object value = "value";

        commandLine.addValue(option, value);

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals("Should retrieve correct values", value, values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Option("switch");

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue("Switch value should be true", switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Option("propertyOption");
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        String result = commandLine.getProperty(option, property, null);
        Assert.assertEquals("Should retrieve correct property value", value, result);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Option("propertyOption");
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        String result = commandLine.getProperty(option, property, null);
        Assert.assertEquals("Should add property", value, result);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Option("propertyOption");
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        Properties properties = (Properties) commandLine.getProperties(option);
        Assert.assertEquals("Should retrieve correct properties", value, properties.getProperty(property));
    }

    @Test
    public void testGetPropertiesDefault() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        Option option = new Option("propertyOption");

        Properties properties = (Properties) commandLine.getProperties(option);
        Assert.assertEquals("Should retrieve empty properties", 0, properties.size());
    }
}