package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;

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
    public void testWriteableCommandLineImpl() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Arrays.asList("arg1", "arg2"));

        // test getters
        assertEquals(Collections.singletonList(new Option("a")), commandLine.getOptions());
        assertEquals(Collections.singletonMap("a", new Option("a")), commandLine.getNameToOption());
        assertEquals(Collections.singletonMap("arg1", "arg1"), commandLine.getDefaultValues());
        assertEquals(Collections.singletonMap("a", Boolean.FALSE), commandLine.getDefaultSwitches());
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
        assertEquals(Arrays.asList("--a", "--arg1", "--arg2"), commandLine.getOptionTriggers());
        assertNull(commandLine.getProperty("unknown"));
        assertEquals(Collections.emptyMap(), commandLine.getProperties());
        assertFalse(commandLine.looksLikeOption(""));
        assertEquals("arg1 arg2", commandLine.toString());

        // test methods
        commandLine.addOption(new Option("b"));
        commandLine.addValue(new Option("a"), "value1");
        commandLine.addSwitch(new Option("a"), true);
        commandLine.addProperty(new Option("a"), "property1", "value1");
        commandLine.addProperty(new Option("a"), "property2", "value2");

        assertEquals(Arrays.asList(new Option("a"), new Option("b")), commandLine.getOptions());
        assertEquals(Collections.singletonMap("a", new Option("a")), commandLine.getNameToOption());
        assertEquals(Arrays.asList(Collections.singletonList("value1"), null), commandLine.getValues(new Option("a"), null));
        assertEquals(Arrays.asList(Boolean.TRUE), commandLine.getSwitches(new Option("a")));
        assertEquals(Arrays.asList("property1=value1", "property2=value2"), commandLine.getProperties(new Option("a")));
        assertEquals("value1", commandLine.getProperty(new Option("a"), "property1"));
        assertEquals(1, commandLine.getProperty(new Option("a"), "property1").split("=").length);
        assertEquals("value2", commandLine.getProperty(new Option("a"), "property2"));
        assertEquals(2, commandLine.getProperty(new Option("a"), "property2").split("=").length);
        assertEquals(Collections.singletonMap("a", "value1"), commandLine.getValueMap());
        assertEquals(Collections.singletonMap("a", true), commandLine.getSwitchMap());
        assertEquals(Arrays.asList("--a=value1", "--property1=value1", "--property2=value2"), commandLine.getOptionTriggers());
        assertEquals("arg1 arg2 --a=value1 --property1=value1 --property2=value2", commandLine.toString());

        // test methods with Option arguments
        Option optionA = new Option("a");
        Option optionB = new Option("b");
        commandLine = new WriteableCommandLineImpl(optionA, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(optionB);
        commandLine.addValue(optionA, "value1");
        commandLine.addSwitch(optionA, true);
        commandLine.addProperty(optionA, "property1", "value1");
        commandLine.addProperty(optionB, "property2", "value2");

        assertEquals(Arrays.asList(optionA, optionB), commandLine.getOptions());
        assertEquals(Collections.singletonMap(optionA.getLongOpt(), optionA), commandLine.getNameToOption());
        assertEquals(Arrays.asList(Collections.singletonList("value1"), null), commandLine.getValues(optionA, null));
        assertEquals(Arrays.asList(Boolean.TRUE), commandLine.getSwitches(optionA));
        assertEquals(Collections.singletonMap(optionA.getLongOpt(), Arrays.asList("property1=value1", "property2=value2")), commandLine.getProperties(optionA));
        assertEquals("value1", commandLine.getProperty(optionA, "property1"));
        assertEquals(1, commandLine.getProperty(optionA, "property1").split("=").length);
        assertEquals("value2", commandLine.getProperty(optionB, "property2"));
        assertEquals(2, commandLine.getProperty(optionB, "property2").split("=").length);
        assertEquals(Collections.singletonMap(optionA.getLongOpt(), "value1"), commandLine.getValueMap());
        assertEquals(Collections.singletonMap(optionA.getLongOpt(), true), commandLine.getSwitchMap());
        assertEquals(Arrays.asList(optionA.getOptString(), optionB.getOptString()), commandLine.getOptionTriggers());
        assertEquals("arg1 arg2 --a=value1", commandLine.toString());

        // test methods with PropertyOption arguments
        Option propertyOptionA = new PropertyOption("p", "property");
        commandLine = new WriteableCommandLineImpl(propertyOptionA);
        commandLine.addProperty(propertyOptionA, "property1", "value1");
        commandLine.addProperty(propertyOptionA, "property2", "value2");

        assertEquals(Collections.emptyList(), commandLine.getOptions());
        assertEquals(Collections.singletonMap(propertyOptionA.getLongOpt(), propertyOptionA), commandLine.getNameToOption());
        assertEquals(Arrays.asList(Collections.singletonList("property1=value1"), null), commandLine.getValues(propertyOptionA, null));
        assertEquals(Collections.emptyMap(), commandLine.getSwitches(new Option("a")));
        assertEquals(Collections.singletonMap(propertyOptionA.getLongOpt(), Arrays.asList("property1=value1", "property2=value2")), commandLine.getProperties(propertyOptionA));
        assertEquals("property1=value1", commandLine.getProperty(propertyOptionA, "property1"));
        assertEquals(1, commandLine.getProperty(propertyOptionA, "property1").split("=").length);
        assertEquals("property2=value2", commandLine.getProperty(propertyOptionA, "property2"));
        assertEquals(2, commandLine.getProperty(propertyOptionA, "property2").split("=").length);
        assertEquals(Collections.emptyMap(), commandLine.getValueMap());
        assertEquals(Collections.emptyMap(), commandLine.getSwitchMap());
        assertEquals(Collections.singletonList(propertyOptionA.getLongOpt()), commandLine.getOptionTriggers());
        assertEquals("--" + propertyOptionA.getLongOpt() + "=property1=value1" + (propertyOptionA.hasArg() ? " " + propertyOptionA.getArgName() + " " : ""), commandLine.toString());

        // test methods with Argument arguments
        Argument argument = new Argument("arg");
        commandLine = new WriteableCommandLineImpl(new Option("option", "option", true, argument.getName()));
        commandLine.addValue(commandLine.getOption("option"), "value1");
        commandLine.addValue(commandLine.getOption("option"), "value2");

        assertEquals(Collections.singletonList(new Option("option")), commandLine.getOptions());
        assertEquals(Collections.singletonMap(commandLine.getOption("option").getLongOpt(), commandLine.getOption("option")), commandLine.getNameToOption());
        assertEquals(Collections.singletonList(Arrays.asList("value1", "value2")), commandLine.getValues(commandLine.getOption("option"), null));
        assertEquals(Collections.emptyMap(), commandLine.getSwitches(new Option("a")));
        assertEquals(Collections.emptyMap(), commandLine.getProperties(new Option("a")));
        assertEquals("value1 value2", commandLine.getProperty(commandLine.getOption("option"), null));
        assertEquals(0, commandLine.getProperty(commandLine.getOption("option"), null).split(" ").length);
        assertEquals(null, commandLine.getValueMap().get(argument.getName()));
        assertEquals(Collections.emptyMap(), commandLine.getSwitchMap());
        assertEquals(Collections.singletonList(commandLine.getOption("option").getOptString()), commandLine.getOptionTriggers());
        assertEquals("option value1 value2", commandLine.toString());
    }
}