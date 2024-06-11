import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("r");
        List<Argument> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("o1");
        Option option2 = new Option("o2");

        commandLine.addOption(option1);

        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        Option argumentOption = new Argument();
        Object value1 = "value1";
        Object value2 = 123;

        commandLine.addValue(argumentOption, value1);
        commandLine.addValue(argumentOption, value2);

        List<Object> values = commandLine.getValues(argumentOption, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals(value1, values.get(0));
        Assert.assertEquals(value2, values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option switchOption = new Option("s");

        commandLine.addSwitch(switchOption, true);

        Boolean switchValue = commandLine.getSwitch(switchOption, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o");

        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("o");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        Object value1 = "value1";
        Object value2 = 123;

        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<Object> values = commandLine.getValues(option, defaultValues);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals(value1, values.get(0));
        Assert.assertEquals(value2, values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o");
        Object value1 = "value1";
        Object value2 = 123;

        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<Object> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals(value1, values.get(0));
        Assert.assertEquals(value2, values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option switchOption = new Option("s");
        Boolean defaultValue = false;

        Boolean switchValue = commandLine.getSwitch(switchOption, defaultValue);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "prop";
        String value = "value";

        commandLine.addProperty(property, value);

        String retrievedValue = commandLine.getProperty(property);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("o");
        String property = "prop";
        String value = "value";

        commandLine.addProperty(option, property, value);

        String retrievedValue = commandLine.getProperty(option, property, null);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("o");
        String property1 = "prop1";
        String property2 = "prop2";
        String value = "value";

        commandLine.addProperty(option, property1, value);
        commandLine.addProperty(option, property2, value);

        List<Object> properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains(property1));
        Assert.assertTrue(properties.contains(property2));
    }

    @Test
    public void testToString() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        Option rootOption = new Option("r");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        String expectedOutput = "arg1 arg2";
        String output = commandLine.toString();

        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1");
        Option option2 = new Option("o2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1");
        Option option2 = new Option("o2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> triggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, triggers.size());
        Assert.assertTrue(triggers.contains("o1"));
        Assert.assertTrue(triggers.contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);
        List<Object> retrievedDefaultValues = commandLine.getValues(option, null);

        Assert.assertEquals(2, retrievedDefaultValues.size());
        Assert.assertEquals("default1", retrievedDefaultValues.get(0));
        Assert.assertEquals("default2", retrievedDefaultValues.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("o");
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean retrievedDefaultSwitch = commandLine.getSwitch(option, null);

        Assert.assertEquals(defaultSwitch, retrievedDefaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        Option rootOption = new Option("r");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        List<String> normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("arg1", normalised.get(0));
        Assert.assertEquals("arg2", normalised.get(1));
    }
}