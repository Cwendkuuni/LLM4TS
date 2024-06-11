
package org.apache.commons.cli2.commandline;

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
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", true);

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        commandLine = new WriteableCommandLineImpl(option1, arguments);
        commandLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option("o3", "Option 3", true);
        commandLine.addOption(option3);

        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertEquals(option3, options.get(2));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("arg3", "Argument 3", true);
        commandLine.addValue(argument, "val1");

        List<String> values = commandLine.getUndefaultedValues(argument);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("val1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option3 = new Option("o3", "Option 3", false);
        commandLine.addSwitch(option3, true);

        Boolean switchValue = commandLine.getSwitch(option3, false);
        Assert.assertEquals(true, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("o1", "Option 1", false);
        Option nonexistentOption = new Option("no", "Nonexistent Option", false);

        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertFalse(commandLine.hasOption(nonexistentOption));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("o1");

        Assert.assertNotNull(option);
        Assert.assertEquals("o1", option.getPreferredName());
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("o1", "Option 1", true);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");

        commandLine.setDefaultValues(option1, defaultValues);

        List<String> values = commandLine.getValues(option1, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("arg1", values.get(0));
        Assert.assertEquals("arg2", values.get(1));

        values = commandLine.getValues(option1, defaultValues);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("arg1", values.get(0));
        Assert.assertEquals("arg2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option1 = new Option("o1", "Option 1", true);
        Argument argument = new Argument("arg3", "Argument 3", true);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");

        commandLine.addOption(option1);
        commandLine.addValue(argument, "arg3");

        List<String> values = commandLine.getUndefaultedValues(argument);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("arg3", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);

        commandLine.addSwitch(option1, true);

        Boolean switchValue = commandLine.getSwitch(option1, false);
        Assert.assertEquals(true, switchValue);

        switchValue = commandLine.getSwitch(option2, true);
        Assert.assertEquals(true, switchValue);
    }

    @Test
    public void testGetProperty() {
        String propertyValue = commandLine.getProperty("property");
        Assert.assertNull(propertyValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");

        String propertyValue = commandLine.getProperty("property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetPropertyWithDefault() {
        Option option = new Option("o1", "Option 1", false);
        commandLine.addProperty(option, "property", "value");

        String propertyValue = commandLine.getProperty(option, "property", "default");
        Assert.assertEquals("value", propertyValue);

        propertyValue = commandLine.getProperty(option, "nonexistent-property", "default");
        Assert.assertEquals("default", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option1 = new Option("o1", "Option 1", false);
        commandLine.addProperty(option1, "property1", "value1");

        Option option2 = new Option("o2", "Option 2", false);
        commandLine.addProperty(option2, "property2", "value2");

        Assert.assertEquals(2, commandLine.getProperties().size());
        Assert.assertEquals(1, commandLine.getProperties(option1).size());
        Assert.assertEquals(1, commandLine.getProperties(option2).size());
    }

    @Test
    public void testLooksLikeOption() {
        String optionTrigger = "-o1";
        Assert.assertTrue(commandLine.looksLikeOption(optionTrigger));

        optionTrigger = "arg1";
        Assert.assertFalse(commandLine.looksLikeOption(optionTrigger));
    }

    @Test
    public void testToString() {
        String expectedString = "arg1 arg2";
        Assert.assertEquals(expectedString, commandLine.toString());
    }
}
