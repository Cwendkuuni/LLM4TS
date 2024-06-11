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
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testCreateWriteableCommandLine() {
        Option option = new Option("o", "output", true, "output file");
        List arguments = Arrays.asList("input.txt", "-o output.txt");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals("output.txt", commandLine.getOption("o").getValue(null)[0]);
        assertEquals(1, commandLine.getOptions().size());
        assertEquals("output", commandLine.getOption("o").getLongOpt());
        assertEquals("output file", commandLine.getOption("o").getDescription());
        assertEquals(true, commandLine.getOption("o").isRequired());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        commandLine.addOption(option);

        assertEquals(1, commandLine.getOptions().size());
        assertEquals("output", commandLine.getOption("o").getLongOpt());
        assertEquals("output file", commandLine.getOption("o").getDescription());
        assertEquals(true, commandLine.getOption("o").isRequired());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");

        List values = commandLine.getValues(option, null);
        assertEquals("output.txt", values.get(0));
        assertEquals(1, values.size());
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("o", "output", false, "output flag");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("o", "output", false, "output flag");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        Option result = commandLine.getOption("o");
        assertSame(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("output1.txt", "output2.txt"));

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("output1.txt", values.get(0));
        assertEquals("output2.txt", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "output", false, "output flag");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertNull(switchValue);
    }

    @Test
    public void testGetSwitchWithValue() {
        Option option = new Option("o", "output", false, "output flag");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        commandLine.addProperty("property1", "value1");
        assertEquals("value1", commandLine.getProperty("property1", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addProperty("property1", "value1");

        Set properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        assertTrue(commandLine.looksLikeOption("o"));
        assertFalse(commandLine.looksLikeOption("x"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("output1.txt", "output2.txt"));

        assertEquals("output1.txt output2.txt", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "option1");
        Option option2 = new Option("o2", "option2", true, "option2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "option1", false, "option1");
        Option option2 = new Option("o2", "option2", true, "option2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("o1", "o2")));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        List defaultValues = Arrays.asList("defaultOutput1.txt", "defaultOutput2.txt");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultOutput1.txt", values.get(0));
        assertEquals("defaultOutput2.txt", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("o", "output", false, "output flag");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.setDefaultSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("input.txt", "-o output.txt"));

        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("input.txt", normalised.get(0));
        assertEquals("-o output.txt", normalised.get(1));
    }
}