package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {
    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "option");
        Option argOption = new Option("a", "arg", true, "argument");
        Option switchOption = new Option("s", "switch");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        commandLine.addOption(argOption);
        commandLine.addOption(switchOption);

        commandLine.addValue(argOption, "value1");
        commandLine.addSwitch(switchOption, true);

        assertEquals(2, commandLine.getOptions().size());
        assertEquals("option", commandLine.getOptions().get(0).getLongOpt());
        assertEquals("o", commandLine.getOptions().get(0).getOpt());
        assertEquals("option", commandLine.getOptions().get(0).getName());
        assertFalse(commandLine.getOptions().get(0).isRequired());
        assertFalse(commandLine.getOptions().get(0).getValueSeparatorPresent());
        assertFalse(commandLine.getOptions().get(0).isUnbounded());
        assertEquals(Arrays.asList("value1"), commandLine.getValues(argOption, null));
        assertNull(commandLine.getSwitch(switchOption, null));

        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property", null));
        assertEquals(1, commandLine.getProperties().size());
        assertSame(commandLine, commandLine.setDefaultValues(argOption, Arrays.asList("defaultValue1", "defaultValue2")));
        assertSame(commandLine, commandLine.setDefaultSwitch(switchOption, false));
        assertEquals(Arrays.asList("defaultValue1", "defaultValue2"), commandLine.getValues(argOption, null));
        assertFalse(commandLine.getSwitch(switchOption, null));
    }

    @Test
    public void testWritableCommandLineImplToString() {
        Option option = new Option("o", "option");
        Option argOption = new Option("a", "arg", true, "argument");
        Option switchOption = new Option("s", "switch");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        commandLine.addOption(argOption);
        commandLine.addOption(switchOption);

        commandLine.addValue(argOption, "value1");
        commandLine.addSwitch(switchOption, true);
        commandLine.addProperty("property", "value");

        commandLine.setDefaultValues(argOption, Arrays.asList("defaultValue1", "defaultValue2"));
        commandLine.setDefaultSwitch(switchOption, false);

        assertEquals("arg1 arg2 option value1 arg arg defaultValue1 defaultValue2", commandLine.toString());
    }
}