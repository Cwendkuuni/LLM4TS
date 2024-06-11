package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

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
        // Create a list of options and arguments
        final List options = new ArrayList();
        options.add(createOption("optA", "option A", false));
        options.add(createOption("optB", "option B", true));
        options.add(createOption("argA", "argument A", Argument.VALUE_OPTIONAL, null));

        // Create a group with the options and arguments
        final Group group = new GroupImpl(options, "MyGroup", "A group", 1, Integer.MAX_VALUE, false);

        // Test canProcess method
        final WriteableCommandLine commandLine = createCommandLine();
        commandLine.addOption("optA");
        commandLine.addOption("optB");
        commandLine.addOption("argA");

        assertTrue(group.canProcess(commandLine, "optA"));
        assertTrue(group.canProcess(commandLine, "optB"));
        assertTrue(group.canProcess(commandLine, "argA"));

        // Test getPrefixes method
        Set prefixes = group.getPrefixes();
        assertEquals(Collections.emptySet(), prefixes);

        // Test getTriggers method
        Set triggers = group.getTriggers();
        assertEquals(Collections.emptySet(), triggers);

        // Test process method
        ListIterator iterator = commandLine.iterator();
        iterator.next(); // skip "MyGroup"
        iterator.next(); // skip "optA"
        iterator.next(); // skip "optB"

        group.process(commandLine, iterator);

        assertEquals("optA", commandLine.getOptionValues("optA")[0]);
        assertEquals("optB", commandLine.getOptionValues("optB")[0]);
        assertNull(commandLine.getOptionValues("argA")[0]);

        // Test validate method
        group.validate(commandLine);

        // Test getPreferredName method
        assertEquals("MyGroup", group.getPreferredName());

        // Test getDescription method
        assertEquals("A group", group.getDescription());

        // Test appendUsage method
        StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, new HashSet<>(), null);
        String usage = sb.toString();

        String expectedUsage = "MyGroup:\n" +
                "  A group\n" +
                "Options:\n" +
                "  optA OPTIONAL\n" +
                "  optB REQUIRED\n" +
                "  argA OPTIONAL\n";

        assertEquals(expectedUsage, usage);

        // Test helpLines method
        List<HelpLine> helpLines = group.helpLines(0, new HashSet<>(), null);
        String helpText = HelpLine.getHelpText(helpLines);

        String expectedHelpText = "MyGroup:\n" +
                "  A group\n" +
                "Options:\n" +
                "  optA\n" +
                "        OPTIONAL\n" +
                "        option A\n" +
                "  optB\n" +
                "        REQUIRED\n" +
                "  argA\n" +
                "        OPTIONAL\n" +
                "        argument A\n";

        assertEquals(expectedHelpText, helpText);

        // Test getOptions method
        List optionsList = group.getOptions();
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.contains(createOption("optA", "option A", false)));
        assertTrue(optionsList.contains(createOption("optB", "option B", true)));

        // Test getAnonymous method
        List anonymousList = group.getAnonymous();
        assertEquals(1, anonymousList.size());
        assertTrue(anonymousList.contains(createOption("argA", "argument A", Argument.VALUE_OPTIONAL, null)));

        // Test findOption method
        Option foundOption = group.findOption("optA");
        assertTrue(foundOption != null);
        assertEquals("optA", foundOption.getLongOpt());

        // Test getMinimum and getMaximum methods
        assertEquals(1, group.getMinimum());
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }

    @Test
    public void testGroupImplWithMinimumAndMaximum() {
        // Create a list of options and arguments
        final List options = new ArrayList();
        options.add(createOption("optA", "option A", false));
        options.add(createOption("optB", "option B", true));
        options.add(createOption("argA", "argument A", Argument.VALUE_OPTIONAL, null));

        // Create a group with the options and arguments and minimum and maximum constraints
        final Group group = new GroupImpl(options, "MyGroup", "A group", 2, 3, false);

        // Test canProcess method
        final WriteableCommandLine commandLine = createCommandLine();
        commandLine.addOption("optA");
        commandLine.addOption("optB");
        commandLine.addOption("argA");
        commandLine.addOption("optA");

        assertTrue(group.canProcess(commandLine, "optA"));
        assertTrue(group.canProcess(commandLine, "optB"));
        assertTrue(group.canProcess(commandLine, "argA"));

        assertFalse(group.canProcess(commandLine, "optA")); // should have reached minimum

        // Test process method
        ListIterator iterator = commandLine.iterator();
        iterator.next(); // skip "MyGroup"
        iterator.next(); // skip "optA"
        iterator.next(); // skip "optB"
        iterator.next(); // skip "optA"

        group.process(commandLine, iterator);

        assertEquals("optA", commandLine.getOptionValues("optA")[0]);
        assertEquals("optB", commandLine.getOptionValues("optB")[0]);
        assertNull(commandLine.getOptionValues("argA")[0]);

        // Test validate method
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }

        // Test getPreferredName method
        assertEquals("MyGroup", group.getPreferredName());

        // Test getDescription method
        assertEquals("A group", group.getDescription());

        // Test appendUsage method
        StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, new HashSet<>(), null);
        String usage = sb.toString();

        String expectedUsage = "MyGroup:\n" +
                "  A group\n" +
                "Options:\n" +
                "  optA OPTIONAL\n" +
                "  optB REQUIRED\n" +
                "  argA OPTIONAL\n";

        assertEquals(expectedUsage, usage);

        // Test helpLines method
        List<HelpLine> helpLines = group.helpLines(0, new HashSet<>(), null);
        String helpText = HelpLine.getHelpText(helpLines);

        String expectedHelpText = "MyGroup:\n" +
                "  A group\n" +
                "Options:\n" +
                "  optA\n" +
                "        OPTIONAL\n" +
                "        option A\n" +
                "  optB\n" +
                "        REQUIRED\n" +
                "  argA\n" +
                "        OPTIONAL\n" +
                "        argument A\n";

        assertEquals(expectedHelpText, helpText);

        // Test getOptions method
        List optionsList = group.getOptions();
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.contains(createOption("optA", "option A", false)));
        assertTrue(optionsList.contains(createOption("optB", "option B", true)));

        // Test getAnonymous method
        List anonymousList = group.getAnonymous();
        assertEquals(1, anonymousList.size());
        assertTrue(anonymousList.contains(createOption("argA", "argument A", Argument.VALUE_OPTIONAL, null)));

        // Test findOption method
        Option foundOption = group.findOption("optA");
        assertTrue(foundOption != null);
        assertEquals("optA", foundOption.getLongOpt());

        // Test getMinimum and getMaximum methods
        assertEquals(2, group.getMinimum());
        assertEquals(3, group.getMaximum());
    }

    private Option createOption(String longOpt, String description, boolean required, String argName) {
        return new OptionBuilder()
                .withLongOpt(longOpt)
                .withDescription(description)
                .withRequired(required)
                .withArgName(argName)
                .create();
    }

    private WriteableCommandLine createCommandLine() {
        return new DefaultCommandLine();
    }
}

class ReverseStringComparator implements Comparator<String> {
    public int compare(String o1, String o2) {
        return o1.compareTo(o2) * -1;
    }
}