package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupImplConstructor() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        options.add(createOption("option2", "opt2", false, null, new int[]{1}));
        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        assertEquals("group", group.getPreferredName());
        assertEquals("group description", group.getDescription());
        assertEquals(2, group.getOptions().size());
        assertFalse(group.isRequired());
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testCanProcess() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        // test with valid option
        CommandLine commandLine = new CommandLine();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLine(commandLine);
        String arg = "opt1";
        assertTrue(group.canProcess(writeableCommandLine, arg));

        // test with invalid option
        arg = "invalid";
        assertFalse(group.canProcess(writeableCommandLine, arg));

        // test with argument
        arg = "arg";
        assertFalse(group.canProcess(writeableCommandLine, arg));

        // test with an existing option
        writeableCommandLine.parseArgument(arg);
        assertTrue(group.canProcess(writeableCommandLine, arg));

        // test with maximum number of options
        for (int i = 0; i < 2; i++) {
            writeableCommandLine.parseArgument("opt1");
        }
        assertFalse(group.canProcess(writeableCommandLine, "opt1"));
    }

    @Test
    public void testGetPrefixes() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        Set prefixes = group.getPrefixes();
        assertEmpty(prefixes);

        options.remove(0);
        options.add(0, createOption("option1", "opt1", false, null, new String[]{"prefix"}));
        group = new GroupImpl(options, "group", "group description", 1, 2);

        prefixes = group.getPrefixes();
        assertEquals("prefix", prefixes.iterator().next());
    }

    @Test
    public void testGetTriggers() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        Set triggers = group.getTriggers();
        assertEmpty(triggers);

        options.remove(0);
        options.add(0, createOption("option1", "opt1", false, null, new String[]{"trigger"}));
        group = new GroupImpl(options, "group", "group description", 1, 2);

        triggers = group.getTriggers();
        assertEquals("trigger", triggers.iterator().next());
    }

    @Test(expected = NullPointerException.class)
    public void testProcessNullCommandLine() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        group.process(null, null);
    }

    @Test
    public void testProcessValidOptions() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        CommandLine commandLine = new CommandLine();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLine(commandLine);

        String[] args = {"opt1", "opt1"};
        writeableCommandLine.parseArguments(args);

        group.process(writeableCommandLine, null);

        assertEquals(2, commandLine.getArguments().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("opt1", commandLine.getOptionValue("option1"));
    }

    @Test
    public void testProcessInvalidOptions() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null, new int[]{1}));
        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        CommandLine commandLine = new CommandLine();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLine(commandLine);

        String[] args = {"opt1", "opt1", "opt1"};
        writeableCommandLine.parseArguments(args);

        try {
            group.process(writeableCommandLine, null);
            fail();
        } catch (OptionException e) {
            assertEquals("This group requires 1 to 2 options, but 3 were provided", e.getMessage());
        }
    }

    @Test
    public void testProcessAnonymousArguments() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        options.add(createArgument("arg1", "arg1"));
        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        CommandLine commandLine = new CommandLine();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLine(commandLine);

        String[] args = {"opt1", "arg1", "arg1"};
        writeableCommandLine.parseArguments(args);

        group.process(writeableCommandLine, null);

        assertEquals(2, commandLine.getArguments().size());
        assertTrue(commandLine.hasOption("option1"));
        assertEquals("", commandLine.getOptionValue("option1"));
        assertEquals("arg1", commandLine.getArgumentValue(0));
        assertEquals("arg1", commandLine.getArgumentValue(1));
    }

    @Test
    public void testValidateValidOptions() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        CommandLine commandLine = new CommandLine();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLine(commandLine);

        String[] args = {"opt1", "opt1"};
        writeableCommandLine.parseArguments(args);

        group.validate(writeableCommandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateTooFewOptions() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null, new int[]{1}));
        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        CommandLine commandLine = new CommandLine();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLine(commandLine);

        group.validate(writeableCommandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateTooManyOptions() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null, new int[]{1}));
        Group group = new GroupImpl(options, "group", "group description", 1, 2);

        CommandLine commandLine = new CommandLine();
        WriteableCommandLine writeableCommandLine = new WriteableCommandLine(commandLine);

        String[] args = {"opt1", "opt1", "opt1"};
        writeableCommandLine.parseArguments(args);

        group.validate(writeableCommandLine);
    }

    @Test
    public void testAppendUsage() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "group", "group description");

        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null);

        assertEquals("group [(option1)]\n", buffer.toString());

        buffer.setLength(0);
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null);

        assertEquals("group (options)\n" +
                "  option1            opt1\n", buffer.toString());

        buffer.setLength(0);
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), null);

        assertEquals("group (arguments)\n" +
                "  arg1               arg1\n", buffer.toString());

        buffer.setLength(0);
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), new String[]{"|"});

        assertEquals("group [|(option1)]\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "group", "group description");

        List helpLines = group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null);

        assertEquals(1, helpLines.size());
        assertEquals("group [(option1)]\n", helpLines.get(0).toString());

        helpLines = group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null);

        assertEquals(1, helpLines.size());
        assertEquals("group (options)\n" +
                "  option1            opt1\n", helpLines.get(0).toString());

        helpLines = group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), null);

        assertEquals(1, helpLines.size());
        assertEquals("group (arguments)\n" +
                "  arg1               arg1\n", helpLines.get(0).toString());

        helpLines = group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), new String[]{"|"});

        assertEquals(1, helpLines.size());
        assertEquals("group [|(option1)]\n", helpLines.get(0).toString());

        helpLines = group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), new String[]{"|"});

        assertEquals(1, helpLines.size());
        assertEquals("group (options)\n" +
                "  option1            opt1\n" +
                "|  |--opt1           opt1\n", helpLines.get(0).toString());

        helpLines = group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), new String[]{"|"});

        assertEquals(1, helpLines.size());
        assertEquals("group (arguments)\n" +
                "  arg1               arg1\n" +
                "|  |--arg1           arg1\n", helpLines.get(0).toString());
    }

    @Test
    public void testGetOptions() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "group", "group description");

        List groupOptions = group.getOptions();

        assertEquals(1, groupOptions.size());
        assertEquals("option1", groupOptions.get(0).getPreferredName());
    }

    @Test
    public void testGetAnonymous() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        options.add(createArgument("arg1", "arg1"));
        Group group = new GroupImpl(options, "group", "group description");

        List groupAnonymous = group.getAnonymous();

        assertEquals(1, groupAnonymous.size());
        assertEquals("arg1", groupAnonymous.get(0).getPreferredName());
    }

    @Test
    public void testFindOption() {
        List options = new ArrayList();
        options.add(createOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "group", "group description");

        Option found = group.findOption("opt1");

        assertEquals("option1", found.getPreferredName());
    }

    private Option createOption(String name, String shortName, boolean required, String[] longNames) {
        return createOption(name, shortName, required, longNames, new int[0]);
    }

    private Option createOption(String name, String shortName, boolean required, String[] longNames, int minOccurs) {
        List options = new ArrayList();
        options.add(new Option.Builder()
                .withRequired(required)
                .withShortOpt(shortName)
                .withLongOpt(longNames)
                .withArgName("arg")
                .build());
        return new OptionImpl(name, shortName, longNames, options, minOccurs, null);
    }

    private Argument createArgument(String name, String shortName) {
        return new ArgumentImpl(name, shortName, null, new ArrayList());
    }
}