package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

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
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "The output file");
        List<String> arguments = new ArrayList<>();
        arguments.add("--output");
        arguments.add("test.txt");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(option, commandLine.getOption("o"));
        assertEquals(Arrays.asList("test.txt"), commandLine.getValues(option, null));
        assertFalse(commandLine.hasOption(new Option("n", "no-output", false, "No output file")));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "The output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "output", true, "The output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "test.txt");

        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertEquals(Collections.singletonList("test.txt"), commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("o", "output", false, "Output a file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addSwitch(option, true);

        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("o", "output", false, "Output a file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "output", true, "The output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("i", "input", false, "The input file")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "The output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        assertEquals(option, commandLine.getOption("o"));
        assertNull(commandLine.getOption("i"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "The output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test.txt", "output2.txt"));

        assertEquals(Arrays.asList("test.txt", "output2.txt"), commandLine.getValues(option, null));
        assertEquals(Collections.emptyList(), commandLine.getValues(option, Collections.singletonList("test.txt")));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "output", true, "The output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test.txt"));

        assertEquals(Arrays.asList("test.txt"), commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "output", false, "Output a file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "A property");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addProperty(option, "name", "value");

        assertEquals("value", commandLine.getProperty(option, "default"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "A property");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addProperty(option, "name1", "value1");
        commandLine.addProperty(option, "name2", "value2");

        Map<String, String> properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("name1"));
        assertEquals("value2", properties.get("name2"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "A property");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonMap("name1", "value1"));

        Map<String, String> properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("value1", properties.get("name1"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "The output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--output", "test.txt", "--output", "output2.txt"));

        assertEquals("--output test.txt --output output2.txt", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("o", "output", true, "The output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(new Option("i", "input", false, "The input file"));

        assertEquals(Arrays.asList(option, new Option("i", "input", false, "The input file")), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("o", "output", true, "The output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(new Option("i", "input", false, "The input file"));

        assertEquals(Arrays.asList("o", "i"), commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "output", true, "The output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultValues(option, Arrays.asList("default1.txt", "default2.txt"));

        assertEquals(Arrays.asList("default1.txt", "default2.txt"), commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("o", "output", false, "Output a file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "output", true, "The output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--output", "test.txt"));

        assertEquals(Arrays.asList("--output", "test.txt"), commandLine.getNormalised());
    }
}