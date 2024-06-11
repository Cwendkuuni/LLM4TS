package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = new Option("o", "opt", false, "An option with a short flag");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("v", "val", true, "An option with a value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value");
        List values = writeableCommandLine.getValues(option, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("s", "switch", false, "A switch");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("h", "help", false, "Display this help message.");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = new Option("o", "opt", false, "An option with a short flag");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Option result = writeableCommandLine.getOption("opt");
        assertEquals(option, result);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = new Option("v", "val", true, "An option with a value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, Collections.emptyList());
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = new Option("s", "switch", false, "A switch");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);

        writeableCommandLine.addSwitch(option, false);
        Boolean newSwitchValue = writeableCommandLine.getSwitch(option, true);
        assertFalse(newSwitchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addProperty("property", "value");
        String propertyValue = writeableCommandLine.getProperty("property", "");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addProperty("property", "value");
        String propertyValue = writeableCommandLine.getProperty("property", "");
        assertEquals("value", propertyValue);

        String emptyPropertyValue = writeableCommandLine.getProperty("emptyProperty", "");
        assertEquals("", emptyPropertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");
        Set properties = writeableCommandLine.getProperties();
        assertEquals(2, properties.size());
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertTrue(writeableCommandLine.looksLikeOption("o"));
        assertFalse(writeableCommandLine.looksLikeOption("x"));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_setDefaultSwitch_switchAlreadySet() {
        Option option = new Option("s", "switch", false, "A switch");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.setDefaultSwitch(option, Boolean.FALSE);
    }
}