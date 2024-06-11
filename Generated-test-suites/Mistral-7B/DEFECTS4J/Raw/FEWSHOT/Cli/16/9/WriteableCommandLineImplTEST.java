package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.commandline.WriteableCommandLineTestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {
    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = WriteableCommandLineTestUtil.createOption("option1", true);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        assertEquals(1, options.size());
        Option optionFromList = (Option) options.get(0);
        assertEquals(option, optionFromList);
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = WriteableCommandLineTestUtil.createArg("option1", "value1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = WriteableCommandLineTestUtil.createArg("option1", false);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = WriteableCommandLineTestUtil.createArg("option1", false);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = WriteableCommandLineTestUtil.createPropertyOption("property1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addProperty(option, "value1");
        String propertyValue = writeableCommandLine.getProperty(option, "defaultValue");
        assertEquals("value1", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = WriteableCommandLineTestUtil.createPropertyOption("property1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addProperty(option, "value1");
        Map properties = writeableCommandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("value1", properties.get("property1"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = WriteableCommandLineTestUtil.createArg("option1");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.singletonList("arg1"));
        String commandLineString = writeableCommandLine.toString();
        assertEquals("arg1 option1", commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option1 = WriteableCommandLineTestUtil.createArg("option1");
        Option option2 = WriteableCommandLineTestUtil.createArg("option2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        String trigger = "option1";
        boolean result = writeableCommandLine.looksLikeOption(trigger);
        assertFalse(result);

        Option option = WriteableCommandLineTestUtil.createArg("option1");
        writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        prefixes = option.getPrefixes();
        result = writeableCommandLine.looksLikeOption(trigger);
        assertTrue(result);
    }
}