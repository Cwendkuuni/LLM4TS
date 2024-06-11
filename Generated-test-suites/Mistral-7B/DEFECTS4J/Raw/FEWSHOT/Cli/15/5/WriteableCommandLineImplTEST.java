package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testCreateWriteableCommandLineInstance() {
        Option rootOption = OptionBuilder.withLongOpt("rootOption")
                .withDescription("The root option")
                .create("rootOption");
        List arguments = Arrays.asList("arg1", "arg2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals(rootOption, writeableCommandLine.getCommandLine());
        assertEquals(arguments, writeableCommandLine.getArguments());
    }

    @Test
    public void testAddOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .create("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        assertEquals(option, writeableCommandLine.getOption("option1"));
    }

    @Test
    public void testAddValue() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .create("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .withValue(false)
                .create("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .withValue(false)
                .create("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .create("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .create("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        Option retrievedOption = writeableCommandLine.getOption("option1");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("option1")
                .longOpt("long-option1")
                .hasArg(true)
                .valueSeparator('=')
                .build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1=arg1");
        writeableCommandLine.addValue(option, "value2=arg2");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(Arrays.asList(Arrays.asList("value1", "arg1"), Arrays.asList("value2", "arg2")), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("option1")
                .longOpt("long-option1")
                .hasArg(true)
                .valueSeparator('=')
                .build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(Collections.emptyList(), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .withValue(false)
                .create("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertFalse(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testgetProperty() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .withValue(new PropertyOption("property", "defaultValue"))
                .build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");
        assertEquals("value", writeableCommandLine.getProperty("property"));
    }

    @Test
    public void testGetProperties() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .withValue(new PropertyOption("property1", "defaultValue1"))
                .withValue(new PropertyOption("property2", "defaultValue2"))
                .build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property1", "propertyValue1");
        Map properties = writeableCommandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.containsKey("property1"));
        assertTrue(properties.containsKey("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        assertTrue(writeableCommandLine.looksLikeOption("--option1"));
    }

    @Test
    public void testToString() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .create("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        String result = writeableCommandLine.toString();
        String expected = "--option1 arg1 arg2";
        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("An option1")
                .create("option1");
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("An option2")
                .create("option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("An option1")
                .create("option1");
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("An option2")
                .create("option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("option1", "option2")));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .create("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(defaultValues, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .withValue(false)
                .create("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.setDefaultSwitch(option, true);
        boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .create("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        List normalised = writeableCommandLine.getNormalised();
        assertEquals(Arrays.asList("--option1", "arg1", "arg2"), normalised);
    }
}