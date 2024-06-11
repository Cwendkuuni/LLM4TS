package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        final Option rootOption = new Option("root", true, true, "root option");
        final List arguments = Arrays.asList("--root", "arg1", "--another", "arg2");
        final WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // When

        // test adding an option
        final Option option = new Option("opt", true, true, "option");
        writeableCommandLine.addOption(option);
        final Option addedOption = writeableCommandLine.getOption("opt");
        assertTrue(writeableCommandLine.hasOption(option));
        assertSame(option, addedOption);

        // test adding a value to an option
        writeableCommandLine.addValue(option, "value1");
        assertEquals(Arrays.asList("value1"), writeableCommandLine.getValues(option, null));

        // test adding a switch to an option
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));

        // test getting an option by trigger
        final Option getOption = writeableCommandLine.getOption("opt");
        assertSame(option, getOption);

        // test getting values for an option
        writeableCommandLine.addValue(option, "value2");
        assertEquals(Arrays.asList("value1", "value2"), writeableCommandLine.getValues(option, null));
        assertEquals(Arrays.asList("value2"), writeableCommandLine.getValues(option, Collections.singletonList("value1")));

        // test getting a switch value for an option
        writeableCommandLine.addSwitch(option, false);
        assertFalse(writeableCommandLine.getSwitch(option, true));
        assertNull(writeableCommandLine.getSwitch(option, null));

        // test setting default values for an option
        final List defaultValues = Arrays.asList("default1", "default2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getValues(option, null));

        // test setting a default switch value for an option
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));

        // test getting properties
        writeableCommandLine.addProperty("property", "value");
        assertEquals("value", writeableCommandLine.getProperty("property", null));
        Set properties = writeableCommandLine.getProperties();
        assertTrue(properties.contains("property"));

        // test looking like an option
        assertTrue(writeableCommandLine.looksLikeOption("--opt"));
        assertFalse(writeableCommandLine.looksLikeOption("--another"));

        // test getting normalised arguments
        assertEquals(arguments, writeableCommandLine.getNormalised());

        // test getting options
        assertEquals(Collections.singletonList(option), writeableCommandLine.getOptions());

        // test getting option triggers
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertTrue(optionTriggers.contains("opt"));
        assertTrue(optionTriggers.contains("root"));

        // test setting default values
        writeableCommandLine.setDefaultValues(option, Collections.emptyList());
        assertNull(writeableCommandLine.getValues(option, null));

        // test setting default switch
        writeableCommandLine.setDefaultSwitch(option, null);
        assertNull(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testWriteableCommandLineImplWithArgument() {
        // Given
        final Option rootOption = new Option("root", true, true, "root option");
        final Argument argument = new Argument("arg", true, "description", "arg value");
        final List<String> arguments = Arrays.asList("--root", "arg1", "--arg", "value1");
        final WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // When

        // test adding an argument
        final Argument getArgument = writeableCommandLine.getOption("arg");
        assertSame(argument, getArgument);
        assertTrue(writeableCommandLine.hasOption(argument.getOption()));
        assertEquals("value1", writeableCommandLine.getValues(argument.getOption(), null).get(0));

        // test adding a value to an argument
        writeableCommandLine.addValue(argument.getOption(), "value2");
        assertEquals(Arrays.asList("value1", "value2"), writeableCommandLine.getValues(argument.getOption(), null));
    }
}