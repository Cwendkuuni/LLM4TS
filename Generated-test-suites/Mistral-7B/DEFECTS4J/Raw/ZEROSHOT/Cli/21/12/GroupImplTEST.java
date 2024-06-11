package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ GroupImpl.class, OptionImpl.class })
public class GroupImplTest {

    private GroupImpl group;
    private Option option1;
    private Option option2;
    private Option option3;
    private Option option4;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        option1 = PowerMockito.mock(Option.class);
        option2 = PowerMockito.mock(Option.class);
        option3 = PowerMockito.mock(Option.class);
        option4 = PowerMockito.mock(Option.class);
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);

        group = new GroupImpl(options, "myGroup", null, 1, 3, false);
    }

    @Test
    public void testCanProcess_NoOptions() {
        CommandLine commandLine = TestUtils.createCommandLine();
        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_OneOption() {
        CommandLine commandLine = TestUtils.createCommandLine();
        commandLine.parse(Arrays.asList("--option1"));

        assertTrue(group.canProcess(commandLine, "--option1"));
    }

    @Test
    public void testCanProcess_MultipleOptions() {
        CommandLine commandLine = TestUtils.createCommandLine();
        commandLine.parse(Arrays.asList("--option1", "--option2", "--option3"));

        assertTrue(group.canProcess(commandLine, "--option2"));
    }

    @Test
    public void testCanProcess_UnrecognizedOption() {
        CommandLine commandLine = TestUtils.createCommandLine();
        commandLine.parse(Arrays.asList("--unknown"));

        assertFalse(group.canProcess(commandLine, "--unknown"));
    }

    @Test
    public void testCanProcess_AnonymousArg() {
        CommandLine commandLine = TestUtils.createCommandLine();
        commandLine.parse(Arrays.asList("arg"));

        assertTrue(group.canProcess(commandLine, "arg"));
    }

    @Test
    public void testCanProcess_AnonymousArgs() {
        CommandLine commandLine = TestUtils.createCommandLine();
        commandLine.parse(Arrays.asList("arg1", "arg2"));

        assertTrue(group.canProcess(commandLine, "arg2"));
    }

    @Test
    public void testCanProcess_BurstedOption() {
        Option option = PowerMockito.mock(Option.class);
        Option burstedOption = PowerMockito.mock(Option.class);
        PowerMockito.when(option.canProcess(commandLine, "--option1")).thenReturn(true);
        PowerMockito.when(option.getTriggers()).thenReturn(Collections.singletonList("--option1"));
        PowerMockito.when(option.getTriggers()).thenReturn(Collections.singletonList("--option2"));
        PowerMockito.when(burstedOption.canProcess(commandLine, "--option2")).thenReturn(true);

        List<Option> options = new ArrayList<>();
        options.add(option);
        options.add(burstedOption);

        GroupImpl group = new GroupImpl(options, null, null, 1, Integer.MAX_VALUE, false);

        CommandLine commandLine2 = TestUtils.createCommandLine();
        commandLine2.parse(Arrays.asList("--option1", "--option2"));

        assertTrue(group.canProcess(commandLine2, "--option2"));
    }

    @Test
    public void testValidate_Minimum() {
        CommandLine commandLine = TestUtils.createCommandLine();

        try {
            group.validate(commandLine);
            assertFalse(true);
        } catch (OptionException e) {
            // expected
        }

        CommandLine commandLine2 = TestUtils.createCommandLine();
        commandLine2.parse(Arrays.asList("--option1", "--option2"));

        group.validate(commandLine2);
    }

    @Test
    public void testValidate_Maximum() {
        CommandLine commandLine = TestUtils.createCommandLine();
        commandLine.parse(Arrays.asList("--option1", "--option2", "--option3"));

        try {
            group.validate(commandLine);
            assertFalse(true);
        } catch (OptionException e) {
            assertEquals("option 'option3' is not allowed", e.getMessage());
        }
    }

    @Test
    public void testValidate_MinimumAndMaximum() {
        CommandLine commandLine = TestUtils.createCommandLine();
        commandLine.parse(Arrays.asList("--option1", "--option2", "--option3"));

        try {
            group.validate(commandLine);
            assertFalse(true);
        } catch (OptionException e) {
            assertEquals("option 'option3' is not allowed", e.getMessage());
        }

        CommandLine commandLine2 = TestUtils.createCommandLine();
        commandLine2.parse(Arrays.asList("--option1", "--option2"));

        group.validate(commandLine2);
    }

    @Test
    public void testValidate_NoOptions() {
        CommandLine commandLine = TestUtils.createCommandLine();

        try {
            group.validate(commandLine);
            assertTrue(true);
        } catch (OptionException e) {
            assertFalse(true);
        }
    }

    @Test
    public void testProcess_NoOptions() {
        CommandLine commandLine = TestUtils.createCommandLine();

        // no options to process
        List<String> arguments = new ArrayList<>();

        group.process(commandLine, arguments.listIterator());

        assertNull(commandLine.getOptionValue("option1"));
        assertNull(commandLine.getOptionValue("option2"));
        assertNull(commandLine.getOptionValue("option3"));
        assertNull(commandLine.getOptionValue("option4"));
    }

    @Test
    public void testProcess_OneOption() {
        CommandLine commandLine = TestUtils.createCommandLine();
        commandLine.parse(Arrays.asList("--option1"));

        List<String> arguments = new ArrayList<>();

        group.process(commandLine, arguments.listIterator());

        assertEquals("option1", commandLine.getOptionValue("option1"));
        assertNull(commandLine.getOptionValue("option2"));
        assertNull(commandLine.getOptionValue("option3"));
        assertNull(commandLine.getOptionValue("option4"));
    }

    @Test
    public void testProcess_MultipleOptions() {
        CommandLine commandLine = TestUtils.createCommandLine();
        commandLine.parse(Arrays.asList("--option1", "--option2", "--option3"));

        List<String> arguments = new ArrayList<>();

        group.process(commandLine, arguments.listIterator());

        assertEquals("option1", commandLine.getOptionValue("option1"));
        assertEquals("option2", commandLine.getOptionValue("option2"));
        assertEquals("option3", commandLine.getOptionValue("option3"));
        assertNull(commandLine.getOptionValue("option4"));
    }

    @Test
    public void testProcess_AnonymousArg() {
        CommandLine commandLine = TestUtils.createCommandLine();
        commandLine.parse(Arrays.asList("arg"));

        List<String> arguments = new ArrayList<>();

        group.process(commandLine, arguments.listIterator());

        assertNull(commandLine.getOptionValue("option1"));
        assertNull(commandLine.getOptionValue("option2"));
        assertNull(commandLine.getOptionValue("option3"));
        assertNull(commandLine.getOptionValue("option4"));
        assertEquals("arg", commandLine.getArg(0));
    }

    @Test
    public void testProcess_AnonymousArgs() {
        CommandLine commandLine = TestUtils.createCommandLine();
        commandLine.parse(Arrays.asList("arg1", "arg2"));

        List<String> arguments = new ArrayList<>();

        group.process(commandLine, arguments.listIterator());

        assertNull(commandLine.getOptionValue("option1"));
        assertNull(commandLine.getOptionValue("option2"));
        assertNull(commandLine.getOptionValue("option3"));
        assertNull(commandLine.getOptionValue("option4"));
        assertEquals("arg1", commandLine.getArg(0));
        assertEquals("arg2", commandLine.getArg(1));
    }

    @Test
    public void testProcess_BurstedOption() {
        Option option = PowerMockito.mock(Option.class);
        Option burstedOption = PowerMockito.mock(Option.class);
        PowerMockito.when(option.canProcess(commandLine, "--option1")).thenReturn(true);
        PowerMockito.when(option.process(commandLine, arguments.listIterator())).thenReturn();
        PowerMockito.when(option.getName()).thenReturn("option1");
        PowerMockito.when(burstedOption.canProcess(commandLine, "--option2")).thenReturn(true);
        PowerMockito.when(burstedOption.process(commandLine, arguments.listIterator())).thenReturn();
        PowerMockito.when(burstedOption.getName()).thenReturn("option2");

        List<Option> options = new ArrayList<>();
        options.add(option);
        options.add(burstedOption);

        GroupImpl group = new GroupImpl(options, null, null, 1, Integer.MAX_VALUE, false);

        CommandLine commandLine2 = TestUtils.createCommandLine();
        commandLine2.parse(Arrays.asList("--option1", "--option2"));

        List<String> arguments2 = new ArrayList<>();

        group.process(commandLine2, arguments2.listIterator());

        assertEquals("option1", commandLine2.getOptionValue("option1"));
        assertEquals("option2", commandLine2.getOptionValue("option2"));
        assertNull(commandLine2.getOptionValue("option3"));
        assertNull(commandLine2.getOptionValue("option4"));
    }

    @Test
    public void testGetName() {
        assertEquals("myGroup", group.getName());
    }

    @Test
    public void testGetDescription() {
        assertNull(group.getDescription());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();

        assertEquals(3, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
        assertEquals(option3, options.get(2));
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();

        assertEquals(0, anonymous.size());
    }

    @Test
    public void testFindOption() {
        Option option = PowerMockito.mock(Option.class);
        PowerMockito.when(option.getName()).thenReturn("option1");

        Option foundOption = group.findOption("option1");

        assertEquals(option, foundOption);
    }

    @Test
    public void testFindOption_Null() {
        assertNull(group.findOption(null));
    }

    @Test
    public void testFindOption_Unknown() {
        assertNull(group.findOption("unknown"));
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
        assertFalse(group.isRequired());
    }

    @Test
    public void testValidate_NoOptions_NoError() {
        CommandLine commandLine = TestUtils.createCommandLine();

        try {
            group.validate(commandLine);
            assertTrue(true);
        } catch (OptionException e) {
            assertFalse(true);
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);

        String usage = buffer.toString();

        String expectedUsage = "myGroup:\n" +
                "  This is myGroup\n";

        assertEquals(expectedUsage, usage);
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        String expectedHelpLines = "myGroup:\n" +
                "  This is myGroup\n" +
                "  \n" +
                "  Options:\n" +
                "    option1:\n" +
                "      This is option1\n" +
                "      \n" +
                "      Required: false\n" +
                "      Default: null\n" +
                "      Synonyms: []\n" +
                "    option2:\n" +
                "      This is option2\n" +
                "      \n" +
                "      Required: false\n" +
                "      Default: null\n" +
                "      Synonyms: []\n" +
                "    option3:\n" +
                "      This is option3\n" +
                "      \n" +
                "      Required: false\n" +
                "      Default: null\n" +
                "      Synonyms: []\n";

        assertEquals(expectedHelpLines, TestUtils.getHelpLines(helpLines));
    }
}

class ReverseStringComparator {
    public static final Comparator INSTANCE = new ReverseStringComparator();

    private ReverseStringComparator() {
        // private constructor
    }

    public int compare(String o1, String o2) {
        return o1.compareTo(o2) * -1;
    }
}