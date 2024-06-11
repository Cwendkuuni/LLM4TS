import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
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
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Option option = new Option("b", "bool");
        writeableCommandLineImpl.addOption(option);
        List options = writeableCommandLineImpl.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Argument argument = new Argument("arg");
        writeableCommandLineImpl.addArgument(argument);
        writeableCommandLineImpl.addValue(argument, "value");
        List values = writeableCommandLineImpl.getValues(argument, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Option option = new Option("b", "bool");
        writeableCommandLineImpl.addOption(option);
        writeableCommandLineImpl.addSwitch(option, true);
        Boolean switchValue = writeableCommandLineImpl.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySet() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Option option = new Option("b", "bool");
        writeableCommandLineImpl.addOption(option);
        writeableCommandLineImpl.addSwitch(option, true);
        writeableCommandLineImpl.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Option option = new Option("a", "arg");
        writeableCommandLineImpl.addOption(option);
        Assert.assertTrue(writeableCommandLineImpl.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Option option = new Option("a", "arg");
        writeableCommandLineImpl.addOption(option);
        Option returnedOption = writeableCommandLineImpl.getOption("a");
        Assert.assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Argument argument = new Argument("arg");
        writeableCommandLineImpl.addArgument(argument);
        writeableCommandLineImpl.addValue(argument, "value1");
        writeableCommandLineImpl.addValue(argument, "value2");
        List values = writeableCommandLineImpl.getValues(argument, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Option option = new Option("b", "bool");
        writeableCommandLineImpl.addOption(option);
        writeableCommandLineImpl.addSwitch(option, true);
        Boolean switchValue = writeableCommandLineImpl.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchWithDefaultValue() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Option option = new Option("b", "bool");
        writeableCommandLineImpl.addOption(option);
        Boolean switchValue = writeableCommandLineImpl.getSwitch(option, false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLineImpl.addProperty("property", "value");
        String propertyValue = writeableCommandLineImpl.getProperty("property", "defaultValue");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLineImpl.addProperty("property1", "value1");
        writeableCommandLineImpl.addProperty("property2", "value2");
        Set properties = writeableCommandLineImpl.getProperties();
        Assert.assertEquals(2, properties.size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Assert.assertFalse(writeableCommandLineImpl.looksLikeOption(""));
        Assert.assertFalse(writeableCommandLineImpl.looksLikeOption("a"));
        Assert.assertTrue(writeableCommandLineImpl.looksLikeOption("aarg"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLineImpl.addArgument(new Argument("arg1"));
        writeableCommandLineImpl.addArgument(new Argument("arg2"));
        String commandLineString = writeableCommandLineImpl.toString();
        Assert.assertEquals("arg1 arg2", commandLineString);
    }

    @Test
    public void testGetOptionsWithEmptyList() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        List options = writeableCommandLineImpl.getOptions();
        Assert.assertEquals(0, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Option option = new Option("a", "arg");
        writeableCommandLineImpl.addOption(option);
        Set optionTriggers = writeableCommandLineImpl.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("a"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Argument argument = new Argument("arg");
        writeableCommandLineImpl.addArgument(argument);
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        writeableCommandLineImpl.setDefaultValues(argument, defaultValues);
        List values = writeableCommandLineImpl.getValues(argument, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        Option option = new Option("b", "bool");
        writeableCommandLineImpl.addOption(option);
        writeableCommandLineImpl.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLineImpl.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLineImpl.addArgument(new Argument("arg1"));
        writeableCommandLineImpl.addArgument(new Argument("arg2"));
        List normalised = writeableCommandLineImpl.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("arg1", normalised.get(0));
        Assert.assertEquals("arg2", normalised.get(1));
    }
}