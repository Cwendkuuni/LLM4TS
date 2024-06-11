package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testCreateWriteableCommandLine() {
        Option rootOption = Option.builder("root")
                .required(false)
                .longOpt("root-long")
                .desc("Root option")
                .build();

        List arguments = Arrays.asList("arg1", "arg2");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals("arg1 arg2", commandLine.toString());
        assertEquals("root", commandLine.getCommandLineName());
        assertEquals(Collections.singleton(rootOption), commandLine.getOptions());
        assertEquals(Collections.emptySet(), commandLine.getOptionTriggers());
        assertEquals(arguments, commandLine.getNormalised());
        assertFalse(commandLine.hasOption(Option.builder("empty").build()));
        assertNull(commandLine.getOption("empty"));
    }

    @Test
    public void testAddOption() {
        Option rootOption = Option.builder("root")
                .required(false)
                .longOpt("root-long")
                .desc("Root option")
                .build();

        Option option1 = Option.builder("option1")
                .longOpt("option1-long")
                .desc("Option 1")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        commandLine.addOption(option1);

        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option1, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("option")
                .longOpt("option-long")
                .argName("arg")
                .hasArg(true)
                .desc("Option with argument")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(Option.UNBOUND, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(1, commandLine.getOptions().size());
        Argument arg1 = (Argument) commandLine.getOptions().get(0);
        assertEquals("value1", arg1.getValue());
        assertEquals("value1", arg1.getValueString());
        assertEquals("value2", arg1.getNextValue().getValueString());

        commandLine.addValue(option, "value3");
        assertEquals(3, arg1.getValues().size());
        assertEquals("value1", arg1.getValues().get(0).getValueString());
        assertEquals("value2", arg1.getValues().get(1).getValueString());
        assertEquals("value3", arg1.getValues().get(2).getValueString());
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("option")
                .longOpt("option-long")
                .desc("Option with switch")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(Option.UNBOUND, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption(option));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, Boolean.FALSE));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        Option option = Option.builder("option")
                .longOpt("option-long")
                .desc("Option with switch")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(Option.UNBOUND, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option rootOption = Option.builder("root")
                .required(false)
                .longOpt("root-long")
                .desc("Root option")
                .build();

        Option option1 = Option.builder("option1")
                .longOpt("option1-long")
                .desc("Option 1")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        commandLine.addOption(option1);

        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(Option.builder("empty").build()));
    }

    @Test
    public void testGetOption() {
        Option rootOption = Option.builder("root")
                .required(false)
                .longOpt("root-long")
                .desc("Root option")
                .build();

        Option option1 = Option.builder("option1")
                .longOpt("option1-long")
                .desc("Option 1")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        commandLine.addOption(option1);

        Option option = commandLine.getOption("option1");
        assertEquals(option1, option);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("option")
                .longOpt("option-long")
                .argName("arg")
                .hasArg(true)
               .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(Option.UNBOUND, Collections.emptyList());
        commandLine.addOption(option);

        Argument arg = new Argument("arg1", "arg1-value");
        commandLine.addValue(option, arg);

        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals(arg, values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("option")
                .longOpt("option-long")
                .desc("Option with switch")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(Option.UNBOUND, Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, Boolean.FALSE);
        assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(Option.UNBOUND, Collections.emptyList());
        commandLine.addProperty("property", "value");

        Map properties = commandLine.getProperties();
        assertEquals("value", properties.getProperty("property"));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(Option.UNBOUND, Collections.emptyList());
        commandLine.addProperty("property", "value");

        String propertyValue = commandLine.getProperty("property");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(Option.UNBOUND, Collections.emptyList());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Set properties = commandLine.getProperties();
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        Option rootOption = Option.builder("root")
                .required(false)
                .longOpt("root-long")
                .desc("Root option")
                .build();

        Option option1 = Option.builder("option1")
                .longOpt("option1-long")
                .argName("arg1")
                .hasArg(true)
                .desc("Option 1 with argument")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option1);
        commandLine.addValue(option1, new Argument("arg1", "value1"));
        commandLine.addValue(option1, new Argument("arg2", "value2"));

        String toString = commandLine.toString();
        assertEquals("root arg1=\"value1\" arg2=\"value2\"", toString);
    }
}