package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("opt").build(), Arrays.asList("arg1", "arg2"));
        Option option = new Option.Builder("test").build();
        commandLine.addOption(option);
        assertEquals(Arrays.asList(option), commandLine.getOptions());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("arg").build(), Arrays.asList("val1", "val2"));
        commandLine.addValue(commandLine.getOption("arg"), "value");
        assertEquals(Arrays.asList("val1", "val2", "value"), commandLine.getValues(commandLine.getOption("arg"), null));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("switch").build(), null);
        commandLine.addSwitch(commandLine.getOption("switch"), true);
        assertTrue(commandLine.getSwitch(commandLine.getOption("switch"), false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("switch").build(), null);
        commandLine.addSwitch(commandLine.getOption("switch"), true);
        commandLine.addSwitch(commandLine.getOption("switch"), false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("opt").build(), Arrays.asList("arg1", "arg2"));
        assertTrue(commandLine.hasOption(commandLine.getOption("opt")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("opt").build(), Arrays.asList("arg1", "arg2"));
        Option option = commandLine.getOption("opt");
        assertEquals(option, commandLine.getOption("opt"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("arg").build(), Arrays.asList("val1", "val2"));
        List values = commandLine.getValues(commandLine.getOption("arg"), null);
        assertEquals(Arrays.asList("val1", "val2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("arg").build(), Arrays.asList("val1", "val2"));
        List values = commandLine.getUndefaultedValues(commandLine.getOption("arg"));
        assertEquals(Arrays.asList(), values);
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("switch").build(), null);
        commandLine.addSwitch(commandLine.getOption("switch"), true);
        assertTrue(commandLine.getSwitch(commandLine.getOption("switch"), false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("prop").build(), null);
        commandLine.addProperty(commandLine.getOption("prop"), "value");
        assertEquals("value", commandLine.getProperty(commandLine.getOption("prop"), ""));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("prop").build(), null);
        commandLine.addProperty(commandLine.getOption("prop"), "key1", "value1");
        commandLine.addProperty(commandLine.getOption("prop"), "key2", "value2");
        Map properties = commandLine.getProperties(commandLine.getOption("prop"));
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("key1"));
        assertEquals("value2", properties.get("key2"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("prop").build(), null);
        commandLine.addProperty(commandLine.getOption("prop"), "key1", "value1");
        commandLine.addProperty(commandLine.getOption("prop"), "key2", "value2");
        Set properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.containsAll(commandLine.getOption("prop").getOptions()));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("opt").build(), Arrays.asList("arg1", "arg2"));
        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option.Builder("opt").withLongOpt("longopt").build(), Arrays.asList("arg1", "arg2"));
        assertTrue(commandLine.looksLikeOption("opt"));
        assertTrue(commandLine.looksLikeOption("longopt"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testGetOptionsList() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder()
                .withLongOpt("longopt")
                .withOption("opt", true, "o")
                .withOption("opt2", 2, "n")
                .withOption("opt3", "str")
                .build(), Arrays.asList("--opt", "--opt2=1", "--longopt", "arg1", "--opt3=str"));
        List<Option> options = commandLine.getOptions();
        assertEquals(3, options.size());
        Option opt = options.get(0);
        assertEquals("o", opt.getOpt());
        assertEquals(true, opt.isRequired());
        assertEquals(Arrays.asList("arg1"), opt.getArgs());
        assertEquals("Option 1", opt.getLongOpt());

        Option opt2 = options.get(1);
        assertEquals("n", opt2.getOpt());
        assertEquals(2, opt2.getValue().intValue());

        Option opt3 = options.get(2);
        assertEquals("str", opt3.getValue());
        assertFalse(opt3.isRequired());
        assertEquals("Option 3", opt3.getLongOpt());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder()
                .withLongOpt("longopt")
                .withOption("opt", true, "o")
                .withOption("opt2", 2, "n")
                .build(), null);
        commandLine.setDefaultValues(commandLine.getOption("opt2"), Arrays.asList("default1", "default2"));
        List values = commandLine.getValues(commandLine.getOption("opt2"), null);
        assertEquals(0, values.size());
        commandLine.addValue(commandLine.getOption("opt2"), "arg1");
        values = commandLine.getValues(commandLine.getOption("opt2"), null);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));

        commandLine.setDefaultValues(commandLine.getOption("opt2"), Arrays.asList("default1", "default2"));
        values = commandLine.getValues(commandLine.getOption("opt2"), null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder()
                .withLongOpt("longopt")
                .withOption("opt", true, "o")
                .build(), null);
        commandLine.setDefaultSwitch(commandLine.getOption("opt"), true);
        assertTrue(commandLine.getSwitch(commandLine.getOption("opt"), false));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionBuilder()
                .withLongOpt("longopt")
                .withOption("opt", true, "o")
                .build(), Arrays.asList("--opt", "--longopt=val"));
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("--opt", normalised.get(0));
        assertEquals("--longopt=val", normalised.get(1));
    }
}