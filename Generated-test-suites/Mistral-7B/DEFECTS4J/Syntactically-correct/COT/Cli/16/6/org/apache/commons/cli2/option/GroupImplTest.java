package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.apache.commons.cli2.CliBuilder.newInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GroupImpl.class})
public class GroupImplTest {
    private static final List options = new ArrayList();
    private static final GroupImpl group;

    static {
        options.add(Option.builder("a").longOpt("optionA").build());
        options.add(Option.builder("b").longOpt("optionB").build());
        options.add(Argument.builder("c").build());
        group = new GroupImpl(options, "group", "test group", 2, 3);
    }

    private CommandLine commandLine;
    private HelpFormatter helpFormatter;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
        helpFormatter = new HelpFormatter();
    }

    

    @Test
    public void testGroupImpl_constructor() {
        assertNotNull(group);
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("test group", group.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(2, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testGetOptions() {
        List expected = new ArrayList();
        expected.add(Option.builder("a").longOpt("optionA").build());
        expected.add(Option.builder("b").longOpt("optionB").build());
        assertSame(expected, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        List expected = new ArrayList();
        expected.add(Argument.builder("c").build());
        assertSame(expected, group.getAnonymous());
    }

    @Test
    public void testCanProcess_withNoneArgument() {
        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_withValidOption() {
        commandLine.parse("-a");
        assertTrue(group.canProcess(commandLine, "a"));
    }

    @Test
    public void testCanProcess_withValidOptionB() {
        commandLine.parse("-b");
        assertTrue(group.canProcess(commandLine, "b"));
    }

    @Test
    public void testCanProcess_withInvalidOption() {
        commandLine.parse("-d");
        assertFalse(group.canProcess(commandLine, "d"));
    }

    @Test
    public void testCanProcess_withAnonymousArgument() {
        commandLine.parse("c");
        assertTrue(group.canProcess(commandLine, "c"));
    }

    @Test
    public void testGetTriggers() {
        Set expected = new HashSet();
        expected.add("a");
        expected.add("b");
        assertSame(expected, group.getTriggers());
    }

    @Test
    public void testGetPrefixes() {
        Set expected = new HashSet();
        assertSame(expected, group.getPrefixes());
    }

    @Test
    public void testProcess_withValidOption() {
        commandLine.parse("-a");
        group.process(commandLine, commandLine.listIterator());
        assertEquals("-a", commandLine.getOption("optionA").getValue());
    }

    @Test
    public void testProcess_withValidOptionB() {
        commandLine.parse("-b");
        group.process(commandLine, commandLine.listIterator());
        assertEquals("-b", commandLine.getOption("optionB").getValue());
    }

    @Test
    public void testProcess_withInvalidOption() {
        commandLine.parse("-d");
        group.process(commandLine, commandLine.listIterator());
        assertNull(commandLine.getOption("invalidOption"));
    }

    @Test
    public void testProcess_withAnonymousArgument() {
        commandLine.parse("c");
        group.process(commandLine, commandLine.listIterator());
        assertEquals("c", commandLine.getArgument(0));
    }

    @Test
    public void testProcess_whenMinimumIsNotMet() {
        commandLine.parse("a");
        group.process(commandLine, commandLine.listIterator());
        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.listIterator()));
    }

    @Test
    public void testProcess_whenMaximumIsExceeded() {
        commandLine.parse("a");
        commandLine.parse("a");
        commandLine.parse("a");
        group.process(commandLine, commandLine.listIterator());
        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.listIterator()));
    }

    @Test
    public void testValidate_whenMinimumIsNotMet() {
        commandLine.parse("a");
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testValidate_whenMaximumIsExceeded() {
        commandLine.parse("a");
        commandLine.parse("a");
        commandLine.parse("a");
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testValidate_whenValidOptionsArePresent() {
        commandLine.parse("a");
        commandLine.parse("b");
        group.validate(commandLine);
    }

    @Test
    public void testValidate_whenInvalidOptionIsPresent() {
        commandLine.parse("a");
        commandLine.parse("d");
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testValidate_whenAnonymousArgumentIsPresent() {
        commandLine.parse("c");
        group.validate(commandLine);
    }

    @Test
    public void testValidate_whenMinimumAndMaximumAreMet() {
        commandLine.parse("a");
        commandLine.parse("b");
        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage_withoutArgument() {
        final StringBuffer expected = new StringBuffer();
        group.appendUsage(expected, new HashSet(), null);
        String actual = expected.toString();
        assertEquals("test group", actual.substring(0, actual.indexOf("\n")));
        assertTrue(actual.contains("test group"));
        assertTrue(actual.contains("OPTIONS"));
        assertTrue(actual.contains("optionA"));
        assertTrue(actual.contains("optionB"));
        assertTrue(actual.contains("c"));
    }

    @Test
    public void testHelpLines_withDepthZero() {
        List expected = new ArrayList();
        expected.add(new HelpLineImpl("group", "", new String[0], "test group"));
        List actual = group.helpLines(0, new HashSet(), null);
        assertEquals(expected, actual);
    }

    @Test
    public void testHelpLines_withDepthOne() {
        List expected = new ArrayList();
        expected.add(new HelpLineImpl("optionA", "optionA", new String[0], "optionA"));
        expected.add(new HelpLineImpl("optionB", "optionB", new String[0], "optionB"));
        expected.add(new HelpLineImpl("c", "c", new String[0], "c"));
        List actual = group.helpLines(1, new HashSet(), null);
        assertEquals(expected, actual);
    }

    
}