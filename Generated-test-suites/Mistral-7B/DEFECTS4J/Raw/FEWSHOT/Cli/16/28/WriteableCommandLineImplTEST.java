package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
    @Test
    public void testWriteableCommandLineImpl_whenCreated_thenInstanceIsCreated() {
        // Given
        Option rootOption = new Option("root", "root option", false, "r");
        List args = Arrays.asList("arg1", "arg2", "arg3");

        // When
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, args);

        // Then
        assertEquals("WriteableCommandLineImpl", commandLine.getClass().getSimpleName());
    }

    @Test
    public void testWriteableCommandLineImpl_whenAddOption_thenOptionIsAdded() {
        // Given
        Option rootOption = new Option("root", "root option", false, "r");
        Option option = new Option("option", "option", false, "o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        // When
        commandLine.addOption(option);

        // Then
        assertEquals(1, commandLine.getOptions().size());
        assertSame(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_whenAddValue_thenValueIsAdded() {
        // Given
        Option option = new Argument("argument", "Argument name", true);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root option", false, "r"), Collections.emptyList());

        // When
        commandLine.addValue(option, "value1");

        // Then
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_whenAddSwitch_thenSwitchIsAdded() {
        // Given
        Option option = new Option("option", "option", false, "o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root option", false, "r"), Collections.emptyList());

        // When
        commandLine.addSwitch(option, true);

        // Then
        assertEquals(true, commandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_whenHasOption_thenReturnsTrueIfOptionIsPresent() {
        // Given
        Option option = new Option("option", "option", false, "o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // When
        boolean result = commandLine.hasOption(option);

        // Then
        assertTrue(result);
    }

    @Test
    public void testWriteableCommandLineImpl_whenGetOption_thenReturnsOption() {
        // Given
        Option option = new Option("option", "option", false, "o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // When
        Option result = commandLine.getOption("o");

        // Then
        assertSame(option, result);
    }

    @Test
    public void testWriteableCommandLineImpl_whenGetValues_thenReturnsCorrectValues() {
        // Given
        Option option = new Argument("argument", "Argument name", true);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root option", false, "r"), Arrays.asList(option));
        commandLine.addValue(option, "value1");

        // When
        List values = commandLine.getValues(option, null);

        // Then
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_whenGetUndefaultedValues_thenReturnsEmptyList() {
        // Given
        Option option = new Argument("argument", "Argument name", true);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root option", false, "r"), Collections.emptyList());

        // When
        List values = commandLine.getUndefaultedValues(option);

        // Then
        assertEquals(0, values.size());
    }

    @Test
    public void testWriteableCommandLineImpl_whenGetSwitch_thenReturnsCorrectSwitchValue() {
        // Given
        Option option = new Option("option", "option", false, "o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root option", false, "r"), Collections.emptyList());
        commandLine.addSwitch(option, true);

        // When
        Boolean switchValue = commandLine.getSwitch(option, false);

        // Then
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_whenGetProperty_thenReturnsCorrectPropertyValue() {
        // Given
        Option option = new PropertyOption("property", "Property name", true, "p");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root option", false, "r"), Collections.emptyList());
        commandLine.addProperty(option, "property", "value");

        // When
        String propertyValue = commandLine.getProperty("p");

        // Then
        assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_whenAddProperty_thenPropertyIsAdded() {
        // Given
        Option option = new PropertyOption("property", "Property name", true, "p");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root option", false, "r"), Collections.emptyList());

        // When
        commandLine.addProperty(option, "property", "value");

        // Then
        Properties properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("value", properties.getProperty("property"));
    }

    @Test
    public void testWriteableCommandLineImpl_whenToString_thenCorrectStringRepresentationIsGenerated() {
        // Given
        Option rootOption = new Option("root", "root option", false, "r");
        List args = Arrays.asList("arg1", "arg2", "arg3");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, args);

        // When
        String stringRepresentation = commandLine.toString();

        // Then
        String expected = rootOption.getLongOpt() + " " + String.join(" ", args);
        assertEquals(expected, stringRepresentation);
    }

    @Test
    public void testWriteableCommandLineImpl_whenGetOptions_thenReturnsCorrectOptions() {
        // Given
        Option rootOption = new Option("root", "root option", false, "r");
        Option option1 = new Option("option1", "option1", false, "o1");
        Option option2 = new Option("option2", "option2", false, "o2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        // When
        List options = commandLine.getOptions();

        // Then
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_whenGetOptionTriggers_thenReturnsCorrectOptionTriggers() {
        // Given
        Option rootOption = new Option("root", "root option", false, "r");
        Option option1 = new Option("option1", "option1", false, "o1");
        Option option2 = new Option("option2", "option2", false, "o2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        // When
        Set optionTriggers = commandLine.getOptionTriggers();

        // Then
        assertEquals(3, optionTriggers.size());
        assertTrue(optionTriggers.contains("r"));
        assertTrue(optionTriggers.contains("o1"));
        assertTrue(optionTriggers.contains("o2"));
    }

    @Test
    public void testWriteableCommandLineImpl_whenSetDefaultValues_thenDefaultValuesAreSet() {
        // Given
        Option option = new Argument("argument", "Argument name", true);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root option", false, "r"), Collections.emptyList());
        List defaults = Arrays.asList("default1", "default2");

        // When
        commandLine.setDefaultValues(option, defaults);

        // Then
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_whenSetDefaultSwitch_thenDefaultSwitchIsSet() {
        // Given
        Option option = new Option("option", "option", false, "o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root", "root option", false, "r"), Collections.emptyList());
        Boolean defaultSwitch = true;

        // When
        commandLine.setDefaultSwitch(option, defaultSwitch);

        // Then
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_whenGetNormalised_thenReturnsCorrectNormalisedArguments() {
        // Given
        Option rootOption = new Option("root", "root option", false, "r");
        List args = Arrays.asList("arg1", "arg2", "arg3");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, args);

        // When
        List normalised = commandLine.getNormalised();

        // Then
        assertEquals(args, normalised);
    }
}