
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
        Option rootOption = new Option("rootOption");
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("option1");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option1");
        Object value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option1");
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option1");
        Assert.assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("option1");
        commandLine.addOption(option1);
        Option option = commandLine.getOption("option1");
        Assert.assertEquals(option1, option);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option1");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);

        List values = new ArrayList();
        values.add("value1");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");

        Assert.assertEquals(expectedValues, commandLine.getValues(option, null));
        Assert.assertEquals(expectedValues, commandLine.getValues(option, new ArrayList()));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("option1");
        List values = new ArrayList();
        values.add("value1");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");

        Assert.assertEquals(expectedValues, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option1");
        Boolean defaultValue = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultValue);

        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(defaultValue, switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("option1");
        String property = "property1";
        String value = "value1";
        commandLine.addProperty(option, property, value);

        String propertyValue = commandLine.getProperty(option, property, null);
        Assert.assertEquals(value, propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("option1");
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);

        Assert.assertTrue(commandLine.getProperties(option).contains(property1));
        Assert.assertTrue(commandLine.getProperties(option).contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("-option"));
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, arguments);
        commandLine.addOption(option2);

        Assert.assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertTrue(commandLine.getOptionTriggers().contains("option1"));
        Assert.assertTrue(commandLine.getOptionTriggers().contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option1");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");

        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(defaultValues, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option1");
        Boolean defaultSwitch = Boolean.TRUE;

        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option1 = new Option("option1");
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, arguments);
        commandLine.addOption(option1);

        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertTrue(normalised.contains("arg1"));
        Assert.assertTrue(normalised.contains("arg2"));
    }
}
