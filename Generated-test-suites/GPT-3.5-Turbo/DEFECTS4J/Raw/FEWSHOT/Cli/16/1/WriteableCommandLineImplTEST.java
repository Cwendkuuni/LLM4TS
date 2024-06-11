package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("option1", "description1");
        Option option2 = new Option("option2", "description2");
        Option option3 = new Option("option3", "description3");

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        commandLine = new WriteableCommandLineImpl(option1, arguments);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
    }

    @Test
    public void testAddOption() {
        Option option4 = new Option("option4", "description4");
        commandLine.addOption(option4);

        Assert.assertTrue(commandLine.hasOption(option4));
    }

    @Test
    public void testHasOption() {
        Option option2 = new Option("option2", "description2");
        Assert.assertTrue(commandLine.hasOption(option2));

        Option option4 = new Option("option4", "description4");
        Assert.assertFalse(commandLine.hasOption(option4));
    }

    @Test
    public void testGetOption() {
        Option option3 = new Option("option3", "description3");
        Option result = commandLine.getOption("option3");

        Assert.assertEquals(option3, result);
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("option1", "description1");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("value1");
        commandLine.setDefaultValues(option1, defaultValues);

        List<Object> values = new ArrayList<>();
        values.add("arg1");

        List<Object> result = commandLine.getValues(option1, null);

        Assert.assertEquals(values, result);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option1 = new Option("option1", "description1");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("value1");
        commandLine.setDefaultValues(option1, defaultValues);

        List<Object> values = new ArrayList<>();
        values.add("arg1");

        List<Object> result = commandLine.getUndefaultedValues(option1);

        Assert.assertEquals(values, result);
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Option("option1", "description1");
        commandLine.addSwitch(option1, true);
        Boolean defaultValue = false;

        Boolean result = commandLine.getSwitch(option1, defaultValue);

        Assert.assertTrue(result);
    }

    @Test
    public void testGetProperty() {
        Option option1 = new Option("option1", "description1");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option1, property, value);

        String result = commandLine.getProperty(option1, property, null);

        Assert.assertEquals(value, result);
    }

    @Test
    public void testGetProperties() {
        Option option1 = new Option("option1", "description1");
        String property = "property";
        String value = "value";
        commandLine.addProperty(option1, property, value);

        Properties result = commandLine.getProperties(option1);

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.containsKey(property));
        Assert.assertEquals(value, result.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        boolean result = commandLine.looksLikeOption("option1");

        Assert.assertTrue(result);
    }

    @Test
    public void testToString() {
        String expected = "arg1";
        String result = commandLine.toString();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        List<Option> expected = new ArrayList<>();
        expected.add(new Option("option1", "description1"));
        expected.add(new Option("option2", "description2"));
        expected.add(new Option("option3", "description3"));

        List<Option> result = commandLine.getOptions();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1", "description1");
        Option option2 = new Option("option2", "description2");
        Option option3 = new Option("option3", "description3");

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);

        String trigger1 = "option1";
        String trigger2 = "option2";
        String trigger3 = "option3";

        Set expected = new HashSet<>();
        expected.add(trigger1);
        expected.add(trigger2);
        expected.add(trigger3);

        Set result = commandLine.getOptionTriggers();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("option1", "description1");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");

        commandLine.setDefaultValues(option1, defaultValues);

        List<Object> result = commandLine.getValues(option1, null);

        Assert.assertEquals(defaultValues, result);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Option("option1", "description1");
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option1, defaultSwitch);

        Boolean result = commandLine.getSwitch(option1, null);

        Assert.assertEquals(defaultSwitch, result);
    }

    @Test
    public void testGetNormalised() {
        List<String> expected = new ArrayList<>();
        expected.add("arg1");

        List<String> result = commandLine.getNormalised();

        Assert.assertEquals(expected, result);
    }

}