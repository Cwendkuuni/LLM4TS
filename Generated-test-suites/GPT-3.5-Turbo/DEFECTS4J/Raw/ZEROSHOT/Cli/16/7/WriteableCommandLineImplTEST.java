import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();

        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        String value = "testValue";
        commandLine.addValue(option, value);

        List<String> values = commandLine.getValues(option, null);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        boolean value = true;
        commandLine.addSwitch(option, value);

        Boolean switchValue = commandLine.getSwitch(option, null);

        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        boolean hasOption = commandLine.hasOption(option);

        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        Option result = commandLine.getOption("testOption");

        Assert.assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List<String> values = new ArrayList<>(Arrays.asList("value1", "value2"));
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> result = commandLine.getValues(option, null);

        Assert.assertEquals(values, result);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List<String> values = new ArrayList<>(Arrays.asList("value1", "value2"));
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> result = commandLine.getUndefaultedValues(option);

        Assert.assertEquals(values, result);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        boolean defaultValue = true;
        commandLine.addSwitch(option, true);

        Boolean result = commandLine.getSwitch(option, defaultValue);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetProperty() {
        String property = "testProperty";
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addProperty(option, property, "testValue");

        String result = commandLine.getProperty(option, property, null);

        Assert.assertEquals("testValue", result);
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
        commandLine.addProperty(new PropertyOption(), property, value);

        Set<String> properties = commandLine.getProperties();

        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
        commandLine.addOption(new Option("testOption"));

        boolean result = commandLine.looksLikeOption("testOption");

        Assert.assertTrue(result);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>(Arrays.asList("test", "argument")));

        String result = commandLine.toString();

        Assert.assertEquals("test argument", result);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
        commandLine.addOption(new Option("testOption"));

        List<Option> options = commandLine.getOptions();

        Assert.assertEquals(1, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
        commandLine.addOption(new Option("testOption"));

        Set<String> optionTriggers = commandLine.getOptionTriggers();

        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("testOption"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List<String> defaultValues = new ArrayList<>(Arrays.asList("defaultValue1", "defaultValue2"));
        commandLine.setDefaultValues(option, defaultValues);

        List<String> result = commandLine.getValues(option, null);

        Assert.assertEquals(defaultValues, result);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.setDefaultSwitch(option, true);

        Boolean result = commandLine.getSwitch(option, null);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>(Arrays.asList("test", "argument")));

        List<String> result = commandLine.getNormalised();

        Assert.assertEquals(Arrays.asList("test", "argument"), result);
    }
}