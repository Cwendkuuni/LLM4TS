package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    @Test
    public void testCreateWriteableCommandLine() {
        Option rootOption = new Option("root", "root option", false, "root");
        List arguments = Arrays.asList("--root", "arg1", "--optionA", "val1", "-o", "optB", "--optionB=val2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals(rootOption, writeableCommandLine.getRootOption());
        assertEquals(arguments, writeableCommandLine.getArguments());
        assertEquals(rootOption, writeableCommandLine.getOption(writeableCommandLine.getOption("root").getPreferredName()));
        assertEquals(Collections.unmodifiableList(Arrays.asList(new Option[]{rootOption})), writeableCommandLine.getOptions());
        assertEquals(Collections.unmodifiableSet(Arrays.asList("root")), writeableCommandLine.getOptionTriggers());
        assertEquals(writeableCommandLine.toString(), "--root arg1 --optionA val1 -o optB --optionB val2");
    }

    @Test
    public void testAddOption() {
        Option optionA = new Option("a", "add option A", false, "a");
        Option optionB = new Option("b", "add option B", false, "b");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);

        writeableCommandLine.addOption(optionA);
        writeableCommandLine.addOption(optionB);

        assertEquals(Collections.unmodifiableList(Arrays.asList(new Option[]{optionA, optionB})), writeableCommandLine.getOptions());
        assertEquals(Collections.unmodifiableSet(Arrays.asList("a", "b")), writeableCommandLine.getOptionTriggers());
    }

    @Test
    public void testAddValue() {
        Option optionA = new Option("a", "add option A", false, "a");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(optionA);

        writeableCommandLine.addValue(optionA, "value1");
        writeableCommandLine.addValue(optionA, "value2");

        List values = writeableCommandLine.getValues(optionA, null);
        assertEquals(Collections.unmodifiableList(Arrays.asList("value1", "value2")), values);
    }

    @Test
    public void testAddSwitch() {
        Option optionA = new Option("a", "add option A", false, "a");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(optionA);

        writeableCommandLine.addSwitch(optionA, true);
        assertTrue(writeableCommandLine.getSwitch(optionA, false));

        writeableCommandLine.addSwitch(optionA, false);
        assertFalse(writeableCommandLine.getSwitch(optionA, false));
    }

    @Test
    public void testHasOption() {
        Option optionA = new Option("a", "add option A", false, "a");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(optionA);

        assertTrue(writeableCommandLine.hasOption(optionA));
        assertFalse(writeableCommandLine.hasOption(new Option("b", "add option B", false, "b")));
    }

    @Test
    public void testGetOption() {
        Option optionA = new Option("a", "add option A", false, "a");
        Option optionB = new Option("b", "add option B", false, "b");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(optionA);
        writeableCommandLine.addOption(optionB);

        Option resultOptionA = writeableCommandLine.getOption("a");
        Option resultOptionB = writeableCommandLine.getOption("b");

        assertEquals(optionA, resultOptionA);
        assertEquals(optionB, resultOptionB);
    }

    @Test
    public void testGetValues() {
        Option optionA = new Option("a", "add option A", false, "a");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(optionA);

        writeableCommandLine.addValue(optionA, "value1");
        writeableCommandLine.addValue(optionA, "value2");

        List values = writeableCommandLine.getValues(optionA, null);
        assertEquals(Collections.unmodifiableList(Arrays.asList("value1", "value2")), values);

        List defaultValues = Arrays.asList("default1", "default2");
        values = writeableCommandLine.getValues(optionA, defaultValues);
        assertEquals(Collections.unmodifiableList(Arrays.asList("value1", "value2", "default1", "default2")), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option optionA = new Option("a", "add option A", false, "a");
        Option optionB = new Option("b", "add option B", false, "b");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(optionA);
        writeableCommandLine.addOption(optionB);

        writeableCommandLine.addValue(optionA, "value1");
        writeableCommandLine.addValue(optionA, "value2");
        writeableCommandLine.addValue(optionB, "value3");

        List valuesA = writeableCommandLine.getUndefaultedValues(optionA);
        assertEquals(Collections.unmodifiableList(Arrays.asList("value1", "value2")), valuesA);

        List valuesB = writeableCommandLine.getUndefaultedValues(optionB);
        assertEquals(Collections.EMPTY_LIST, valuesB);
    }

    @Test
    public void testGetSwitch() {
        Option optionA = new Option("a", "add option A", false, "a");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(optionA);

        writeableCommandLine.addSwitch(optionA, true);
        assertTrue(writeableCommandLine.getSwitch(optionA, false));

        writeableCommandLine.addSwitch(optionA, false);
        assertFalse(writeableCommandLine.getSwitch(optionA, false));

        writeableCommandLine.addProperty(new PropertyOption(), "a", "propValue");
        assertTrue(writeableCommandLine.getSwitch(optionA, Boolean.TRUE));
    }

    @Test
    public void testGetProperty() {
        Option optionA = new Option("a", "add option A", false, "a");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(optionA);

        writeableCommandLine.addProperty(optionA, "propertyA", "propValueA");
        assertEquals("propValueA", writeableCommandLine.getProperty("propertyA"));

        writeableCommandLine.addProperty(new PropertyOption(), "propertyB", "propValueB");
        assertEquals("propValueB", writeableCommandLine.getProperty("propertyB"));
    }

    @Test
    public void testGetProperties() {
        Option optionA = new Option("a", "add option A", false, "a");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(optionA);

        Map<String, String> properties = new HashMap<>();
        properties.put("propertyA", "propValueA");
        writeableCommandLine.addProperties(optionA, properties);

        Set<String> propertiesSet = writeableCommandLine.getProperties(optionA);
        assertEquals(properties.keySet(), propertiesSet);

        Map<String, String> defaultProperties = new HashMap<>();
        propertiesSet = writeableCommandLine.getProperties(new PropertyOption());
        assertEquals(defaultProperties.keySet(), propertiesSet);
    }

    @Test
    public void testToString() {
        Option optionA = new Option("a", "add option A", false, "a");
        Option optionB = new Option("b", "add option B", false, "b");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        writeableCommandLine.addOption(optionA);
        writeableCommandLine.addOption(optionB);

        writeableCommandLine.addValue(optionA, "value1");
        writeableCommandLine.addValue(optionA, "value2");
        writeableCommandLine.addValue(optionB, "value3");

        writeableCommandLine.addProperty(optionA, "propertyA", "propValueA");
        writeableCommandLine.addProperty(new PropertyOption(), "propertyB", "propValueB");

        String result = writeableCommandLine.toString();
        String expected = "--a value1 value2 --propertyA propValueA --b value3";
        assertEquals(expected, result);
    }
}