package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.validator.Ranges;

public class ArgumentImplTest {

    @Test
    public void testArgumentImpl() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);
        assertEquals("arg", arg.getPreferredName());
        assertEquals("arg description", arg.getDescription());
        assertEquals(1, arg.getMinimum());
        assertEquals(2, arg.getMaximum());
        assertEquals(NUL, arg.getInitialSeparator());
        assertEquals(NUL, arg.getSubsequentSeparator());
        assertEquals(false, arg.isSubsequentSplit());
        assertNull(arg.getValidator());
        assertNull(arg.getConsumeRemaining());
        assertEquals(Arrays.asList("1", "2"), arg.getDefaultValues());

        Argument argNullName = new ArgumentImpl(null, "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);
        assertNull(argNullName.getPreferredName());

        Argument argMinExceedsMax = new ArgumentImpl("arg", "arg description", 4, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);
        assertThrows(IllegalArgumentException.class, () -> argMinExceedsMax.validate(null, null));

        Argument argTooFewDefaults = new ArgumentImpl("arg", "arg description", 2, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.singletonList("1"), 0);
        assertThrows(IllegalArgumentException.class, () -> argTooFewDefaults.validate(null, null));

        Argument argTooManyDefaults = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2", "3"), 0);
        assertThrows(IllegalArgumentException.class, () -> argTooManyDefaults.validate(null, null));

        Argument argValidator = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new Ranges(1, 5), null, Arrays.asList("1", "2"), 0);
        CommandLine commandLine = new CommandLine();
        assertThrows(OptionException.class, () -> argValidator.validate(commandLine, argValidator));
    }

    @Test
    public void testProcessValues() throws OptionException {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);

        List<String> args = Arrays.asList("--arg", "value1", "--arg value2", "value3");
        CommandLine commandLine = new CommandLine();
        arg.process(commandLine, args.listIterator());

        assertEquals(2, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));
    }

    @Test
    public void testCanProcess() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);

        CommandLine commandLine = new CommandLine();
        assertEquals(true, arg.canProcess(commandLine, "value1"));
        assertEquals(true, arg.canProcess(commandLine, "value1 value2"));
        assertEquals(true, arg.canProcess(commandLine, "--arg"));
        assertEquals(false, arg.canProcess(commandLine, "option"));
    }

    @Test
    public void testGetTriggers() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);

        Set<String> triggers = arg.getTriggers();
        assertEquals(Collections.EMPTY_SET, triggers);
    }

    @Test
    public void testProcess() throws OptionException {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);

        List<String> args = Arrays.asList("--arg", "value1", "--arg value2", "value3");
        CommandLine commandLine = new CommandLine();
        arg.process(commandLine, args.listIterator());

        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));

        HelpFormatter helpFormatter = new HelpFormatter();
        StringWriter writer = new StringWriter();
        helpFormatter.printHelp(writer, "test", "1.0", Arrays.asList(arg), null, true);
        assertEquals("arg description\n" +
                "[ARG]\n" +
                "arg description:\n" +
                "\targ\n" +
                "\t\tvalue1\n" +
                "\t\tvalue2\n", writer.toString());
    }

    @Test
    public void testGetInitialSeparator() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);
        assertEquals(NUL, arg.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);
        assertEquals(NUL, arg.getSubsequentSeparator());
    }

    @Test
    public void testAppendUsage() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);

        StringWriter writer = new StringWriter();
        arg.appendUsage(writer, null, null);
        assertEquals("arg 1 2\n", writer.toString());

        writer = new StringWriter();
        arg.appendUsage(writer, Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED), null);
        assertEquals("arg [1] [2]\n", writer.toString());

        writer = new StringWriter();
        arg.appendUsage(writer, null, Collections.singleton(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED));
        assertEquals("<arg> [1] [2]\n", writer.toString());

        writer = new StringWriter();
        arg.appendUsage(writer, null, Arrays.asList(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED, DisplaySetting.DISPLAY_ARGUMENT_BRACKETED));
        assertEquals("<arg> [1] [2]\n", writer.toString());
    }

    @Test
    public void testGetDescription() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);
        assertEquals("arg description", arg.getDescription());
    }

    @Test
    public void testHelpLines() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);

        HelpLine helpLine = arg.helpLines(1, null, null).get(0);
        assertEquals("arg description\n", helpLine.getShortDescription());
        assertEquals("arg description\n" +
                "[ARG]\n" +
                "arg description:\n" +
                "\targ\n", helpLine.getLongDescription());
    }

    @Test
    public void testGetMaximum() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);
        assertEquals(2, arg.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);
        assertEquals(1, arg.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);

        assertEquals("arg", arg.stripBoundaryQuotes("arg"));
        assertEquals("arg", arg.stripBoundaryQuotes("\"arg\""));
        assertEquals("arg", arg.stripBoundaryQuotes("arg\""));
        assertEquals("arg", arg.stripBoundaryQuotes("\"arg\""));
        assertEquals("arg", arg.stripBoundaryQuotes("\"arg\""));
        assertEquals("arg", arg.stripBoundaryQuotes("\"arg\""));
        assertEquals("arg", arg.stripBoundaryQuotes("arg\""));
        assertEquals("arg", arg.stripBoundaryQuotes("\"arg\""));
        assertEquals("arg", arg.stripBoundaryQuotes("\"arg\""));
        assertEquals("arg", arg.stripBoundaryQuotes("\"arg\""));
        assertEquals("arg", arg.stripBoundaryQuotes("arg"));
        assertEquals("arg", arg.stripBoundaryQuotes("arg"));

        assertEquals("arg1", arg.stripBoundaryQuotes("arg1"));
        assertEquals("arg1", arg.stripBoundaryQuotes("\"arg1\""));
        assertEquals("arg1", arg.stripBoundaryQuotes("arg1"));
        assertEquals("arg1", arg.stripBoundaryQuotes("arg1"));

        assertEquals("arg1 arg2", arg.stripBoundaryQuotes("arg1 arg2"));
        assertEquals("arg1 arg2", arg.stripBoundaryQuotes("\"arg1 arg2\""));
        assertEquals("arg1 arg2", arg.stripBoundaryQuotes("arg1 arg2"));
        assertEquals("arg1 arg2", arg.stripBoundaryQuotes("arg1 arg2"));
    }

    @Test
    public void testIsRequired() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);
        assertTrue(arg.isRequired());

        arg = new ArgumentImpl("arg", "arg description", 0, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);
        assertFalse(arg.isRequired());
    }

    @Test
    public void testValidate() throws OptionException {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("1", "2"), 0);

        CommandLine commandLine = new CommandLine();
        commandLine.addValue("arg1", "value1");
        commandLine.addValue("arg1", "value2");
        commandLine.addValue("arg1", "value3");
        arg.validate(commandLine);

        assertEquals(3, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
        assertEquals("value2", commandLine.getValues(arg).get(1));
        assertEquals("value3", commandLine.getValues(arg).get(2));
    }

    @Test
    public void testValidateWithValidator() throws OptionException, InvalidArgumentException {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new Ranges(1, 5), null, Arrays.asList("1", "2"), 0);

        CommandLine commandLine = new CommandLine();
        commandLine.addValue("arg1", "1");
        commandLine.addValue("arg1", "2");
        commandLine.addValue("arg1", "6");
        arg.validate(commandLine);

        assertEquals(2, commandLine.getValues(arg).size());
        assertEquals("1", commandLine.getValues(arg).get(0));
        assertEquals("2", commandLine.getValues(arg).get(1));

        assertThrows(InvalidArgumentException.class, () -> arg.validate(commandLine));
    }
}