package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Test constructor with empty list of options
        List options = Collections.emptyList();
        GroupImpl group = new GroupImpl(options, null, null, 0, 0, false);

        assertTrue(group.isRequired());
        assertEquals(0, group.getMinimum());
        assertEquals(0, group.getMaximum());
        assertNull(group.getName());
        assertNull(group.getDescription());
        assertEquals(Collections.emptyList(), group.getOptions());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertEquals(Collections.emptyMap(), group.getOptionMap());
        assertEquals(Collections.emptySet(), group.getPrefixes());

        // Test constructor with non-empty list of options
        List options2 = new ArrayList();
        options2.add(createOption("option1"));
        options2.add(createArgument("arg1"));
        GroupImpl group2 = new GroupImpl(options2, "name", "description", 1, 2, false);

        assertFalse(group2.isRequired());
        assertEquals(1, group2.getMinimum());
        assertEquals(2, group2.getMaximum());
        assertEquals("name", group2.getName());
        assertEquals("description", group2.getDescription());
        assertEquals(options2, group2.getOptions());
        assertEquals(Collections.singletonList(createArgument("arg1")), group2.getAnonymous());
        assertEquals(getOptionMap(options2), group2.getOptionMap());
        assertEquals(getPrefixes(options2), group2.getPrefixes());

        // Test canProcess()
        WriteableCommandLine commandLine = createCommandLine();
        String[] arguments = {"--option1", "arg1", "arg2"};
        for (String arg : arguments) {
            commandLine.parseArgument(arg);
        }

        assertTrue(group2.canProcess(commandLine, "option1"));
        assertFalse(group2.canProcess(commandLine, "arg1"));
        assertFalse(group2.canProcess(commandLine, "arg2"));
        assertFalse(group2.canProcess(commandLine, "nonexistent"));

        // Test validate()
        try {
            group2.validate(commandLine);
        } catch (OptionException e) {
            fail("validate() should not throw an exception.");
        }

        // Test process()
        ListIterator iterator = commandLine.getArgs().listIterator();
        group2.process(commandLine, iterator);

        assertEquals(3, commandLine.getArgs().size());
        assertEquals("arg2", commandLine.getArgs().get(2).getValue());

        // Test getPreferredName() and getDescription()
        assertEquals("name", group2.getPreferredName());
        assertEquals("description", group2.getDescription());

        // Test appendUsage()
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME);

        group2.appendUsage(buffer, helpSettings, null);

        assertEquals("name [option1 [ARG]]\n", buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        group2.appendUsage(buffer, helpSettings, null);

        assertEquals("name\n" +
                " option1 [ARG]\n", buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        group2.appendUsage(buffer, helpSettings, null);

        assertEquals("name (option1 [ARG])\n", buffer.toString());

        // Test helpLines()
        List helpLines = group2.helpLines(0, helpSettings, null);

        assertEquals(5, helpLines.size());
        assertEquals("name", ((HelpLine) helpLines.get(0)).getHeader());
        assertEquals("name", ((HelpLine) helpLines.get(1)).getHeader());
        assertEquals("option1 [ARG]", ((HelpLine) helpLines.get(2)).getHeader());
        assertEquals("ARG", ((HelpLine) helpLines.get(3)).getHeader());
        assertEquals("", ((HelpLine) helpLines.get(4)).getHeader());
    }

    private Option createOption(String name) {
        OptionImpl option = new OptionImpl(0, name, true, null);
        return option;
    }

    private Argument createArgument(String name) {
        ArgumentImpl argument = new ArgumentImpl(0, name, true, null);
        return argument;
    }

    private WriteableCommandLine createCommandLine() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        return commandLine;
    }

    private SortedMap getOptionMap(List options) {
        SortedMap optionMap = new TreeMap(ReverseStringComparator.getInstance());

        for (Object option : options.toArray()) {
            Option optionObj = (Option) option;
            optionMap.put(optionObj.getLongOpt(), optionObj);
        }

        return optionMap;
    }

    private Set getPrefixes(List options) {
        Set prefixes = new HashSet();

        for (Object option : options.toArray()) {
            Option optionObj = (Option) option;
            prefixes.addAll(optionObj.getPrefixes());
        }

        return prefixes;
    }
}