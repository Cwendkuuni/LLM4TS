package org.apache.commons.cli.WriteableCommandLineTest;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "file", true, "file option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("file"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("f", "file", true, "file option");
        commandLine.addOption(option);

        commandLine.addValue(option, "test.txt");

        assertTrue(commandLine.hasOptionValue("f", "test.txt"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("f", "file", true, "file option");
        commandLine.addOption(option);

        commandLine.addValue(option, "test1.txt");
        commandLine.addValue(option, "test2.txt");

        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertTrue(commandLine.getUndefaultedValues(option).contains("test1.txt"));
        assertTrue(commandLine.getUndefaultedValues(option).contains("test2.txt"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("f", "file", true, "file option");
        commandLine.addOption(option);

        commandLine.setDefaultValues(option, Arrays.asList("default1.txt", "default2.txt"));

        assertEquals(2, commandLine.getUndefaultedValues(option).size());
        assertTrue(commandLine.getUndefaultedValues(option).contains("default1.txt"));
        assertTrue(commandLine.getUndefaultedValues(option).contains("default2.txt"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v","verbose", false, "verbose option");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasSwitch("v"));
        assertTrue(commandLine.hasSwitch("verbose"));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v","verbose", false, "verbose option");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.hasSwitch("v"));
        assertTrue(commandLine.hasSwitch("verbose"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "property option");
        commandLine.addOption(option);

        commandLine.addProperty(option, "key", "value");

        assertEquals("value", commandLine.getPropertyValue("p", "key"));
    }

    @Test
    public void testAddDefaultProperty() {
        commandLine.addProperty("key", "value");

        assertEquals("value", commandLine.getDefaultPropertyValue("key"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-f"));
        assertTrue(commandLine.looksLikeOption("--file"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {

        @Override
        public void addOption(Option option) {
            // implementation
        }

        @Override
        public void addValue(Option option, Object value) {
            // implementation
        }

        @Override
        public List getUndefaultedValues(Option option) {
            // implementation
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            // implementation
        }

        @Override
        public void addSwitch(Option option, boolean value) throws IllegalStateException {
            // implementation
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            // implementation
        }

        @Override
        public void addProperty(Option option, String property, String value) {
            // implementation
        }

        @Override
        public void addProperty(String property, String value) {
            // implementation
        }

        @Override
        public boolean looksLikeOption(String argument) {
            // implementation
        }

        // other methods
    }
}