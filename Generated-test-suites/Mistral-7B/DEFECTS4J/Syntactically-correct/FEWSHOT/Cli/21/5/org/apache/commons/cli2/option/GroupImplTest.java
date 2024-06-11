package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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

    @Test
    public void testGroupImpl() {
        final List options = new ArrayList();
        options.add(createOption("--option1", "option1", false, null));
        options.add(createOption("--option2", "option2", true, "value"));
        options.add(createOption("--option3", "option3", false, null));

        final Group group = new GroupImpl(options, "mygroup", "My Group", 1, Integer.MAX_VALUE, false);
        assertNull(group.getPreferredName());
        assertSame(group, group.getParent());
        assertEquals(3, group.getOptions().size());
        assertTrue(group.canProcess(createCommandLine("--option1"), "--option1"));
        assertTrue(group.canProcess(createCommandLine("--option2 value"), "--option2 value"));
        assertTrue(group.canProcess(createCommandLine("--option3"), "--option3"));
        assertTrue(group.looksLikeOption(createCommandLine("--option1"), "--option1"));
        assertFalse(group.looksLikeOption(createCommandLine("--option1"), "--option2"));
        assertFalse(group.looksLikeOption(createCommandLine("--option3"), "--option2 value"));
        assertFalse(group.looksLikeOption(createCommandLine("--unknown"), "--unknown"));
        assertNull(group.findOption("--unknown"));
        assertEquals(2, group.getMinimum());
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
        assertFalse(group.isRequired());
        assertEquals("My Group", group.getDescription());
        assertNull(group.getName());
        assertSame(new ArrayList(), group.getAnonymous());
        assertEquals(Collections.unmodifiableMap(new TreeMap(GroupImplTest.reverseStringComparator())), group.getOptionMap());
        assertEquals(Collections.unmodifiableSet(new HashSet()), group.getPrefixes());
    }

    @Test
    public void testGroupImplWithAnonymous() {
        final List options = new ArrayList();
        options.add(createOption("--option1", "option1", false, null));
        options.add(createAnonymousArgument("--arg1", "arg1"));
        options.add(createOption("--option2", "option2", true, "value"));
        options.add(createAnonymousArgument("--arg2", "arg2"));

        final Group group = new GroupImpl(options, "mygroup", "My Group", 1, Integer.MAX_VALUE, false);
        assertEquals(3, group.getOptions().size());
        assertEquals(2, group.getAnonymous().size());
        assertTrue(group.canProcess(createCommandLine("--arg1"), "--arg1"));
        assertTrue(group.canProcess(createCommandLine("--arg2"), "--arg2"));
        assertTrue(group.looksLikeOption(createCommandLine("--option1"), "--option1"));
        assertFalse(group.looksLikeOption(createCommandLine("--option1"), "--arg1"));
        assertFalse(group.looksLikeOption(createCommandLine("--arg1"), "--option1"));
        assertFalse(group.looksLikeOption(createCommandLine("--arg2"), "--option1"));
        assertFalse(group.looksLikeOption(createCommandLine("--option2 value"), "--arg1"));
        assertFalse(group.looksLikeOption(createCommandLine("--arg2"), "--arg1"));
        assertFalse(group.looksLikeOption(createCommandLine("--unknown"), "--unknown"));
        assertNull(group.findOption("--unknown"));
        assertEquals(2, group.getMinimum());
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
        assertFalse(group.isRequired());
        assertEquals("My Group", group.getDescription());
        assertNull(group.getName());
    }

    @Test
    public void testGroupImplWithMinMax() {
        final List options = new ArrayList();
        options.add(createOption("--option1", "option1", false, null));
        options.add(createOption("--option2", "option2", true, "value"));
        options.add(createOption("--option3", "option3", false, null));

        final Group group = new GroupImpl(options, "mygroup", "My Group", 2, 3, false);
        assertFalse(group.canProcess(createCommandLine("--option1"), "--option1"));
        assertFalse(group.canProcess(createCommandLine("--option1 --option1"), "--option1 --option1"));
        assertFalse(group.canProcess(createCommandLine("--option1 --option1 --option1"), "--option1 --option1 --option1"));
        assertTrue(group.canProcess(createCommandLine("--option1 --option2"), "--option1 --option2"));
        assertTrue(group.canProcess(createCommandLine("--option1 --option2 --option3"), "--option1 --option2 --option3"));
        assertFalse(group.canProcess(createCommandLine("--option1 --option2 --option3 --option3"), "--option1 --option2 --option3 --option3"));
        assertFalse(group.canProcess(createCommandLine("--option1 --option2 --option3 --option4"), "--option1 --option2 --option3 --option4"));
        assertEquals(2, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertFalse(group.isRequired());
    }

    @Test
    public void testGroupImplWithRequired() {
        final List options = new ArrayList();
        options.add(createOption("--option1", "option1", false, null));
        options.add(createOption("--option2", "option2", true, "value"));
        options.add(createOption("--option3", "option3", false, null));

        final Group group = new GroupImpl(options, "mygroup", "My Group", 1, Integer.MAX_VALUE, true);
        assertFalse(group.canProcess(createCommandLine(""), ""));
        assertTrue(group.canProcess(createCommandLine("--option1"), "--option1"));
        assertTrue(group.canProcess(createCommandLine("--option1 --option2"), "--option1 --option2"));
        assertTrue(group.canProcess(createCommandLine("--option1 --option2 value"), "--option1 --option2 value"));
        assertTrue(group.canProcess(createCommandLine("--option1 --option2 value --option3"), "--option1 --option2 value --option3"));
        assertFalse(group.canProcess(createCommandLine("--option1 --option2 value --option3 --option4"), "--option1 --option2 value --option3 --option4"));
        assertFalse(group.canProcess(createCommandLine("--option1 --option2 value --option3 --option4 --option5"), "--option1 --option2 value --option3 --option4 --option5"));
        assertEquals(1, group.getMinimum());
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
        assertTrue(group.isRequired());
    }

    @Test
    public void testGroupImplWithAnonymousAndMinMax() {
        final List options = new ArrayList();
        options.add(createOption("--option1", "option1", false, null));
        options.add(createAnonymousArgument("--arg1", "arg1"));
        options.add(createOption("--option2", "option2", true, "value"));
        options.add(createAnonymousArgument("--arg2", "arg2"));

        final Group group = new GroupImpl(options, "mygroup", "My Group", 2, 3, false);
        assertEquals(3, group.getOptions().size());
        assertEquals(2, group.getAnonymous().size());
        assertTrue(group.canProcess(createCommandLine("--arg1"), "--arg1"));
        assertTrue(group.canProcess(createCommandLine("--arg2"), "--arg2"));
        assertTrue(group.canProcess(createCommandLine("--option1 --option2"), "--option1 --option2"));
        assertTrue(group.canProcess(createCommandLine("--option1 --option2 --arg1"), "--option1 --option2 --arg1"));
        assertTrue(group.canProcess(createCommandLine("--option1 --option2 --arg1 --option3"), "--option1 --option2 --arg1 --option3"));
        assertTrue(group.canProcess(createCommandLine("--option1 --option2 --arg1 --option3 --arg2"), "--option1 --option2 --arg1 --option3 --arg2"));
        assertFalse(group.canProcess(createCommandLine("--option1 --option2 --arg1 --option3 --option4"), "--option1 --option2 --arg1 --option3 --option4"));
        assertFalse(group.canProcess(createCommandLine("--option1 --option2 --arg1 --option3 --option4 --option5"), "--option1 --option2 --arg1 --option3 --option4 --option5"));
        assertEquals(2, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertFalse(group.isRequired());
    }

    @Test
    public void testGroupImplWithAnonymousAndRequired() {
        final List options = new ArrayList();
        options.add(createOption("--option1", "option1", false, null));
        options.add(createAnonymousArgument("--arg1", "arg1"));
        options.add(createOption("--option2", "option2", true, "value"));
        options.add(createAnonymousArgument("--arg2", "arg2"));

        final Group group = new GroupImpl(options, "mygroup", "My Group", 1, Integer.MAX_VALUE, true);
        assertEquals(3, group.getOptions().size());
        assertEquals(2, group.getAnonymous().size());
        assertTrue(group.canProcess(createCommandLine(""), ""));
        assertTrue(group.canProcess(createCommandLine("--arg1"), "--arg1"));
        assertTrue(group.canProcess(createCommandLine("--arg2"), "--arg2"));
        assertTrue(group.canProcess(createCommandLine("--option1"), "--option1"));
        assertTrue(group.canProcess(createCommandLine("--option1 --arg1"), "--option1 --arg1"));
        assertTrue(group.canProcess(createCommandLine("--option1 --arg1 --option2"), "--option1 --arg1 --option2"));
        assertTrue(group.canProcess(createCommandLine("--option1 --arg1 --option2 value"), "--option1 --arg1 --option2 value"));
        assertTrue(group.canProcess(createCommandLine("--option1 --arg1 --option2 value --arg2"), "--option1 --arg1 --option2 value --arg2"));
        assertFalse(group.canProcess(createCommandLine("--option1 --arg1 --option2 value --option3"), "--option1 --arg1 --option2 value --option3"));
        assertFalse(group.canProcess(createCommandLine("--option1 --arg1 --option2 value --option3 --option4"), "--option1 --arg1 --option2 value --option3 --option4"));
        assertFalse(group.canProcess(createCommandLine("--option1 --arg1 --option2 value --option3 --option4 --option5"), "--option1 --arg1 --option2 value --option3 --option4 --option5"));
        assertEquals(1, group.getMinimum());
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
        assertTrue(group.isRequired());
    }

    @Test
    public void testGroupImplWithCustomProcessor() {
        final List options = new ArrayList();
        options.add(createOption("--option1", "option1", false, null));
        options.add(createOption("--option2", "option2", false, null));
        options.add(createOption("--option3", "option3", true, "value"));

        final Group group = new GroupImpl(options, "mygroup", "My Group", 1, Integer.MAX_VALUE, false) {
            @Override
            public boolean canProcess(final WriteableCommandLine commandLine, final String arg) {
                return super.canProcess(commandLine, arg) || arg.equals("--custom");
            }
        };
        assertTrue(group.canProcess(createCommandLine("--custom"), "--custom"));
        assertTrue(group.canProcess(createCommandLine("--option1"), "--option1"));
        assertTrue(group.canProcess(createCommandLine("--option1 --option2"), "--option1 --option2"));
        assertFalse(group.canProcess(createCommandLine("--option1 --option2 value"), "--option1 --option2 value"));
        assertFalse(group.canProcess(createCommandLine("--option1 --option3"), "--option1 --option3"));
        assertFalse(group.canProcess(createCommandLine("--option3 value"), "--option3 value"));
        assertFalse(group.canProcess(createCommandLine("--option1 --option3 value"), "--option1 --option3 value"));
        assertEquals(1, group.getMinimum());
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
        assertFalse(group.isRequired());
    }

    @Test
    public void testGroupImplWithCustomUsage() {
        final List options = new ArrayList();
        options.add(createOption("--option1", "option1", false, null));
        options.add(createOption("--option2", "option2", false, null));
        options.add(createOption("--option3", "option3", true, "value"));

        final Group group = new GroupImpl(options, "mygroup", "My Group", 1, Integer.MAX_VALUE, false) {
            @Override
            public void appendUsage(final StringBuffer buffer,
                                    final Set<DisplaySetting> helpSettings,
                                    final Comparator<Option> comp,
                                    final String separator) {
                super.appendUsage(buffer, helpSettings, comp, separator);
                buffer.append(" --custom\n");
            }
        };
        final String expectedUsage = "--mygroup\n" +
                "  --option1\n" +
                "  --option2\n" +
                "  --option3 <value>\n" +
                "  --custom\n";
        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null, "|");
        assertEquals(expectedUsage, buffer.toString());
    }

    private Option createOption(final String longOpt, final String opt, final boolean hasArg, final String arg) {
        return new OptionBuilder(longOpt)
                .withLongOpt(longOpt)
                .withShortOpt(opt)
                .withHasArg(hasArg)
                .withArgName(arg)
                .create();
    }

    private Argument createAnonymousArgument(final String longOpt, final String arg) {
        final Option option = new OptionBuilder(longOpt)
                .withLongOpt(longOpt)
                .withArgName("value")
                .isValueSeparatorAllowed(true)
                .withValuesSeparatorByComma()
                .createArgument();
        option.setArgName(arg);
        return option;
    }

    private static WriteableCommandLine createCommandLine(final String... args) {
        final String[] cmdLine = new String[args.length];
        System.arraycopy(args, 0, cmdLine, 0, args.length);
        return new DefaultCommandLine();
    }

    private static Comparator<Option> reverseStringComparator() {
        return new Comparator<Option>() {
            @Override
            public int compare(final Option o1, final Option o2) {
                return o2.getLongOpt().compareTo(o1.getLongOpt());
            }
        };
    }
}