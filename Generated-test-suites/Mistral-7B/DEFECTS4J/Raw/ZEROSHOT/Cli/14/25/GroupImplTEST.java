package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class GroupImplTest {

    private GroupImpl group;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();

        Option option1 = new OptionBuilder("a")
                .longOpt("long-a")
                .desc("Option A")
                .build();
        options.add(option1);

        Option option2 = new OptionBuilder("b")
                .longOpt("long-b")
                .desc("Option B")
                .hasArg()
                .build();
        options.add(option2);

        Option option3 = new OptionBuilder("c")
                .longOpt("long-c")
                .desc("Option C")
                .build();
        options.add(option3);

        group = new GroupImpl(options, "MyGroup", "This is my group.", 1, 3);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        String[] args = {"a", "1", "b", "arg", "c"};

        for (String arg : args) {
            assertEquals(true, group.canProcess(commandLine, arg));
        }
    }

    @Test
    public void testCanProcessWithMaximum() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        String[] args = {"a", "1", "b", "arg", "c", "c"};

        assertEquals(false, group.canProcess(commandLine, "c"));
    }

    @Test
    public void testCanProcessWithMinimum() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        String[] args = {"a", "1", "b"};

        assertEquals(false, group.canProcess(commandLine, "c"));
    }

    @Test
    public void testCanProcessWithArgument() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        String[] args = {"b", "arg"};

        assertEquals(true, group.canProcess(commandLine, "b"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();
        assertEquals(Collections.emptySet(), prefixes);
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        assertEquals(Arrays.asList("a", "b", "c"), triggers);
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        group.process(commandLine, arguments);

        Object value1 = commandLine.getOptionValue("a");
        assertEquals("1", value1.toString());

        Object value2 = commandLine.getOptionValue("b");
        assertEquals("arg", value2.toString());

        Object value3 = commandLine.getOptionValue("c");
        assertEquals("", value3.toString());

        assertEquals(3, commandLine.getNumOptions());
        assertEquals(1, commandLine.getNumArgs());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithMaximum() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        for (String arg : Arrays.asList("a", "1", "b", "arg", "c", "c")) {
            group.process(commandLine, arguments);
        }

        group.process(commandLine, arguments);
    }

    @Test(expected = OptionException.class)
    public void testProcessWithMinimum() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        for (String arg : Arrays.asList("a", "1")) {
            group.process(commandLine, arguments);
        }

        group.process(commandLine, arguments);
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        group.validate(commandLine);

        // no exceptions should be thrown
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMinimum() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        // process one option
        group.process(commandLine, arguments);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMaximum() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        // process three options
        for (int i = 0; i < 3; i++) {
            group.process(commandLine, arguments);
        }

        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("MyGroup", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("This is my group.", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();

        group.appendUsage(buffer, helpSettings, null);

        String expected = "group MyGroup (OPTIONS)\n" +
                "This is my group.\n\n" +
                "  -a, --long-a\n" +
                "      Option A\n" +
                "  -b, --long-b <ARG>\n" +
                "      Option B\n" +
                "      ARG: an argument for option b\n" +
                "  -c, --long-c\n" +
                "      Option C\n";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(1, new HashSet<>(), null);

        String expected = "group MyGroup (OPTIONS)\n" +
                "This is my group.\n\n" +
                "  -a, --long-a\n" +
                "      Option A\n" +
                "  -b, --long-b <ARG>\n" +
                "      Option B\n" +
                "      ARG: an argument for option b\n" +
                "  -c, --long-c\n" +
                "      Option C\n";

        for (HelpLine helpLine : helpLines) {
            assertEquals(expected, helpLine.getDefaultLine());
        }
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();

        assertEquals(3, options.size());
        assertEquals("a", options.get(0).getLongOpt());
        assertEquals("b", options.get(1).getLongOpt());
        assertEquals("c", options.get(2).getLongOpt());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();
        assertEquals(0, anonymous.size());
    }

    @Test
    public void testFindOption() {
        Option option = group.findOption("b");

        assertEquals("b", option.getLongOpt());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertEquals(true, group.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new DefaultCommandLine();

        group.defaults(commandLine);

        assertEquals(3, commandLine.getNumOptions());
        assertEquals(0, commandLine.getNumArgs());
    }
}