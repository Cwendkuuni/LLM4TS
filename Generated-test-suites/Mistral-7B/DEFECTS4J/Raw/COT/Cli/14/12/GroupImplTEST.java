package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class GroupImplTest {

    @Mock
    private WriteableCommandLine commandLine;

    @Mock
    private Option option;

    @Mock
    private Argument argument;

    private Group group;

    @Before
    public void setUp() {
        final List options = Arrays.asList(option, option, argument);
        final List anonymous = Arrays.asList(argument);

        group = new GroupImpl(options, "testGroup", "Test Group", 1, 2);
    }

    @Test
    public void testCanProcess_noArg_validOption() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);

        assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_arg_validOption() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(option.canProcess(commandLine, "test")).thenReturn(true);

        assertTrue(group.canProcess(commandLine, "test"));
    }

    @Test
    public void testCanProcess_arg_validOption_bursting() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(option.canProcess(commandLine, "test")).thenReturn(true);

        final Option lastOption = Mockito.spy(option);
        Mockito.when(lastOption.process(commandLine, Mockito.anyList())).thenReturn(true);

        final List<Option> options = new ArrayList<>();
        options.add(lastOption);
        options.add(option);

        final Group group = new GroupImpl(options, "testGroup", "Test Group", 1, 2);

        assertTrue(group.canProcess(commandLine, "test"));
    }

    @Test
    public void testCanProcess_arg_invalidOption() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);

        assertFalse(group.canProcess(commandLine, "invalid"));
    }

    @Test
    public void testCanProcess_arg_anonymousArg() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(argument.canProcess(commandLine, Mockito.anyList())).thenReturn(true);

        assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_arg_anonymousArg_multiple() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(argument.canProcess(commandLine, Mockito.anyList())).thenReturn(true);
        Mockito.when(argument.canProcess(commandLine, Mockito.anyList())).thenReturn(true);

        assertTrue(group.canProcess(commandLine, null));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_arg_anonymousArg_noneAvailable() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(argument.canProcess(commandLine, Mockito.anyList())).thenReturn(true);

        group.anonymous.clear();

        group.canProcess(commandLine, null);
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_arg_tooManyOptions() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(true);

        group.canProcess(commandLine, "test");
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_arg_tooFewOptions() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);

        group.canProcess(commandLine, "test");

        Mockito.verify(commandLine, Mockito.times(1)).addOption(ArgumentMatchers.isA(Option.class));
    }

    @Test
    public void testGetPrefixes() {
        Mockito.when(option.getTriggers()).thenReturn(Collections.singleton("prefix"));

        assertEquals(Collections.singleton("prefix"), group.getPrefixes());
    }

    @Test
    public void testGetTriggers() {
        Mockito.when(option.getTriggers()).thenReturn(Collections.singleton("trigger"));

        assertEquals(Collections.singleton("trigger"), group.getTriggers());
    }

    @Test
    public void testProcess() {
        final String[] args = {"arg1", "arg2", "arg3"};

        Mockito.when(commandLine.getArgs()).thenReturn(args);
        Mockito.when(option.process(commandLine, Mockito.anyList())).thenReturn(true);

        group.process(commandLine, Mockito.mock(ListIterator.class));

        Mockito.verify(commandLine, Mockito.times(1)).addOption(ArgumentMatchers.isA(Option.class));
    }

    @Test
    public void testProcess_anonymousArg() {
        final String[] args = {"arg1", "arg2", "arg3"};

        Mockito.when(commandLine.getArgs()).thenReturn(args);
        Mockito.when(argument.process(commandLine, Mockito.anyList())).thenReturn(true);

        group.process(commandLine, Mockito.mock(ListIterator.class));

        Mockito.verify(commandLine, Mockito.times(1)).addArgument(ArgumentMatchers.isA(Argument.class));
    }

    @Test(expected = OptionException.class)
    public void testProcess_anonymousArg_noneAvailable() {
        final String[] args = {"arg1", "arg2", "arg3"};

        Mockito.when(commandLine.getArgs()).thenReturn(args);
        Mockito.when(argument.process(commandLine, Mockito.anyList())).thenReturn(true);

        group.anonymous.clear();

        group.process(commandLine, Mockito.mock(ListIterator.class));
    }

    @Test
    public void testValidate() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(true);

        group.validate(commandLine);

        Mockito.verify(commandLine, Mockito.times(1)).addOption(ArgumentMatchers.isA(Option.class));
    }

    @Test(expected = OptionException.class)
    public void testValidate_tooManyOptions() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(true);

        group.validate(commandLine);

        Mockito.verify(commandLine, Mockito.times(1)).addOption(ArgumentMatchers.isA(Option.class));
        Mockito.verify(commandLine, Mockito.times(1)).addOption(ArgumentMatchers.isA(Option.class));
    }

    @Test(expected = OptionException.class)
    public void testValidate_tooFewOptions() {
        Mockito.when(commandLine.hasOption(ArgumentMatchers.anyString())).thenReturn(false);

        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("testGroup", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test Group", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, null, null);

        assertEquals("testGroup\n", buffer.toString());
    }

    @Test
    public void testAppendUsage_expanded() {
        final StringBuilder buffer = new StringBuilder();
        final Set helpSettings = new HashSet(DisplaySetting.values());

        group.appendUsage(buffer, helpSettings, null);

        assertEquals("-testGroup\n" +
                        "-testGroup\n" +
                        "--\n", buffer.toString());
    }

    @Test
    public void testAppendUsage_separator() {
        final StringBuilder buffer = new StringBuilder();
        final Set helpSettings = new HashSet(DisplaySetting.values());

        group.appendUsage(buffer, helpSettings, new ReverseStringComparator());

        assertEquals("--testGroup\n" +
                        "-testGroup\n" +
                        "--\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List<String> lines = group.helpLines(0, null, null);

        assertEquals(3, lines.size());
        assertEquals("-testGroup\n" +
                        "--testGroup\n" +
                        "---\n", Arrays.toString(lines.toArray()));
    }

    @Test
    public void testHelpLines_expanded() {
        final List<String> lines = group.helpLines(0, null, null);

        assertEquals(6, lines.size());
        assertEquals("-testGroup\n" +
                        "--testGroup\n" +
                        "  Test Group\n" +
                        "--\n" +
                        "-testGroup\n" +
                        "--testGroup\n" +
                        "  Test Group\n" +
                        "--\n", Arrays.toString(lines.toArray()));
    }

    @Test
    public void testHelpLines_sorted() {
        final List<String> lines = group.helpLines(0, null, new ReverseStringComparator());

        assertEquals(6, lines.size());
        assertEquals("--testGroup\n" +
                        "-testGroup\n" +
                        "  Test Group\n" +
                        "--\n" +
                        "--testGroup\n" +
                        "-testGroup\n" +
                        "  Test Group\n" +
                        "--\n", Arrays.toString(lines.toArray()));
    }

    @Test
    public void testGetOptions() {
        assertEquals(Arrays.asList(option, option), group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(Collections.singletonList(argument), group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        Mockito.when(option.getTriggers()).thenReturn(Collections.singleton("test"));

        assertSame(option, group.findOption("test"));
    }

    @Test
    public void testFindOption_noneAvailable() {
        assertNull(group.findOption("none"));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        Mockito.verify(option, Mockito.times(1)).defaults(commandLine);
        Mockito.verify(commandLine, Mockito.times(1)).addOption(ArgumentMatchers.isA(Option.class));
    }
}