package org.apache.commons.cli2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.base.Option;
import org.apache.commons.cli2.base.OptionBuilder;
import org.apache.commons.cli2.base.OptionType;
import org.apache.commons.cli2.base.ValuesAcceptingOption;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setup() {
        commandLine = mock(WriteableCommandLine.class);
        option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .withType(OptionType.NO_ARGUMENT)
                .isRequired(false)
                .create();
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        verify(commandLine, times(1)).addOption(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNullOption() {
        commandLine.addOption(null);
    }

    @Test
    public void testAddValue() {
        Option valuesOption = OptionBuilder.withLongOpt("valuesOption")
                .withDescription("An option with values")
                .isRequired(false)
                .withValuesSeparator(' ')
                .create(new ValuesAcceptingOption(true));
        commandLine.addOption(valuesOption);

        commandLine.addValue(valuesOption, "value1");
        commandLine.addValue(valuesOption, "value2");
        commandLine.addValue(valuesOption, "value3");

        List<String> values = commandLine.getUndefaultedValues(valuesOption);
        assertEquals(Arrays.asList("value1", "value2", "value3"), values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullOption() {
        Option valuesOption = OptionBuilder.withLongOpt("valuesOption")
                .withDescription("An option with values")
                .isRequired(false)
                .create(new ValuesAcceptingOption(true));
        commandLine.addOption(valuesOption);

        commandLine.addValue(null, "value");
    }

    @Test
    public void testGetUndefaultedValues() {
        Option noValuesOption = OptionBuilder.withLongOpt("noValuesOption")
                .withDescription("An option with no values")
                .isRequired(false)
                .create();
        commandLine.addOption(noValuesOption);

        List<String> values = commandLine.getUndefaultedValues(noValuesOption);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        Option defaultValuesOption = OptionBuilder.withLongOpt("defaultValuesOption")
                .withDescription("An option with default values")
                .isRequired(false)
                .create();
        List<String> defaultValues = Arrays.asList("value1", "value2");

        commandLine.setDefaultValues(defaultValuesOption, defaultValues);

        List<String> values = commandLine.getUndefaultedValues(defaultValuesOption);
        assertEquals(defaultValues, values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesNullOption() {
        List<String> defaultValues = Arrays.asList("value1", "value2");

        commandLine.setDefaultValues(null, defaultValues);
    }

    @Test
    public void testAddSwitch() {
        Option switchOption = OptionBuilder.withLongOpt("switchOption")
                .withDescription("A switch option")
                .isRequired(false)
                .create();

        commandLine.addSwitch(switchOption, true);
        verify(commandLine, times(1)).addSwitch(switchOption, true);

        commandLine.addSwitch(switchOption, false);
        verify(commandLine, times(2)).addSwitch(switchOption, anyBoolean());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option switchOption = OptionBuilder.withLongOpt("switchOption")
                .withDescription("A switch option")
                .isRequired(false)
                .create();

        commandLine.addSwitch(switchOption, true);
        commandLine.addSwitch(switchOption, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option switchOption = OptionBuilder.withLongOpt("switchOption")
                .withDescription("A switch option")
                .isRequired(false)
                .create();

        commandLine.setDefaultSwitch(switchOption, true);
        verify(commandLine, times(1)).setDefaultSwitch(switchOption, true);

        commandLine.setDefaultSwitch(switchOption, false);
        verify(commandLine, times(2)).setDefaultSwitch(switchOption, anyBoolean());
    }

    @Test
    public void testAddProperty() {
        Option propertyOption = OptionBuilder.withLongOpt("propertyOption")
                .withDescription("An option with properties")
                .isRequired(false)
                .create();

        commandLine.addProperty(propertyOption, "property", "value");
        verify(commandLine, times(1)).addProperty(propertyOption, "property", "value");
    }

    @Test
    public void testAddPropertyWithEmptyValue() {
        Option propertyOption = OptionBuilder.withLongOpt("propertyOption")
                .withDescription("An option with properties")
                .isRequired(false)
                .create();

        commandLine.addProperty(propertyOption, "property", "");
        verify(commandLine, times(1)).addProperty(propertyOption, "property", "");
    }

    @Test
    public void testAddPropertyNullOption() {
        commandLine.addProperty(null, "property", "value");
    }

    @Test
    public void testAddPropertyWithNullValue() {
        Option propertyOption = OptionBuilder.withLongOpt("propertyOption")
                .withDescription("An option with properties")
                .isRequired(false)
                .create();

        commandLine.addProperty(propertyOption, "property", null);
        verify(commandLine, times(1)).addProperty(propertyOption, "property", null);
    }

    @Test
    public void testAddPropertyWithEmptyValueAndNullValue() {
        Option propertyOption = OptionBuilder.withLongOpt("propertyOption")
                .withDescription("An option with properties")
                .isRequired(false)
                .create();

        commandLine.addProperty(propertyOption, "property", "");
        commandLine.addProperty(propertyOption, "property", null);
        verify(commandLine, times(2)).addProperty(propertyOption, "property", anyString());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("--"));
        assertFalse(commandLine.looksLikeOption(""));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testGetCurrentOption() {
        Option currentOption = OptionBuilder.withLongOpt("currentOption")
                .withDescription("The current option")
                .isRequired(false)
                .create();

        commandLine.setCurrentOption(currentOption);

        assertEquals(currentOption, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option currentOption = OptionBuilder.withLongOpt("currentOption")
                .withDescription("The current option")
                .isRequired(false)
                .create();

        commandLine.setCurrentOption(currentOption);

        verify(commandLine, times(1)).setCurrentOption(currentOption);
    }

    @Test
    public void testHelpFormatter() {
        Option helpOption = OptionBuilder.withLongOpt("help")
                .withDescription("Show this help message and exit")
                .isRequired(false)
                .create();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", Arrays.asList("--help", "--version"), 1, "1.0", false);

        assertEquals("Test\n" +
                "Options:\n" +
                "--help       Show this help message and exit\n" +
                "--version    Output version information, then exit\n", formatter.getHelpText());

        commandLine.addOption(helpOption);
        commandLine.parse(new String[0]);

        assertTrue(commandLine.hasOption("help"));
    }
}