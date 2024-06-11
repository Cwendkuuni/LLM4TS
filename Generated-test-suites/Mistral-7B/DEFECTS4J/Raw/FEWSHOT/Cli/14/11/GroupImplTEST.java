package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;

import org.apache.commons.cli2.option.GroupImpl;

public class GroupImplTest {
    @Test
    public void testGroupImpl() {
        final List options = new LinkedList();
        options.add(createOption("opt1", false, null));
        options.add(createOption("opt2", true, "value2"));
        options.add(createOption("opt3", false, null));

        final Group group = new GroupImpl(options, "group", "Group description", 0, 1);
        assertEquals("group", group.getName());
        assertEquals("Group description", group.getDescription());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertTrue(group.isRequired());
        assertFalse(group.canProcess(null, ""));
        assertNull(group.findOption("opt4"));

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        group.defaults(commandLine);
        assertEquals(0, commandLine.getNumOptions());
        assertNull(commandLine.getOptionValues("opt1"));
        assertNull(commandLine.getOptionValues("opt2"));
        assertNull(commandLine.getOptionValues("opt3"));

        final String[] args = new String[]{"--group", "--opt1", "--opt2"};
        group.process(commandLine, Arrays.asList(args).listIterator());
        assertEquals(3, commandLine.getNumOptions());
        assertEquals(Collections.singletonList("--opt1"), commandLine.getOptionNames());
        assertEquals(Collections.singletonList("--opt2"), commandLine.getOptionValues("opt2"));
        assertNull(commandLine.getOptionValues("opt3"));

        final String[] argsInvalid = new String[]{"--group", "--opt1", "--opt1"};
        try {
            group.process(commandLine, Arrays.asList(argsInvalid).listIterator());
            fail();
        } catch (OptionException e) {
            // expected
        }

        final Map optionMap = group.getOptionMap();
        assertEquals(2, optionMap.size());
        assertTrue(optionMap.containsKey("opt1"));
        assertTrue(optionMap.containsKey("opt2"));

        final Option opt1 = (Option) optionMap.get("opt1");
        assertSame(opt1, group.findOption("opt1"));
        assertEquals(0, opt1.getMinimum());
        assertEquals(0, opt1.getMaximum());
        assertFalse(opt1.isRequired());

        final Option opt2 = (Option) optionMap.get("opt2");
        assertSame(opt2, group.findOption("opt2"));
        assertEquals(1, opt2.getMinimum());
        assertEquals(1, opt2.getMaximum());
        assertTrue(opt2.isRequired());

        final Option opt3 = (Option) group.findOption("opt3");
        assertSame(opt3, group.findOption("opt3"));
        assertEquals(0, opt3.getMinimum());
        assertEquals(0, opt3.getMaximum());
        assertFalse(opt3.isRequired());

        final Set prefixes = group.getPrefixes();
        assertEquals(0, prefixes.size());

        final String[] argsValid = new String[]{"--group", "--opt1", "--opt2=value2"};
        group.process(commandLine, Arrays.asList(argsValid).listIterator());
        assertEquals(3, commandLine.getNumOptions());
        assertEquals(Collections.singletonList("--opt1"), commandLine.getOptionNames());
        assertEquals(Collections.singletonList("--opt2=value2"), commandLine.getOptionValues("opt2"));
        assertNull(commandLine.getOptionValues("opt3"));

        final String[] argsValidWithAnonymous = new String[]{"--group", "--opt1", "value1", "--opt2=value2"};
        group.process(commandLine, Arrays.asList(argsValidWithAnonymous).listIterator());
        assertEquals(4, commandLine.getNumOptions());
        assertEquals(Arrays.asList("--opt1", "--opt2=value2"), commandLine.getOptionNames());
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getOptionValues());

        final String[] argsValidWithAnonymousAndMultiple = new String[]{"--group", "--opt1=value1", "--opt1=value2", "--opt2=value2"};
        group.process(commandLine, Arrays.asList(argsValidWithAnonymousAndMultiple).listIterator());
        assertEquals(4, commandLine.getNumOptions());
        assertEquals(Arrays.asList("--opt1=value1", "--opt1=value2"), commandLine.getOptionValues("opt1"));
        assertEquals(Arrays.asList("value2"), commandLine.getOptionValues("opt2"));
    }

    @Test
    public void testGroupImplWithGroupOption() {
        final List options = new LinkedList();
        options.add(createGroupOption("group1", "group1 description"));
        
        final Group group = new GroupImpl(options, null, null, 0, 1);
        assertEquals("group1", group.getName());
        assertEquals("group1 description", group.getDescription());
        assertTrue(group.isRequired());

        final String[] argsValid = new String[]{"--group1"};
        group.process(new WriteableCommandLine(), Arrays.asList(argsValid).listIterator());
        assertEquals(1, group.getNumOptions());
        assertEquals(Arrays.asList("group1"), group.getOptionNames());
        assertTrue(group.isSuccess());

        final String[] argsInvalid = new String[]{"--group1", "--group1"};
        try {
            group.process(new WriteableCommandLine(), Arrays.asList(argsInvalid).listIterator());
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    private Option createOption(final String name, final boolean required, final String description) {
        return new OptionBuilder()
                .withName(name)
                .withDescription(description)
                .withRequiredOption(required)
                .withValueSeparator('=')
                .create();
    }

    private Option createGroupOption(final String name, final String description) {
        return new OptionBuilder()
                .withName(name)
                .withDescription(description)
                .isUnbounded()
                .create();
    }
}