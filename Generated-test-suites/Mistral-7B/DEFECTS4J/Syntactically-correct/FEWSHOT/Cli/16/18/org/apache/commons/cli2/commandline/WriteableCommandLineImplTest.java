package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option1 = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        assertNull(commandLine.getOption("nonexistent"));
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option1, commandLine.getOption("o"));
        assertNull(commandLine.getValues(option1, null));
        assertNull(commandLine.getSwitch(option1, null));
        assertNull(commandLine.getProperty(option1, "nonexistent"));
        assertNull(commandLine.getProperties(option1));
        assertFalse(commandLine.looksLikeOption("o"));
        assertEquals("", commandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImplWithOptions() {
        Option option1 = new Option("o", "output", true, "Output file");
        Option option2 = new Option("i", "input", true, "Input file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList("input.txt"));
        commandLine.addOption(option2);
        commandLine.addValue(option2, "output.txt");
        commandLine.addSwitch(option1, true);
        commandLine.addProperty(option1, "property1", "value1");
        commandLine.addProperty(option1, "property2", "value2");

        assertEquals(2, commandLine.getOptions().size());
        assertEquals(option1, commandLine.getOption("o"));
        assertEquals(option2, commandLine.getOption("i"));
        assertEquals(1, commandLine.getValues(option2, null).size());
        assertEquals("output.txt", commandLine.getValues(option2, null).get(0));
        assertTrue(commandLine.getSwitch(option1, null));
        assertEquals("value1", commandLine.getProperty(option1, "property1"));
        assertEquals("value2", commandLine.getProperty(option1, "property2"));
        assertEquals(2, commandLine.getProperties(option1).size());
        assertTrue(commandLine.looksLikeOption("o"));
        assertEquals("input.txt output.txt", commandLine.toString());
    }
}