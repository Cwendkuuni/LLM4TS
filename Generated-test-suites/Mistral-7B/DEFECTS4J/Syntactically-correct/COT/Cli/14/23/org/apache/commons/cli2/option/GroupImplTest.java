package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        // Create a list of options for testing
        List<Option> options = new ArrayList<>();
        options.add(new Option("o", null, false, "Option 1"));
        options.add(new Option("o2", null, false, "Option 2"));
        options.add(new Option("g", "group", 2, "Group Option with minimum 2 and name 'group'"));
        options.add(new Option("a", null, true, "Anonymous Argument"));
        options.add(new Option("a2", null, true, "Another Anonymous Argument"));

        // Create a GroupImpl instance with the options and some basic configuration
        group = new GroupImpl(options, "myGroup", "My Group", 0, 5);
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcess() {
        // Basic functionality test
        assertTrue(group.canProcess(commandLine, "o"));
        assertTrue(group.canProcess(commandLine, "o2"));
        assertFalse(group.canProcess(commandLine, "invalidOption"));
        assertFalse(group.canProcess(commandLine, ""));
        assertFalse(group.canProcess(commandLine, null));

        // Test with minimum and maximum values
        assertFalse(group.canProcess(commandLine, "o")); // Should only be able to process once with minimum of 0
        assertTrue(group.canProcess(commandLine, "o")); // Consumed the first option, so can process again
        assertFalse(group.canProcess(commandLine, "o")); // Consumed the minimum number of options
        assertTrue(group.canProcess(commandLine, "o2")); // Has not reached maximum number of options yet
        assertFalse(group.canProcess(commandLine, "o")); // Has reached maximum number of options
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());

        group = new GroupImpl(Collections.singletonList(new Option("p", null, false, "Prefix Option")), null, null, 0, 0);
        prefixes = group.getPrefixes();
        assertTrue(prefixes.contains("p"));
    }

    @Test
    public void testGetTriggers() {
        Map<String, Option> triggers = group.getTriggers();
        assertTrue(triggers.isEmpty());

        group = new GroupImpl(Arrays.asList(new Option("o", null, false, "Option 1"), new Option("o2", null, false, "Option 2")), null, null, 0, 0);
        triggers = group.getTriggers();
        assertTrue(triggers.size() == 2);
        assertEquals("o", triggers.keySet().iterator().next());
        assertEquals("o2", triggers.keySet().iterator().next());
    }

    @Test
    public void testProcess() {
        // Test basic functionality
        group.process(commandLine, commandLine.iterator());
        assertSame(commandLine, group.getCommandLine());
        assertEquals(0, commandLine.getArgs().size());

        // Test with minimum and maximum values
        commandLine.getArgs().add("");
        group.process(commandLine, commandLine.iterator());
        assertSame(commandLine, group.getCommandLine());
        assertEquals(0, commandLine.getArgs().size()); // Option should have been consumed

        commandLine.getArgs().add("");
        commandLine.getArgs().add("");
        group.process(commandLine, commandLine.iterator());
        assertEquals("o", commandLine.getArgs().get(0)); // First option processed
        assertEquals("o2", commandLine.getArgs().get(1)); // Second option processed

        // Test anonymous arguments
        commandLine.getArgs().clear();
        commandLine.getArgs().add("--anonymous");
        group.process(commandLine, commandLine.iterator());
        assertSame(commandLine, group.getCommandLine());
        assertEquals("--anonymous", commandLine.getArgs().get(0));

        // Test invalid command line
        commandLine.getArgs().clear();
        commandLine.getArgs().add("--invalid");
        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.iterator()));
    }

    @Test
    public void testValidate() {
        // Test valid command line
        group.validate(commandLine);

        // Test missing options
        commandLine.getArgs().clear();
        commandLine.getArgs().add("--o");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test too many options
        commandLine.getArgs().clear();
        commandLine.getArgs().add("--o");
        commandLine.getArgs().add("--o");
        commandLine.getArgs().add("--o2");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test anonymous arguments
        commandLine.getArgs().clear();
        commandLine.getArgs().add("--anonymous");
        commandLine.getArgs().add("--anonymous2");
        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("myGroup", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertNull(group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        String usage = buffer.toString();
        assertTrue(usage.contains("My Group"));
        assertFalse(usage.contains("Option 1"));
        assertFalse(usage.contains("Option 2"));

        // Test with different helpSettings
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        buffer.setLength(0);
        group.appendUsage(buffer, helpSettings, null);
        usage = buffer.toString();
        assertTrue(usage.contains("My Group"));
        assertTrue(usage.contains("Option 1"));
        assertTrue(usage.contains("Option 2"));
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(1, helpLines.size());
        assertNotNull(helpLines.get(0).getOptionSpec());
        assertNull(helpLines.get(0).getLongOpt());

        // Test with different helpSettings
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        helpLines = group.helpLines(0, helpSettings, null);
        assertEquals(3, helpLines.size());
        assertNotNull(helpLines.get(0).getOptionSpec());
        assertNotNull(helpLines.get(1).getLongOpt());
        assertNotNull(helpLines.get(2).getLongOpt());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(options.size(), 4);
        assertTrue(options.contains(new Option("o", null, false, "Option 1")));
        assertTrue(options.contains(new Option("o2", null, false, "Option 2")));
        assertTrue(options.contains(new Option("g", "group", 2, "Group Option with minimum 2 and name 'group'")));
        assertTrue(options.contains(new Option("a", null, true, "Anonymous Argument")));
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();
        assertEquals(anonymous.size(), 2);
        assertTrue(anonymous.contains(new Option("a", null, true, "Anonymous Argument")));
        assertTrue(anonymous.contains(new Option("a2", null, true, "Another Anonymous Argument")));
    }

    @Test
    public void testFindOption() {
        Option option = group.findOption("o");
        assertSame(new Option("o", null, false, "Option 1"), option);

        option = group.findOption("g");
        assertSame(new Option("g", "group", 2, "Group Option with minimum 2 and name 'group'"), option);

        option = group.findOption("invalid");
        assertNull(option);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(5, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }
}