package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option", true, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("o", "option", false, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Option getOption = writeableCommandLine.getOption("o");
        assertSame(option, getOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "option", true, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "option", true, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "option", false, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testgetProperty() {
        Option option = new Option("o", "option", true, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.put("property", "value");
        writeableCommandLine.addProperty(option, "property", "value");
        String propertyValue = writeableCommandLine.getProperty(option, "property", "defaultValue");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("o", "option", true, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.put("property", "value");
        writeableCommandLine.addProperty(option, properties);
        Set propertiesSet = writeableCommandLine.getProperties(option);
        assertEquals(1, propertiesSet.size());
        assertTrue(propertiesSet.contains("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        assertTrue(writeableCommandLine.looksLikeOption("o"));
        assertTrue(writeableCommandLine.looksLikeOption("--option"));
        assertFalse(writeableCommandLine.looksLikeOption("argument"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "option", true, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        String commandLineString = writeableCommandLine.toString();
        assertTrue(commandLineString.contains("option value1"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", true, "An option with a boolean value 1");
        Option option2 = new Option("o2", "option2", true, "An option with a boolean value 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "option1", true, "An option with a boolean value 1");
        Option option2 = new Option("o2", "option2", true, "An option with a boolean value 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o1"));
        assertTrue(optionTriggers.contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "option", true, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("o", "option", false, "An option with a boolean value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "option", true, "An option with a boolean value");
        Argument argument = new Argument("a", "argument");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addArgument(argument);
        List normalised = writeableCommandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertTrue(normalised.contains("option"));
        assertTrue(normalised.contains("argument"));
    }
}