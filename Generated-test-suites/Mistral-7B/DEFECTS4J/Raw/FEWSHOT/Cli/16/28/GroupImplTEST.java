package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

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
    public void testGroupImpl_basicConstruction() {
        final List options = new ArrayList();
        final Group group = new GroupImpl(options, "test", "test group", 1, 1);
        assertEquals("test", group.getPreferredName());
        assertEquals("test group", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertSame(options, group.getOptions());
        assertSame(Collections.emptyList(), group.getAnonymous());
    }

    @Test
    public void testGroupImpl_constructionWithAnonymous() {
        final List options = new ArrayList();
        final Argument argument = new ArgumentImpl("arg", "test arg", true, "defaultValue");
        options.add(argument);
        final Group group = new GroupImpl(options, "test", "test group", 1, 1);
        assertEquals(1, group.getAnonymous().size());
        assertEquals(argument, group.getAnonymous().get(0));
    }

    @Test
    public void testGroupImpl_constructionWithOptions() {
        final List options = new ArrayList();
        final Option option1 = new OptionImpl("opt1", "test opt1", true, "defaultValue1");
        final Option option2 = new OptionImpl("opt2", "test opt2", true, "defaultValue2");
        options.add(option1);
        options.add(option2);
        final Group group = new GroupImpl(options, "test", "test group", 1, 1);
        assertEquals(2, group.getOptions().size());
        assertEquals(option1, group.getOptions().get(0));
        assertEquals(option2, group.getOptions().get(1));
    }

    @Test
    public void testGroupImpl_constructionWithPrefixes() {
        final List options = new ArrayList();
        final Option option1 = new OptionImpl("opt1", "test opt1", true, "defaultValue1")
                .setPrefixes(Collections.singletonList("prefix1"));
        final Option option2 = new OptionImpl("opt2", "test opt2", true, "defaultValue2")
                .setPrefixes(Collections.singletonList("prefix2"));
        options.add(option1);
        options.add(option2);
        final Group group = new GroupImpl(options, "test", "test group", 1, 1);
        assertEquals(2, group.getPrefixes().size());
        assertTrue(group.getPrefixes().contains("prefix1"));
        assertTrue(group.getPrefixes().contains("prefix2"));
    }

    @Test
    public void testGroupImpl_constructionWithOptionsAndAnonymous() {
        final List options = new ArrayList();
        final Option option1 = new OptionImpl("opt1", "test opt1", true, "defaultValue1");
        final Option option2 = new OptionImpl("opt2", "test opt2", true, "defaultValue2");
        final Argument argument = new ArgumentImpl("arg", "test arg", true, "defaultValue");
        options.add(option1);
        options.add(option2);
        options.add(argument);
        final Group group = new GroupImpl(options, "test", "test group", 1, 1);
        assertEquals(3, group.getOptions().size());
        assertEquals(option1, group.getOptions().get(0));
        assertEquals(option2, group.getOptions().get(1));
        assertEquals(argument, group.getAnonymous().get(0));
    }

    @Test
    public void testGroupImpl_canProcess() {
        final List options = new ArrayList();
        final Option option1 = new OptionImpl("opt1", "test opt1", true, "defaultValue1");
        options.add(option1);
        final Group group = new GroupImpl(options, "test", "test group", 1, 1);
        assertFalse(group.canProcess(null, ""));
        assertFalse(group.canProcess(null, "opt1"));
        assertFalse(group.canProcess(null, "opt2"));
        assertFalse(group.canProcess(null, "arg"));
        assertFalse(group.canProcess(null, "argArg"));

        final WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        commandLine.addOption(option1);

        assertFalse(group.canProcess(commandLine, ""));
        assertFalse(group.canProcess(commandLine, "opt1"));
        assertFalse(group.canProcess(commandLine, "opt2"));
        assertFalse(group.canProcess(commandLine, "arg"));
        assertFalse(group.canProcess(commandLine, "argArg"));
        assertFalse(group.canProcess(commandLine, "opt1 arg"));
        assertFalse(group.canProcess(commandLine, "arg opt1"));
        assertFalse(group.canProcess(commandLine, "opt1 opt1"));

        assertTrue(group.canProcess(commandLine, "opt1 arg"));
        assertTrue(group.canProcess(commandLine, "arg opt1"));
        assertTrue(group.canProcess(commandLine, "opt1 opt1"));
    }

    @Test
    public void testGroupImpl_getTriggers() {
        final List options = new ArrayList();
        final Option option1 = new OptionImpl("opt1", "test opt1", true, "defaultValue1");
        options.add(option1);
        final Group group = new GroupImpl(options, "test", "test group", 1, 1);
        assertEquals(0, group.getTriggers().size());

        final List options2 = new ArrayList();
        final Option option1_ = new OptionImpl("opt1", "test opt1", true, "defaultValue1")
                .setTriggers(Collections.singletonList("opt1Trigger"));
        final Option option2 = new OptionImpl("opt2", "test opt2", true, "defaultValue2");
        options2.add(option1_);
        options2.add(option2);
        final Group group2 = new GroupImpl(options2, "test", "test group", 1, 1);
        assertEquals(1, group2.getTriggers().size());
        assertEquals("opt1Trigger", group2.getTriggers().iterator().next());
    }

    @Test
    public void testGroupImpl_process() {
        final List options = new ArrayList();
        final Option option1 = new OptionImpl("opt1", "test opt1", true, "defaultValue1");
        final Option option2 = new OptionImpl("opt2", "test opt2", true, "defaultValue2");
        options.add(option1);
        options.add(option2);
        final Group group = new GroupImpl(options, "test", "test group", 1, 1);

        final WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        final ListIterator arguments = commandLine.getArguments();

        assertThrows(OptionException.class, () -> group.process(commandLine, arguments));

        arguments.add("opt1");
        assertThrows(OptionException.class, () -> group.process(commandLine, arguments));

        arguments.add("opt1 arg");
        group.process(commandLine, arguments);
        assertEquals(2, commandLine.getOptions().size());
        assertEquals(option1, commandLine.getOption(option1.getOpt()));
        assertEquals(option2, commandLine.getOption(option2.getOpt()));

        arguments.add("arg");
        assertThrows(OptionException.class, () -> group.process(commandLine, arguments));

        arguments.add("arg arg");
        assertThrows(OptionException.class, () -> group.process(commandLine, arguments));

        arguments.add("opt1 opt1");
        assertThrows(OptionException.class, () -> group.process(commandLine, arguments));

        arguments.add("opt1 arg arg");
        group.process(commandLine, arguments);
        assertEquals(4, commandLine.getOptions().size());
        assertEquals(option1, commandLine.getOption(option1.getOpt()));
        assertEquals(option2, commandLine.getOption(option2.getOpt()));
        assertEquals(option1, commandLine.getOption("opt1"));
        assertEquals(option1, commandLine.getOption("opt1 arg"));
    }

    @Test
    public void testGroupImpl_validate() {
        final List options = new ArrayList();
        final Option option1 = new OptionImpl("opt1", "test opt1", true, "defaultValue1");
        final Option option2 = new OptionImpl("opt2", "test opt2", true, "defaultValue2");
        options.add(option1);
        options.add(option2);
        final Group group = new GroupImpl(options, "test", "test group", 1, 1);

        final WriteableCommandLine commandLine = new DefaultWriteableCommandLine();

        assertThrows(OptionException.class, () -> group.validate(commandLine));

        commandLine.addOption(option1);
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        group.validate(commandLine);

        commandLine.removeOption(option2.getOpt());
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        commandLine.removeOption(option1.getOpt());
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        commandLine.addOption(option1);
        commandLine.addOption(option1);
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        group.validate(commandLine);

        commandLine.addOption(option1);
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        group.validate(commandLine);
    }

    @Test
    public void testGroupImpl_appendUsage() {
        final List options = new ArrayList();
        final Option option1 = new OptionImpl("opt1", "test opt1", true, "defaultValue1")
                .setLongOpt("long-opt1");
        final Option option2 = new OptionImpl("opt2", "test opt2", false, "defaultValue2")
                .setLongOpt("long-opt2");
        options.add(option1);
        options.add(option2);
        final Group group = new GroupImpl(options, "test", "test group", 1, 1);

        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, null, null);
        assertEquals("test group:\n", buffer.toString());

        final Set helpSettings = new HashSet(DisplaySetting.ALL);
        group.appendUsage(buffer, helpSettings, null);
        assertEquals("test group:\n" +
                "test group\n" +
                "  test group\n" +
                "  --opt1 <value>  test opt1 (=defaultValue1)\n" +
                "  --long-opt1 <value>  test opt1\n" +
                "  --opt2  test opt2 (=defaultValue2)\n" +
                "  --long-opt2  test opt2\n", buffer.toString());

        group.appendUsage(buffer, helpSettings, ReverseStringComparator.getInstance());
        assertEquals("test group:\n" +
                "test group\n" +
                "  test opt2 (=defaultValue2)\n" +
                "  --long-opt2  test opt2\n" +
                "  test opt1 (=defaultValue1)\n" +
                "  --long-opt1 <value>  test opt1\n", buffer.toString());
    }

    @Test
    public void testGroupImpl_helpLines() {
        final List options = new ArrayList();
        final Option option1 = new OptionImpl("opt1", "test opt1", true, "defaultValue1")
                .setLongOpt("long-opt1");
        final Option option2 = new OptionImpl("opt2", "test opt2", false, "defaultValue2")
                .setLongOpt("long-opt2");
        options.add(option1);
        options.add(option2);
        final Group group = new GroupImpl(options, "test", "test group", 1, 1);

        final List helpLines = group.helpLines(1, null, null);
        assertEquals(1, helpLines.size());
        assertEquals("test group:\n", ((HelpLine) helpLines.get(0)).getHeader());

        final List helpLines2 = group.helpLines(1, null, ReverseStringComparator.getInstance());
        assertEquals(1, helpLines2.size());
        assertEquals("test group:\n", ((HelpLine) helpLines2.get(0)).getHeader());

        final Set helpSettings = new HashSet(DisplaySetting.ALL);
        helpLines2 = group.helpLines(1, helpSettings, null);
        assertEquals(2, helpLines2.size());
        assertEquals("test group:\n" +
                "test group\n" +
                "  test opt1 (=defaultValue1)\n" +
                "      --long-opt1 <value>  test opt1\n" +
                "  test opt2 (=defaultValue2)\n" +
                "      --long-opt2  test opt2\n", ((HelpLine) helpLines2.get(0)).getHelpText());
        assertEquals("", ((HelpLine) helpLines2.get(1)).getHelpText());
    }
}