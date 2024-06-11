package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Test for WriteableCommandLineImpl class.
 */
public class WriteableCommandLineImplTest {

    /**
     * Test method for {@link WriteableCommandLineImpl#WriteableCommandLineImpl(Option, List)} constructor.
     */
    @org.junit.Test
    public void testWriteableCommandLineImplConstruction() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        List arguments = new ArrayList();
        arguments.add("--optionA");
        arguments.add("valueA");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(option, commandLine.getOption("optionA"));
        assertEquals(1, commandLine.getOptions().size());
        assertEquals("valueA", commandLine.getValues(option, null).get(0));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#addOption(Option)} method.
     */
    @org.junit.Test
    public void testAddOption() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);

        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("optionA"));
        assertEquals(1, commandLine.getOptions().size());
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#addValue(Option, Object)} method.
     */
    @org.junit.Test
    public void testAddValue() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addValue(option, "valueA");

        assertEquals("valueA", commandLine.getValues(option, null).get(0));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#addSwitch(Option, boolean)} method.
     */
    @org.junit.Test
    public void testAddSwitch() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#hasOption(Option)} method.
     */
    @org.junit.Test
    public void testHasOption() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getOption(String)} method.
     */
    @org.junit.Test
    public void testGetOption() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addOption(option);

        Option result = commandLine.getOption("optionA");

        assertEquals(option, result);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getValues(Option, List)} method.
     */
    @org.junit.Test
    public void testGetValues() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addValue(option, "valueA");
        commandLine.addValue(option, "valueB");

        List result = commandLine.getValues(option, null);

        assertEquals(2, result.size());
        assertEquals("valueA", result.get(0));
        assertEquals("valueB", result.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getUndefaultedValues(Option)} method.
     */
    @org.junit.Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addValue(option, "valueA");

        List result = commandLine.getUndefaultedValues(option);

        assertEquals(1, result.size());
        assertEquals("valueA", result.get(0));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getSwitch(Option, Boolean)} method.
     */
    @org.junit.Test
    public void testGetSwitch() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addSwitch(option, true);

        Boolean result = commandLine.getSwitch(option, false);

        assertTrue(result);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperty(String)} method.
     */
    @org.junit.Test
    public void testGetProperty() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addProperty(option, "propertyA", "valueA");

        String result = commandLine.getProperty("propertyA");

        assertEquals("valueA", result);
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperties()} method.
     */
    @org.junit.Test
    public void testGetProperties() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addProperty(option, "propertyA", "valueA");
        commandLine.addProperty(option, "propertyB", "valueB");

        Map<String, String> result = commandLine.getProperties();

        assertEquals(2, result.size());
        assertEquals("valueA", result.get("propertyA"));
        assertEquals("valueB", result.get("propertyB"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperties(Option)} method.
     */
    @org.junit.Test
    public void testGetPropertiesForOption() {
        Option option = Option.builder("optionA")
                .longOpt("long-option-a")
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addProperty(option, "propertyA", "valueA");
        commandLine.addProperty(option, "propertyB", "valueB");

        Map<String, String> result = commandLine.getProperties(option);

        assertEquals(2, result.size());
        assertEquals("valueA", result.get("propertyA"));
        assertEquals("valueB", result.get("propertyB"));
    }
}