import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@RunWith(JUnit4.class)
public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt2", "option2"));
        Option option = writeableCommandLine.getOption("opt");
        Assert.assertEquals(option, writeableCommandLine.getOption("opt"));
        Assert.assertEquals(option, writeableCommandLine.getOption("opt2"));
        Assert.assertEquals(2, writeableCommandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        Argument arg = new Argument("arg");
        writeableCommandLine.addOption(new Option("opt", "option"));
        writeableCommandLine.addValue(new Option("opt", "option"), arg);
        List values = writeableCommandLine.getValues(new Option("opt", "option"), null);
        Assert.assertEquals(arg, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "option"));
        writeableCommandLine.addSwitch(new Option("opt", "option"), true);
        Boolean switchValue = writeableCommandLine.getSwitch(new Option("opt", "option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "option"));
        writeableCommandLine.addSwitch(new Option("opt", "option"), true);
        writeableCommandLine.addSwitch(new Option("opt", "option"), false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "option"));
        boolean result = writeableCommandLine.hasOption(new Option("opt", "option"));
        Assert.assertTrue(result);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "option"));
        Option result = writeableCommandLine.getOption("opt");
        Assert.assertEquals(result, writeableCommandLine.getOption("opt"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        Argument arg1 = new Argument("arg1");
        Argument arg2 = new Argument("arg2");
        writeableCommandLine.addOption(new Option("opt", "option"));
        writeableCommandLine.addValue(new Option("opt", "option"), arg1);
        writeableCommandLine.addValue(new Option("opt", "option"), arg2);
        List values = writeableCommandLine.getValues(new Option("opt", "option"), null);
        Assert.assertEquals(arg1, values.get(0));
        Assert.assertEquals(arg2, values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        Argument arg = new Argument("arg");
        writeableCommandLine.addOption(new Option("opt", "option"));
        writeableCommandLine.addValue(new Option("opt", "option"), arg);
        List values = writeableCommandLine.getUndefaultedValues(new Option("opt", "option"));
        Assert.assertEquals(arg, values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "option"));
        writeableCommandLine.addSwitch(new Option("opt", "option"), true);
        Boolean switchValue = writeableCommandLine.getSwitch(new Option("opt", "option"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchDefaultValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "option"));
        Boolean switchValue = writeableCommandLine.getSwitch(new Option("opt", "option"), null);
        Assert.assertNull(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new PropertyOption("prop", "property", "defaultValue"));
        writeableCommandLine.addProperty(new PropertyOption("prop", "property"), "value");
        String propertyValue = writeableCommandLine.getProperty("prop");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new PropertyOption("prop", "property"));
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        writeableCommandLine.addProperty(new PropertyOption("prop", "property"), properties);
        Set propertiesSet = writeableCommandLine.getProperties("prop");
        Assert.assertEquals(properties, propertiesSet.stream().findFirst().get());
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new PropertyOption("prop", "property"));
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        writeableCommandLine.addProperty(new PropertyOption("prop", "property"), properties);
        Set propertiesSet = writeableCommandLine.getProperties();
        Assert.assertEquals(2, propertiesSet.size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        Assert.assertTrue(writeableCommandLine.looksLikeOption("opt"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("opt --option"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "option"));
        writeableCommandLine.addValue(new Option("opt", "option"), "arg1");
        writeableCommandLine.addValue(new Option("opt", "option"), "arg2");
        String result = writeableCommandLine.toString();
        String expected = "arg1 arg2";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt1", "opt1"));
        writeableCommandLine.addOption(new Option("opt2", "opt2"));
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt1", "opt1"));
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertEquals("opt", optionTriggers.iterator().next());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "option"));
        List defaultValues = new ArrayList();
        defaultValues.add("value1");
        defaultValues.add("value2");
        writeableCommandLine.setDefaultValues(new Option("opt", "option"), defaultValues);
        List values = writeableCommandLine.getValues(new Option("opt", "option"), null);
        Assert.assertEquals(defaultValues, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "option"));
        Boolean defaultSwitch = Boolean.TRUE;
        writeableCommandLine.setDefaultSwitch(new Option("opt", "option"), defaultSwitch);
        Boolean switchValue = writeableCommandLine.getSwitch(new Option("opt", "option"), null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testSetDefaultValuesNull() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "option"));
        writeableCommandLine.setDefaultValues(new Option("opt", "option"), null);
        List values = writeableCommandLine.getValues(new Option("opt", "option"), null);
        Assert.assertEquals(Collections.EMPTY_LIST, values);
    }

    @Test
    public void testSetDefaultSwitchNull() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("opt", "option"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "option"));
        writeableCommandLine.setDefaultSwitch(new Option("opt", "option"), null);
        Boolean switchValue = writeableCommandLine.getSwitch(new Option("opt", "option"), null);
        Assert.assertNull(switchValue);
    }
}