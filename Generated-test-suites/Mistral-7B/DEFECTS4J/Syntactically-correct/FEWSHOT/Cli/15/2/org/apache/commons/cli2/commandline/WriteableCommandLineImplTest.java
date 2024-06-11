package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLine.WriteableCommandLineBuilder;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testCreateWriteableCommandLine() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertEquals(option, commandLine.getOption("foo"));
        assertNull(commandLine.getValues(option, null));
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "value1");
        assertEquals("value1", commandLine.getValues(option, null).get(0));
        assertEquals(1, commandLine.getValues(option, null).size());

        commandLine.addValue(option, "value2");
        assertEquals("value2", commandLine.getValues(option, null).get(1));
        assertEquals(2, commandLine.getValues(option, null).size());
    }

    @Test
    public void testAddSwitch() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertSame(option, commandLine.getOption("foo"));
    }

    @Test
    public void testGetValues() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals(2, values.size());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetSwitchFalse() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertNull(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder("property")
                .withLongOpt("property")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, ""));
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("property")
                .withLongOpt("property")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        commandLine.addProperty(option, properties);
        assertEquals("value", commandLine.getProperty(option, ""));
    }

    @Test
    public void testGetProperties() {
        Option option = Option.builder("property")
                .withLongOpt("property")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        commandLine.addProperty(option, properties);
        Set propertiesSet = commandLine.getProperties(option);
        assertTrue(propertiesSet.contains("property1"));
        assertTrue(propertiesSet.contains("property2"));
    }

    @Test
    public void testToString() {
        Option option1 = OptionBuilder.withLongOpt("foo1")
                .withDescription("Option foo1")
                .isRequired(false)
                .create();
        Option option2 = OptionBuilder.withLongOpt("foo2")
                .withDescription("Option foo2")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        String expected = "foo1 foo2";
        String actual = commandLine.toString();
        assertEquals(expected, actual);

        commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));
        expected = "arg1 arg2 foo1";
        actual = commandLine.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOptions() {
        Option option1 = OptionBuilder.withLongOpt("foo1")
                .withDescription("Option foo1")
                .isRequired(false)
                .create();
        Option option2 = OptionBuilder.withLongOpt("foo2")
                .withDescription("Option foo2")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = OptionBuilder.withLongOpt("foo1")
                .withDescription("Option foo1")
                .isRequired(false)
                .create();
        Option option2 = OptionBuilder.withLongOpt("bar")
                .withDescription("Option bar")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertTrue(optionTriggers.contains("foo1"));
        assertTrue(optionTriggers.contains("bar"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
        assertEquals(2, values.size());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testNormalised() {
        Option option = OptionBuilder.withLongOpt("foo")
                .withDescription("Option foo")
                .isRequired(false)
                .create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        List normalised = commandLine.getNormalised();
        assertEquals(3, normalised.size());
        assertTrue(normalised.contains("arg1"));
        assertTrue(normalised.contains("arg2"));
        assertTrue(normalised.contains("foo"));
    }
}