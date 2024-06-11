package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_constructor() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .withDescription("The root option.")
                .create();

        List arguments = Arrays.asList("arg1", "arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals(rootOption, commandLine.getOption(0));
        assertEquals(arguments, commandLine.getNormalised());
        assertTrue(commandLine.getOptionTriggers().contains("root"));
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .withDescription("The root option.")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("An option.")
                .create();

        commandLine.addOption(option1);

        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option1, options.get(0));

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Another option.")
                .create();

        commandLine.addOption(option2);

        options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option with values.")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        values = commandLine.getUndefaultedValues(option);
        assertEquals(Collections.emptyList(), values);
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option with a switch.")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_switchAlreadySet() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option with a switch.")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option.")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option.")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        Option result = commandLine.getOption("option");
        assertEquals(option, result);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option with properties.")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");

        String result = commandLine.getProperty("option.property");
        assertEquals("value", result);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option with properties.")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        Properties properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");

        commandLine.addProperty(option, properties);

        Map result = commandLine.getProperties(option);
        assertEquals(properties, result);
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option.")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Arrays.asList("arg1", "arg2"));

        commandLine.addOption(option);

        String result = commandLine.toString();
        assertTrue(result.contains("arg1"));
        assertTrue(result.contains("arg2"));
        assertTrue(result.contains("option"));
    }
}