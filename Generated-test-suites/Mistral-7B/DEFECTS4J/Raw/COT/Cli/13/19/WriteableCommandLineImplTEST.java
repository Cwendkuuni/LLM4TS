import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "apple", false, "Apple option");
        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "apple", false, "Apple option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "red");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("red", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "apple", false, "Apple option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertEquals(true, switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "apple", false, "Apple option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false); // should raise an exception
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "apple", false, "Apple option");
        writeableCommandLine.addOption(option);
        boolean hasOption = writeableCommandLine.hasOption(option);
        Assert.assertEquals(true, hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "apple", false, "Apple option");
        writeableCommandLine.addOption(option);
        Option result = writeableCommandLine.getOption("a");
        Assert.assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "apple", false, "Apple option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "red");
        writeableCommandLine.addValue(option, "green");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("red", values.get(0));
        Assert.assertEquals("green", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "apple", false, "Apple option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertEquals(true, switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addProperty("property", "value");
        String propertyValue = writeableCommandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addProperty("property", "value");
        String propertyValue = writeableCommandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");
        Set properties = writeableCommandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.containsAll(Arrays.asList("property1", "property2")));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "opt", true, "Option test"));
        writeableCommandLine.addOption(new Option("arg", "arg", true, "Argument test"));
        writeableCommandLine.addValue(writeableCommandLine.getOption("arg"), "value1");
        writeableCommandLine.addValue(writeableCommandLine.getOption("arg"), "value2");
        String result = writeableCommandLine.toString();
        String expected = "value1 value2 opt";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option1 = new Option("opt1", "opt1", false, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.containsAll(Arrays.asList(option1, option2)));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option1 = new Option("opt1", "opt1", false, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("opt1", "opt2")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "apple", false, "Apple option");
        writeableCommandLine.addOption(option);
        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");
        writeableCommandLine.setDefaultValues(option, defaults);
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("a", "apple", false, "Apple option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertEquals(true, switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addOption(new Option("opt", "opt", true, "Option test"));
        writeableCommandLine.addOption(new Option("arg", "arg", true, "Argument test"));
        writeableCommandLine.addValue(writeableCommandLine.getOption("arg"), "value1");
        writeableCommandLine.addValue(writeableCommandLine.getOption("arg"), "value2");
        List normalised = writeableCommandLine.getNormalised();
        List expected = new ArrayList();
        expected.add("arg value1");
        expected.add("arg value2");
        expected.add("opt");
        Assert.assertEquals(3, normalised.size());
        Assert.assertTrue(normalised.containsAll(expected));
    }
}