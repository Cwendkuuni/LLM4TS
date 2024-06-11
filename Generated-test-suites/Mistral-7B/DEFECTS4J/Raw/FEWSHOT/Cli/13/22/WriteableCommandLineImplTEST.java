package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("opt", "option", false, "this is an option");
        List arguments = new ArrayList();
        arguments.add("--opt");
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(option, writeableCommandLine.getOption("opt"));
        assertEquals(Collections.singletonList("arg1"), writeableCommandLine.getValues(option, null));
        assertEquals(Collections.singletonList("arg2"), writeableCommandLine.getValues(option, Collections.singletonList("arg3")));
        assertNull(writeableCommandLine.getValues(new Option("nonexistent"), null));
        assertFalse(writeableCommandLine.hasOption(new Option("nonexistent")));

        writeableCommandLine.addProperty("property1", "value1");
        assertEquals("value1", writeableCommandLine.getProperty("property1", "defaultValue"));

        Properties properties = writeableCommandLine.getProperties();
        assertEquals(1, properties.size());
        assertEquals("value1", properties.getProperty("property1"));

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("opt"));
    }

    @Test
    public void testWriteableCommandLineImplWithArgument() {
        Option option = new Option("opt", "option", true, "this is an option with argument");
        List arguments = new ArrayList();
        arguments.add("--opt");
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(option, writeableCommandLine.getOption("opt"));
        assertEquals(Arrays.asList("arg1", "arg2"), writeableCommandLine.getValues(option, null));
        assertNull(writeableCommandLine.getValues(new Option("nonexistent"), null));
        assertFalse(writeableCommandLine.hasOption(new Option("nonexistent")));

        writeableCommandLine.addProperty("property1", "value1");
        assertEquals("value1", writeableCommandLine.getProperty("property1", "defaultValue"));

        Properties properties = writeableCommandLine.getProperties();
        assertEquals(1, properties.size());
        assertEquals("value1", properties.getProperty("property1"));

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("opt"));
    }

    @Test
    public void testWriteableCommandLineImplWithSwitch() {
        Option option = new Option("opt", "option", false, "this is an option");
        List arguments = new ArrayList();
        arguments.add("--opt");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine.addSwitch(option, true);

        assertEquals(option, writeableCommandLine.getOption("opt"));
        assertTrue(writeableCommandLine.getSwitch(option, false));

        WriteableCommandLineImpl writeableCommandLine2 = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine2.addSwitch(option, false);

        assertFalse(writeableCommandLine2.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImplWithSwitchTwice() {
        Option option = new Option("opt", "option", false, "this is an option");
        List arguments = new ArrayList();
        arguments.add("--opt");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImplWithDefaultValues() {
        Option option = new Option("opt", "option", true, "this is an option with default value");
        List arguments = new ArrayList();
        arguments.add("--opt");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine.setDefaultValues(option, Collections.singletonList("defaultValue1"));

        assertEquals(Collections.singletonList("defaultValue1"), writeableCommandLine.getValues(option, null));
        assertEquals(Collections.singletonList("defaultValue1"), writeableCommandLine.getValues(option, Collections.emptyList()));

        WriteableCommandLineImpl writeableCommandLine2 = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine2.setDefaultValues(option, null);

        assertNull(writeableCommandLine2.getValues(option, null));
        assertNull(writeableCommandLine2.getValues(option, Collections.emptyList()));
    }

    @Test
    public void testWriteableCommandLineImplWithDefaultSwitches() {
        Option option = new Option("opt", "option", false, "this is an option with default switch");
        List arguments = new ArrayList();
        arguments.add("--opt");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine.setDefaultSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitch(option, false));

        WriteableCommandLineImpl writeableCommandLine2 = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine2.setDefaultSwitch(option, false);

        assertFalse(writeableCommandLine2.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImplWithStringArgument() {
        Option option = new Option("opt", "option", true, "this is an option with string argument");
        List arguments = new ArrayList();
        arguments.add("--opt=arg");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine.addValue(option, "value1");

        assertEquals(Collections.singletonList("value1"), writeableCommandLine.getValues(option, null));

        WriteableCommandLineImpl writeableCommandLine2 = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine2.addValue(option, "value2");

        assertEquals(Collections.singletonList("value2"), writeableCommandLine2.getValues(option, null));

        WriteableCommandLineImpl writeableCommandLine3 = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine3.addValue(option, "value1");
        writeableCommandLine3.addValue(option, "value2");

        assertEquals(Arrays.asList("value1", "value2"), writeableCommandLine3.getValues(option, null));
    }

    @Test
    public void testWriteableCommandLineImplWithListArgument() {
        Option option = new Option("opt", "option", true, "this is an option with list argument");
        List arguments = new ArrayList();
        arguments.add("--opt");
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine.addValue(option, new ArrayList(Arrays.asList(1, 2, 3)));

        assertEquals(new ArrayList(Arrays.asList(1, 2, 3)), writeableCommandLine.getValues(option, null));

        WriteableCommandLineImpl writeableCommandLine2 = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine2.addValue(option, new ArrayList(Arrays.asList(2, 3, 4)));

        assertEquals(new ArrayList(Arrays.asList(2, 3, 4)), writeableCommandLine2.getValues(option, null));

        WriteableCommandLineImpl writeableCommandLine3 = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine3.addValue(option, new ArrayList(Arrays.asList(1, 2, 3)));
        writeableCommandLine3.addValue(option, new ArrayList(Arrays.asList(2, 3, 4)));

        assertEquals(new ArrayList(Arrays.asList(1, 2, 3, 2, 3, 4)),
                writeableCommandLine3.getValues(option, null));
    }

    @Test
    public void testToString() {
        Option option = new Option("opt", "option", false, "this is an option");
        List arguments = new ArrayList();
        arguments.add("--opt");
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine.addValue(option, "value1");

        String toString = writeableCommandLine.toString();
        assertEquals("--opt arg1 arg2 value1", toString);

        WriteableCommandLineImpl writeableCommandLine2 = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine2.addValue(option, "value2");

        String toString2 = writeableCommandLine2.toString();
        assertEquals("--opt arg1 arg2 value2", toString2);

        WriteableCommandLineImpl writeableCommandLine3 = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine3.addValue(option, "value1");
        writeableCommandLine3.addValue(option, "value2");

        String toString3 = writeableCommandLine3.toString();
        assertEquals("--opt arg1 arg2 value1 value2", toString3);
    }
}