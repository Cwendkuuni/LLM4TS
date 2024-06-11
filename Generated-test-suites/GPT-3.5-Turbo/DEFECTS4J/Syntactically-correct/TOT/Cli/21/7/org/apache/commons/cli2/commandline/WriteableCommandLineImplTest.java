import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new OptionBuilder().withName("root").create();
        List arguments = Collections.singletonList("argument");
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withName("option").create();
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Argument argument = new ArgumentBuilder().withName("argument").create();
        Object value = "value";
        commandLine.addValue(argument, value);

        List values = commandLine.getValues(argument, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withName("option").create();
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withName("option").create();
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withName("option").create();
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("option");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Argument argument = new ArgumentBuilder().withName("argument").create();
        Object value1 = "value1";
        Object value2 = "value2";
        List<Object> defaultValues = Arrays.asList("default1", "default2");
        commandLine.addValue(argument, value1);
        commandLine.addValue(argument, value2);

        List values = commandLine.getValues(argument, defaultValues);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals(value1, values.get(0));
        Assert.assertEquals(value2, values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new ArgumentBuilder().withName("argument").create();
        Object value = "value";
        commandLine.addValue(argument, value);

        List values = commandLine.getUndefaultedValues(argument);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withName("option").create();
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
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
        Option option = new OptionBuilder().withName("option").create();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);

        String retrievedValue = commandLine.getProperty(option, property, null);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetPropertyWithDefault() {
        Option option = new OptionBuilder().withName("option").create();
        String property = "property";
        String defaultValue = "default";
        commandLine.addProperty(option, property, null);

        String retrievedValue = commandLine.getProperty(option, property, defaultValue);
        Assert.assertEquals(defaultValue, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(property1, value1);
        commandLine.addProperty(property2, value2);

        Set properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains(property1));
        Assert.assertTrue(properties.contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "--option";

        Assert.assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String expected = "\"argument\"";
        String result = commandLine.toString();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withName("option1").create();
        Option option2 = new OptionBuilder().withName("option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withName("option1").create();
        Option option2 = new OptionBuilder().withName("option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set triggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, triggers.size());
        Assert.assertTrue(triggers.contains("option1"));
        Assert.assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withName("option").create();
        List<Object> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);

        List retrievedDefaults = commandLine.getValues(option, null);
        Assert.assertEquals(defaults, retrievedDefaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withName("option").create();
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean retrievedSwitch = commandLine.getSwitch(option, null);
        Assert.assertEquals(defaultSwitch, retrievedSwitch);
    }

    @Test
    public void testGetNormalised() {
        Option option = new OptionBuilder().withName("option").create();
        List arguments = Arrays.asList("argument1", "argument2");
        commandLine = new WriteableCommandLineImpl(option, arguments);

        List normalised = commandLine.getNormalised();
        Assert.assertEquals(arguments, normalised);
    }
}