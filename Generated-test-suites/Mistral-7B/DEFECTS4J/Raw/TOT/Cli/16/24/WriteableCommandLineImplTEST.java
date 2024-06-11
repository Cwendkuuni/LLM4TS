package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("o", "output", true, "Define output file")
                .required(false)
                .longOpt("outputFile")
                .defaultValue("output.txt")
                .desc("Define output file");
        Option option2 = new Option("i", "input", true, "Define input file")
                .required(true)
                .longOpt("inputFile")
                .desc("Define input file");
        Option option3 = new Option("v", "verbose", false, "Print detailed output")
                .longOpt("verbose")
                .desc("Print detailed output");

        List arguments = new ArrayList();
        arguments.add("test");
        arguments.add("--input");
        arguments.add("input.txt");
        arguments.add("--output");
        arguments.add("output.txt");
        arguments.add("--verbose");

        commandLine = new WriteableCommandLineImpl(null, arguments);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
    }

    @Test
    public void testNormalisedArguments() {
        List normalised = commandLine.getNormalised();
        assertEquals("--input input.txt", normalised.get(1).toString());
        assertEquals("--output output.txt", normalised.get(2).toString());
        assertEquals("--verbose", normalised.get(3).toString());
        assertEquals("test", normalised.get(0).toString());
    }

    @Test(expected = ParseException.class)
    public void testMissingInputFile() {
        List arguments = new ArrayList();
        arguments.add("test");
        arguments.add("--output");
        arguments.add("output.txt");

        try {
            commandLine = new WriteableCommandLineImpl(null, arguments);
            commandLine.addOption(option2);
        } catch (ParseException e) {
            assertEquals("Missing required option: -i,--input", e.getMessage());
        }
    }

    @Test
    public void testDefaultValue() {
        assertEquals("output.txt", commandLine.getProperty("outputFile"));
    }

    @Test
    public void testSetDefaultValue() {
        commandLine.setDefaultValues(option1, new ArrayList() {{
            add("output2.txt");
        }});
        assertEquals("output2.txt", commandLine.getProperty("outputFile", "output.txt"));
    }

    @Test
    public void testSwitchValue() {
        commandLine.addSwitch(option3, true);
        assertTrue(commandLine.getSwitch(option3, false));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("outputFile");
        assertEquals(option1, option);
    }

    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }

    @Test
    public void testGetProperty() {
        assertEquals("", commandLine.getProperty("nonexistentProperty"));
    }

    @Test
    public void testGetProperties() {
        Set properties = commandLine.getProperties();
        assertEquals(3, properties.size());
        assertTrue(properties.containsAll(option1.getOptions()));
        assertTrue(properties.containsAll(option2.getOptions()));
        assertTrue(properties.containsAll(option3.getOptions()));
    }
}