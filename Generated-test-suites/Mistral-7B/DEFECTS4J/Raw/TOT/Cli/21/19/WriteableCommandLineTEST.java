package org.apache.commons.cli2.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.BaseOption;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineTest {

    @org.junit.Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[]{"--test"});

        Option option = new Option("t", "test", false, "Test mode");
        commandLine.addOption(option);

        assertNotNull(commandLine.getOption("t"));
        assertTrue(commandLine.hasOption("test"));
    }

    @org.junit.Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[]{"--dir", "test"});

        Option option = new Option("d", "dir", 1, "Directory");
        commandLine.addOption(option);
        commandLine.addValue(option, "newDir");

        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains("test"));
        assertTrue(values.contains("newDir"));
    }

    @org.junit.Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[]{"--file", "file1", "--file", "file2"});

        Option option = new Option("f", "file", 1, "File");
        commandLine.addOption(option);

        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains("file1"));
        assertTrue(values.contains("file2"));
    }

    @org.junit.Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[0]);

        Option option = new Option("i", "input", 1, "Input files", Arrays.asList("file1.txt", "file2.txt"));
        commandLine.setDefaultValues(option, Arrays.asList("input.txt", "input2.txt"));

        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
        List defaults = commandLine.getOptionDefaultValues(option);
        assertTrue(defaults.contains("input.txt"));
        assertTrue(defaults.contains("input2.txt"));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[0]);

        Option option = new Option("v", "verbose", false, "Verbose mode");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @org.junit.Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[0]);

        Option option = new Option("v", "verbose", false, "Verbose mode");
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.hasOption("verbose"));
        assertTrue(commandLine.isSet("verbose"));
    }

    @org.junit.Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[0]);

        Option option = new Option("p", "prefix", true, "Prefix");
        commandLine.addProperty(option, "test");

        assertNull(commandLine.getOption("prefix"));
        assertEquals("test", commandLine.getOption("p").getValue());
    }

    @org.junit.Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[0]);

        commandLine.addProperty("prefix", "default");

        assertNull(commandLine.getProperty("prefix"));
        assertEquals("default", commandLine.getProperty("prefix"));
    }

    @org.junit.Test
    public void testLooksLikeOption() {
        assertTrue(WriteableCommandLine.looksLikeOption("--test"));
        assertTrue(WriteableCommandLine.looksLikeOption("-t"));
        assertFalse(WriteableCommandLine.looksLikeOption("--testvalue"));
        assertFalse(WriteableCommandLine.looksLikeOption("testvalue"));
        assertFalse(WriteableCommandLine.looksLikeOption("test"));
    }

    @org.junit.Test
    public void testCurrentOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(new String[]{"--test", "--file", "test.txt"});
        Option testOption = new Option("t", "test", false, "Test mode");
        Option fileOption = new Option("f", "file", 1, "File");

        commandLine.setCurrentOption(testOption);
        assertFalse(commandLine.hasOption("file"));
        assertFalse(commandLine.isSet("file"));

        commandLine.setCurrentOption(fileOption);
        assertTrue(commandLine.hasOption("file"));
        assertTrue(commandLine.isSet("file"));
        assertEquals("test.txt", commandLine.getOptionValue("file"));
    }
}