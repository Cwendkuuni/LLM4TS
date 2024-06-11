package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testCreateNewInstance() {
        final Option rootOption = new Option("root", "root option");
        final List arguments = Arrays.asList("arg1", "arg2");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals("root option", cmdLine.getRootOption().getLongOpt());
        assertEquals("root", cmdLine.getRootOption().getOpt());
        assertEquals(new HashSet<>(Arrays.asList("root")), cmdLine.getOptionTriggers());
        assertEquals(arguments, cmdLine.getNormalised());
    }

    @Test
    public void testAddOption() {
        final Option rootOption = new Option("root", "root option");
        final List arguments = Arrays.asList("arg1", "arg2");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, arguments);

        final Option option = new Option("opt", "option");
        cmdLine.addOption(option);

        final Option addedOption = cmdLine.getOption("opt");
        assertEquals("opt", addedOption.getOpt());
        assertEquals("option", addedOption.getLongOpt());
        assertSame(option, addedOption);
        assertTrue(cmdLine.hasOption(option));
        assertTrue(cmdLine.getOptions().contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        final Option rootOption = new Option("root", "root option");
        final List arguments = Arrays.asList("arg1", "arg2");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, arguments);

        final Option option = new Option("opt", "option");
        cmdLine.addOption(option);
        cmdLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        final Option option = new Option("opt", "option");
        final List arguments = Arrays.asList("arg1", "arg2", "arg3");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("root", "root option"), arguments);
        cmdLine.addOption(option);

        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");

        final List values = cmdLine.getValues(option, Collections.EMPTY_LIST);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        final Option option = new Option("opt", "option");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("root", "root option"), Collections.emptyList());
        cmdLine.addOption(option);

        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, null));
    }

    @Test
    public void testGetOption() {
        final Option rootOption = new Option("root", "root option");
        final Option option1 = new Option("opt1", "option 1");
        final Option option2 = new Option("opt2", "option 2");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        final Option result = cmdLine.getOption("opt2");
        assertSame(option2, result);
    }

    @Test
    public void testGetValues() {
        final Option option = new Option("opt", "option");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("root", "root option"), Arrays.asList("arg1", "arg2"));
        cmdLine.addOption(option);

        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");

        final List result = cmdLine.getValues(option, Collections.emptyList());
        assertEquals(2, result.size());
        assertEquals("value1", result.get(0));
        assertEquals("value2", result.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        final Option option = new Option("opt", "option");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("root", "root option"), Arrays.asList("arg1", "arg2"));
        cmdLine.addOption(option);

        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");

        final List result = cmdLine.getUndefaultedValues(option);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetSwitch() {
        final Option option = new Option("opt", "option");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("root", "root option"), Collections.emptyList());
        cmdLine.addOption(option);

        cmdLine.addSwitch(option, true);

        final Boolean result = cmdLine.getSwitch(option, false);
        assertTrue(result);
    }

    @Test
    public void testGetProperties() {
        final Option option = new Option("opt", "option");
        final Map<String, String> properties = new HashMap<>();
        properties.put("prop1", "val1");
        properties.put("prop2", "val2");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("root", "root option"), Collections.emptyList());
        cmdLine.addOption(option);
        cmdLine.addProperty(option, "prop1", "prop1Value");
        cmdLine.addProperty(option, "prop2", "prop2Value");

        final Map result = cmdLine.getProperties(option);
        assertEquals(2, result.size());
        assertEquals("prop1Value", result.get("prop1"));
        assertEquals("prop2Value", result.get("prop2"));
    }

    @Test
    public void testGetProperty() {
        final Option option = new Option("opt", "option");
        final Map<String, String> properties = new HashMap<>();
        properties.put("prop1", "val1");
        properties.put("prop2", "val2");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("root", "root option"), Collections.emptyList());
        cmdLine.addOption(option);
        cmdLine.addProperty(option, "prop1", "prop1Value");

        final String result = cmdLine.getProperty(option, "prop1");
        assertEquals("prop1Value", result);
    }

    @Test
    public void testToString() {
        final Option rootOption = new Option("root", "root option");
        final Option option1 = new Option("opt1", "option 1");
        final Option option2 = new Option("opt2", "option 2");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        final String result = cmdLine.toString();
        assertEquals("arg1 arg2", result);
    }

    @Test
    public void testGetOptionTriggers() {
        final Option rootOption = new Option("root", "root option");
        final Option option1 = new Option("opt1", "option 1");
        final Option option2 = new Option("opt2", "option 2");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1"));
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        final Set result = cmdLine.getOptionTriggers();
        assertEquals(3, result.size());
        assertTrue(result.contains("opt1"));
        assertTrue(result.contains("opt2"));
        assertTrue(result.contains("root"));
    }

    @Test
    public void testHasOption() {
        final Option rootOption = new Option("root", "root option");
        final Option option = new Option("opt", "option");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmdLine.addOption(option);

        assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testSetDefaultValues() {
        final Option option = new Option("opt", "option");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("root", "root option"), Collections.emptyList());
        cmdLine.addOption(option);

        final List defaults = Arrays.asList("default1", "default2");
        cmdLine.setDefaultValues(option, defaults);

        final List result = cmdLine.getValues(option, Collections.emptyList());
        assertEquals(2, result.size());
        assertEquals("default1", result.get(0));
        assertEquals("default2", result.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        final Option option = new Option("opt", "option");
        final WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("root", "root option"), Collections.emptyList());
        cmdLine.addOption(option);

        cmdLine.setDefaultSwitch(option, true);

        final Boolean result = cmdLine.getSwitch(option, false);
        assertTrue(result);
    }
}