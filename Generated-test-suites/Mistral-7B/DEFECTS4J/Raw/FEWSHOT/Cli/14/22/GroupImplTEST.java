package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.GroupImpl;
import org.apache.commons.cli2.util.OptionImpl;
import org.apache.commons.cli2.util.ReverseStringComparator;

public class GroupImplTest {

    @Test
    public void testGroupImpl_simple() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("foo", false, null, null, null));
        options.add(createOption("bar", false, null, null, null));

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        assertTrue(group.getName().equals("testGroup"));
        assertEquals(2, group.getOptions().size());
        assertFalse(group.isRequired());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertNull(group.getAnonymous());
        assertEquals(Collections.emptyList(), group.getOptionMap().keySet());
        assertEquals(Collections.emptyList(), group.getPrefixes());
    }

    @Test
    public void testCanProcess_simple() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null),
            createOption("baz", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        String[] args = {"foo", "bar", "baz", "qux"};
        WriteableCommandLine commandLine = new WriteableCommandLine();

        for (String arg : args) {
            commandLine.parseArgument(arg);
        }

        for (String arg : args) {
            assertTrue(group.canProcess(commandLine, arg));
        }
    }

    @Test
    public void testCanProcess_noMatch() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        String[] args = {"qux", "quux"};
        WriteableCommandLine commandLine = new WriteableCommandLine();

        for (String arg : args) {
            commandLine.parseArgument(arg);
        }

        assertFalse(group.canProcess(commandLine, "qux"));
        assertFalse(group.canProcess(commandLine, "quux"));
    }

    @Test
    public void testCanProcess_bursting() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null),
            createOption("baz", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        String[] args = {"foo", "foo", "bar", "qux"};
        WriteableCommandLine commandLine = new WriteableCommandLine();

        for (String arg : args) {
            commandLine.parseArgument(arg);
        }

        String lastArg = args[args.length - 1];
        assertTrue(group.canProcess(commandLine, lastArg));
    }

    @Test(expected = OptionException.class)
    public void testValidate_min() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 0);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument("foo");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_max() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null),
            createOption("baz", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 2);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument("foo");
        commandLine.parseArgument("bar");
        commandLine.parseArgument("baz");
        commandLine.parseArgument("qux");
        group.validate(commandLine);
    }

    @Test
    public void testProcess_simple() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        String[] args = {"foo", "bar"};
        WriteableCommandLine commandLine = new WriteableCommandLine();

        for (String arg : args) {
            commandLine.parseArgument(arg);
        }

        List<String> processedArgs = new ArrayList<>();
        group.process(commandLine, commandLine.argumentIterator());

        assertEquals(2, processedArgs.size());
        assertTrue(processedArgs.contains("foo"));
        assertTrue(processedArgs.contains("bar"));
    }

    @Test
    public void testProcess_error() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null),
            createOption("baz", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        String[] args = {"foo", "bar", "baz", "qux"};
        WriteableCommandLine commandLine = new WriteableCommandLine();

        for (String arg : args) {
            commandLine.parseArgument(arg);
        }

        List<String> processedArgs = new ArrayList<>();
        group.process(commandLine, commandLine.argumentIterator());

        assertEquals(3, processedArgs.size());
        assertTrue(processedArgs.contains("foo"));
        assertTrue(processedArgs.contains("bar"));
        assertTrue(processedArgs.contains("baz"));

        assertFalse(processedArgs.contains("qux"));
    }

    @Test
    public void testAppendUsage() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Collections.emptySet();

        group.appendUsage(buffer, helpSettings, null);

        String expected = "testGroup: " + DisplaySetting.SHORT_FORM.getName() + "[" +
            DisplaySetting.DISPLAY_GROUP_NAME.getName() + "]\n";
        expected += "  foo\n";
        expected += "  bar\n";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        StringBuffer expected = new StringBuffer();
        expected.append("testGroup:\n");
        expected.append("  foo\n");
        expected.append("  bar\n");

        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(1, helpLines.size());
        assertEquals(expected.toString(), helpLines.get(0).getShortDescription());
    }

    @Test
    public void testFindOption() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null),
            createOption("baz", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        Option option = group.findOption("foo");
        assertEquals("foo", option.getLongOpt());
    }

    @Test
    public void testGetMinimum() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null),
            createOption("baz", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 3);

        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        List<Option> options = Arrays.asList(
            createOption("foo", true, null, null, null),
            createOption("bar", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        List<Option> options = Arrays.asList(
            createOption("foo", false, null, null, null),
            createOption("bar", false, null, null, null)
        );

        GroupImpl group = new GroupImpl(options, "testGroup", null, 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        group.defaults(commandLine);

        assertTrue(commandLine.hasOption("foo"));
        assertTrue(commandLine.hasOption("bar"));
    }

    private Option createOption(String longOpt, boolean required, String[] args,
                               List<String> values, Set<String> triggers) {
        return new OptionImpl(longOpt, required, args, values, triggers);
    }
}

class HelpLineImpl implements HelpLine {
    private final Group group;
    private final int depth;

    public HelpLineImpl(Group group, int depth) {
        this.group = group;
        this.depth = depth;
    }

    public String getShortDescription() {
        String description = group.getDescription();

        if (depth > 0) {
            description = "(" + depth + ") " + description;
        }

        return description;
    }
}