package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Test case for a simple Group
        List<Option> options = new ArrayList<>();
        Group group = new GroupImpl(options, "testGroup", "Test group", 0, 1);

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Test option 1")
                .create(null);
        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Test option 2")
                .create(null);
        Option option3 = OptionBuilder.withLongOpt("option3")
                .withDescription("Test option 3")
                .create(null);

        options.add(option1);
        options.add(option2);
        options.add(option3);

        // Test canProcess method
        WriteableCommandLine commandLine = new WriteableCommandLine();
        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "option2"));
        assertTrue(group.canProcess(commandLine, "option3"));
        assertFalse(group.canProcess(commandLine, "unknownOption"));

        // Test getPrefixes method
        Set prefixes = group.getPrefixes();
        assertEquals(Collections.emptySet(), prefixes);

        // Test getTriggers method
        Set triggers = group.getTriggers();
        assertEquals(options.size(), triggers.size());
        assertTrue(triggers.containsAll(Collections.list(option1, option2, option3)));

        // Test process method
        List<String> arguments = new ArrayList<>();
        arguments.add("testGroup");
        arguments.add("--option1");
        arguments.add("value1");
        arguments.add("--option2");
        arguments.add("value2");
        arguments.add("--option3");
        arguments.add("value3");

        group.process(commandLine, arguments.listIterator());

        assertEquals("testGroup", commandLine.getOptions().get(0).getLongOpt());
        assertEquals("Test group", commandLine.getOptions().get(0).getDescription());
        assertEquals(3, commandLine.getOptions().size());

        // Test process with an unrecognized option
        CommandLine cmdLine = new CommandLine();
        List<String> args = new ArrayList<>();
        args.add("testGroup");
        args.add("--unknownOption");

        group.process(cmdLine, args.listIterator());

        assertTrue(cmdLine.getUnrecognized().size() > 0);
        assertEquals("unknownOption", cmdLine.getUnrecognized().get(0));

        // Test validate method
        cmdLine = new CommandLine();
        List<String> args2 = new ArrayList<>();
        args2.add("testGroup");
        args2.add("--option1");
        args2.add("value1");

        group.validate(cmdLine);

        // Test getPreferredName method
        assertEquals("testGroup", group.getPreferredName());

        // Test getDescription method
        assertEquals("Test group", group.getDescription());

        // Test appendUsage method
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "testGroup:\n" +
                "  Test group\n" +
                "  --option1 <value>  Test option 1\n" +
                "  --option2 <value>  Test option 2\n" +
                "  --option3 <value>  Test option 3\n";

        assertEquals(expectedUsage, buffer.toString());

        // Test helpLines method
        List<HelpLine> helpLines = group.helpLines(1, helpSettings, null);

        assertEquals(3, helpLines.size());

        assertEquals("testGroup", helpLines.get(0).getOpt());
        assertEquals("Test group", helpLines.get(0).getLongOpt());
        assertEquals("", helpLines.get(0).getDescription());
        assertEquals(Collections.emptyList(), helpLines.get(0).getArgs());

        assertEquals("option1", helpLines.get(1).getOpt());
        assertEquals("--option1 <value>", helpLines.get(1).getLongOpt());
        assertEquals("Test option 1", helpLines.get(1).getDescription());
        assertEquals(Collections.emptyList(), helpLines.get(1).getArgs());

        assertEquals("option2", helpLines.get(2).getOpt());
        assertEquals("--option2 <value>", helpLines.get(2).getLongOpt());
        assertEquals("Test option 2", helpLines.get(2).getDescription());
        assertEquals(Collections.emptyList(), helpLines.get(2).getArgs());

        // Test findOption method
        Option foundOption = group.findOption("option1");
        assertEquals(option1, foundOption);

        // Test getMinimum and getMaximum methods
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());

        // Test isRequired method
        assertFalse(group.isRequired());
    }

    @Test
    public void testGroupWithArguments() {
        // Test case for a Group with anonymous arguments
        List<Option> options = new ArrayList<>();
        Group group = new GroupImpl(options, "testGroup", "Test group", 0, 1);

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Test option 1")
                .create(null);

        Argument arg1 = OptionBuilder.withLongOpt("arg1")
                .withDescription("Test argument 1")
                .isRequired(false)
                .create(null);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Test option 2")
                .create(null);

        Argument arg2 = OptionBuilder.withLongOpt("arg2")
                .withDescription("Test argument 2")
                .isRequired(false)
                .create(null);

        options.add(option1);
        options.add(arg1);
        options.add(option2);
        options.add(arg2);

        // Test canProcess method
        WriteableCommandLine commandLine = new WriteableCommandLine();
        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "arg1"));
        assertTrue(group.canProcess(commandLine, "arg2"));
        assertFalse(group.canProcess(commandLine, "unknownOption"));

        // Test process method
        List<String> arguments = new ArrayList<>();
        arguments.add("testGroup");
        arguments.add("--option1");
        arguments.add("value1");
        arguments.add("--arg1");
        arguments.add("valueArg1");
        arguments.add("--arg2");
        arguments.add("valueArg2");

        group.process(commandLine, arguments.listIterator());

        assertEquals("testGroup", commandLine.getOptions().get(0).getLongOpt());
        assertEquals("Test group", commandLine.getOptions().get(0).getDescription());
        assertEquals(5, commandLine.getOptions().size());

        // Test validate method
        CommandLine cmdLine = new CommandLine();
        List<String> args = new ArrayList<>();
        args.add("testGroup");
        args.add("--option1");
        args.add("value1");

        group.validate(cmdLine);

        assertEquals(2, cmdLine.getArgs().size());
        assertEquals("valueArg1", cmdLine.getArgs().get(0));
        assertEquals("valueArg2", cmdLine.getArgs().get(1));

        // Test appendUsage method
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "testGroup:\n" +
                "  Test group\n" +
                "  --option1 <value>\n" +
                "  --arg1 <valueArg1>\n" +
                "  --arg2 <valueArg2>\n";

        assertEquals(expectedUsage, buffer.toString());

        // Test helpLines method
        List<HelpLine> helpLines = group.helpLines(1, helpSettings, null);

        assertEquals(5, helpLines.size());

        assertEquals("testGroup", helpLines.get(0).getOpt());
        assertEquals("Test group", helpLines.get(0).getLongOpt());
        assertEquals("", helpLines.get(0).getDescription());

        assertEquals("option1", helpLines.get(1).getOpt());
        assertEquals("--option1 <value>", helpLines.get(1).getLongOpt());
        assertEquals("Test option 1", helpLines.get(1).getDescription());
        assertEquals(Collections.emptyList(), helpLines.get(1).getArgs());

        assertEquals("arg1", helpLines.get(2).getOpt());
        assertEquals("--arg1 <valueArg1>", helpLines.get(2).getLongOpt());
        assertEquals("Test argument 1", helpLines.get(2).getDescription());
        assertEquals(Collections.emptyList(), helpLines.get(2).getArgs());

        assertEquals("arg2", helpLines.get(3).getOpt());
        assertEquals("--arg2 <valueArg2>", helpLines.get(3).getLongOpt());
        assertEquals("Test argument 2", helpLines.get(3).getDescription());
        assertEquals(Collections.emptyList(), helpLines.get(3).getArgs());

        assertEquals("option2", helpLines.get(4).getOpt());
        assertEquals("--option2 <value>", helpLines.get(4).getLongOpt());
        assertEquals("Test option 2", helpLines.get(4).getDescription());
        assertEquals(Collections.emptyList(), helpLines.get(4).getArgs());
    }
}