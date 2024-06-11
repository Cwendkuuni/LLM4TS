package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ResourceHelper;
import org.apache.commons.cli2.validator.IntRangeValidator;
import org.apache.commons.cli2.validator.NumberValidator;
import org.apache.commons.cli2.validator.RangeValidator;
import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testCreateArgument() {
        Argument arg = new ArgumentImpl("arg1", "Description for arg1", 1, 5, 'a', 'b', new NumberValidator(), "--", "1,2,3", 0);

        assertEquals("arg1", arg.getPreferredName());
        assertEquals("Description for arg1", arg.getDescription());
        assertEquals(1, arg.getMinimum());
        assertEquals(5, arg.getMaximum());
        assertEquals('a', arg.getInitialSeparator());
        assertEquals('b', arg.getSubsequentSeparator());
        assertFalse(arg.getSubsequentSplit());
        assertNull(arg.getValidator());
        assertEquals("--", arg.getConsumeRemaining());
        assertSame(Collections.singletonList("1,2,3"), arg.getDefaultValues());
    }

    @Test
    public void testCreateArgumentWithValidator() {
        List<String> validValues = Arrays.asList("value1", "value2");
        Argument arg = new ArgumentImpl("arg1", "Description for arg1", 1, 5, 'a', 'b', new RangeValidator<>(1, 5), "--", validValues, 0);

        assertEquals("arg1", arg.getPreferredName());
        assertEquals("Description for arg1", arg.getDescription());
        assertEquals(1, arg.getMinimum());
        assertEquals(5, arg.getMaximum());
        assertEquals('a', arg.getInitialSeparator());
        assertEquals('b', arg.getSubsequentSeparator());
        assertTrue(arg.getSubsequentSplit());
        assertSame(new RangeValidator<>(1, 5), arg.getValidator());
        assertSame(validValues, arg.getDefaultValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimumGreaterThanMaximum() {
        new ArgumentImpl("arg1", "Description for arg1", 6, 4, 'a', 'b', null, "--", null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimumGreaterThanDefaultValues() {
        List<String> defaultValues = Arrays.asList("value1", "value2");
        new ArgumentImpl("arg1", "Description for arg1", 6, 5, 'a', 'b', null, "--", null, 0, defaultValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaximumLessThanMinimum() {
        new ArgumentImpl("arg1", "Description for arg1", 4, 6, 'a', 'b', null, "--", null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultValuesGreaterThanMaximum() {
        List<String> defaultValues = Arrays.asList("value1", "value2", "value3", "value4");
        new ArgumentImpl("arg1", "Description for arg1", 5, 5, 'a', 'b', null, "--", null, 0, defaultValues);
    }

    @Test
    public void testProcessValues() {
        String[] args = {"--arg1", "--arg1=value1", "--arg1=value1 value2", "--arg1=value1 value2 value3"};
        List<String> values = new ArrayList<>();

        Argument arg = new ArgumentImpl("arg1", "Description for arg1", 1, 3, 'a', 'b', null, "--", null, 0);

        CommandLine cmd = new CommandLine();
        cmd.parseArgument(args[0]);
        arg.process(cmd, args.listIterator(1), arg);

        assertEquals(2, cmd.getValues(arg).size());
        assertEquals("value1", cmd.getValues(arg).get(0));
        assertEquals("value1 value2", cmd.getValues(arg).get(1));

        cmd.clearValues(arg);
        arg.process(cmd, args.listIterator(1), arg);

        assertEquals(3, cmd.getValues(arg).size());
        assertEquals("value1", cmd.getValues(arg).get(0));
        assertEquals("value1 value2 value3", cmd.getValues(arg).get(1));
    }

    @Test
    public void testProcessValuesWithValidator() {
        Argument arg = new ArgumentImpl("arg1", "Description for arg1", 1, 3, 'a', 'b', new IntRangeValidator(1, 3), "--", null, 0);

        String[] args = {"--arg1", "--arg1=1", "--arg1=1 2"};

        CommandLine cmd = new CommandLine();
        arg.process(cmd, args.listIterator(1), arg);

        assertEquals(1, cmd.getValues(arg).size());
        assertEquals("1", cmd.getValues(arg).get(0));

        cmd.clearValues(arg);
        arg.process(cmd, args.listIterator(1), arg);

        assertEquals(2, cmd.getValues(arg).size());
        assertEquals("1", cmd.getValues(arg).get(0));
        assertEquals("2", cmd.getValues(arg).get(1));

        String[] invalidArgs = {"--arg1=4", "--arg1=4 5"};

        for (String invalidArg : invalidArgs) {
            arg.process(cmd, args.listIterator(1), arg);
            assertEquals(1, cmd.getValues(arg).size());
            assertEquals(arg.getName(), cmd.getValues(arg).get(0));
        }
    }

    @Test
    public void testProcessValueWithEmptyStringAndValidator() {
        Argument arg = new ArgumentImpl("arg1", "Description for arg1", 1, 3, 'a', 'b', new NumberValidator(), "--", null, 0);

        String[] args = {"--arg1"};

        CommandLine cmd = new CommandLine();
        arg.process(cmd, args.listIterator(1), arg);

        assertEquals(1, cmd.getValues(arg).size());
        assertEquals("", cmd.getValues(arg).get(0));

        cmd.clearValues(arg);

        arg = new ArgumentImpl("arg1", "Description for arg1", 1, 3, 'a', 'b', new NumberValidator(), "--", null, 0, Arrays.asList("1", "2"));

        String[] args2 = {"--arg1"};

        cmd.parseArgument(args2[0]);
        arg.process(cmd, args2.listIterator(1), arg);

        assertEquals(1, cmd.getValues(arg).size());
        assertEquals(arg.getName(), cmd.getValues(arg).get(0));

        cmd.clearValues(arg);

        arg.process(cmd, args2.listIterator(1), arg);

        assertEquals(1, cmd.getValues(arg).size());
        assertEquals("", cmd.getValues(arg).get(0));
    }

    @Test
    public void testProcessValuesWithDefaults() {
        String[] args = {"--arg1", "--arg1=value1", "--arg1=value1 value2", "--arg1=value1 value2 value3"};
        List<String> values = new ArrayList<>(Arrays.asList("value4", "value5"));

        Argument arg = new ArgumentImpl("arg1", "Description for arg1", 1, 3, 'a', 'b', null, "--", values, 0);

        CommandLine cmd = new CommandLine();
        arg.process(cmd, args.listIterator(1), arg);

        assertEquals(4, cmd.getValues(arg).size());
        assertEquals("value1", cmd.getValues(arg).get(0));
        assertEquals("value1 value2", cmd.getValues(arg).get(1));
        assertEquals("value1 value2 value3", cmd.getValues(arg).get(2));
        assertEquals("value4", cmd.getValues(arg).get(3));

        cmd.clearValues(arg);
        arg.process(cmd, args.listIterator(1), arg);

        assertEquals(5, cmd.getValues(arg).size());
        assertEquals("value1", cmd.getValues(arg).get(0));
        assertEquals("value1 value2", cmd.getValues(arg).get(1));
        assertEquals("value1 value2 value3", cmd.getValues(arg).get(2));
        assertEquals("value5", cmd.getValues(arg).get(3));
        assertEquals("", cmd.getValues(arg).get(4));
    }

    @Test
    public void testHelpLines() {
        Argument arg = new ArgumentImpl("arg1", "Description for arg1", 1, 3, 'a', 'b', null, "--", null, 0);

        List<HelpLine> helpLines = arg.helpLines(1, Collections.emptySet(), null);

        HelpLine helpLine = helpLines.get(0);

        assertEquals("--arg1 <value>...", helpLine.getLine());
        assertEquals("", helpLine.getSynopsis());
        assertEquals("", helpLine.getDescription());
        assertEquals(arg.getName(), helpLine.getOptionSpec().getLongOpt());
        assertEquals(arg.getName(), helpLine.getOptionSpec().getOpt());
        assertEquals("", helpLine.getValues());
    }

    @Test
    public void testHelpLinesWithDefaults() {
        Argument arg = new ArgumentImpl("arg1", "Description for arg1", 1, 3, 'a', 'b', null, "--", Arrays.asList("value1", "value2"), 0);

        List<HelpLine> helpLines = arg.helpLines(1, Collections.emptySet(), null);

        HelpLine helpLine = helpLines.get(0);

        assertEquals("--arg1 <value>...", helpLine.getLine());
        assertEquals("", helpLine.getSynopsis());
        assertEquals("", helpLine.getDescription());
        assertEquals(arg.getName(), helpLine.getOptionSpec().getLongOpt());
        assertEquals(arg.getName(), helpLine.getOptionSpec().getOpt());
        assertEquals("[value1, value2]", helpLine.getValues());
    }

    @Test
    public void testHelpLinesWithValidator() {
        Argument arg = new ArgumentImpl("arg1", "Description for arg1", 1, 3, 'a', 'b', new NumberValidator(), "--", null, 0);

        List<HelpLine> helpLines = arg.helpLines(1, Collections.emptySet(), null);

        HelpLine helpLine = helpLines.get(0);

        assertEquals("--arg1 <number>...", helpLine.getLine());
        assertEquals("", helpLine.getSynopsis());
        assertEquals("", helpLine.getDescription());
        assertEquals(arg.getName(), helpLine.getOptionSpec().getLongOpt());
        assertEquals(arg.getName(), helpLine.getOptionSpec().getOpt());
        assertEquals("", helpLine.getValues());
    }

    @Test
    public void testHelpLinesWithValidatorAndDefaults() {
        Argument arg = new ArgumentImpl("arg1", "Description for arg1", 1, 3, 'a', 'b', new NumberValidator(), "--", Arrays.asList("value1", "value2"), 0);

        List<HelpLine> helpLines = arg.helpLines(1, Collections.emptySet(), null);

        HelpLine helpLine = helpLines.get(0);

        assertEquals("--arg1 <number>...", helpLine.getLine());
        assertEquals("", helpLine.getSynopsis());
        assertEquals("", helpLine.getDescription());
        assertEquals(arg.getName(), helpLine.getOptionSpec().getLongOpt());
        assertEquals(arg.getName(), helpLine.getOptionSpec().getOpt());
        assertEquals("[value1, value2]", helpLine.getValues());
    }
}