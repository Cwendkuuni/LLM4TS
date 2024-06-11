import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "opt", false, "Option test option");
        Option argumentOption = new Option("a", "arg", true, "Option test argument");

        List arguments = new ArrayList();
        arguments.add("test");
        arguments.add("argValue");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // test option addition
        commandLine.addOption(option);
        commandLine.addOption(argumentOption);
        assertTrue(commandLine.getOptions().contains(option));
        assertTrue(commandLine.getOptions().contains(argumentOption));

        // test option value addition
        commandLine.addValue(option, "value1");
        assertEquals(Collections.singletonList("value1"), commandLine.getValues(option, new ArrayList()));

        commandLine.addValue(option, "value2");
        assertEquals(Collections.unmodifiableList(Arrays.asList("value1", "value2")), commandLine.getValues(option, new ArrayList()));

        commandLine.addValue(argumentOption, "argValue1");
        assertEquals(Collections.singletonList("argValue1"), commandLine.getValues(argumentOption, new ArrayList()));

        commandLine.addValue(argumentOption, "argValue2");
        assertEquals(Collections.unmodifiableList(Arrays.asList("argValue1", "argValue2")), commandLine.getValues(argumentOption, new ArrayList()));

        // test switch addition
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, true));

        // test property addition
        commandLine.addProperty("property", "propertyValue");
        assertEquals("propertyValue", commandLine.getProperty("property", "defaultValue"));

        // test getProperties
        Set properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property"));

        // test toString
        assertEquals("test argValue argValue1 argValue2", commandLine.toString());

        // test looksLikeOption
        assertTrue(commandLine.looksLikeOption("o"));
        assertFalse(commandLine.looksLikeOption("x"));

        // test getOption
        Option optionFromCommandLine = commandLine.getOption("o");
        assertEquals(option, optionFromCommandLine);

        // test getOptionTriggers
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o"));
        assertTrue(optionTriggers.contains("a"));

        // test setDefaultValues
        commandLine.setDefaultValues(option, new ArrayList(Arrays.asList("defaultValue1", "defaultValue2")));
        assertEquals(Collections.unmodifiableList(Arrays.asList("defaultValue1", "defaultValue2")), commandLine.getValues(option, new ArrayList()));

        // test setDefaultSwitch
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));

        // test getNormalised
        assertEquals(arguments, commandLine.getNormalised());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSwitchTwice() {
        Option option = new Option("o", "opt", false, "Option test option");

        List arguments = new ArrayList();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImplEmpty() {
        Option option = new Option("o", "opt", false, "Option test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());

        // test option addition
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().isEmpty());

        // test option value addition
        commandLine.addValue(option, "value1");
        assertNull(commandLine.getValues(option, new ArrayList()));

        // test switch addition
        commandLine.addSwitch(option, true);
        assertNull(commandLine.getSwitch(option, false));

        // test property addition
        commandLine.addProperty("property", "propertyValue");
        assertNull(commandLine.getProperty("property", "defaultValue"));

        // test toString
        assertEquals("", commandLine.toString());

        // test looksLikeOption
        assertFalse(commandLine.looksLikeOption("o"));

        // test getOption
        assertNull(commandLine.getOption("o"));

        // test getOptionTriggers
        assertTrue(commandLine.getOptionTriggers().isEmpty());
    }
}