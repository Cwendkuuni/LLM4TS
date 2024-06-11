package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        commandLine = new WriteableCommandLineImpl(new Option("test", "t", true, "test"), new ArrayList());
        commandLine.addOption(new Option("input", "i", true, "input file"));
        commandLine.addOption(new Option("output", "o", false, "output file"));
        commandLine.addOption(new Option("verbose", "v", false, "verbose output"));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test2", "t2", true, "test2");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("input", "i", true, "input file");
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("i");
        assertSame(option, retrievedOption);
    }

    @Test
    public void testHasOption() {
        Option inputOption = new Option("input", "i", true, "input file");
        commandLine.addOption(inputOption);
        assertTrue(commandLine.hasOption(inputOption));
    }

    @Test
    public void testGetValues() {
        Option inputOption = new Option("input", "i", true, "input file");
        commandLine.addOption(inputOption);

        commandLine.addValue(inputOption, "test1.txt");
        List<String> values = commandLine.getValues(inputOption, null);
        assertEquals(1, values.size());
        assertEquals("test1.txt", values.get(0));

        commandLine.addValue(inputOption, "test2.txt");
        values = commandLine.getValues(inputOption, null);
        assertEquals(2, values.size());
        assertEquals("test1.txt", values.get(0));
        assertEquals("test2.txt", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option inputOption = new Option("input", "i", true, "input file");
        commandLine.addOption(inputOption);

        commandLine.addValue(inputOption, "test1.txt");
        List<String> values = commandLine.getUndefaultedValues(inputOption);
        assertEquals(1, values.size());
        assertEquals("test1.txt", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option verboseOption = new Option("verbose", "v", false, "verbose output");
        commandLine.addOption(verboseOption);
        commandLine.addSwitch(verboseOption, false);

        assertFalse(commandLine.getSwitch(verboseOption, false));
        assertNull(commandLine.getSwitch(verboseOption, true));

        commandLine.addSwitch(verboseOption, true);
        assertTrue(commandLine.getSwitch(verboseOption, true));
    }

    @Test
    public void testToString() {
        Option inputOption = new Option("input", "i", true, "input file");
        Option outputOption = new Option("output", "o", false, "output file");
        Option verboseOption = new Option("verbose", "v", false, "verbose output");

        commandLine.addOption(inputOption);
        commandLine.addOption(outputOption);
        commandLine.addOption(verboseOption);

        commandLine.addValue(inputOption, "test1.txt");
        commandLine.addValue(outputOption, "test2.txt");
        commandLine.addSwitch(verboseOption, true);

        String expected = "-v -i test1.txt -o test2.txt";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option inputOption = new Option("input", "i", true, "input file");
        Option outputOption = new Option("output", "o", false, "output file");
        Option verboseOption = new Option("verbose", "v", false, "verbose output");

        commandLine.addOption(inputOption);
        commandLine.addOption(outputOption);
        commandLine.addOption(verboseOption);

        List<Option> options = commandLine.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(inputOption));
        assertTrue(options.contains(outputOption));
        assertTrue(options.contains(verboseOption));
    }

    @Test
    public void testGetOptionTriggers() {
        Option inputOption = new Option("input", "i", true, "input file");
        Option outputOption = new Option("output", "o", false, "output file");
        Option verboseOption = new Option("verbose", "v", false, "verbose output");

        commandLine.addOption(inputOption);
        commandLine.addOption(outputOption);
        commandLine.addOption(verboseOption);

        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertEquals(3, optionTriggers.size());
        assertTrue(optionTriggers.contains("i"));
        assertTrue(optionTriggers.contains("o"));
        assertTrue(optionTriggers.contains("v"));
    }

    @Test
    public void testSetDefaultValues() {
        Option inputOption = new Option("input", "i", true, "input file");
        commandLine.addOption(inputOption);

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1.txt");
        defaultValues.add("default2.txt");

        commandLine.setDefaultValues(inputOption, defaultValues);

        List<String> values = commandLine.getValues(inputOption, null);
        assertEquals(2, values.size());
        assertEquals("default1.txt", values.get(0));
        assertEquals("default2.txt", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option verboseOption = new Option("verbose", "v", false, "verbose output");
        commandLine.addOption(verboseOption);

        commandLine.setDefaultSwitch(verboseOption, true);

        assertTrue(commandLine.getSwitch(verboseOption, true));
    }

    @Test
    public void testGetNormalised() {
        Option inputOption = new Option("input", "i", true, "input file");
        Option outputOption = new Option("output", "o", false, "output file");
        Option verboseOption = new Option("verbose", "v", false, "verbose output");

        commandLine.addOption(inputOption);
        commandLine.addOption(outputOption);
        commandLine.addOption(verboseOption);

        commandLine.addValue(inputOption, "test1.txt");
        commandLine.addValue(outputOption, "test2.txt");
        commandLine.addSwitch(verboseOption, true);

        List<String> normalised = commandLine.getNormalised();
        assertEquals(4, normalised.size());
        assertEquals("-v", normalised.get(0));
        assertEquals("-i", normalised.get(1));
        assertEquals("test1.txt", normalised.get(2));
        assertEquals("-o", normalised.get(3));
        assertEquals("test2.txt", normalised.get(4));
    }
}