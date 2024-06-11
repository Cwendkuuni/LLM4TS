package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    @Test
    public void testCreateInstance() {
        Option rootOption = Option.builder()
                .argName("option")
                .required(false)
                .build();

        List arguments = Arrays.asList("arg1", "arg2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals(rootOption, commandLine.getRootOption());
        assertEquals(arguments, commandLine.getArguments());
        assertTrue(commandLine.getOptions().isEmpty());
        assertTrue(commandLine.getOptionTriggers().isEmpty());
        assertTrue(commandLine.getProperties().isEmpty());
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder()
                .argName("option")
                .required(false)
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption(option));
        assertSame(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = Option.builder()
                .argName("option")
                .required(false)
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder()
                .argName("option")
                .required(false)
                .valueSeparator(' ')
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        assertNull(commandLine.getValues(option, null).get(2));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder()
                .argName("option")
                .required(false)
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder()
                .argName("option")
                .required(false)
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        Option foundOption = commandLine.getOption(option.getPreferredName());
        assertSame(option, foundOption);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder()
                .argName("option")
                .required(false)
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        Set prefixes = commandLine.getPrefixes();
        assertTrue(prefixes.contains(""));
        assertTrue(prefixes.contains("-"));
        assertFalse(prefixes.contains("--"));

        String arg = "--option";
        assertTrue(commandLine.looksLikeOption(arg));

        arg = "-option";
        assertTrue(commandLine.looksLikeOption(arg));

        arg = "-option=";
        assertTrue(commandLine.looksLikeOption(arg));

        arg = "option";
        assertTrue(commandLine.looksLikeOption(arg));

        arg = "option=";
        assertTrue(commandLine.looksLikeOption(arg));

        arg = "-";
        assertFalse(commandLine.looksLikeOption(arg));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Properties properties = commandLine.getProperties();
        assertEquals(2, properties.size());
        assertEquals("value1", properties.getProperty("property1"));
        assertEquals("value2", properties.getProperty("property2"));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addProperty("property1", "value1");

        String property = commandLine.getProperty("property1", "defaultValue");
        assertEquals("value1", property);

        property = commandLine.getProperty("property2", "defaultValue");
        assertEquals("defaultValue", property);
    }

    @Test
    public void testGetOptions() {
        Option option1 = Option.builder()
                .argName("option1")
                .required(false)
                .build();

        Option option2 = Option.builder()
                .argName("option2")
                .required(false)
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Option.builder()
                .argName("option1")
                .required(false)
                .build();
        Option option2 = Option.builder()
                .argName("option2")
                .required(false)
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains(option1.getPreferredName()));
        assertTrue(optionTriggers.contains(option2.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Argument.builder()
                .longOpt("option")
                .argName("value")
                .required(true)
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));

        assertNull(commandLine.getValues(option, null).get(2));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder()
                .longOpt("option")
                .argName("none")
                .required(false)
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);

        defaultSwitch = Boolean.FALSE;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        switchValue = commandLine.getSwitch(option, null);
        assertFalse(switchValue);
    }

    @Test
    public void testToString() {
        String[] args = {"arg1", "arg2", "arg3"};
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Arrays.asList(args));

        String expected = String.join(" ", Arrays.asList(args));
        assertEquals(expected, commandLine.toString());
    }
}