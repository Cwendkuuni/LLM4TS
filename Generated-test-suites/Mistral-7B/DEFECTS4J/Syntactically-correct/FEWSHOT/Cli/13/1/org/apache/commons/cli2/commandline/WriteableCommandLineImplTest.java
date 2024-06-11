package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {
    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file name");
        List arguments = Arrays.asList("input.txt", "--output=output.txt", "--o=out.txt", "");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // test getters
        assertEquals(option, commandLine.getOption("o"));
        assertEquals(arguments, commandLine.getNormalised());
        assertEquals(Collections.singletonMap("output", option), commandLine.getNameToOption());
        assertEquals(Collections.unmodifiableMap(commandLine.getDefaultSwitches()), commandLine.getDefaultSwitches());
        assertEquals(Collections.unmodifiableMap(commandLine.getDefaultValues()), commandLine.getDefaultValues());
        assertEquals(Collections.unmodifiableSet(commandLine.getOptionTriggers()), commandLine.getOptionTriggers());
        assertEquals(Collections.unmodifiableList(commandLine.getOptions()), commandLine.getOptions());
        assertEquals(new Properties(), commandLine.getProperties());

        // test addOption
        Option option2 = new Option("i", "input", true, "Input file name");
        commandLine.addOption(option2);
        assertEquals(Collections.singletonMap("i", option2), commandLine.getNameToOption());
        assertEquals(2, commandLine.getOptions().size());

        // test addValue
        commandLine.addValue(option, "output.txt");
        assertEquals(Arrays.asList("output.txt"), commandLine.getValues(option, null));

        // test addSwitch
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));

        // test hasOption
        assertTrue(commandLine.hasOption(option));

        // test toString
        assertEquals("input.txt --output=output.txt --o=out.txt", commandLine.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_defaultValues() {
        Option option = new Option("o", "output", true, "Output file name");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        List<String> defaults = Arrays.asList("default1.txt", "default2.txt");

        commandLine.setDefaultValues(option, defaults);

        assertEquals(Arrays.asList("default1.txt", "default2.txt"), commandLine.getValues(option, null));
    }

    @Test
    public void testWriteableCommandLineImpl_defaultSwitch() {
        Option option = new Option("o", "output", false, "Output flag");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_emptyNormalised() {
        Option option = new Option("o", "output", true, "Output file name");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        assertNull(commandLine.getValues(option, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_switchAlreadySet() {
        Option option = new Option("o", "output", false, "Output flag");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        Option option = new Option("o", "output", false, "Output flag");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());

        assertFalse(commandLine.looksLikeOption(""));
        assertTrue(commandLine.looksLikeOption("--output"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("--output="));
        assertTrue(commandLine.looksLikeOption("-o="));
    }
}