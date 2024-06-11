package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "value1");
        List valueList = commandLine.getValues(option, null);
        assertEquals(1, valueList.size());
        assertEquals("value1", valueList.get(0));

        commandLine.addValue(option, "value2");
        valueList = commandLine.getValues(option, null);
        assertEquals(2, valueList.size());
        assertEquals("value1", valueList.get(0));
        assertEquals("value2", valueList.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, true));

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("opt"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List valueList = commandLine.getValues(option, null);
        assertEquals(2, valueList.size());
        assertEquals("value1", valueList.get(0));
        assertEquals("value2", valueList.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "value1");
        List valueList = commandLine.getUndefaultedValues(option);
        assertEquals(1, valueList.size());
        assertEquals("value1", valueList.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertFalse(commandLine.getSwitch(option, true));

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "prop", true, "Property test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property", "value1");
        assertEquals("value1", commandLine.getProperty(option, "property", "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "prop", true, "Property test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Map<String, String> properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("property1"));
        assertEquals("value2", properties.get("property2"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "prop", true, "Property test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set<String> properties = commandLine.getProperties(option).keySet();
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2
        "));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("t", "test"), Collections.emptyList());
        assertTrue(commandLine.looksLikeOption("t--test"));
        assertTrue(commandLine.looksLikeOption("t--test1"));
        assertTrue(commandLine.looksLikeOption("--test"));
        assertFalse(commandLine.looksLikeOption("--not-test"));
        assertFalse(commandLine.looksLikeOption("--test "));
        assertFalse(commandLine.looksLikeOption("test"));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "opt1", true, "Option 1 test");
        Option option2 = new Option("o2", "opt2", true, "Option 2 test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList(option2));
        String expected = String.format("%s %s %s", option1.getLongOpt(), option1.getValueDescription(), option2.getLongOpt());
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "opt1", true, "Option 1 test");
        Option option2 = new Option("o2", "opt2", true, "Option 2 test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList(option2));
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "opt1", true, "Option 1 test");
        Option option2 = new Option("o2", "opt2", true, "Option 2 test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList(option2));
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("opt1"));
        assertTrue(optionTriggers.contains("opt2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        List defaults = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaults);
        List valueList = commandLine.getValues(option, null);
        assertEquals(2, valueList.size());
        assertEquals("value1", valueList.get(0));
        assertEquals("value2", valueList.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}