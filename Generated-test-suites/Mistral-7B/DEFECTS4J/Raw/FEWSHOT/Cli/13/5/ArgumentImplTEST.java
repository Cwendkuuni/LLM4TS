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

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.ResourceHelper;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.HelpFormatterHelper;
import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testArgumentImplWithNoArgs() {
        Argument arg = new ArgumentImpl("arg1", "test argument", 0, 0, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.emptyList(), 0);
        assertEquals("arg1", arg.getPreferredName());
        assertTrue(arg.isRequired());
        assertSame(Collections.EMPTY_LIST, arg.getDefaultValues());
        assertTrue(arg.getInitialSeparator() == DEFAULT_INITIAL_SEPARATOR);
        assertTrue(arg.getSubsequentSeparator() == DEFAULT_SUBSEQUENT_SEPARATOR);
        assertSame(Collections.EMPTY_SET, arg.getPrefixes());
        assertSame(Collections.EMPTY_SET, arg.getTriggers());
        assertNull(arg.getDescription());
        assertTrue(arg.canProcess(null, ""));
        assertTrue(arg.process(null, null) instanceof ArgumentImpl);
        assertNull(arg.getValidator());
        assertSame(Collections.EMPTY_SET, arg.getHelpSettings());
    }

    @Test
    public void testArgumentImplWithOneArg() {
        Argument arg = new ArgumentImpl("arg1", "test argument", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.singletonList("value1"), 0);
        assertEquals("arg1", arg.getPreferredName());
        assertFalse(arg.isRequired());
        assertSame(Arrays.asList("value1"), arg.getDefaultValues());
        assertTrue(arg.getInitialSeparator() == DEFAULT_INITIAL_SEPARATOR);
        assertTrue(arg.getSubsequentSeparator() == DEFAULT_SUBSEQUENT_SEPARATOR);
        assertSame(Collections.EMPTY_SET, arg.getPrefixes());
        assertSame(Collections.EMPTY_SET, arg.getTriggers());
        assertEquals("test argument", arg.getDescription());
        assertTrue(arg.canProcess(null, "value1"));
        assertTrue(arg.process(null, null) instanceof ArgumentImpl);
        assertNull(arg.getValidator());
    }

    @Test
    public void testArgumentImplWithManyArgs() {
        Argument arg = new ArgumentImpl("arg1", "test argument", 3, 5, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, new ArrayList<>(), 0);
        assertEquals("arg1", arg.getPreferredName());
        assertFalse(arg.isRequired());
        assertTrue(arg.getMinimum() == 3);
        assertTrue(arg.getMaximum() == 5);
        assertSame(Collections.emptyList(), arg.getDefaultValues());
        assertTrue(arg.getInitialSeparator() == DEFAULT_INITIAL_SEPARATOR);
        assertTrue(arg.getSubsequentSeparator() == DEFAULT_SUBSEQUENT_SEPARATOR);
        assertSame(Collections.EMPTY_SET, arg.getPrefixes());
        assertSame(Collections.EMPTY_SET, arg.getTriggers());
        assertEquals("test argument", arg.getDescription());
        assertTrue(arg.canProcess(null, "value1 value2 value3"));
        assertTrue(arg.process(null, null) instanceof ArgumentImpl);
        assertNull(arg.getValidator());
    }

    @Test
    public void testArgumentImplWithValidator() {
        Argument arg = new ArgumentImpl("arg1", "test argument", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), null, Collections.singletonList("value1"), 0);
        assertEquals("arg1", arg.getPreferredName());
        assertFalse(arg.isRequired());
        assertSame(Arrays.asList("value1"), arg.getDefaultValues());
        assertTrue(arg.getInitialSeparator() == DEFAULT_INITIAL_SEPARATOR);
        assertTrue(arg.getSubsequentSeparator() == DEFAULT_SUBSEQUENT_SEPARATOR);
        assertSame(Collections.EMPTY_SET, arg.getPrefixes());
        assertSame(Collections.EMPTY_SET, arg.getTriggers());
        assertEquals("test argument", arg.getDescription());
        assertNull(arg.getValidator());

        try {
            arg.validate(null, null);
        } catch (OptionException e) {
            // expected
        }

        String[] values = {"value1", "value2", "value3"};
        CommandLine commandLine = new CommandLine();
        arg.process(commandLine, Arrays.asList(values).listIterator());

        assertSame(3, commandLine.getValues(arg).size());
        try {
            arg.validate(commandLine);
        } catch (OptionException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentImplWithMinimumGreaterThanMaximum() {
        new ArgumentImpl("arg1", "test argument", 5, 3, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, new ArrayList<>(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentImplWithFewerDefaultValuesThanMinimum() {
        new ArgumentImpl("arg1", "test argument", 3, 3, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, new ArrayList<>(Arrays.asList("value1", "value2")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentImplWithMoreDefaultValuesThanMaximum() {
        new ArgumentImpl("arg1", "test argument", 3, 2, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, new ArrayList<>(Arrays.asList("value1", "value2", "value3")), 0);
    }

    @Test
    public void testArgumentImplHelpText() {
        Argument arg = new ArgumentImpl("arg1", "test argument", 1, 1, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Collections.singletonList("value1"), 0);
        List<HelpLine> helpLines = arg.helpLines(0, Collections.emptySet(DisplaySetting.class), null);
        HelpFormatter formatter = new HelpFormatterHelper().newHelpFormatter();
        String helpText = formatter.printHelp("Test", Arrays.asList(arg), true);
        assertTrue(helpText.contains("arg1"));
        assertTrue(helpText.contains("test argument"));
        assertTrue(helpText.contains("optional"));
        assertTrue(helpText.contains("value1"));
    }

    static class MyValidator implements Validator {
        @Override
        public void validate(List values) throws InvalidArgumentException {
            int sum = 0;
            for (Object value : values) {
                sum += Integer.parseInt(value.toString());
            }
            if (sum % 2 != 0) {
                throw new InvalidArgumentException("Invalid arguments!");
            }
        }
    }
}