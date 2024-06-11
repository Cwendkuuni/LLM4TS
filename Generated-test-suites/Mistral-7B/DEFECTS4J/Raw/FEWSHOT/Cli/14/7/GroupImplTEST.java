package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli2.ArgParseException;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.OptionValue;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GroupImplTest {
    private Option option1;
    private Option option2;
    private Option anonymousArg;
    private OptionGroup group;

    @Before
    public void setUp() {
        option1 = OptionBuilder.withLongOpt("option1")
                .withValueSeparator('=')
                .withDescription("This is option 1")
                .create();

        option2 = OptionBuilder.withLongOpt("option2")
                .withValueSeparator('=')
                .withDescription("This is option 2")
                .create();

        anonymousArg = OptionBuilder.withLongOpt("anonymous")
                .withValueSeparator(' ')
                .withoutArgName()
                .isAnnotationPresent(Option.Type.ARGUMENT)
                .create();

        final List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        group = new OptionGroupImpl(options, "myGroup", null, 1, Integer.MAX_VALUE);
    }

    @Test
    public void testGroupName() {
        assertEquals("myGroup", group.getPreferredName());
    }

    @Test
    public void testDescription() {
        assertNull(group.getDescription());
    }

    @Test
    public void testCanProcessWithNoOptions() {
        assertFalse(group.canProcess(null, null));
    }

    @Test
    public void testCanProcessWithEmptyArgument() {
        assertFalse(group.canProcess(null, ""));
    }

    @Test
    public void testCanProcessWithSingleOption() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1"));
        assertTrue(group.canProcess(cmd, "option1"));
    }

    @Test
    public void testCanProcessWithTwoOptions() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 -option2"));
        assertTrue(group.canProcess(cmd, "option1"));
        assertTrue(group.canProcess(cmd, "option2"));
    }

    @Test
    public void testCanProcessWithAnonymousArg() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 -anonymous arg1 arg2"));
        assertTrue(group.canProcess(cmd, "option1"));
        assertTrue(group.canProcess(cmd, "anonymous"));
        assertEquals(2, anonymousArg.getValueCount(cmd));
    }

    @Test
    public void testCanProcessWithOptionAfterAnonymousArg() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-anonymous arg1 -option1"));
        assertTrue(group.canProcess(cmd, "option1"));
        assertNull(anonymousArg.getValue(cmd));
    }

    @Test
    public void testCanProcessWithDuplicateOption() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 -option1"));
        assertFalse(group.canProcess(cmd, "option1"));
    }

    @Test
    public void testCanProcessWithDuplicateAnonymousArg() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-anonymous arg1 -anonymous arg2"));
        assertFalse(group.canProcess(cmd, "anonymous"));
    }

    @Test
    public void testCanProcessWithInvalidOption() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-invalid"));
        assertFalse(group.canProcess(cmd, "invalid"));
    }

    @Test
    public void testCanProcessWithInvalidAnonymousArg() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-anonymous invalid"));
        assertFalse(group.canProcess(cmd, "anonymous"));
    }

    @Test
    public void testCanProcessWithInvalidArgument() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 arg"));
        assertFalse(group.canProcess(cmd, "option1"));
    }

    @Test
    public void testCanProcessWithInvalidAnonymousArgument() {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-anonymous arg"));
        assertFalse(group.canProcess(cmd, "anonymous"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        List<String> triggers = new ArrayList<>(group.getTriggers());
        assertEquals(2, triggers.size());
        assertTrue(triggers.containsAll(List.of("option1", "option2")));
    }

    @Test
    public void testGetTriggersWithEmptyGroup() {
        OptionGroup emptyGroup = new OptionGroupImpl(new ArrayList<>(), null, null, 0, 0);
        List<String> triggers = new ArrayList<>(emptyGroup.getTriggers());
        assertTrue(triggers.isEmpty());
    }

    @Test
    public void testProcess() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 value1 -option2 value2"));
        group.process(cmd, cmd.iterator());
        assertEquals(1, option1.getValueCount(cmd));
        assertEquals("value1", option1.getValue(cmd));
        assertEquals(1, option2.getValueCount(cmd));
        assertEquals("value2", option2.getValue(cmd));
        assertNull(anonymousArg.getValue(cmd));
    }

    @Test
    public void testProcessWithSingleOption() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 value1"));
        group.process(cmd, cmd.iterator());
        assertEquals(1, option1.getValueCount(cmd));
        assertEquals("value1", option1.getValue(cmd));
        assertNull(anonymousArg.getValue(cmd));
    }

    @Test
    public void testProcessWithSingleAnonymousArg() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-anonymous arg1"));
        group.process(cmd, cmd.iterator());
        assertEquals(1, anonymousArg.getValueCount(cmd));
        assertEquals("arg1", anonymousArg.getValue(cmd));
    }

    @Test
    public void testProcessWithTwoOptionsAndAnonymousArg() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 value1 -anonymous arg1 -option2 value2"));
        group.process(cmd, cmd.iterator());
        assertEquals(1, option1.getValueCount(cmd));
        assertEquals("value1", option1.getValue(cmd));
        assertEquals(1, anonymousArg.getValueCount(cmd));
        assertEquals("arg1", anonymousArg.getValue(cmd));
        assertEquals(1, option2.getValueCount(cmd));
        assertEquals("value2", option2.getValue(cmd));
    }

    @Test
    public void testProcessWithTwoOptionsNoAnonymousArg() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 value1 -option2 value2"));
        group.process(cmd, cmd.iterator());
        assertEquals(1, option1.getValueCount(cmd));
        assertEquals("value1", option1.getValue(cmd));
        assertEquals(1, option2.getValueCount(cmd));
        assertEquals("value2", option2.getValue(cmd));
        assertNull(anonymousArg.getValue(cmd));
    }

    @Test
    public void testProcessWithDuplicateOption() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 value1 -option1 value2"));
        assertThrows(ArgParseException.class, () -> group.process(cmd, cmd.iterator()));
    }

    @Test
    public void testProcessWithDuplicateAnonymousArg() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-anonymous arg1 -anonymous arg2"));
        assertThrows(ArgParseException.class, () -> group.process(cmd, cmd.iterator()));
    }

    @Test
    public void testProcessWithInvalidOption() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-invalid"));
        assertThrows(ArgParseException.class, () -> group.process(cmd, cmd.iterator()));
    }

    @Test
    public void testProcessWithInvalidAnonymousArg() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-anonymous invalid"));
        assertThrows(ArgParseException.class, () -> group.process(cmd, cmd.iterator()));
    }

    @Test
    public void testProcessWithInvalidArgument() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 arg"));
        assertThrows(ArgParseException.class, () -> group.process(cmd, cmd.iterator()));
    }

    @Test
    public void testProcessWithInvalidAnonymousArgument() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-anonymous arg"));
        assertThrows(ArgParseException.class, () -> group.process(cmd, cmd.iterator()));
    }

    @Test
    public void testValidate() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 value1 -option2 value2"));
        group.validate(cmd);
    }

    @Test
    public void testValidateWithTooFewOptions() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 value1"));
        assertThrows(ArgParseException.class, () -> group.validate(cmd));
    }

    @Test
    public void testValidateWithTwoOptions() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 value1 -option2 value2"));
        group.validate(cmd);
    }

    @Test
    public void testValidateWithTooManyOptions() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 value1 -option2 value1 value2"));
        assertThrows(ArgParseException.class, () -> group.validate(cmd));
    }

    @Test
    public void testValidateWithTwoOptionsAndAnonymousArg() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 value1 -anonymous arg1 -option2 value2"));
        group.validate(cmd);
    }

    @Test
    public void testValidateWithTwoOptionsNoAnonymousArg() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 value1 -option2 value2"));
        group.validate(cmd);
    }

    @Test
    public void testValidateWithInvalidOption() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-invalid"));
        assertThrows(ArgParseException.class, () -> group.validate(cmd));
    }

    @Test
    public void testValidateWithInvalidAnonymousArg() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-anonymous invalid"));
        assertThrows(ArgParseException.class, () -> group.validate(cmd));
    }

    @Test
    public void testValidateWithInvalidArgument() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-option1 arg"));
        assertThrows(ArgParseException.class, () -> group.validate(cmd));
    }

    @Test
    public void testValidateWithInvalidAnonymousArgument() throws ParseException {
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parseCommandLine("-anonymous arg"));
        assertThrows(ArgParseException.class, () -> group.validate(cmd));
    }

    @Test
    public void testWithHelpFormatter() {
        final HelpFormatter formatter = new HelpFormatter();
        final String usage = formatter.printHelp(GroupImpl.class.getName(), "", true);
        assertTrue(usage.contains("option1"));
        assertTrue(usage.contains("option2"));
        assertTrue(usage.contains("anonymous"));
    }

    @Test
    public void testHelpLines() {
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(3, helpLines.size());

        final HelpLine helpLine1 = helpLines.get(0);
        assertEquals("--option1 VALUE", helpLine1.getLeftDescription());
        assertEquals("This is option 1", helpLine1.getLongDescription());

        final HelpLine helpLine2 = helpLines.get(1);
        assertEquals("--option2 VALUE", helpLine2.getLeftDescription());
        assertEquals("This is option 2", helpLine2.getLongDescription());

        final HelpLine helpLine3 = helpLines.get(2);
        assertEquals("--anonymous VALUE", helpLine3.getLeftDescription());
        assertEquals("This is an anonymous argument", helpLine3.getLongDescription());
    }

    @Test
    public void testHelpLinesWithEmptyGroup() {
        OptionGroup emptyGroup = new OptionGroupImpl(new ArrayList<>(), null, null, 0, 0);
        List<HelpLine> helpLines = emptyGroup.helpLines(0, Collections.emptySet(), null);
        assertTrue(helpLines.isEmpty());
    }
}

class OptionGroupImpl extends GroupImpl {
    public OptionGroupImpl(final List<Option> options,
                           final String name,
                           final String description,
                           final int minimum,
                           final int maximum) {
        super(options, name, description, minimum, maximum);
    }
}

class ReverseStringComparator implements Comparator<String> {
    public static final Comparator INSTANCE = new ReverseStringComparator();

    private ReverseStringComparator() {
        // Prevent instantiation
    }

    @Override
    public int compare(final String o1, final String o2) {
        return o1.compareTo(o2) * -1;
    }
}