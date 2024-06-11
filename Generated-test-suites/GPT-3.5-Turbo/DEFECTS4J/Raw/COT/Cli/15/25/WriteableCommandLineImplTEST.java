
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root");
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("option");
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        argument.setId("argument");
        Option option = argument;
        String value = "value";
        commandLine.addValue(option, value);
        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("switch");
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(value, switchValue.booleanValue());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option");
        commandLine.addOption(option);
        boolean hasOption = commandLine.hasOption(option);
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option");
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("option");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument();
        argument.setId("argument");
        Option option = argument;
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getValues(option, defaultValues);
        Assert.assertEquals(defaultValues, values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument();
        argument.setId("argument");
        Option option = argument;
        String value = "value";
        commandLine.addValue(option, value);
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option");
        Boolean defaultValue = Boolean.FALSE;
        commandLine.setDefaultSwitch(option, defaultValue);
        Boolean switchValue = commandLine.getSwitch(option, defaultValue);
        Assert.assertEquals(defaultValue, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        String retrievedValue = commandLine.getProperty(property);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("option");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, "");
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("option");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        List<String> properties = new ArrayList<>();
        properties.add(property);
        Assert.assertEquals(properties, commandLine.getProperties(option));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-option";
        boolean looksLikeOption = commandLine.looksLikeOption(trigger);
        Assert.assertTrue(looksLikeOption);
    }

    @Test
    public void testToString() {
        String argument = "argument";
        commandLine.addArgument(argument);
        String toString = commandLine.toString();
        Assert.assertEquals(argument, toString);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("option");
        commandLine.addOption(option);
        List<String> triggers = new ArrayList<>();
        triggers.add("option");
        Assert.assertEquals(triggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> retrievedDefaultValues = commandLine.getDefaultValues(option);
        Assert.assertEquals(defaultValues, retrievedDefaultValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option");
        Boolean defaultValue = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultValue);
        Boolean retrievedDefaultValue = commandLine.getDefaultSwitch(option);
        Assert.assertEquals(defaultValue, retrievedDefaultValue);
    }

    @Test
    public void testGetNormalised() {
        String argument = "argument";
        commandLine.addArgument(argument);
        List<String> normalised = commandLine.getNormalised();
        Assert.assertEquals(1, normalised.size());
        Assert.assertEquals(argument, normalised.get(0));
    }
}

