import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("b", "bool");
        writeableCommandLine.addOption(option);

        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
        Assert.assertEquals(option, writeableCommandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, false);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertFalse(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);

        Option result = writeableCommandLine.getOption("a");
        Assert.assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("a", "arg");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("p", "property");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");

        String propertyValue = writeableCommandLine.getProperty(option, "property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("p", "property");
        writeableCommandLine.addOption(option);

        Map<String, String> properties = new HashMap<>();
        properties.put("property", "value");
        Properties propertiesObject = new Properties();
        propertiesObject.putAll(properties);

        writeableCommandLine.addProperty(option, "property", "value");
        Properties resultProperties = writeableCommandLine.getProperties(option);

        Assert.assertEquals(1, resultProperties.size());
        Assert.assertEquals("value", resultProperties.getProperty("property"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        Option option = new Option("p", "property");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        Set<String> resultProperties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, resultProperties.size());
        Assert.assertTrue(resultProperties.contains("property1"));
        Assert.assertTrue(resultProperties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());

        Assert.assertTrue(writeableCommandLine.looksLikeOption("a"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("b"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg"), Collections.emptyList());
        writeableCommandLine.addValue(new Option("a", "arg"), "arg1");
        writeableCommandLine.addValue(new Option("a", "arg"), "arg2");

        String result = writeableCommandLine.toString();
        String expected = "arg1 arg2";

        Assert.assertEquals(expected, result);
    }
}