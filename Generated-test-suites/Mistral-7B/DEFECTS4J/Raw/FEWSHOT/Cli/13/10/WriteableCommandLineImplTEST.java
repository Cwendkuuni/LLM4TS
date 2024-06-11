package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertIterablesEqual;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImplCreation() {
        Option rootOption = new Option("root", "rootOption", false, "Description for rootOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        assertEquals(rootOption, commandLine.getOption("root"));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOption("opt"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        assertEquals(1, commandLine.getOptions().size());
        assertEquals("value1", commandLine.getValues(option, Collections.emptyList()).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("opt"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property", ""));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property", ""));
        assertEquals(1, commandLine.getProperties().size());
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set properties = commandLine.getProperties();
        assertEquals(2, properties.size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("--nooption"));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDefaultValuesNull() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, null);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(2, commandLine.getValues(option, Collections.emptyList()).size());
        assertEquals("default1", commandLine.getValues(option, Collections.emptyList()).get(0));
        assertEquals("default2", commandLine.getValues(option, Collections.emptyList()).get(1));
    }

    @Test
    public void testSetDefaultSwitchNull() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, null);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, Boolean.TRUE);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("option", "opt", false, "Description for opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}