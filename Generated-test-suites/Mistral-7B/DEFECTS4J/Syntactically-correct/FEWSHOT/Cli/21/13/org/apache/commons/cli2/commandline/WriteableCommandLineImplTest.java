package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg(true)
                .withDescription("root option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        assertEquals(rootOption, commandLine.getOption(0));
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
        assertEquals(Collections.singletonMap("root", rootOption), commandLine.getNameToOption());
        assertNull(commandLine.getValue(rootOption, null));
        assertFalse(commandLine.hasSwitch("root"));
        assertFalse(commandLine.hasOption(OptionBuilder.withLongOpt("non-existent").create()));
        assertTrue(commandLine.looksLikeOption("root"));
        assertEquals("arg1 arg2", commandLine.toString());
        assertEquals(Collections.unmodifiableList(Arrays.asList(rootOption)), commandLine.getOptions());
        assertEquals(Collections.unmodifiableSet(Collections.singleton("root")), commandLine.getOptionTriggers());
    }

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option rootOption = OptionBuilder.withLongOpt("root")
                .hasArg(true)
                .withDescription("root option")
                .create();

        Option childOption = OptionBuilder.withLongOpt("child")
                .hasArg(true)
                .withDescription("child option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList());

        commandLine.addOption(childOption);

        assertEquals(childOption, commandLine.getOption(1));
        assertEquals(Arrays.asList(rootOption, childOption), commandLine.getOptions());
        assertEquals(Arrays.asList("root", "child"), commandLine.getOptionTriggers());

        for (Iterator<String> i = commandLine.getPrefixes().iterator(); i.hasNext();) {
            assertEquals("--", i.next());
        }

        Option parentOption = OptionBuilder.withLongOpt("parent")
                .create();
        commandLine.addOption(parentOption);
        assertEquals(Arrays.asList(rootOption, childOption, parentOption), commandLine.getOptions());
        assertEquals(Arrays.asList("root", "child", "parent"), commandLine.getOptionTriggers());
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .withDescription("an option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .withDescription("an option with a switch")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_switchAlreadySet() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .withDescription("an option with a switch")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .withDescription("an option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .withDescription("an option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .withDescription("an option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .withDescription("an option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Arrays.asList("value1", "value2"));

        assertEquals(Collections.emptyList(), commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .withDescription("an option with a switch")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .withDescription("an option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");

        assertEquals("value", commandLine.getProperty(option, "default"));
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .withDescription("an option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        commandLine.addProperties(option, properties);

        Map<String, String> expected = new HashMap<>();
        expected.put("property", "value");
        expected.putAll(properties);

        assertEquals(expected, commandLine.getProperties(option));
    }

    @Test
    public void testWriteableCommandLineImpl_addProperties() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .withDescription("an option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        commandLine.addOption(option);
        commandLine.addProperties(option, properties);

        Map<String, String> expected = new HashMap<>();
        expected.putAll(properties);

        assertEquals(expected, commandLine.getProperties(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .withDescription("an option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        commandLine.addOption(option);
        commandLine.addProperties(option, properties);

        assertEquals(Collections.unmodifiableMap(properties), commandLine.getProperties(option));
        assertEquals(Collections.unmodifiableMap(properties), commandLine.getProperties());
    }

    @Test
    public void testWriteableCommandLineImpl_getValues_withDefaultValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .withDescription("an option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("default");

        assertEquals(expected, commandLine.getValues(option, Arrays.asList("default")));
    }

    @Test
    public void testWriteableCommandLineImpl_getValues_noDefaultValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(true)
                .withDescription("an option")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List<String> expected = Arrays.asList("value1");

        assertEquals(expected, commandLine.getValues(option, null));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch_withDefaultSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .withDescription("an option with a switch")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);

        Boolean expected = Boolean.TRUE;

        assertEquals(expected, commandLine.getSwitch(option, null));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch_noDefaultSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .hasArg(false)
                .withDescription("an option with a switch")
                .create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(option);

        Boolean expected = Boolean.FALSE;

        assertEquals(expected, commandLine.getSwitch(option, null));
    }
}