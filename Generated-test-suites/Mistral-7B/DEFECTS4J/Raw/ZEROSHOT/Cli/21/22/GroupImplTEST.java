package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
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

public class GroupImplTest {

    @org.junit.Test
    public void testGroupImpl() {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(createOption("--option1", "Option 1", true));
        options.add(createOption("--option2", "Option 2", false));
        options.add(createOption("--option3", "Option 3", false));

        final Group group = new GroupImpl(options, "MyGroup", "My Group", 1, Integer.MAX_VALUE, true);

        // When

        // test canProcess method
        WriteableCommandLine commandLine = createCommandLine();
        commandLine.parse("--option1 --option3");
        assertTrue(group.canProcess(commandLine, "--option1"));
        assertTrue(group.canProcess(commandLine, "--option3"));
        assertFalse(group.canProcess(commandLine, "--option2"));

        // test getPrefixes method
        Set prefixes = group.getPrefixes();
        assertEquals(Collections.emptySet(), prefixes);

        // test getTriggers method
        Set triggers = group.getTriggers();
        assertEquals(Collections.emptySet(), triggers);

        // test process method
        commandLine = createCommandLine();
        ListIterator<String> arguments = commandLine.getArguments().listIterator();
        group.process(commandLine, arguments);
        assertEquals("--option1", arguments.next());
        assertEquals("--option3", arguments.next());
        assertFalse(arguments.hasNext());

        // test validate method
        commandLine = createCommandLine();
        group.validate(commandLine);

        // Then
        assertEquals("MyGroup", group.getPreferredName());
        assertEquals("My Group", group.getDescription());

        // test appendUsage method
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        String usage = buffer.toString();
        assertTrue(usage.contains("MyGroup"));
        assertTrue(usage.contains("(required)"));
        assertTrue(usage.contains("Option 1"));
        assertTrue(usage.contains("--option1"));
        assertTrue(usage.contains("Option 3"));
        assertTrue(usage.contains("--option3"));

        // test helpLines method
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        int helpLinesSize = helpLines.size();
        assertEquals(2, helpLinesSize);
        HelpLine helpLine1 = helpLines.get(0);
        String helpLine1String = helpLine1.toString();
        assertTrue(helpLine1String.contains("MyGroup"));
        assertTrue(helpLine1String.contains("OPTIONS"));
        assertTrue(helpLine1String.contains("--option1"));
        HelpLine helpLine2 = helpLines.get(1);
        String helpLine2String = helpLine2.toString();
        assertTrue(helpLine2String.contains("MyGroup"));
        assertTrue(helpLine2String.contains("ARGUMENTS"));
        assertTrue(helpLine2String.contains("--option3"));

        // test getOptions method
        List<Option> groupOptions = group.getOptions();
        assertEquals(3, groupOptions.size());
        Option option1 = groupOptions.get(0);
        assertEquals("--option1", option1.getLongOpt());
        Option option2 = groupOptions.get(1);
        assertEquals("--option2", option2.getLongOpt());
        Option option3 = groupOptions.get(2);
        assertEquals("--option3", option3.getLongOpt());

        // test getAnonymous method
        List<Argument> groupAnonymous = group.getAnonymous();
        assertEquals(0, groupAnonymous.size());

        // test findOption method
        Option foundOption = group.findOption("--option1");
        assertEquals("--option1", foundOption.getLongOpt());

        // test getMinimum and getMaximum methods
        int minimum = group.getMinimum();
        int maximum = group.getMaximum();
        assertEquals(1, minimum);
        assertEquals(Integer.MAX_VALUE, maximum);

        // test isRequired method
        boolean isRequired = group.isRequired();
        assertTrue(isRequired);

        // test defaults method
        commandLine = createCommandLine();
        group.defaults(commandLine);
        assertTrue(commandLine.hasOption("--option1"));
        assertTrue(commandLine.hasOption("--option3"));
        assertFalse(commandLine.hasOption("--option2"));

        // test toString method
        String groupString = group.toString();
        assertTrue(groupString.contains("GroupImpl"));
        assertTrue(groupString.contains("MyGroup"));
    }

    private Option createOption(String longOpt, String description, boolean hasArg) {
        return new OptionBuilder()
                .withLongOpt(longOpt)
                .withDescription(description)
                .isRequired(false)
                .hasArg(hasArg)
                .create();
    }

    private WriteableCommandLine createCommandLine() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addOption(new Option("h", "help", false, "display this help message and exit"));
        return commandLine;
    }
}

class HelpLineImpl implements HelpLine {

    private final Group group;
    private final int depth;

    public HelpLineImpl(Group group, int depth) {
        this.group = group;
        this.depth = depth;
    }

    @Override
    public String getHeadText() {
        return "";
    }

    @Override
    public String getOptionSpecString() {
        return group.getPreferredName();
    }

    @Override
    public String getLongOptionName() {
        return group.getPreferredName();
    }

    @Override
    public String getShortOptionName() {
        return null;
    }

    @Override
    public String getArgumentDescription() {
        return "<arguments>";
    }

    @Override
    public String getDescription() {
        String description = group.getDescription();
        StringBuilder sb = new StringBuilder(description);
        int index = sb.indexOf("\n");
        if (index > 0) {
            sb.delete(0, index);
        }
        return sb.toString();
    }

    @Override
    public String getUsage() {
        Map<String, Option> optionMap = new LinkedHashMap<>();
        for (Option option : group.getOptions()) {
            optionMap.put(option.getLongOpt(), option);
        }
        StringBuilder sb = new StringBuilder();
        if (depth > 0) {
            sb.append("    ");
        }
        if (group.isRequired()) {
            sb.append("[");
        }
        sb.append(group.getPreferredName());
        if (group.isRequired()) {
            sb.append("]");
        }
        if (group.getOptions().size() > 0) {
            sb.append(":\n");
            for (Map.Entry<String, Option> entry : optionMap.entrySet()) {
                Option option = entry.getValue();
                sb.append(option.getHelpLine(depth + 1).toString());
            }
        }
        if (group.getAnonymous().size() > 0) {
            sb.append("\n Argument(s):\n");
            for (Argument argument : group.getAnonymous()) {
                sb.append("    ");
                sb.append(argument.getLongOpt());
                sb.append(":\n");
                sb.append("        " + argument.getDescription());
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getUsage();
    }
}