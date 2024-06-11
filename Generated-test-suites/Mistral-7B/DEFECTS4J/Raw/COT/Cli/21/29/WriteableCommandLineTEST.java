package org.apache.commons.cli2.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

/**
 * WriteableCommandLineTest class contains junit tests for WriteableCommandLine methods
 */
public class WriteableCommandLineTest {

    /**
     * Test method for {@link WriteableCommandLine#addOption(Option)}
     */
    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = Option.builder("t").longOpt("test").build();
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[]{"-t"});

        commandLine.addOption(option);

        Option addedOption = commandLine.getOption("test");
        assertEquals(option, addedOption);
    }

    /**
     * Test method for {@link WriteableCommandLine#addValue(Option, Object)}
     */
    @Test
    public void testAddValue() {
        Options options = new Options();
        Option option = Option.builder("i").longOpt("input").valueSeparator('=').build();
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[]{"--input=value"});

        commandLine.addValue(option, "testValue");

        assertEquals("testValue", commandLine.getUndefaultedValues(option).get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#getUndefaultedValues(Option)}
     */
    @Test
    public void testGetUndefaultedValues() {
        Options options = new Options();
        Option option = Option.builder("i").longOpt("input").build();
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[]{"--input=value1", "--input=value2"});

        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultValues(Option, List)}
     */
    @Test
    public void testSetDefaultValues() {
        Options options = new Options();
        Option option = Option.builder("i").longOpt("input").build();
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[0]);

        List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
        assertEquals(defaultValues.size(), commandLine.getNumOptions());
        for (String defaultValue : defaultValues) {
            Option optionWithDefaultValue = commandLine.findOption(defaultValue);
            assertSame(option, optionWithDefaultValue);
            assertEquals(defaultValue, optionWithDefaultValue.getValue());
        }
    }

    /**
     * Test method for {@link WriteableCommandLine#addSwitch(Option, boolean)}
     */
    @Test
    public void testAddSwitch() {
        Options options = new Options();
        Option option = Option.builder("d").longOpt("debug").build();

        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[0]);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption("debug"));
        assertTrue(commandLine.isSet("debug"));

        commandLine.addSwitch(option, false);

        assertFalse(commandLine.isSet("debug"));
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultSwitch(Option, Boolean)}
     */
    @Test
    public void testSetDefaultSwitch() {
        Options options = new Options();
        Option option = Option.builder("d").longOpt("debug").build();

        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[0]);

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.isSet("debug"));

        commandLine.setDefaultSwitch(option, false);

        assertFalse(commandLine.isSet("debug"));
    }

    /**
     * Test method for {@link WriteableCommandLine#addProperty(Option, String, String)}
     */
    @Test
    public void testAddProperty() {
        Options options = new Options();
        Option option = Option.builder("p").longOpt("property").build();

        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[0]);

        commandLine.addProperty(option, "name", "value");

        assertEquals("value", commandLine.getOptionValue("property.name"));
    }

    /**
     * Test method for {@link WriteableCommandLine#addProperty(String, String)}
     */
    @Test
    public void testAddPropertyWithShortName() {
        Options options = new Options();
        Option option = Option.builder("p").longOpt("property").build();

        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[0]);

        commandLine.addProperty("p", "name", "value");

        assertEquals("value", commandLine.getOptionValue("property.p"));
    }

    /**
     * Test method for {@link WriteableCommandLine#looksLikeOption(String)}
     */
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(new Option[0], new String[0]);

        assertFalse(commandLine.looksLikeOption(""));
        assertFalse(commandLine.looksLikeOption("value"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
    }

    /**
     * Test method for {@link WriteableCommandLine#getOption(String)}
     */
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[]{"--option"});

        Option foundOption = commandLine.getOption("option");

        assertEquals(option, foundOption);
    }

    /**
     * Test method for {@link WriteableCommandLine#getOption(String)} with non-existent option
     */
    @Test(expected = IllegalStateException.class)
    public void testGetOptionWithNonExistentOption() {
        Options options = new Options();
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[0]);

        commandLine.getOption("nonExistentOption");
    }

    /**
     * Test method for {@link WriteableCommandLine#getValue(String)}
     */
    @Test
    public void testgetValue() {
        Options options = new Options();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[]{"--option=value"});

        String value = commandLine.getValue("option");

        assertEquals("value", value);
    }

    /**
     * Test method for {@link WriteableCommandLine#getValue(String)} with non-existent option
     */
    @Test
    public void testgetValueWithNonExistentOption() {
        Options options = new Options();
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[0]);

        String value = commandLine.getValue("nonExistentOption");

        assertNull(value);
    }

    /**
     * Test method for {@link WriteableCommandLine#getOptionValue(String)}
     */
    @Test
    public void testGetOptionValue() {
        Options options = new Options();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[]{"--option=value"});

        String optionValue = commandLine.getOptionValue("option");

        assertEquals("value", optionValue);
    }

    /**
     * Test method for {@link WriteableCommandLine#getOptionValue(String)} with non-existent property
     */
    @Test
    public void testGetOptionValueWithNonExistentProperty() {
        Options options = new Options();
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[0]);

        String optionValue = commandLine.getOptionValue("option.nonExistentProperty");

        assertNull(optionValue);
    }

    /**
     * Test method for {@link WriteableCommandLine#getOptionValue(String)} with non-existent option
     */
    @Test
    public void testGetOptionValueWithNonExistentOption() {
        Options options = new Options();
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLine commandLine = new CliCommandLineParser().parse(options.toArray(new Option[0]), new String[0]);

        String optionValue = commandLine.getOptionValue("nonExistentOption");

        assertNull(optionValue);
    }
}