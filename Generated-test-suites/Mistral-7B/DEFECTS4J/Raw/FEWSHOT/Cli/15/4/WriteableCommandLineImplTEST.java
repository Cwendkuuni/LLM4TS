package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("root", "rootOption", false, "Root option description.");
        List arguments = new ArrayList();
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals(rootOption, commandLine.getOption(rootOption.getPreferredName()));
        assertEquals(Collections.singletonList(rootOption), commandLine.getOptions());
        assertNull(commandLine.getValues(rootOption, null));
        assertFalse(commandLine.hasOption(rootOption));
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", "rootOption", false, "Root option description.");
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption(option.getPreferredName()));
        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testGetValue() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals("value1", commandLine.getValue(option));
        assertEquals("value2", commandLine.getValue(option, "value2"));
        assertNull(commandLine.getValue(option, "nonexistentValue"));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());

        commandLine.addValue(option, "value2");

        values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");

        assertEquals("value", commandLine.getProperty(option, "value"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property", properties);

        Map optionProperties = commandLine.getProperties(option);
        assertEquals(1, optionProperties.size());
        Map propertyMap = (Map) optionProperties.get("property");
        assertEquals(1, propertyMap.size());
        assertEquals("value1", propertyMap.get("property1"));
    }

    @Test
    public void testAddPropertyWithDefaultValue() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "defaultValue");

        Map optionProperties = commandLine.getProperties(option);
        assertEquals(1, optionProperties.size());
        Map propertyMap = (Map) optionProperties.get("property");
        assertEquals(1, propertyMap.size());
        assertEquals("defaultValue", propertyMap.get("property"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        commandLine.addProperty(option, "property", properties);

        Map optionProperties = commandLine.getProperties(option);
        assertEquals(1, optionProperties.size());
        Map propertyMap = (Map) optionProperties.get("property");
        assertEquals(1, propertyMap.size());
        assertEquals("value1", propertyMap.get("property1"));

        Set propertiesSet = commandLine.getProperties(option);
        assertEquals(1, propertiesSet.size());
        assertTrue(propertiesSet.containsAll(propertyMap.keySet()));
    }

    @Test
    public void testGetPropertiesEmpty() {
        Option option = new Option("option", "opt", false, "Option description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option);

        assertEquals(Collections.emptySet(), commandLine.getProperties(option));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("option1", "opt1", false, "Option1 description.");
        Option option2 = new Option("option2", "opt2", false, "Option2 description.");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root", "rootOption", false, "Root option description."), new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        String rootOptionString = commandLine.toString();
        String expectedRootOptionString = "rootOption";
        assertTrue(expectedRootOptionString.startsWith(rootOptionString));

        List normalised = commandLine.getNormalised();
        String expectedNormalisedString = "--option1 <option1> --option2";
        String actualNormalisedString = "";
        for (Object arg : normalised) {
            if (arg instanceof Option) {
                Option option = (Option) arg;
                actualNormalisedString += option.getPreferredName();
                if (option.isRequired()) {
                    actualNormalisedString += " <";
                    actualNormalisedString += option.getArgName();
                    actualNormalisedString += ">";
                }
            } else {
                actualNormalisedString += " " + arg;
            }
            actualNormalisedString += " ";
        }

        assertTrue(expectedNormalisedString.equals(actualNormalisedString));
    }
}