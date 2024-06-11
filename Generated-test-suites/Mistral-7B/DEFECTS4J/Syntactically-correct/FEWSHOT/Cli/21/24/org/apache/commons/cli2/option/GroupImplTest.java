package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2. WriteableCommandLine;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.exceptions.MissingArgumentException;
import org.apache.commons.cli2.exceptions.NoSuchOptionException;
import org.apache.commons.cli2.util.Arguments;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testCreateGroupWithEmptyList() {
        List options = Collections.emptyList();
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);
        assertNull(group.getName());
        assertEquals(Collections.emptyList(), group.getOptions());
        assertEquals(Collections.emptySet(), group.getTriggers());
        assertFalse(group.isRequired());
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertFalse(group.canProcess(null, ""));
    }

    @Test
    public void testCreateGroupWithOneOption() {
        Option opt = new Option("o", "option", true, "a description");
        List options = new ArrayList();
        options.add(opt);
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);
        assertEquals("option", group.getPreferredName());
        assertEquals("a description", group.getDescription());
        assertEquals(Collections.singletonList(opt), group.getOptions());
        assertEquals(Collections.singleton("o"), group.getTriggers());
        assertFalse(group.isRequired());
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertTrue(group.canProcess(null, "o"));
    }

    @Test
    public void testCreateGroupWithOneOptionAndOneArgument() {
        Option opt = new Option("o", "option", true, "a description");
        Argument arg = new Argument("a", "argument");
        List options = new ArrayList();
        options.add(opt);
        options.add(arg);
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);
        assertEquals("option", group.getPreferredName());
        assertEquals("a description", group.getDescription());
        assertEquals(Collections.singletonList(opt), group.getOptions());
        assertEquals(Collections.emptySet(), group.getTriggers());
        assertFalse(group.isRequired());
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Collections.singletonList(arg), group.getAnonymous());
        assertFalse(group.canProcess(null, "o"));
        assertTrue(group.canProcess(null, "a"));
    }

    @Test
    public void testCreateGroupWithMultipleOptionsAndArguments() {
        Option opt1 = new Option("o1", "option1", true, "a description1");
        Option opt2 = new Option("o2", "option2", false, "a description2");
        Argument arg1 = new Argument("a1", "argument1");
        Argument arg2 = new Argument("a2", "argument2");
        List options = new ArrayList();
        options.add(opt1);
        options.add(opt2);
        options.add(arg1);
        options.add(arg2);
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);
        assertEquals("option1 option2", group.getPreferredName());
        assertEquals("a description1 a description2", group.getDescription());
        assertEquals(Arrays.asList(opt1, opt2, arg1, arg2), group.getOptions());
        assertEquals(Collections.singleton("o1"), group.getTriggers());
        assertFalse(group.isRequired());
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Arrays.asList(arg1, arg2), group.getAnonymous());
        assertTrue(group.canProcess(null, "o1"));
        assertTrue(group.canProcess(null, "o2"));
        assertTrue(group.canProcess(null, "a1"));
        assertTrue(group.canProcess(null, "a2"));
    }

    @Test
    public void testCreateGroupWithMinimumAndMaximum() {
        Option opt = new Option("o", "option", true, "a description");
        List options = new ArrayList();
        options.add(opt);
        GroupImpl group = new GroupImpl(options, null, null, 3, 5, false);
        assertEquals("option", group.getPreferredName());
        assertEquals("a description", group.getDescription());
        assertEquals(Collections.singletonList(opt), group.getOptions());
        assertEquals(Collections.singleton("o"), group.getTriggers());
        assertFalse(group.isRequired());
        assertEquals(3, group.getMinimum());
        assertEquals(5, group.getMaximum());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertFalse(group.canProcess(null, "o"));
        assertFalse(group.canProcess(null, ""));
    }

    @Test
    public void testCreateGroupWithRequired() {
        Option opt = new Option("o", "option", true, "a description");
        List options = new ArrayList();
        options.add(opt);
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, true);
        assertEquals("option", group.getPreferredName());
        assertEquals("a description", group.getDescription());
        assertEquals(Collections.singletonList(opt), group.getOptions());
        assertEquals(Collections.singleton("o"), group.getTriggers());
        assertTrue(group.isRequired());
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertFalse(group.canProcess(null, ""));
    }

    @Test
    public void testProcessWithValidArguments() throws OptionException {
        Option opt = new Option("o", "option", true, "a description");
        Argument arg = new Argument("a", "argument");
        List options = new ArrayList();
        options.add(opt);
        options.add(arg);
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);

        // test empty command line
        WriteableCommandLine cmd = new WriteableCommandLine("", new Parser());
        group.process(cmd, cmd.getParsedArgs().listIterator());
        assertFalse(cmd.hasOption("o"));
        assertFalse(cmd.hasOption("a"));

        // test command line with valid arguments
        cmd = new WriteableCommandLine("-o foo -a bar", new Parser());
        group.process(cmd, cmd.getParsedArgs().listIterator());
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.hasOption("a"));
        assertEquals("foo", cmd.getOptionValue("o"));
        assertEquals("bar", cmd.getOptionValue("a"));

        // test command line with multiple valid arguments
        cmd = new WriteableCommandLine("-o1 foo1 -o2 foo2 -a1 bar1 -a2 bar2", new Parser());
        group.process(cmd, cmd.getParsedArgs().listIterator());
        assertTrue(cmd.hasOption("o1"));
        assertTrue(cmd.hasOption("o2"));
        assertTrue(cmd.hasOption("a1"));
        assertTrue(cmd.hasOption("a2"));
        assertEquals("foo1", cmd.getOptionValue("o1"));
        assertEquals("foo2", cmd.getOptionValue("o2"));
        assertEquals("bar1", cmd.getOptionValue("a1"));
        assertEquals("bar2", cmd.getOptionValue("a2"));
    }

    @Test
    public void testProcessWithInvalidArguments() throws OptionException {
        Option opt = new Option("o", "option", true, "a description");
        Argument arg = new Argument("a", "argument");
        List options = new ArrayList();
        options.add(opt);
        options.add(arg);
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);

        // test empty command line
        WriteableCommandLine cmd = new WriteableCommandLine("", new Parser());
        assertThrows(NoSuchOptionException.class, () -> group.process(cmd, cmd.getParsedArgs().listIterator()));

        // test command line with invalid arguments
        cmd = new WriteableCommandLine("--foo", new Parser());
        assertThrows(NoSuchOptionException.class, () -> group.process(cmd, cmd.getParsedArgs().listIterator()));

        cmd = new WriteableCommandLine("-o baz", new Parser());
        assertThrows(MissingArgumentException.class, () -> group.process(cmd, cmd.getParsedArgs().listIterator()));

        cmd = new WriteableCommandLine("-o foo -o bar", new Parser());
        assertThrows(IllegalArgumentException.class, () -> group.process(cmd, cmd.getParsedArgs().listIterator()));

        cmd = new WriteableCommandLine("-o foo -a", new Parser());
        assertThrows(MissingArgumentException.class, () -> group.process(cmd, cmd.getParsedArgs().listIterator()));

        cmd = new WriteableCommandLine("-o foo -a1 bar -a2", new Parser());
        assertThrows(IllegalArgumentException.class, () -> group.process(cmd, cmd.getParsedArgs().listIterator()));
    }

    @Test
    public void testValidate() throws OptionException {
        Option opt = new Option("o", "option", true, "a description");
        Argument arg = new Argument("a", "argument");
        List options = new ArrayList();
        options.add(opt);
        options.add(arg);
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);

        // test empty command line
        WriteableCommandLine cmd = new WriteableCommandLine("", new Parser());
        assertThrows(OptionException.class, () -> group.validate(cmd));

        // test command line with one valid argument
        cmd = new WriteableCommandLine("-o foo", new Parser());
        group.validate(cmd);

        // test command line with less than minimum arguments
        cmd = new WriteableCommandLine("-o foo", new Parser());
        assertThrows(OptionException.class, () -> group.validate(cmd));

        // test command line with more than maximum arguments
        cmd = new WriteableCommandLine("-o foo -o bar", new Parser());
        assertThrows(OptionException.class, () -> group.validate(cmd));

        // test command line with valid and invalid arguments
        cmd = new WriteableCommandLine("-o foo -o bar -a", new Parser());
        assertThrows(OptionException.class, () -> group.validate(cmd));

        // test command line with all valid arguments
        cmd = new WriteableCommandLine("-o foo -o bar -a1 arg1 -a2 arg2", new Parser());
        group.validate(cmd);

        // test command line with all valid and invalid arguments
        cmd = new WriteableCommandLine("-o foo -o bar -a1 arg1 -a2 arg2 -a3", new Parser());
        assertThrows(OptionException.class, () -> group.validate(cmd));
    }

    @Test
    public void testHelpLines() {
        Option opt = new Option("o", "option", true, "a description");
        Argument arg = new Argument("a", "argument");
        List options = new ArrayList();
        options.add(opt);
        options.add(arg);
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);

        List<String> expectedHelpLines = List.of(
                "options:",
                "  option (-o <VALUE>)",
                "    a description",
                "  argument (<VALUE>)",
                "");

        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        StringWriter writer = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        group.helpLines(1, helpSettings, null, writer);
        List<String> actualHelpLines = Arrays.asList(writer.toString().split("\n"));
        assertEquals(expectedHelpLines, actualHelpLines);

        writer = new StringWriter();
        group.helpLines(1, helpSettings, new ReverseStringComparator(), writer);
        actualHelpLines = Arrays.asList(writer.toString().split("\n"));
        Collections.reverse(expectedHelpLines);
        assertEquals(expectedHelpLines, actualHelpLines);
    }

    @Test
    public void testToString() {
        Option opt1 = new Option("o1", "option1", true, "a description1");
        Option opt2 = new Option("o2", "option2", false, "a description2");
        Argument arg1 = new Argument("a1", "argument1");
        Argument arg2 = new Argument("a2", "argument2");
        List options = new ArrayList();
        options.add(opt1);
        options.add(opt2);
        options.add(arg1);
        options.add(arg2);
        GroupImpl group = new GroupImpl(options, null, null, 0, 1, false);
        String expectedToString =
                "GroupImpl{name=null, description=null, options=[OptionImpl{name='o1', description='a description1', values=[], required=false, args=[], nested=[], options=[], anonymous=[], min=0, max=1}, OptionImpl{name='o2', description='a description2', values=[], required=false, args=[], nested=[], options=[], anonymous=[], min=0, max=1}, ArgumentImpl{name='a1', description=null, values=[], required=false, args=[], nested=[], options=[], anonymous=[], min=0, max=1}, ArgumentImpl{name='a2', description=null, values=[], required=false, args=[], nested=[], options=[], anonymous=[], min=0, max=1}], anonymous=[], min=0, max=1, required=false, prefixes=[]}";
        assertEquals(expectedToString, group.toString());
    }
}

class ReverseStringComparator implements Comparator<String> {
    public int compare(String o1, String o2) {
        return o2.compareTo(o1);
    }
}

class StringWriter extends StringWriter {
}