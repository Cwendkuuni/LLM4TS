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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "opt", false, "option");
        List arguments = Arrays.asList("arg1", "arg2", "--opt", "--no-opt", "--opt-val=val1");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // test basic methods
        assertEquals("arg1 arg2 --opt arg2 --no-opt --opt optVal=val1", commandLine.toString());
        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertEquals(Collections.singleton("opt"), commandLine.getOptionTriggers());
        assertEquals(Collections.emptyList(), commandLine.getValues(option, null));
        assertNull(commandLine.getSwitch(option, null));
        assertEquals(Collections.singletonMap("opt", option), commandLine.nameToOption);
        assertEquals(new Properties(), commandLine.properties);
        assertFalse(commandLine.hasOption(null));
        assertNull(commandLine.getOption(null));
        assertNull(commandLine.getValues(null, null));
        assertNull(commandLine.getSwitch(null, null));
        assertEquals(Collections.emptyMap(), commandLine.values);
        assertEquals(Collections.emptyMap(), commandLine.switches);
        assertEquals(Collections.emptyList(), commandLine.normalised);
        assertEquals(Collections.emptySet(), commandLine.prefixes);

        // test setters
        commandLine.setDefaultValues(option, Arrays.asList("defaultVal1"));
        assertEquals(Arrays.asList("defaultVal1"), commandLine.getValues(option, null));
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));

        // test add methods
        Argument arg = new Argument("a", "arg", true, "argument");
        commandLine.addOption(option);
        commandLine.addArgument(arg);
        commandLine.addValue(option, "val2");
        commandLine.addSwitch(option, false);
        commandLine.addProperty("prop", "property");

        List values = commandLine.getValues(option, null);
        assertSame(values, Arrays.asList("val1", "val2"));
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertSame(switchValue, Boolean.FALSE);
        assertEquals("property", commandLine.getProperty("prop", "default"));

        // test iterator
        Iterator<Object> iterator = commandLine.iterator();
        List expected = new ArrayList(commandLine.getNormalised().size());
        while (iterator.hasNext()) {
            expected.add(iterator.next());
        }
        assertEquals(commandLine.getNormalised(), expected);
    }
}