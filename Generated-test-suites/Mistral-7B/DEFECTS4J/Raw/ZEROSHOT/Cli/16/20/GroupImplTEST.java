package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupImpl() throws OptionException {
        // Given
        final List<Option> options = new ArrayList<>();

        final Option option1 = new OptionBuilder("o1", "o1")
                .withDescription("Option 1")
                .withLongOpt("long-o1")
                .withArgName("value1")
                .isRequired()
                .create();
        options.add(option1);

        final Option option2 = new OptionBuilder("o2", "o2")
                .withDescription("Option 2")
                .withLongOpt("long-o2")
                .withArgName("value2")
                .create();
        options.add(option2);

        final Option option3 = new OptionBuilder("o3", "o3")
                .withDescription("Option 3")
                .withLongOpt("long-o3")
                .withArgName("value3")
                .create();
        options.add(option3);

        final Argument argument = new ArgumentBuilder()
                .withName("arg")
                .withDescription("Argument")
                .isRequired()
                .create();

        final List<Argument> anonymous = new ArrayList<>();
        anonymous.add(argument);

        final SortedMap<String, Option> optionMap = new TreeMap<>(Collections.reverseOrder());
        optionMap.put("o3", option3);
        optionMap.put("o2", option2);
        optionMap.put("o1", option1);

        final Set<String> prefixes = new HashSet<>();

        final Group group = new GroupImpl(options, "group", "Group", 1, 3, anonymous, optionMap, prefixes);

        // When
        final WriteableCommandLine commandLine = new DefaultCommandLine();

        // Can process
        assertTrue(group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, "invalid"));
        assertTrue(group.canProcess(commandLine, "o1"));
        assertTrue(group.canProcess(commandLine, "o2"));
        assertTrue(group.canProcess(commandLine, "o3"));

        // Process
        commandLine.parse(group.toArray());

        assertEquals(commandLine.getOptionValue("o1"), "value1");
        assertEquals(commandLine.getOptionValue("o2"), "value2");
        assertEquals(commandLine.getOptionValue("o3"), "value3");
        assertNull(commandLine.getOptionValue("invalid"));

        // Validate
        group.validate(commandLine);

        // GetPreferredName
        assertEquals(group.getPreferredName(), "group");

        // GetDescription
        assertEquals(group.getDescription(), "Group");

        // AppendUsage
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals(buffer.toString(),
                "group (|)\n" +
                        "  |--o3 <value3>\n" +
                        "  |--o2 <value2>\n" +
                        "  |--o1 <value1>\n" +
                        "  |--arg\n");

        // HelpLines
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(helpLines.size(), 4);
        assertEquals(helpLines.get(0).getOptionSpec().getLongOpt(), "group");
        assertEquals(helpLines.get(0).getOptionSpec().getName(), "g");
        assertEquals(helpLines.get(0).getOptionSpec().getDescription(), "Group");
        assertEquals(helpLines.get(1).getOptionSpec().getLongOpt(), "long-o3");
        assertEquals(helpLines.get(1).getOptionSpec().getName(), "o3");
        assertEquals(helpLines.get(1).getOptionSpec().getDescription(), "Option 3");
        assertEquals(helpLines.get(2).getOptionSpec().getLongOpt(), "long-o2");
        assertEquals(helpLines.get(2).getOptionSpec().getName(), "o2");
        assertEquals(helpLines.get(2).getOptionSpec().getDescription(), "Option 2");
        assertEquals(helpLines.get(3).getOptionSpec().getLongOpt(), "long-o1");
        assertEquals(helpLines.get(3).getOptionSpec().getName(), "o1");
        assertEquals(helpLines.get(3).getOptionSpec().getDescription(), "Option 1");

        // GetOptions
        final List optionsResult = group.getOptions();
        assertEquals(options.size(), optionsResult.size());
        for (int i = 0; i < options.size(); i++) {
            assertEquals(options.get(i), optionsResult.get(i));
        }

        // GetAnonymous
        final List anonymousResult = group.getAnonymous();
        assertEquals(anonymous.size(), anonymousResult.size());
        for (int i = 0; i < anonymous.size(); i++) {
            assertEquals(anonymous.get(i), anonymousResult.get(i));
        }

        // FindOption
        final Option found = group.findOption("o1");
        assertEquals(found, option1);

        // GetMinimum
        assertEquals(group.getMinimum(), 1);

        // GetMaximum
        assertEquals(group.getMaximum(), 3);

        // IsRequired
        assertTrue(group.isRequired());

        // Defaults
        commandLine.clearParsed();
        group.defaults(commandLine);
        assertNull(commandLine.getOptionValue("o1"));
        assertNull(commandLine.getOptionValue("o2"));
        assertNull(commandLine.getOptionValue("o3"));
        assertNull(commandLine.getOptionValue("arg"));
    }
}