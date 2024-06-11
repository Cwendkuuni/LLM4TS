package org.apache.commons.cli2.option;

import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.ValidationResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ArgumentImplTest {

    private ArgumentImpl argument;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, null, Collections.emptyList(), 0);
        formatter = new HelpFormatter();
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() throws ParseException {
        CommandLine cmd = new CommandLine();
        List args = new ArrayList();
        args.add("--arg");
        args.add("value1");
        args.add("value2");

        argument.processValues(cmd, args.listIterator(), argument);

        assertEquals(2, cmd.getValues(argument).size());
        assertEquals("value1", cmd.getValues(argument).get(0));
        assertEquals("value2", cmd.getValues(argument).get(1));
    }

    @Test(expected = ParseException.class)
    public void testProcessValuesWithExceedingNumberOfValues() throws ParseException {
        CommandLine cmd = new CommandLine();
        List args = new ArrayList();
        args.add("--arg");
        args.add("value1");
        args.add("value2");
        args.add("value3");

        argument.processValues(cmd, args.listIterator(), argument);
    }

    @Test(expected = ParseException.class)
    public void testProcessValuesWithInvalidValue() throws ParseException {
        CommandLine cmd = new CommandLine();
        List args = new ArrayList();
        args.add("--arg");
        args.add("value1");
        args.add("invalidValue");

        argument.processValues(cmd, args.listIterator(), argument);
    }

    @Test(expected = ParseException.class)
    public void testProcessValuesWithConsumingOption() throws ParseException {
        CommandLine cmd = new CommandLine();
        List args = new ArrayList();
        args.add("--consumingOption");
        args.add("--arg");
        args.add("value1");

        Argument arg = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, null, Collections.emptyList(), 0);
        arg.setConsumeRemaining(true);

        arg.processValues(cmd, args.listIterator(), arg);

        assertEquals(1, cmd.getValues(arg).size());
        assertEquals("value1", cmd.getValues(arg).get(0));
        assertEquals("--consumingOption", cmd.getValues(Option.class.cast(arg.getOption())).get(0));
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(new CommandLine(), "value"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testProcess() throws ParseException {
        CommandLine cmd = new CommandLine();
        List args = new ArrayList();
        args.add("--arg");
        args.add("value1");

        argument.process(cmd, args.listIterator());

        assertEquals(1, cmd.getValues(argument).size());
        assertEquals("value1", cmd.getValues(argument).get(0));
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(' ', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals(null, argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(Collections.emptyList(), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertEquals(null, argument.getValidator());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithExceedingNumberOfValues() throws OptionException {
        Argument arg = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, null, Collections.emptyList(), 0);
        Validator validator = mock(Validator.class);

        arg.validate(new CommandLine(), arg, validator);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValue() throws OptionException {
        Argument arg = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, null, Collections.emptyList(), 0);
        Validator validator = mock(Validator.class);
        ValidationResult validationResult = mock(ValidationResult.class);

        when(validator.validate(arg.getDefaultValues())).thenThrow(new InvalidArgumentException("Invalid value"));

        arg.validate(new CommandLine(), arg, validator);
    }

    @Test
    public void testValidateWithValidValues() throws OptionException {
        Argument arg = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, null, Collections.emptyList(), 0);
        Validator validator = mock(Validator.class);

        when(validator.validate(arg.getDefaultValues())).thenReturn(new ValidationResult());

        arg.validate(new CommandLine(), arg, validator);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        assertEquals("  arg", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        List helpLines = argument.helpLines(1, null, null);

        assertEquals(1, helpLines.size());

        HelpLine helpLine = HelpLine.class.cast(helpLines.get(0));

        assertEquals("arg: ", helpLine.getLeftPadding());
        assertEquals("description", helpLine.getHelp());
        assertEquals("arg [value...]", helpLine.getLongOpt());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        assertEquals("", argument.stripBoundaryQuotes(""));
        assertEquals("foo", argument.stripBoundaryQuotes("foo"));
        assertEquals("\"foo\"", argument.stripBoundaryQuotes("\"foo\""));
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        CommandLine cmd = new CommandLine();

        argument.defaults(cmd);

        assertTrue(cmd.hasOption(argument.getOption()));
    }
}