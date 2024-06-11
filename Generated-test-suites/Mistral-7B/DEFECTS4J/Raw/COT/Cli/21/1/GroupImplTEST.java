package org.apache.commons.cli2.option; // import necessary packages here

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for GroupImpl.
 */
public class GroupImplTest {

    // A sample GroupImpl instance for testing
    private GroupImpl group;

    @Before
    public void setUp() {
        // Create a sample list of options for GroupImpl
        List<Option> options = new ArrayList<>();
        options.add(new Option("opt1", "option 1", true, "o1"));
        options.add(new Option("opt2", "option 2", false, "o2"));
        options.add(new Option("opt3", "option 3", false, "o3"));

        // Create a GroupImpl instance with the sample options
        group = new GroupImpl(options, "MyGroup", "My Group", 1, 3, true);
    }

    @Test
    public void testConstructor() {
        assertNotNull(group);
        assertEquals(3, group.getOptions().size());
        assertEquals("MyGroup", group.getPreferredName());
        assertEquals("My Group", group.getDescription());
        assertTrue(group.isRequired());
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test valid options
        String validOption1 = "o1";
        String validOption2 = "o2";
        String validOption3 = "o3";

        // Test the first valid option
        commandLine.parse(Arrays.asList(validOption1.split("=")));
        assertTrue(group.canProcess(commandLine, validOption1));

        // Test the other valid options
        commandLine.clear();
        commandLine.parse(Arrays.asList(validOption2.split("=")));
        assertTrue(group.canProcess(commandLine, validOption2));
        commandLine.parse(Arrays.asList(validOption3.split("=")));
        assertTrue(group.canProcess(commandLine, validOption3));

        // Test an option with a different trigger
        String invalidOption = "invalidOption";
        assertFalse(group.canProcess(commandLine, invalidOption));

        // Test anonymous arguments
        assertFalse(group.canProcess(commandLine, "arg"));
        assertTrue(group.canProcess(commandLine, "arg"));
        assertFalse(group.canProcess(commandLine, "arg"));
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test valid command line
        commandLine.parse(Arrays.asList("opt1", "arg1"));
        group.validate(commandLine);

        // Test invalid command line with less options than minimum
        commandLine.clear();
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test invalid command line with more options than maximum
        commandLine.parse(Arrays.asList("opt1", "opt1", "arg1"));
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Set up command line with valid options
        commandLine.parse(Arrays.asList("opt1", "arg1", "opt2", "arg2", "opt3"));

        // Test processing the options in order
        ListIterator<Option> optionsIterator = group.getOptions().listIterator();
        while (optionsIterator.hasNext()) {
            Option option = optionsIterator.next();
            option.process(commandLine, commandLine.getArguments());
            String arg = commandLine.getArgument(option.getLongOpt());
            assertEquals(option.getDefaultValue(), arg);
        }

        // Test processing an anonymous argument
        Argument anonymousArgument = (Argument) group.findOption("arg");
        anonymousArgument.process(commandLine, commandLine.getArguments());
        assertEquals("", commandLine.getArgument("arg"));
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<String> comp = null;

        group.appendUsage(buffer, helpSettings, comp);

        String expectedUsage = "MyGroup:";
        if (group.isRequired()) {
            expectedUsage += " (required)";
        }
        expectedUsage += "\n";
        expectedUsage += "  opt1  --option1 [ARG]\n";
        expectedUsage += "  opt2  --option2\n";
        expectedUsage += "  opt3  --option3 [ARG]\n";
        expectedUsage += "  arg   [ARG]\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<String> comp = null;

        List<HelpLine> helpLines = group.helpLines(depth, helpSettings, comp);

        assertEquals(5, helpLines.size());

        String expectedUsage = "MyGroup:";
        if (group.isRequired()) {
            expectedUsage += " (required)";
        }
        expectedUsage += "\n";
        expectedUsage += "  opt1  --option1 [ARG]\n";
        expectedUsage += "  opt2  --option2\n";
        expectedUsage += "  opt3  --option3 [ARG]\n";
        expectedUsage += "  arg   [ARG]\n";

        assertEquals(expectedUsage, helpLines.get(0).getHelp());
        assertEquals("option 1", helpLines.get(1).getOptionSpec().getLongOpt());
        assertEquals("option 1", helpLines.get(1).getOptionSpec().getShortOpt());
        assertEquals("option 1", helpLines.get(1).getOptionSpec().getName());
        assertEquals("option 1", helpLines.get(1).getOptionSpec().getDescription());

        assertEquals("option 2", helpLines.get(2).getOptionSpec().getLongOpt());
        assertEquals("option 2", helpLines.get(2).getOptionSpec().getShortOpt());
        assertEquals("option 2", helpLines.get(2).getOptionSpec().getName());
        assertEquals("option 2", helpLines.get(2).getOptionSpec().getDescription());

        assertEquals("option 3", helpLines.get(3).getOptionSpec().getLongOpt());
        assertEquals("option 3", helpLines.get(3).getOptionSpec().getShortOpt());
        assertEquals("option 3", helpLines.get(3).getOptionSpec().getName());
        assertEquals("option 3", helpLines.get(3).getOptionSpec().getDescription());

        assertEquals("arg", helpLines.get(4).getOptionSpec().getLongOpt());
        assertEquals("arg", helpLines.get(4).getOptionSpec().getShortOpt());
        assertEquals("arg", helpLines.get(4).getOptionSpec().getName());
        assertEquals("", helpLines.get(4).getOptionSpec().getDescription());
    }
}