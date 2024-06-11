package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        Option option1 = new Argument("option1");
        option1.setPreferredName("arg1");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addValue(option1, "value1");
        assertEquals(Arrays.asList("value1"), commandLine.getValues(option1, Arrays.asList("default")));
    }

    @Test
    public void testAddSwitch() {
        Option option1 = new Option("option1");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("option1");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("option1");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption("option1"));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Argument("option1");
        option1.setPreferredName("arg1");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addValue(option1, "value1");
        assertEquals(Arrays.asList("value1"), commandLine.getValues(option1, new ArrayList<>()));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Option("option1");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(""), new ArrayList<>());
        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1", ""));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(""), new ArrayList<>());
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(""), new ArrayList<>());
        commandLine.addOption(new Option("option1"));
        commandLine.addValue(new Argument("arg1"), "value1");
        assertEquals("option1 value1", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        option1.setPreferredName("opt1");
        option2.setPreferredName("opt2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(Arrays.asList("option1", "opt1", "option2", "opt2"), commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Argument("option1");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.setDefaultValues(option1, Arrays.asList("default1"));
        assertEquals(Arrays.asList("default1"), commandLine.getValues(option1, new ArrayList<>()));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Option("option1");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(""), Arrays.asList("arg1", "arg2"));
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
    }

}