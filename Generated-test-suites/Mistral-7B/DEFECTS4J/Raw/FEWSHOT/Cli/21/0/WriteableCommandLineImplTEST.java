package org.apache.commons.cli;

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

import org.apache.commons.cli.Argument;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.WriteableCommandLine;
import org.apache.commons.cli.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg()
                .withArgName("arg")
                .create();

        WriteableCommandLine cmd = new WriteableCommandLineImpl(rootOption, Arrays.asList("test", "arg1", "arg2"));

        assertEquals(Arrays.asList("test", "arg1", "arg2"), cmd.getNormalised());
        assertEquals("test", cmd.getOption(0).getPreferredName());
        assertEquals("arg1", cmd.getValues(cmd.getOption(0), null).get(0));
        assertEquals("arg2", cmd.getValues(cmd.getOption(0), null).get(1));
        assertTrue(cmd.hasOption(cmd.getOption(0)));
    }

    @Test
    public void testAddOption() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg()
                .withArgName("arg")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .withArgName("value")
                .create();

        cmd.addOption(option);

        assertEquals(Arrays.asList(rootOption, option), cmd.getOptions());
        assertEquals("option", cmd.getOption(1).getPreferredName());
        assertNull(cmd.getValues(cmd.getOption(1), null));
        assertFalse(cmd.hasOption(cmd.getOption(1)));
    }

    @Test
    public void testAddValue() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg()
                .withArgName("arg")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .withArgName("value")
                .create();

        cmd.addOption(option);

        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");

        List values = cmd.getValues(option, null);

        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testAddSwitch() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg(false)
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create();

        cmd.addOption(option);

        cmd.addSwitch(option, true);

        Boolean switchValue = cmd.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test
    public void testGetOption() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg()
                .withArgName("arg")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Arrays.asList("test", "arg1", "arg2"));

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .withArgName("value")
                .create();

        cmd.addOption(option);

        assertEquals(option, cmd.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg()
                .withArgName("arg")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Arrays.asList("test", "arg1", "arg2"));

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .withArgName("value")
                .create();

        cmd.addOption(option);
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");

        List values = cmd.getValues(option, null);

        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg()
                .withArgName("arg")
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Arrays.asList("test", "arg1", "arg2"));

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .withArgName("value")
                .create();

        cmd.addOption(option);
        cmd.addValue(option, "value1");

        List values = cmd.getUndefaultedValues(option);

        assertEquals(Collections.emptyList(), values);
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg(false)
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create();

        cmd.addOption(option);
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, false);

        Boolean switchValue = cmd.getSwitch(option, false);

        assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg()
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = OptionBuilder.withLongOpt("option")
                .withValueSeparator('=')
                .create();

        cmd.addOption(option);
        cmd.addProperty(option, "key1", "value1");
        cmd.addProperty(option, "key2", "value2");

        String property = cmd.getProperty(option, "key1");

        assertEquals("value1", property);
    }

    @Test
    public void testAddProperty() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg()
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = OptionBuilder.withLongOpt("option")
                .withValueSeparator('=')
                .create();

        cmd.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("key1", "value1");
        properties.setProperty("key2", "value2");

        cmd.addProperty(option, properties);

        Map optionProperties = (Map) cmd.getOptionToProperties().get(option);

        assertEquals(properties, optionProperties);
    }

    @Test
    public void testGetProperties() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg()
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option option = OptionBuilder.withLongOpt("option")
                .withValueSeparator('=')
                .create();

        cmd.addOption(option);
        cmd.addProperty(option, "key1", "value1");
        cmd.addProperty(option, "key2", "value2");

        Map optionProperties = (Map) cmd.getOptionToProperties().get(option);

        Set properties = cmd.getProperties(option);

        assertEquals(optionProperties.keySet(), properties);
    }

    @Test
    public void testToString() {
        Option rootOption = OptionBuilder.withLongOpt("test")
                .hasArg()
                .create();

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, Arrays.asList("test", "arg1", "arg2"));

        assertEquals("test arg1 arg2", cmd.toString());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);

        assertTrue(cmd.looksLikeOption("--test"));
        assertTrue(cmd.looksLikeOption("test"));

        assertFalse(cmd.looksLikeOption("arg1"));
        assertFalse(cmd.looksLikeOption("test arg1"));
    }
}