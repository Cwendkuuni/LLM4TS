package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImplNewInstance() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        List arguments = new ArrayList();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals(rootOption, commandLine.getRootOption());
        assertEquals(Collections.emptyList(), commandLine.getOptions());
        assertEquals(Collections.emptyMap(), commandLine.getNameToOption());
        assertEquals(Collections.emptyMap(), commandLine.getValues());
        assertEquals(Collections.emptyMap(), commandLine.getSwitches());
        assertEquals(Collections.emptyMap(), commandLine.getDefaultValues());
        assertEquals(Collections.emptyMap(), commandLine.getDefaultSwitches());
        assertEquals(Collections.emptyList(), commandLine.getNormalised());
        assertEquals(Collections.emptySet(), commandLine.getOptionTriggers());
    }

    @Test
    public void testWriteableCommandLineImplAddOption() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        // check that the options were added
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertSame(option1, options.get(0));
        assertSame(option2, options.get(1));

        // check that the name-to-option map was updated
        Map nameToOptionMap = commandLine.getNameToOption();
        assertEquals(2, nameToOptionMap.size());
        assertSame(option1, nameToOptionMap.get("opt1"));
        assertSame(option2, nameToOptionMap.get("opt2"));
    }

    @Test
    public void testWriteableCommandLineImplAddValue() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option option = new Option("opt", "opt", true, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImplAddSwitch() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option option = new Option("opt", "opt", false, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImplAddSwitchTwice() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option option = new Option("opt", "opt", false, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImplHasOption() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
        assertFalse(commandLine.hasOption(new Option("opt3", "opt3", false, "Option 3")));
    }

    @Test
    public void testWriteableCommandLineImplGetOption() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option option = commandLine.getOption("opt1");
        assertSame(option1, option);
    }

    @Test
    public void testWriteableCommandLineImplGetValues() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option option = new Option("opt", "opt", true, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImplGetUndefaultedValues() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option option = new Option("opt", "opt", true, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testWriteableCommandLineImplGetSwitch() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option option = new Option("opt", "opt", false, "Option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);

        switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImplGetProperty() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option propertyOption = new PropertyOption("property", "property", false, "Property");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(propertyOption);

        commandLine.addProperty(propertyOption, "property", "value");

        String propertyValue = commandLine.getProperty("property");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImplGetProperties() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option propertyOption = new PropertyOption("property1", "property1", false, "Property 1");
        Option propertyOption2 = new PropertyOption("property2", "property2", false, "Property 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(propertyOption);
        commandLine.addOption(propertyOption2);

        commandLine.addProperty(propertyOption, "property1", "value1");
        commandLine.addProperty(propertyOption2, "property2", "value2");

        Set properties = commandLine.getProperties(propertyOption);
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property1"));

        properties = commandLine.getProperties(propertyOption2);
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property2"));

        properties = commandLine.getProperties();
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testWriteableCommandLineImplLooksLikeOption() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Iterator prefixes = commandLine.getPrefixes().iterator();

        assertTrue(commandLine.looksLikeOption("opt1"));
        assertTrue(commandLine.looksLikeOption("opt2"));
        assertTrue(commandLine.looksLikeOption("rootOpt")); // root option doesn't have a prefix
        assertFalse(commandLine.looksLikeOption("unknown"));

        String prefix = (String) prefixes.next();
        assertTrue(commandLine.looksLikeOption(prefix + "opt1"));
        assertTrue(commandLine.looksLikeOption(prefix + "opt2"));
    }

    @Test
    public void testWriteableCommandLineImplToString() {
        Option rootOption = new Option("root", "rootOption", false, "Root Option");
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        Option option2 = new Option("opt2", "opt2", false, "Option 2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        commandLine.addSwitch(option2, true);

        String commandLineString = commandLine.toString();
        assertEquals("-root " +
                "-opt1 value1 value2 " +
                "--opt2", commandLineString);
    }
}