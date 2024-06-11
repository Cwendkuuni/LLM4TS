package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

    @Test
    public void testGroupImpl() {
        final List options = new ArrayList();
        options.add(getOptionWithName("opt1"));
        options.add(getOptionWithName("opt2"));
        final Group group = new GroupImpl(options, null, null, 1, 1, false);

        assertEquals("opt1, opt2", group.getPreferredName());
        assertEquals("", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertSame(Collections.emptyList(), group.getAnonymous());
        assertSame(Collections.emptyMap(), group.getOptionMap());
        assertSame(Collections.emptySet(), group.getPrefixes());

        assertTrue(group.canProcess(getCommandLine(), "opt1"));
        assertTrue(group.canProcess(getCommandLine(), "opt2"));
        assertFalse(group.canProcess(getCommandLine(), "unknown"));

        assertEquals(Collections.singleton("opt1"), group.getTriggers());

        group.process(getCommandLine(), getListIterator("opt1"));
        assertEquals(1, getCommandLine().getNumOptions());
        assertEquals("opt1", getCommandLine().getOptionValue("opt1"));

        group.validate(getCommandLine());

        group.appendUsage(new StringBuilder(), Collections.emptySet(), null);
        assertEquals("opt1, opt2", GroupImpl.getDefaultUsage(group));

        final List helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(2, helpLines.size());
        for (final Object o : helpLines) {
            assertEquals(0, ((HelpLine) o).getHelpLineNumber());
        }
    }

    @Test
    public void testGroupImplWithAnonymousOption() {
        final List options = new ArrayList();
        options.add(getOptionWithName("opt1"));
        options.add(getArgumentOptionWithName("arg1"));
        final Group group = new GroupImpl(options, null, null, 1, 1, false);

        assertEquals("opt1", group.getPreferredName());
        assertEquals("", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Collections.singleton(getArgumentOptionWithName("arg1")), group.getAnonymous());
        assertSame(Collections.emptyMap(), group.getOptionMap());
        assertSame(Collections.emptySet(), group.getPrefixes());

        assertTrue(group.canProcess(getCommandLine(), "opt1"));
        assertTrue(group.canProcess(getCommandLine(), "arg1"));
        assertFalse(group.canProcess(getCommandLine(), "unknown"));

        assertEquals(Collections.singleton("opt1"), group.getTriggers());

        group.process(getCommandLine(), getListIterator("opt1"));
        assertEquals(1, getCommandLine().getNumOptions());
        assertEquals("opt1", getCommandLine().getOptionValue("opt1"));

        group.process(getCommandLine(), getListIterator("arg1"));
        assertEquals(2, getCommandLine().getNumOptions());
        assertEquals("arg1", getCommandLine().getOptionValue("arg1"));

        group.validate(getCommandLine());

        group.appendUsage(new StringBuilder(), Collections.emptySet(), null);
        assertEquals("opt1 [ARG]", GroupImpl.getDefaultUsage(group));

        final List helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(3, helpLines.size());
        for (final Object o : helpLines) {
            assertEquals(0, ((HelpLine) o).getHelpLineNumber());
        }
    }

    @Test
    public void testGroupImplWithOptionAndAnonymousOption() {
        final List options = new ArrayList();
        options.add(getOptionWithName("opt1"));
        options.add(getOptionWithName("opt2"));
        options.add(getArgumentOptionWithName("arg1"));
        final Group group = new GroupImpl(options, null, null, 1, 1, false);

        assertEquals("opt1, opt2", group.getPreferredName());
        assertEquals("", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Collections.singleton(getArgumentOptionWithName("arg1")), group.getAnonymous());
        assertSame(Collections.emptyMap(), group.getOptionMap());
        assertSame(Collections.emptySet(), group.getPrefixes());

        assertTrue(group.canProcess(getCommandLine(), "opt1"));
        assertTrue(group.canProcess(getCommandLine(), "opt2"));
        assertTrue(group.canProcess(getCommandLine(), "arg1"));
        assertFalse(group.canProcess(getCommandLine(), "unknown"));

        assertEquals(Collections.singleton("opt1"), group.getTriggers());

        group.process(getCommandLine(), getListIterator("opt1"));
        assertEquals(1, getCommandLine().getNumOptions());
        assertEquals("opt1", getCommandLine().getOptionValue("opt1"));

        group.process(getCommandLine(), getListIterator("opt2"));
        assertEquals(2, getCommandLine().getNumOptions());
        assertEquals("opt2", getCommandLine().getOptionValue("opt2"));

        group.process(getCommandLine(), getListIterator("arg1"));
        assertEquals(3, getCommandLine().getNumOptions());
        assertEquals("arg1", getCommandLine().getOptionValue("arg1"));

        group.validate(getCommandLine());

        group.appendUsage(new StringBuilder(), Collections.emptySet(), null);
        assertEquals("opt1, opt2 [ARG]", GroupImpl.getDefaultUsage(group));

        final List helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(4, helpLines.size());
        for (final Object o : helpLines) {
            assertEquals(0, ((HelpLine) o).getHelpLineNumber());
        }
    }

    @Test
    public void testGroupImplWithMinMax() {
        final List options = new ArrayList();
        options.add(getOptionWithName("opt1"));
        options.add(getOptionWithName("opt2"));
        final Group group = new GroupImpl(options, null, null, 2, 3, false);

        assertEquals("opt1, opt2", group.getPreferredName());
        assertEquals("", group.getDescription());
        assertEquals(2, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertSame(Collections.emptyList(), group.getAnonymous());
        assertSame(Collections.emptyMap(), group.getOptionMap());
        assertSame(Collections.emptySet(), group.getPrefixes());

        assertFalse(group.canProcess(getCommandLine(), "opt1"));
        assertFalse(group.canProcess(getCommandLine(), "opt2"));
        assertFalse(group.canProcess(getCommandLine(), "unknown"));

        assertEquals(Collections.singleton("opt1"), group.getTriggers());

        group.process(getCommandLine(), getListIterator("opt1"));
        group.process(getCommandLine(), getListIterator("opt1"));
        assertEquals(2, getCommandLine().getNumOptions());
        assertEquals("opt1, opt1", getCommandLine().getOptionsDescription());

        assertTrue(group.canProcess(getCommandLine(), "opt2"));
        assertEquals(3, getCommandLine().getNumOptions());
        assertEquals("opt1, opt1, opt2", getCommandLine().getOptionsDescription());

        group.validate(getCommandLine());

        group.appendUsage(new StringBuilder(), Collections.emptySet(), null);
        assertEquals("[opt1, opt1, opt2]", GroupImpl.getDefaultUsage(group));

        final List helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(3, helpLines.size());
        for (final Object o : helpLines) {
            assertEquals(0, ((HelpLine) o).getHelpLineNumber());
        }
    }

    @Test
    public void testGroupImplWithRequiredOptions() {
        final List options = new ArrayList();
        options.add(getOptionWithName("opt1").setRequired(true));
        options.add(getOptionWithName("opt2").setRequired(true));
        final Group group = new GroupImpl(options, null, null, 1, 1, true);

        assertEquals("opt1, opt2", group.getPreferredName());
        assertEquals("", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertSame(Collections.emptyList(), group.getAnonymous());
        assertSame(Collections.emptyMap(), group.getOptionMap());
        assertSame(Collections.emptySet(), group.getPrefixes());

        assertFalse(group.canProcess(getCommandLine(), "opt1"));
        assertFalse(group.canProcess(getCommandLine(), "opt2"));
        assertFalse(group.canProcess(getCommandLine(), "unknown"));

        assertEquals(Collections.singleton("opt1"), group.getTriggers());

        try {
            group.process(getCommandLine(), getListIterator("opt1"));
            fail();
        } catch (final OptionException e) {
            // expected
        }

        try {
            group.process(getCommandLine(), getListIterator("opt2"));
            fail();
        } catch (final OptionException e) {
            // expected
        }

        try {
            group.validate(getCommandLine());
            fail();
        } catch (final OptionException e) {
            // expected
        }

        group.appendUsage(new StringBuilder(), Collections.emptySet(), null);
        assertEquals("[--opt1, --opt2]", GroupImpl.getDefaultUsage(group));

        final List helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(2, helpLines.size());
        for (final Object o : helpLines) {
            assertEquals(0, ((HelpLine) o).getHelpLineNumber());
        }
    }

    @Test
    public void testGroupImplWithPrefixes() {
        final List options = new ArrayList();
        options.add(getOptionWithName("opt1").setPrefix("p"));
        options.add(getOptionWithName("opt2").setPrefix("q"));
        final Group group = new GroupImpl(options, null, null, 1, 1, false);

        assertEquals("opt1, opt2", group.getPreferredName());
        assertEquals("", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertSame(Collections.emptyList(), group.getAnonymous());
        assertSame(Collections.emptyMap(), group.getOptionMap());
        assertSame(Collections.singleton("p", "q"), group.getPrefixes());

        assertTrue(group.canProcess(getCommandLineWithPrefix("p"), "opt1"));
        assertFalse(group.canProcess(getCommandLineWithPrefix("p"), "opt2"));
        assertFalse(group.canProcess(getCommandLineWithPrefix("p"), "unknown"));

        assertTrue(group.canProcess(getCommandLineWithPrefix("q"), "opt2"));
        assertFalse(group.canProcess(getCommandLineWithPrefix("q"), "opt1"));
        assertFalse(group.canProcess(getCommandLineWithPrefix("q"), "unknown"));

        assertEquals(Collections.singleton("opt1"), group.getTriggers());

        try {
            group.process(getCommandLineWithPrefix("p"), getListIterator("opt2"));
            fail();
        } catch (final OptionException e) {
            // expected
        }

        try {
            group.process(getCommandLineWithPrefix("q"), getListIterator("opt1"));
            fail();
        } catch (final OptionException e) {
            // expected
        }

        group.validate(getCommandLineWithPrefix("p"));

        group.appendUsage(new StringBuilder(), Collections.emptySet(), null);
        assertEquals("[--popt1, --qopt2]", GroupImpl.getDefaultUsage(group));

        final List helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(2, helpLines.size());
        for (final Object o : helpLines) {
            assertEquals(0, ((HelpLine) o).getHelpLineNumber());
        }
    }

    @Test
    public void testGroupImplWithMultiplePrefixes() {
        final List options = new ArrayList();
        options.add(getOptionWithName("opt1").setPrefix("p"));
        options.add(getOptionWithName("opt1").setPrefix("q"));
        final Group group = new GroupImpl(options, null, null, 1, 1, false);

        assertEquals("opt1, opt1", group.getPreferredName());
        assertEquals("", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertSame(Collections.emptyList(), group.getAnonymous());
        assertSame(Collections.emptyMap(), group.getOptionMap());
        assertSame(Collections.singleton("p", "q"), group.getPrefixes());

        assertTrue(group.canProcess(getCommandLineWithPrefix("p"), "opt1"));
        assertTrue(group.canProcess(getCommandLineWithPrefix("q"), "opt1"));
        assertFalse(group.canProcess(getCommandLineWithPrefix("p"), "unknown"));

        try {
            group.process(getCommandLineWithPrefix("p"), getListIterator("unknown"));
            fail();
        } catch (final OptionException e) {
            // expected
        }

        try {
            group.validate(getCommandLineWithPrefix("p"));
            fail();
        } catch (final OptionException e) {
            // expected
        }

        group.appendUsage(new StringBuilder(), Collections.emptySet(), null);
        assertEquals("[--popt1, --qopt1]", GroupImpl.getDefaultUsage(group));

        final List helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(2, helpLines.size());
        for (final Object o : helpLines) {
            assertEquals(0, ((HelpLine) o).getHelpLineNumber());
        }
    }

    private static WriteableCommandLine getCommandLine() {
        return new DefaultCommandLine();
    }

    private static WriteableCommandLine getCommandLineWithPrefix(final String prefix) {
        final DefaultCommandLine cmd = new DefaultCommandLine();
        cmd.setPrefixes(Collections.singleton(prefix));
        return cmd;
    }

    private static ListIterator getListIterator(final String optionName) {
        return getCommandLine().getArguments().listIterator();
    }

    private static Option getOptionWithName(final String optionName) {
        return new OptionBuilder()
                .withName(optionName)
                .withDescription("option")
                .create();
    }

    private static Argument getArgumentOptionWithName(final String optionName) {
        return new ArgumentBuilder()
                .withName(optionName)
                .withDescription("argument")
                .create();
    }
}

class ReverseStringComparator implements Comparator<String> {
    private static final Comparator INSTANCE = new ReverseStringComparator();

    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2) * -1;
    }

    public static Comparator getInstance() {
        return INSTANCE;
    }
}