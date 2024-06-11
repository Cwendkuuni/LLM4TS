package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupImpl_1() {
        // Create a test Group
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("o1", "option1", true, "Description 1");
        Option option2 = new Option("o2", "option2", false, "Description 2");
        Argument arg1 = new Argument("arg1", "Argument 1", true);
        options.add(option1);
        options.add(option2);
        options.add(arg1);
        Group group = new GroupImpl(options, "Group1", "Group Description", 0, 1, true);

        // Test method: canProcess
        WriteableCommandLine commandLine = new WriteableCommandLine();
        String[] args = {"--o1", "--o2", "arg1"};
        for (String arg : args) {
            commandLine.parseArgument(arg);
        }
        assertTrue(group.canProcess(commandLine, null));

        // Test edge case: empty arg list
        commandLine.clearArguments();
        assertFalse(group.canProcess(commandLine, null));

        // Test edge case: arg not starting with option prefix
        commandLine.parseArgument("arg");
        assertFalse(group.canProcess(commandLine, null));

        // Test edge case: option without trigger
        options.remove(1);
        group = new GroupImpl(options, null, null, 0, 1, true);
        commandLine.parseArgument("o1");
        assertFalse(group.canProcess(commandLine, null));

        // Test edge case: option with invalid trigger
        option1.setTrigger("invalid");
        options.set(0, option1);
        group = new GroupImpl(options, null, null, 0, 1, true);
        commandLine.parseArgument("o1");
        assertFalse(group.canProcess(commandLine, null));

        // Test exception: minimum constraint
        commandLine.clearArguments();
        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.iterator()));

        // Test exception: maximum constraint
        WriteableCommandLine commandLine2 = new WriteableCommandLine();
        String[] args2 = {"--o1", "--o1", "--o1", "arg1"};
        for (String arg : args2) {
            commandLine2.parseArgument(arg);
        }
        assertThrows(OptionException.class, () -> group.process(commandLine2, commandLine2.iterator()));

        // Test method: getPrefixes
        Set prefixes = group.getPrefixes();
        assertEquals(0, prefixes.size());

        // Test method: getTriggers
        Set triggers = group.getTriggers();
        assertEquals(1, triggers.size());
        assertTrue(triggers.contains("o1"));

        // Test method: process
        commandLine.clearArguments();
        group.process(commandLine, commandLine.iterator());
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option1, commandLine.getOptions().get(0));

        // Test method: validate
        commandLine.clearArguments();
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test method: appendUsage
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        assertEquals("Group Description\n", buffer.toString());

        // Test method: helpLines
        List helpLines = group.helpLines(1, null, null);
        assertEquals(1, helpLines.size());
        String helpLine = (String) helpLines.get(0);
        assertTrue(helpLine.startsWith("Group1:"));
    }
}