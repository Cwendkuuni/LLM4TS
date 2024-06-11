package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.apache.commons.cli2.resource.ResourceConstants.SWITCH_ALREADY_SET;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertEquals(Arrays.asList(option), commandLine.getOptions());
        assertEmpty(commandLine.getOptionTriggers());
        assertNull(commandLine.getProperty("property"));
        assertFalse(commandLine.looksLikeOption("opt"));
        assertEquals("", commandLine.toString());
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        assertEquals(Arrays.asList(option), commandLine.getOptions());
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertNull(commandLine.getProperty("property"));
        assertTrue(commandLine.looksLikeOption("opt"));
        assertEquals("", commandLine.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        assertEquals(Arrays.asList(option), commandLine.getOptions());
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertNull(commandLine.getProperty("property"));
        assertTrue(commandLine.looksLikeOption("opt"));
        assertEquals("", commandLine.toString());
        assertEquals(Arrays.asList("value"), commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertEquals(Arrays.asList(option), commandLine.getOptions());
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertNull(commandLine.getProperty("property"));
        assertTrue(commandLine.looksLikeOption("opt"));
        assertEquals("", commandLine.toString());
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("opt"));
        assertEquals(option, commandLine.getOption("longOpt"));
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        Argument argument = Argument.builder("arg").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList(argument));
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(Arrays.asList(option), commandLine.getOptions());
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertNull(commandLine.getProperty("property"));
        assertTrue(commandLine.looksLikeOption("opt"));
        assertEquals("", commandLine.toString());
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        assertEquals(Arrays.asList(option), commandLine.getOptions());
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertNull(commandLine.getProperty("property"));
        assertTrue(commandLine.looksLikeOption("opt"));
        assertEquals("", commandLine.toString());
        assertTrue(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, Arrays.asList("default value1", "default value2"));

        assertEquals(Arrays.asList(option), commandLine.getOptions());
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertNull(commandLine.getProperty("property"));
        assertTrue(commandLine.looksLikeOption("opt"));
        assertEquals("", commandLine.toString());
        assertEquals(Arrays.asList("default value1", "default value2"), commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);

        assertEquals(Arrays.asList(option), commandLine.getOptions());
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertNull(commandLine.getProperty("property"));
        assertTrue(commandLine.looksLikeOption("opt"));
        assertEquals("", commandLine.toString());
        assertTrue(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addProperty("property", "value");

        assertEquals(0, commandLine.getOptions().size());
        assertEquals(0, commandLine.getOptionTriggers().size());
        assertEquals(Collections.singletonMap("property", "value"), commandLine.getProperties());
        assertNull(commandLine.getProperty("property2"));
        assertFalse(commandLine.looksLikeOption("opt"));
        assertEquals("property=value", commandLine.toString());
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addProperty("property", "value");

        assertEquals(0, commandLine.getOptions().size());
        assertEquals(0, commandLine.getOptionTriggers().size());
        assertEquals("value", commandLine.getProperty("property"));
        assertNull(commandLine.getProperty("property2"));
        assertFalse(commandLine.looksLikeOption("opt"));
        assertEquals("property=value", commandLine.toString());
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertEquals(0, commandLine.getOptions().size());
        assertEquals(2, commandLine.getOptionTriggers().size());
        assertEquals(Collections.singletonMap("property1", "value1"), commandLine.getProperties());
        assertEquals(Collections.singletonMap("property2", "value2"), commandLine.getProperties());
        assertFalse(commandLine.looksLikeOption("opt"));
        assertEquals("property1=value1\nproperty2=value2", commandLine.toString());
    }

    @Test
    public void testToString() {
        Option option = Option.builder("opt").longOpt("longOpt").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addProperty("property", "value");

        assertEquals(Arrays.asList(option), commandLine.getOptions());
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertNull(commandLine.getProperty("property2"));
        assertTrue(commandLine.looksLikeOption("opt"));
        assertEquals("opt\n--opt value1 value2\nproperty=value", commandLine.toString());
    }

    private void assertEmpty(Set set) {
        assertTrue(set.isEmpty());
    }
}