package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImplCreation() {
        Option rootOption = new Option("root", "rootOption", true, "Root option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
        assertTrue(commandLine.hasOption(rootOption));
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", "rootOption", true, "Root option description");
        Option option1 = new Option("o1", "option1", false, "Option 1 description");
        Option option2 = new Option("o2", "option2", true, "Option 2 description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
        assertFalse(commandLine.hasOption(new Option("nonexistent", "nonexistent", false, "Nonexistent option description")));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option", false, "Option with value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root"), Arrays.asList());

        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, Arrays.asList("defaultValue")));
        assertEquals(null, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("o", "option", false, "Option with switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root"), Arrays.asList());

        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);

        assertTrue(commandLine.getSwitch(option, null));
        assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "Option with switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root"), Arrays.asList());

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("nonexistent", "nonexistent", false, "Nonexistent option description")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", false, "Option with switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root"), Arrays.asList());

        commandLine.addOption(option);

        Option optionRetrieved = commandLine.getOption("o");
        Option optionRetrievedByName = commandLine.getOption("option");

        assertSame(option, optionRetrieved);
        assertSame(option, optionRetrievedByName);
    }

    @Test
    public void testGetValues() {
        Option option = new Argument("a", "arg", true, "Option with argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root"), Arrays.asList());

        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValues(option, null);

        assertEquals(Arrays.asList("value1", "value2"), values);

        List<String> defaultValues = Arrays.asList("default1", "default2");
        List<String> valuesWithDefaults = commandLine.getValues(option, defaultValues);

        assertEquals(Arrays.asList("value1", "value2", "default1", "default2"), valuesWithDefaults);

        List<String> emptyValues = Arrays.asList();
        List<String> valuesWithEmptyDefaults = commandLine.getValues(option, emptyValues);

        assertEquals(Arrays.asList("value1", "value2"), valuesWithEmptyDefaults);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "option", false, "Option with switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root"), Arrays.asList());

        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
        assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root"), Arrays.asList());

        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Properties properties = commandLine.getProperties();

        assertEquals("value1", properties.getProperty("property1"));
        assertEquals("value2", properties.getProperty("property2"));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root"), Arrays.asList());

        commandLine.addProperty("property1", "value1");

        assertEquals("value1", commandLine.getProperty("property1", "defaultValue"));
        assertEquals("defaultValue", commandLine.getProperty("nonexistentProperty", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root"), Arrays.asList());

        commandLine.addProperty("property1", "value1");

        Set<String> properties = commandLine.getProperties();

        assertTrue(properties.containsAll(Arrays.asList("property1")));
        assertFalse(properties.contains("nonexistentProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root"), Arrays.asList());

        List<String> arguments = Arrays.asList("--option1", "-o2", "--option-with-hyphen", "-root");

        for (String option : arguments) {
            assertTrue(commandLine.looksLikeOption(option));
        }

        assertFalse(commandLine.looksLikeOption("nonexistent"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "option", false, "Option with switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root"), Arrays.asList());

        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertEquals("root -o", commandLine.toString());
    }
}