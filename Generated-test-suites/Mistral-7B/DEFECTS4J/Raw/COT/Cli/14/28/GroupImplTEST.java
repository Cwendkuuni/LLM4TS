package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

/**
 * Test for the GroupImpl class.
 */
public class GroupImplTest {
    // 

    @Test
    public void testGroupImplConstructor() throws OptionException {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "opt1-arg"));
        options.add(new Option("opt2", "Option 2", false));
        options.add(new Option("opt3", "Option 3", false));
        options.add(new Option("opt4", "Option 4", false));
        options.add(new Option("opt5", "Option 5", false));
        options.add(new Argument("arg1", "Argument 1", true));

        final Group group = new GroupImpl(options, "Group", "Description of the group", 1, 5);

        assertEquals("Group", group.getPreferredName());
        assertEquals("Description of the group", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(5, group.getMaximum());
        assertEquals(5, group.getOptions().size());
        assertEquals(1, group.getAnonymous().size());
        assertFalse(group.canProcess(null, null));
        assertFalse(group.canProcess("opt1", null));
        assertFalse(group.canProcess("opt1", "opt1-arg"));
        assertFalse(group.canProcess("opt1arg", null));
        assertTrue(group.canProcess("opt1arg", "opt1-arg"));
        assertFalse(group.canProcess("opt2", null));
        assertFalse(group.canProcess("opt2", "opt1-arg"));
        assertTrue(group.canProcess("opt2", "arg1"));
        assertTrue(group.canProcess("opt3", null));
        assertTrue(group.canProcess("opt3", ""));
        assertTrue(group.canProcess("opt3", "arg1"));
        assertTrue(group.canProcess("opt4", null));
        assertTrue(group.canProcess("opt4", ""));
        assertTrue(group.canProcess("opt4", "arg1"));
        assertTrue(group.canProcess("opt5", null));
        assertTrue(group.canProcess("opt5", ""));
        assertTrue(group.canProcess("opt5", "arg1"));
        assertFalse(group.canProcess("arg1", null));
        assertFalse(group.canProcess("arg1", "opt5"));
        assertTrue(group.canProcess("arg1", "arg1"));

        final Set prefixes = group.getPrefixes();
        assertEquals(Collections.emptySet(), prefixes);

        final Set triggers = group.getTriggers();
        assertEquals(Collections.emptySet(), triggers);

        final Map optionMap = group.getOptionMap();
        assertEquals(5, optionMap.size());
        assertEquals("opt1", optionMap.get("opt1"));
        assertEquals("opt2", optionMap.get("opt2"));
        assertEquals("opt3", optionMap.get("opt3"));
        assertEquals("opt4", optionMap.get("opt4"));
        assertEquals("opt5", optionMap.get("opt5"));

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("opt1", true);
        commandLine.addOption("opt2");
        commandLine.addOption("opt3");
        commandLine.addOption("opt4");
        commandLine.addOption("opt5");
        commandLine.addOption("arg1", "value");

        group.process(commandLine, commandLine.iterator());

        assertEquals("opt1-arg", commandLine.getOptionValue("opt1"));
        assertEquals(true, commandLine.hasOption("opt1"));
        assertEquals("value", commandLine.getOptionValue("arg1"));
        assertEquals(true, commandLine.hasOption("arg1"));
        assertFalse(commandLine.hasOption("opt2-arg"));
        assertFalse(commandLine.hasOption("opt3-arg"));
        assertFalse(commandLine.hasOption("opt4-arg"));
        assertFalse(commandLine.hasOption("opt5-arg"));

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testGroupImplConstructorInvalid() throws OptionException {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "opt1-arg"));
        options.add(new Option("opt2", "Option 2", false));
        options.add(new Option("opt2", "Option 2", false));

        new GroupImpl(options, "Group", "Description of the group", 1, 5);
    }

    @Test(expected = OptionException.class)
    public void testGroupImplProcessInvalid() throws OptionException {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "opt1-arg"));
        options.add(new Option("opt1", "Option 1", false));
        options.add(new Option("opt2", "Option 2", false));

        final Group group = new GroupImpl(options, "Group", "Description of the group", 1, 5);

        final WriteableCommandLine commandLine = new WriteableCommandLine();

        group.process(commandLine, commandLine.iterator());
    }

    @Test(expected = OptionException.class)
    public void testGroupImplValidateInvalid() throws OptionException {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "opt1-arg"));
        options.add(new Option("opt1", "Option 1", false));

        final Group group = new GroupImpl(options, "Group", "Description of the group", 1, 5);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("opt1", true);
        commandLine.addOption("opt1", true);

        group.validate(commandLine);
    }

    @Test
    public void testGroupImplUsage() {
        final List options = new ArrayList();
        options.add(new Option("opt1", "Option 1", true, "opt1-arg"));
        options.add(new Option("opt2", "Option 2", false));
        options.add(new Option("opt3", "Option 3", false));
        options.add(new Option("opt4", "Option 4", false));
        options.add(new Option("opt5", "Option 5", false));
        options.add(new Argument("arg1", "Argument 1", true));

        final Group group = new GroupImpl(options, "Group", "Description of the group", 1, 5);

        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, DisplaySetting.ALL, null);

        final List helpLines = group.helpLines(0, DisplaySetting.ALL, null);

        assertEquals("Group\n" +
                     "Description of the group\n" +
                     "\n" +
                     "Usage:\n" +
                     "  [--opt1OPT1 opt1-arg]\n" +
                     "  [--opt2]\n" +
                     "  [--opt3]\n" +
                     "  [--opt4]\n" +
                     "  [--opt5]\n" +
                     "  [--arg1 ARG1]\n", buffer.toString());

        for (final Object helpLine : helpLines) {
            final HelpLine line = (HelpLine) helpLine;
            assertEquals("Group", line.getOptionSpec().getLongOpt());
            assertEquals("describes the group", line.getShortDescription());
            assertEquals("Option 1", line.getLongOpt());
            assertEquals("Option 1", line.getOpt());
            assertEquals("Specifies option 1", line.getLongDescription());
            assertEquals(true, line.hasOptionalArg());
            assertEquals("opt1-arg", line.getOptionArg());
            assertEquals("opt1-arg", line.getOptArg());
            assertEquals("", line.getValues());
            assertEquals(DisplaySetting.UNLIMITED, line.getWidth());
            assertEquals(null, line.getDisplaySetting());
            assertEquals(null, line.getHelpSetting());
            assertFalse(line.isHidden());
            assertFalse(line.isHiddenWithDefault());
            assertFalse(line.isHiddenWithArgs());
            assertFalse(line.isHiddenWithValues());
            assertFalse(line.isHiddenWithDefaultAndValues());
            assertFalse(line.isHiddenWithOptionalArg());
            assertFalse(line.isHiddenWithOptionalArgAndValues());
            assertFalse(line.hasArgName());
            assertFalse(line.isHiddenWithArgName());
            assertEquals("opt1", line.getOptionSpec().getAlias());
            assertEquals(DisplaySetting.NOT_REQUIRED, line.getDisplaySetting());
            assertEquals(DisplaySetting.NOT_REQUIRED, line.getHelpSetting());
        }
    }

    // 
}