package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEqualsIgnoreCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;

public class WriteableCommandLineImplTest {

    @Test
    public void testCreateNewWriteableCommandLineImpl() {
        Option rootOption = OptionBuilder.withLongOpt("rootOption")
                .withDescription("A root option").create();
        List arguments = Arrays.asList("arg1", "arg2", "arg3");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals("rootOption", commandLine.getOption(0).getLongOpt());
        assertEquals("arg1 arg2 arg3", commandLine.toString());
        assertEquals(Arrays.asList("arg1", "arg2", "arg3"), commandLine.getNormalised());
        assertSame(rootOption, commandLine.getOption(0));
        assertEquals(Collections.singleton(rootOption), commandLine.getOptions());
        assertEquals(Collections.singletonMap("rootOption", rootOption), commandLine.getNameToOption());
    }

    @Test
    public void testAddOption() {
        Option rootOption = OptionBuilder.withLongOpt("rootOption")
                .withDescription("A root option").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .create();

        commandLine.addOption(option);

        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption(option));
        assertEqualsIgnoreCase("option1", commandLine.getOption(0).getLongOpt());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option rootOption = OptionBuilder.withLongOpt("rootOption")
                .withDescription("A root option").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .create();

        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option argOption = OptionBuilder.withLongOpt("argOption")
                .withDescription("An argument option that accepts values")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(argOption);

        commandLine.addValue(argOption, "value1");
        commandLine.addValue(argOption, "value2");
        commandLine.addValue(argOption, "value3");

        List values = commandLine.getValues(argOption, null);
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testAddSwitch() {
        Option switchOption = OptionBuilder.withLongOpt("switchOption")
                .withDescription("A switch option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(switchOption);

        commandLine.addSwitch(switchOption, true);
        assertTrue(commandLine.getSwitch(switchOption, false));
    }

    @Test
    public void testHasOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Option returnedOption = commandLine.getOption("option");

        assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        Option argOption = Option
                .builder("a")
                .longOpt("arg-option")
                .argName("VALUE")
                .valueSeparator(' ')
                .withDescription("An argument option that accepts values")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(argOption);

        commandLine.addValue(argOption, "value1");
        commandLine.addValue(argOption, "value2");

        List values = commandLine.getValues(argOption, null);
        assertEquals(2, values.size());
        assertEquals("value1 value2", values.toString());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option argOption = Option
                .builder("a")
                .longOpt("arg-option")
                .argName("VALUE")
                .valueSeparator(',')
                .withDescription("An argument option that accepts values")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Arrays.asList("value1", "value2"));
        commandLine.addOption(argOption);

        List values = commandLine.getUndefaultedValues(argOption);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option switchOption = OptionBuilder.withLongOpt("switchOption")
                .withDescription("A switch option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(switchOption);

        Boolean switchValue = commandLine.getSwitch(switchOption, false);
        assertNull(switchValue);

        commandLine.addSwitch(switchOption, true);
        switchValue = commandLine.getSwitch(switchOption, false);
        assertTrue(switchValue);
    }

    @Test
    public void testgetProperty() {
        Option propertyOption = OptionBuilder.withLongOpt("propertyOption")
                .withDescription("An option that sets a property")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(propertyOption);

        commandLine.addProperty(propertyOption, "propertyName", "propertyValue");

        assertEquals("propertyValue", commandLine.getProperty(propertyOption, "defaultValue"));
    }

    @Test
    public void testaddProperty() {
        Option propertyOption = OptionBuilder.withLongOpt("propertyOption")
                .withDescription("An option that sets a property")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(propertyOption);

        Properties properties = new Properties();
        properties.setProperty("propertyName1", "propertyValue1");
        properties.setProperty("propertyName2", "propertyValue2");

        commandLine.addProperty(propertyOption, "propertyName1", "propertyValue1");
        commandLine.addProperty(propertyOption, "propertyName2", "propertyValue2");

        Map optionProperties = commandLine.getProperties(propertyOption);
        assertEquals(2, optionProperties.size());
        assertEquals("propertyValue1", optionProperties.get("propertyName1"));
        assertEquals("propertyValue2", optionProperties.get("propertyName2"));

        Set optionPropertyNames = commandLine.getProperties(propertyOption);
        assertEquals(2, optionPropertyNames.size());
        assertTrue(optionPropertyNames.containsAll(Arrays.asList("propertyName1", "propertyName2")));
    }

    @Test
    public void testToString() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals("option=value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptionsList() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggersSet() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("option1", "option2")));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option);

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1 defaultValue2", values.toString());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option switchOption = OptionBuilder.withLongOpt("switchOption")
                .withDescription("A switch option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(switchOption);

        commandLine.setDefaultSwitch(switchOption, true);
        assertTrue(commandLine.getSwitch(switchOption, false));
    }

    @Test
    public void testGetNormalisedList() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());
        commandLine.addOption(option);

        List arguments = Arrays.asList("arg1", "arg2");
        commandLine.setArguments(arguments);

        List normalised = commandLine.getNormalised();
        assertEquals(3, normalised.size());
        assertEquals("option arg1 arg2", normalised.toString());
    }
}