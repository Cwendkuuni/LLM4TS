package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.PropertyOption;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    @org.junit.Test
    public void testCreateCommandLine() {
        final Option option = new Option("o", "output", true, "output file");
        final List arguments = new ArrayList();
        arguments.add("test");
        arguments.add("--output");
        arguments.add("output.txt");

        final WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getValues(option, Collections.emptyList()).size() == 1);
        assertTrue(commandLine.getSwitch(option, Boolean.FALSE) == null);
    }

    @org.junit.Test
    public void testCreateCommandLineWithSwitch() {
        final Option option = new Option("v", "verbose", false, "verbose output");

        final WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertFalse(commandLine.hasOption(option));
        assertNull(commandLine.getSwitch(option, Boolean.FALSE));

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, Boolean.FALSE) == Boolean.TRUE);
    }

    @org.junit.Test
    public void testCreateCommandLineWithProperty() {
        final Option option = new Option("p", "property", true, "property value");

        final WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertNull(commandLine.getProperty(option, "defaultValue"));

        commandLine.addProperty(option, "propertyName", "propertyValue");

        assertEquals("propertyValue", commandLine.getProperty(option, "propertyName"));
        assertNull(commandLine.getProperty(option, "otherPropertyName"));

        commandLine.addProperty(option, "propertyName2", "propertyValue2");

        Set properties = commandLine.getProperties(option);
        assertTrue(properties.contains("propertyName"));
        assertTrue(properties.contains("propertyName2"));
    }

    @org.junit.Test
    public void testCreateCommandLineWithMixedArgs() {
        // given
        final Option option = new Option("o", "output", true, "output file");
        final Option option2 = new Option("v", "verbose", false, "verbose output");
        final List arguments = new ArrayList();
        arguments.add("test");
        arguments.add("--output");
        arguments.add("output.txt");
        arguments.add("--verbose");

        // when
        final WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // then
        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getValues(option, Collections.emptyList()).size() == 1);
        assertNull(commandLine.getSwitch(option, Boolean.FALSE));

        assertTrue(commandLine.hasOption(option2));
        assertNull(commandLine.getSwitch(option2, Boolean.FALSE));
    }

    @org.junit.Test
    public void testCreateCommandLineWithEmptyOption() {
        final Option option = new Option("o", "output", false, "output file");
        final List arguments = new ArrayList();

        final WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, Boolean.FALSE) == null);
    }

    @org.junit.Test
    public void testCreateCommandLineWithEmptyList() {
        final Option option = new Option("o", "output", true, "output file");
        final WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getValues(option, Collections.emptyList()).isEmpty());
        assertNull(commandLine.getSwitch(option, Boolean.FALSE));
    }

    @org.junit.Test
    public void testCreateCommandLineWithNullOption() {
        final WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertFalse(commandLine.hasOption(null));
    }

    @org.junit.Test
    public void testCreateCommandLineWithNullArgument() {
        final Option option = new Option("o", "output", true, "output file");
        final List arguments = new ArrayList();
        arguments.add(null);
        arguments.add("--output");
        arguments.add("output.txt");

        final WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getValues(option, Collections.emptyList()).size() == 1);
        assertNull(commandLine.getSwitch(option, Boolean.FALSE));
    }
}