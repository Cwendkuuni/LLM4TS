package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.test.SimpleCommandLine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testCreateInstance() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .withValueSeparator(':')
                .create("root");

        List arguments = new ArrayList();
        arguments.add("arg1:val1:val2");
        arguments.add("arg2");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals("root", cmd.getCommandLine().getCommandLineArgs()[0].getName());
        assertEquals("arg1:val1:val2", cmd.getCommandLine().getCommandLineArgs()[1].getValue());
        assertEquals(2, cmd.getCommandLine().getNumArgs());
    }

    @Test
    public void testAddOption() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .withValueSeparator(':')
                .create("root");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());

        Option option1 = OptionBuilder.withLongOpt("option1")
                .hasArg()
                .withValueSeparator(':')
                .create("option1");

        Option option2 = OptionBuilder.withLongOpt("option2")
                .hasArg()
                .withValueSeparator(':')
                .create("option2");

        cmd.addOption(option1);
        cmd.addOption(option2);

        assertEquals(3, cmd.getOptions().size());
        assertEquals(option1, cmd.getOption("option1"));
        assertEquals(option2, cmd.getOption("option2"));
    }

    @Test
    public void testAddValue() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .withValueSeparator(':')
                .create("root");

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .withValueSeparator(':')
                .create("option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option);

        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        cmd.addValue(option, "value3");

        assertEquals(Collections.singletonList("value1:value2:value3"), getValues(cmd, option));
    }

    @Test
    public void testAddSwitch() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg(false)
                .create("root");

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create("option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option);

        cmd.addSwitch(option, true);
        cmd.addSwitch(option, false);

        assertTrue(cmd.getSwitch(option, Boolean.FALSE));
        assertFalse(cmd.getSwitch(option, Boolean.TRUE));
    }

    @Test
    public void testHasOption() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .create("root");

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .create("option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option);

        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .create("root");

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .create("option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option);

        Option returnedOption = cmd.getOption("option");

        assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .create("root");

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .create("option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option);

        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        cmd.addValue(option, "value3");

        assertEquals(Collections.singletonList("value1:value2:value3"), getValues(cmd, option));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .create("root");

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .create("option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option);

        cmd.addValue(option, "value1");

        assertEquals(Collections.singletonList("value1"), getUndefaultedValues(cmd, option));
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg(false)
                .create("root");

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create("option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option);

        cmd.addSwitch(option, true);

        assertTrue(cmd.getSwitch(option, Boolean.FALSE));
        assertFalse(cmd.getSwitch(option, Boolean.TRUE));
    }

    @Test
    public void testAddProperty() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .create("root");

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create("option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option);

        Properties properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");

        cmd.addProperty(option, "property", properties);

        assertEquals(properties, cmd.getProperties(option));
    }

    @Test
    public void testAddPropertyWithDefaultValue() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .create("root");

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create("option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option);

        Properties properties = new Properties();
        properties.put("property1", "value1");

        cmd.addProperty(option, "property1", "default1");
        cmd.addProperty(option, "property2", properties);

        assertEquals("default1", cmd.getProperty(option, "property1"));
        assertEquals(properties, cmd.getProperties(option));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, new ArrayList());

        assertTrue(cmd.looksLikeOption("--option"));
        assertTrue(cmd.looksLikeOption("--no-option"));
        assertFalse(cmd.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .create("root");

        Option option1 = OptionBuilder.withLongOpt("option1")
                .hasArg()
                .create("option1");
        Option option2 = OptionBuilder.withLongOpt("option2")
                .hasArg()
                .create("option2");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option1);
        cmd.addOption(option2);

        cmd.addValue(option1, "value1");
        cmd.addValue(option1, "value2");

        String result = cmd.toString();

        String expected = "root --option1=value1:value2 option2";

        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .create("root");

        Option option1 = OptionBuilder.withLongOpt("option1")
                .hasArg()
                .create("option1");
        Option option2 = OptionBuilder.withLongOpt("option2")
                .hasArg()
                .create("option2");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option1);
        cmd.addOption(option2);

        assertEquals(Collections.unmodifiableList(Arrays.asList(option1, option2)), cmd.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .create("root");

        Option option1 = OptionBuilder.withLongOpt("option1")
                .hasArg()
                .create("option1");
        Option option2 = OptionBuilder.withLongOpt("option2")
                .hasArg()
                .create("option2");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option1);
        cmd.addOption(option2);

        Set triggers = cmd.getOptionTriggers();

        assertEquals(Collections.unmodifiableSet(Arrays.asList("option1", "option2")), triggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .create("root");

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg()
                .create("option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option);

        List defaults = Arrays.asList("default1", "default2");

        cmd.setDefaultValues(option, defaults);

        assertEquals(defaults, cmd.getDefaultValues(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg(false)
                .create("root");

        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .create("option");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option);

        Boolean defaultSwitch = Boolean.TRUE;

        cmd.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, cmd.getDefaultSwitch(option));
    }

    @Test
    public void testGetNormalised() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg()
                .create("root");

        Option option1 = OptionBuilder.withLongOpt("option1")
                .hasArg()
                .create("option1");
        Option option2 = OptionBuilder.withLongOpt("option2")
                .hasArg()
                .create("option2");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, new ArrayList());
        cmd.addOption(option1);
        cmd.addOption(option2);

        List normalised = cmd.getNormalised();

        assertEquals(Collections.unmodifiableList(Arrays.asList("-root", "--option1", "--option2")), normalised);
    }

    private List<Object> getValues(WriteableCommandLineImpl cmd, Option option) {
        return cmd.getValues(option, null);
    }

    private List<Object> getUndefaultedValues(WriteableCommandLineImpl cmd, Option option) {
        return cmd.getValues(option, cmd.getDefaultValues(option));
    }

    private static OptionBuilder withLongOpt(String longOpt) {
        return OptionBuilder.withLongOpt(longOpt);
    }

    private static OptionBuilder create(String optionName) {
        return OptionBuilder.create(optionName);
    }
}