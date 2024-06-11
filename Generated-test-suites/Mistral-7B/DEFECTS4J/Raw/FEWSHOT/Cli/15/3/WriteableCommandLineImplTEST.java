package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.OptionBuilder;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option opt = OptionBuilder.withLongOpt("test")
                .hasArg(true)
                .withValueSeparator(':')
                .create("test");

        Option propOpt = OptionBuilder.withLongOpt("property")
                .isRequired(false)
                .create(PropertyOption.builder("property").build());

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(propOpt, null);

        writeableCommandLine.addOption(opt);
        writeableCommandLine.addProperty("property", "value");

        assertEquals("value", writeableCommandLine.getProperty("property"));
        assertEquals(1, writeableCommandLine.getOptions().size());
        assertEquals("test", writeableCommandLine.getOptions().get(0).getLongOpt());
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option opt = OptionBuilder.withLongOpt("test")
                .hasArg(true)
                .withValueSeparator(':')
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        writeableCommandLine.addValue(opt, "value1");
        writeableCommandLine.addValue(opt, "value2");

        assertEquals(2, writeableCommandLine.getValues(opt, null).size());
        assertEquals("value1", writeableCommandLine.getValues(opt, null).get(0));
        assertEquals("value2", writeableCommandLine.getValues(opt, null).get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option opt = OptionBuilder.withLongOpt("test")
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        writeableCommandLine.addSwitch(opt, true);

        assertTrue(writeableCommandLine.getSwitch(opt, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_existing() {
        Option opt = OptionBuilder.withLongOpt("test")
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        writeableCommandLine.addSwitch(opt, true);
        writeableCommandLine.addSwitch(opt, true);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option opt = OptionBuilder.withLongOpt("test")
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        assertTrue(writeableCommandLine.hasOption(opt));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option opt = OptionBuilder.withLongOpt("test")
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        Option result = writeableCommandLine.getOption("test");

        assertSame(opt, result);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option opt = OptionBuilder.withLongOpt("test")
                .hasArg(true)
                .withValueSeparator(':')
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        writeableCommandLine.addValue(opt, "value1");
        writeableCommandLine.addValue(opt, "value2");

        List values = writeableCommandLine.getValues(opt, null);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option opt = OptionBuilder.withLongOpt("test")
                .hasArg(true)
                .withValueSeparator(':')
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        writeableCommandLine.addValue(opt, "value1");
        writeableCommandLine.addValue(opt, "value2");

        List values = writeableCommandLine.getUndefaultedValues(opt);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option opt = OptionBuilder.withLongOpt("test")
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        writeableCommandLine.addSwitch(opt, true);

        assertTrue(writeableCommandLine.getSwitch(opt, false));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option propOpt = OptionBuilder.withLongOpt("property")
                .isRequired(false)
                .create(PropertyOption.builder("property").build());

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(propOpt, null);

        writeableCommandLine.addProperty("property", "value");

        assertEquals("value", writeableCommandLine.getProperty("property"));
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option propOpt = OptionBuilder.withLongOpt("property")
                .isRequired(false)
                .create(PropertyOption.builder("property").build());

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(propOpt, null);

        Properties props = new Properties();
        props.setProperty("property", "value");
        writeableCommandLine.addProperty("property", props);

        assertEquals("value", writeableCommandLine.getProperty("property"));
        assertEquals(1, writeableCommandLine.getProperties(propOpt).size());
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option propOpt = OptionBuilder.withLongOpt("property")
                .isRequired(false)
                .create(PropertyOption.builder("property").build());

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(propOpt, null);

        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");

        Map properties = writeableCommandLine.getProperties(propOpt);

        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("property1"));
        assertEquals("value2", properties.get("property2"));
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        assertTrue(writeableCommandLine.looksLikeOption("--test"));
        assertTrue(writeableCommandLine.looksLikeOption("--test="));
        assertTrue(writeableCommandLine.looksLikeOption("--test:"));
        assertFalse(writeableCommandLine.looksLikeOption("test"));
        assertFalse(writeableCommandLine.looksLikeOption("--"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option opt = OptionBuilder.withLongOpt("test")
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        writeableCommandLine.addValue(opt, "value1");
        writeableCommandLine.addValue(opt, "value2");

        assertEquals("test:value1 value2", writeableCommandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option opt1 = OptionBuilder.withLongOpt("test1")
                .create("test1");

        Option opt2 = OptionBuilder.withLongOpt("test2")
                .create("test2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option[] { opt1, opt2 }, null);

        assertEquals(2, writeableCommandLine.getOptions().size());
        assertEquals(opt1, writeableCommandLine.getOptions().get(0));
        assertEquals(opt2, writeableCommandLine.getOptions().get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option opt1 = OptionBuilder.withLongOpt("test1")
                .create("test1");

        Option opt2 = OptionBuilder.withLongOpt("test2")
                .create("test2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option[] { opt1, opt2 }, null);

        Set triggers = writeableCommandLine.getOptionTriggers();

        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("test1"));
        assertTrue(triggers.contains("test2"));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option opt = OptionBuilder.withLongOpt("test")
                .hasArg(true)
                .withValueSeparator(':')
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        List values = new ArrayList();
        values.add("default1");
        values.add("default2");

        writeableCommandLine.setDefaultValues(opt, values);

        assertEquals(values, writeableCommandLine.getValues(opt, null));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option opt = OptionBuilder.withLongOpt("test")
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        writeableCommandLine.setDefaultSwitch(opt, true);

        assertTrue(writeableCommandLine.getSwitch(opt, false));
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option opt = OptionBuilder.withLongOpt("test")
                .create("test");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(opt, null);

        writeableCommandLine.addValue(opt, "value1");
        writeableCommandLine.addValue(opt, "value2");

        List normalised = writeableCommandLine.getNormalised();

        assertEquals(2, normalised.size());
        assertEquals("test:value1", normalised.get(0));
        assertEquals("test:value2", normalised.get(1));
    }
}