package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;

    @Before
    public void setUp() {
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(new Option("g", "group", true, "Group name").setRequired(false));
        groupOptions.add(new Option("a", "anonymous", true, "Anonymous argument").setRequired(false));

        options = new Options();
        options.addGroup(new Group("group", "Group", 0, Integer.MAX_VALUE, false).required(false)
                .options(groupOptions));
    }

    @Test
    public void testCanProcess_NoArg() {
        CommandLine commandLine = new CommandLine("-g", new String[0]);
        assertFalse(new GroupImpl(groupOptions, "group", "Group", 0, Integer.MAX_VALUE, false)
                .canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_GroupArg() {
        CommandLine commandLine = new CommandLine("-g group");
        assertTrue(new GroupImpl(groupOptions, "group", "Group", 0, Integer.MAX_VALUE, false)
                .canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_OptionArg() {
        CommandLine commandLine = new CommandLine("-g aAnonymous");
        assertTrue(new GroupImpl(groupOptions, "group", "Group", 0, Integer.MAX_VALUE, false)
                .options().get(1).canProcess(commandLine, "a"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_TooManyOptions() {
        CommandLine commandLine = new CommandLine("-g g1 -g g2");
        new GroupImpl(groupOptions, "group", "Group", 0, 1, false).process(commandLine, commandLine.parser().parse(commandLine.getArgs()));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_TooFewOptions() {
        CommandLine commandLine = new CommandLine("-g");
        new GroupImpl(groupOptions, "group", "Group", 1, 1, false).process(commandLine, commandLine.parser().parse(commandLine.getArgs()));
    }

    @Test
    public void testProcess_NoArg() {
        CommandLine commandLine = new CommandLine("-g");
        new GroupImpl(groupOptions, "group", "Group", 0, Integer.MAX_VALUE, false)
                .process(commandLine, commandLine.parser().parse(commandLine.getArgs()));
        assertEquals(0, commandLine.getOptions().size());
    }

    @Test
    public void testProcess_GroupArg() {
        CommandLine commandLine = new CommandLine("-g group");
        new GroupImpl(groupOptions, "group", "Group", 0, Integer.MAX_VALUE, false)
                .process(commandLine, commandLine.parser().parse(commandLine.getArgs()));
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("anonymous"));
    }

    @Test
    public void testProcess_OptionArg() {
        CommandLine commandLine = new CommandLine("-g aAnonymous");
        new GroupImpl(groupOptions, "group", "Group", 0, Integer.MAX_VALUE, false)
                .process(commandLine, commandLine.parser().parse(commandLine.getArgs()));
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.getOption("anonymous").getValue().isPresent());
        assertEquals("Anonymous argument", commandLine.getOption("anonymous").getValue().get());
    }

    @Test(expected = OptionException.class)
    public void testProcess_TooManyOptions() {
        CommandLine commandLine = new CommandLine("-g g1 -g g2");
        new GroupImpl(groupOptions, "group", "Group", 0, 1, false).process(commandLine, commandLine.parser().parse(commandLine.getArgs()));
    }

    @Test(expected = OptionException.class)
    public void testProcess_TooFewOptions() {
        CommandLine commandLine = new CommandLine("-g");
        new GroupImpl(groupOptions, "group", "Group", 1, 1, false).process(commandLine, commandLine.parser().parse(commandLine.getArgs()));
    }

    @Test
    public void testValidate() {
        CommandLine commandLine = new CommandLine("-g group");
        new GroupImpl(groupOptions, "group", "Group", 0, Integer.MAX_VALUE, false).validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_TooFewOptions() {
        CommandLine commandLine = new CommandLine("-g");
        new GroupImpl(groupOptions, "group", "Group", 1, 1, false).validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_TooManyOptions() {
        CommandLine commandLine = new CommandLine("-g g1 -g g2");
        new GroupImpl(groupOptions, "group", "Group", 0, 1, false).validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        List<String> expectedUsage = new ArrayList<>();
        expectedUsage.add("Usage: group [OPTIONS]");
        expectedUsage.add("");
        expectedUsage.add("Options:");
        expectedUsage.add("   --group          Group name (can be repeated)");
        expectedUsage.add("   --anonymous       Anonymous argument");
        StringBuffer buffer = new StringBuffer();
        new GroupImpl(groupOptions, "group", "Group", 0, Integer.MAX_VALUE, false).appendUsage(buffer, null, null);
        List<String> actualUsage = List.of(buffer.toString().split("\\r\\n|\\n"));
        assertEquals(expectedUsage.size(), actualUsage.size());
        for (int i = 0; i < expectedUsage.size(); i++) {
            assertEquals(expectedUsage.get(i), actualUsage.get(i));
        }
    }

    @Test
    public void testHelpLines() {
        List<String> expectedHelpLines = new ArrayList<>();
        expectedHelpLines.add("group:");
        expectedHelpLines.add("  Group name (can be repeated)");
        expectedHelpLines.add("");
        expectedHelpLines.add("Anonymous:");
        expectedHelpLines.add("  Anonymous argument");
        List<String> actualHelpLines = new GroupImpl(groupOptions, "group", "Group", 0, Integer.MAX_VALUE, false).helpLines(0, null, null);
        assertEquals(expectedHelpLines.size(), actualHelpLines.size());
        for (int i = 0; i < expectedHelpLines.size(); i++) {
            assertEquals(expectedHelpLines.get(i), actualHelpLines.get(i));
        }
    }
}