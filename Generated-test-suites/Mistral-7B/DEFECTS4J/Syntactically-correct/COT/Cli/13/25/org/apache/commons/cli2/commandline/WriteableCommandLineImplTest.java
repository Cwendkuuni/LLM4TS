package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

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
        Option option = new Option("o", "output", true, "Output file name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Option obtainedOption = writeableCommandLine.getOption("o");
        Assert.assertNotNull(obtainedOption);
        Assert.assertEquals("o", obtainedOption.getPreferredName());
        Assert.assertEquals("output", obtainedOption.getLongOpt());
        Assert.assertEquals(true, obtainedOption.isRequired());
        Assert.assertEquals("Output file name", obtainedOption.getDescription());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "output", true, "Output file name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        List<Object> obtainedValues = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, obtainedValues.size());
        Assert.assertEquals("test.txt", obtainedValues.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("d", "debug", false, "Print debug information");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean obtainedSwitchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(obtainedSwitchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySetSwitch() {
        Option option = new Option("d", "debug", false, "Print debug information");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "output", true, "Output file name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        boolean obtainedResult = writeableCommandLine.hasOption(option);
        Assert.assertTrue(obtainedResult);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "Output file name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Option obtainedOption = writeableCommandLine.getOption("o");
        Assert.assertEquals(option, obtainedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "Output file name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        List<Object> obtainedValues = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, obtainedValues.size());
        Assert.assertEquals("test.txt", obtainedValues.get(0));

        List<String> defaultValues = Arrays.asList("default1.txt", "default2.txt");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        obtainedValues = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, obtainedValues.size());
        Assert.assertEquals("default1.txt", obtainedValues.get(0));
        Assert.assertEquals("default2.txt", obtainedValues.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("d", "debug", false, "Print debug information");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean obtainedSwitchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(obtainedSwitchValue);

        writeableCommandLine.setDefaultSwitch(option, true);
        obtainedSwitchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(obtainedSwitchValue);

        writeableCommandLine.setDefaultSwitch(option, false);
        obtainedSwitchValue = writeableCommandLine.getSwitch(option, true);
        Assert.assertFalse(obtainedSwitchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(""), new ArrayList<>());
        writeableCommandLine.addProperty("test.property", "test.value");
        String obtainedValue = writeableCommandLine.getProperty("test.property", "");
        Assert.assertEquals("test.value", obtainedValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(""), new ArrayList<>());
        writeableCommandLine.addProperty("test.property", "test.value");
        String obtainedValue = writeableCommandLine.getProperty("test.property", "");
        Assert.assertEquals("test.value", obtainedValue);

        obtainedValue = writeableCommandLine.getProperty("non.existent.property", "");
        Assert.assertEquals("", obtainedValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(""), new ArrayList<>());
        writeableCommandLine.addProperty("test.property1", "test.value1");
        writeableCommandLine.addProperty("test.property2", "test.value2");
        Set<String> obtainedProperties = writeableCommandLine.getProperties();
        Assert.assertEquals(2, obtainedProperties.size());
        Assert.assertTrue(obtainedProperties.contains("test.property1"));
        Assert.assertTrue(obtainedProperties.contains("test.property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(""), new ArrayList<>());
        Assert.assertFalse(writeableCommandLine.looksLikeOption(""));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("test"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("test-option"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--test-option"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o", "output", true, "Output file name");
        Option option2 = new Option("i", "input", true, "Input file name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test.txt", "input.txt"));
        writeableCommandLine.addOption(option2);
        String obtainedString = writeableCommandLine.toString();
        String expectedString = "test.txt input.txt --input input.txt";
        Assert.assertEquals(expectedString, obtainedString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o", "output", true, "Output file name");
        Option option2 = new Option("i", "input", true, "Input file name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test.txt"));
        writeableCommandLine.addOption(option2);
        List<Option> obtainedOptions = writeableCommandLine.getOptions();
        List<Option> expectedOptions = Arrays.asList(option1, option2);
        Assert.assertEquals(expectedOptions, obtainedOptions);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o", "output", true, "Output file name");
        Option option2 = new Option("i", "input", true, "Input file name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test.txt"));
        writeableCommandLine.addOption(option2);
        Set<String> obtainedOptionTriggers = writeableCommandLine.getOptionTriggers();
        Set<String> expectedOptionTriggers = Arrays.asList("o", "i").stream().map(String::toString).collect(Collectors.toSet());
        Assert.assertEquals(expectedOptionTriggers, obtainedOptionTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "output", true, "Output file name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List<String> defaultValues = Arrays.asList("default1.txt", "default2.txt");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List<Object> obtainedValues = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, obtainedValues.size());
        Assert.assertEquals("default1.txt", obtainedValues.get(0));
        Assert.assertEquals("default2.txt", obtainedValues.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("d", "debug", false, "Print debug information");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean obtainedSwitchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(obtainedSwitchValue);

        writeableCommandLine.setDefaultSwitch(option, false);
        obtainedSwitchValue = writeableCommandLine.getSwitch(option, true);
        Assert.assertFalse(obtainedSwitchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option1 = new Option("o", "output", true, "Output file name");
        Option option2 = new Option("i", "input", true, "Input file name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test.txt", "input.txt", "another.txt"));
        List<String> obtainedNormalised = writeableCommandLine.getNormalised();
        List<String> expectedNormalised = Arrays.asList("test.txt", "input.txt", "another.txt");
        Assert.assertEquals(expectedNormalised, obtainedNormalised);
    }
}