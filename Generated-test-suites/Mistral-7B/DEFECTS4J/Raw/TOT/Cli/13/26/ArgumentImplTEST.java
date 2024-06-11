package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.Validator;
import org.apache.commons.cli2.validation.Validators;

import org.junit.Test;

/**
 * Tests for {@link ArgumentImpl}.
 */
public class ArgumentImplTest {

    /**
     * Test for {@link ArgumentImpl#ArgumentImpl(String, String, int, int, char, char, Validator, String, List, int)}.
     */
    @Test
    public void testArgumentImpl() {
        final Argument arg = new ArgumentImpl("arg", "Description", 0, 1, 'a', 'b', Validators.nullValidator(), "", Arrays.asList("value"), 0);
        assertEquals("arg", arg.getPreferredName());
        assertEquals("Description", arg.getDescription());
        assertEquals(0, arg.getMinimum());
        assertEquals(1, arg.getMaximum());
        assertEquals('a', arg.getInitialSeparator());
        assertEquals('b', arg.getSubsequentSeparator());
        assertFalse(arg.subsequentSplit());
        assertNull(arg.getValidator());
        assertEquals("", arg.getConsumeRemaining());
        assertNull(arg.getDefaultValues());

        final Argument arg2 = new ArgumentImpl("arg2", null, 1, 1, 'a', 'b', null, "", Arrays.asList("value"), 1);
        assertEquals("arg2", arg2.getPreferredName());
        assertNull(arg2.getDescription());
        assertEquals(1, arg2.getMinimum());
        assertEquals(1, arg2.getMaximum());
        assertEquals('a', arg2.getInitialSeparator());
        assertEquals('b', arg2.getSubsequentSeparator());
        assertFalse(arg2.subsequentSplit());
        assertNull(arg2.getValidator());
        assertEquals("", arg2.getConsumeRemaining());
        assertNull(arg2.getDefaultValues());

        final Argument arg3 = new ArgumentImpl("arg3", "", 0, Integer.MAX_VALUE, 'a', 'b', null, "", Arrays.asList("value"), 0);
        assertEquals("arg3", arg3.getPreferredName());
        assertNull(arg3.getDescription());
        assertEquals(0, arg3.getMinimum());
        assertEquals(Integer.MAX_VALUE, arg3.getMaximum());
        assertEquals('a', arg3.getInitialSeparator());
        assertEquals('b', arg3.getSubsequentSeparator());
        assertFalse(arg3.subsequentSplit());
        assertNull(arg3.getValidator());
        assertNull(arg3.getConsumeRemaining());
        assertNull(arg3.getDefaultValues());
    }

    /**
     * Test for {@link ArgumentImpl#processValues(WriteableCommandLine, ListIterator, Option)}.
     */
    @Test
    public void testProcessValues() {
        final Argument arg = new ArgumentImpl("arg", "Description", 0, 1, 'a', 'b', Validators.nullValidator(), "", Arrays.asList("value"), 0);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final List<Object> arguments = new ArrayList<>();
        arguments.add("--arg value1");
        arguments.add("--arg value2");
        arguments.add("--arg value3");
        arguments.add("--arg value4");
        arguments.add("--consume-remaining");
        arguments.add("value5");
        arguments.add("--arg value6");
        arguments.add("--arg value7");

        arg.processValues(commandLine, arguments.listIterator(), arg);

        assertEquals(3, commandLine.getValues(arg).size());
        assertTrue(commandLine.getValues(arg).contains("value1"));
        assertTrue(commandLine.getValues(arg).contains("value2"));
        assertTrue(commandLine.getValues(arg).contains("value3"));

        assertEquals(0, commandLine.getValues("--consume-remaining").size());
        assertEquals(4, commandLine.getValues("--arg").size());
        assertTrue(commandLine.getValues("--arg").contains("value5"));
        assertTrue(commandLine.getValues("--arg").contains("value6"));
        assertTrue(commandLine.getValues("--arg").contains("value7"));
    }

    /**
     * Test for {@link ArgumentImpl#processValues(WriteableCommandLine, ListIterator, Option)} with invalid input.
     */
    @Test
    public void testProcessValuesInvalidInput() {
        final Argument arg = new ArgumentImpl("arg", "Description", 0, 1, 'a', 'b', Validators.nullValidator(), "", Arrays.asList("value"), 0);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final List<Object> arguments = new ArrayList<>();
        arguments.add("--arg value1 value2");
        arguments.add("--arg value2");
        arguments.add("--arg value3 value4");

        assertThrows(OptionException.class, () -> arg.processValues(commandLine, arguments.listIterator(), arg));
    }

    /**
     * Test for {@link ArgumentImpl#process(WriteableCommandLine, ListIterator)}.
     */
    @Test
    public void testProcess() {
        final Argument arg = new ArgumentImpl("arg", "Description", 0, 1, 'a', 'b', null, "", Arrays.asList("value"), 0);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final List<Object> arguments = new ArrayList<>();
        arguments.add("--arg value1");
        arguments.add("--arg value2");
        arguments.add("--arg value3");
        arguments.add("--consume-remaining");
        arguments.add("value5");
        arguments.add("--arg value6");
        arguments.add("--arg value7");

        arg.process(commandLine, arguments.listIterator());

        assertEquals(3, commandLine.getValues(arg).size());
        assertTrue(commandLine.getValues(arg).contains("value1"));
        assertTrue(commandLine.getValues(arg).contains("value2"));
        assertTrue(commandLine.getValues(arg).contains("value3"));

        assertEquals(0, commandLine.getValues("--consume-remaining").size());
        assertEquals(4, commandLine.getValues("--arg").size());
        assertTrue(commandLine.getValues("--arg").contains("value5"));
        assertTrue(commandLine.getValues("--arg").contains("value6"));
        assertTrue(commandLine.getValues("--arg").contains("value7"));
    }

    /**
     * Test for {@link ArgumentImpl#getInitialSeparator()}, {@link ArgumentImpl#getSubsequentSeparator()},
     * {@link ArgumentImpl#subsequentSplit()}.
     */
    @Test
    public void testSeparators() {
        final Argument arg1 = new ArgumentImpl("arg1", "Description", 0, 1, 'a', 'b', null, "", Arrays.asList("value"), 0);

        assertEquals('a', arg1.getInitialSeparator());
        assertEquals('b', arg1.getSubsequentSeparator());
        assertFalse(arg1.subsequentSplit());

        final Argument arg2 = new ArgumentImpl("arg2", "Description", 0, 1, 'a', '\u0000', null, "", Arrays.asList("value"), 0);

        assertEquals('a', arg2.getInitialSeparator());
        assertEquals('\u0000', arg2.getSubsequentSeparator());
        assertFalse(arg2.subsequentSplit());

        final Argument arg3 = new ArgumentImpl("arg3", "Description", 0, 1, 'a', 'b', null, "", Arrays.asList("value"), 0);

        assertEquals('a', arg3.getInitialSeparator());
        assertEquals('b', arg3.getSubsequentSeparator());
        assertTrue(arg3.subsequentSplit());
    }

    /**
     * Test for {@link ArgumentImpl#validate(WriteableCommandLine, Option)}.
     */
    @Test
    public void testValidate() {
        final Argument arg = new ArgumentImpl("arg", "Description", 0, 1, 'a', 'b', null, "", Arrays.asList("value"), 0);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.setValues(arg, Arrays.asList("value1", "value2", "value3"));

        arg.validate(commandLine, arg);

        assertTrue(commandLine.getValues(arg).size() == 3);

        final Argument arg2 = new ArgumentImpl("arg", "Description", 0, 1, 'a', 'b', null, "", Arrays.asList("value"), 0);

        final WriteableCommandLine commandLine2 = new WriteableCommandLine();
        commandLine2.setValues(arg2, Arrays.asList("value1", "value2"));

        assertThrows(OptionException.class, () -> arg2.validate(commandLine2, arg2));
    }

    /**
     * Test for {@link ArgumentImpl#appendUsage(StringBuffer, Set, Comparator)}.
     */
    @Test
    public void testAppendUsage() {
        final Argument arg = new ArgumentImpl("arg", "Description", 0, 1, 'a', 'b', null, "", Arrays.asList("value"), 0);

        final StringBuilder buffer = new StringBuilder();
        arg.appendUsage(buffer, null, null);

        assertEquals("arg", buffer.toString());

        final Argument arg2 = new ArgumentImpl("arg", "Description", 0, 1, 'a', 'b', null, "", Arrays.asList("value"), 0);

        final StringBuilder buffer2 = new StringBuilder();
        arg2.appendUsage(buffer2, null, null);

        assertEquals("arg [value] ...", buffer2.toString());

        final Argument arg3 = new ArgumentImpl("arg", "Description", 0, 1, 'a', 'b', null, "", Arrays.asList("value"), 0);

        final StringBuilder buffer3 = new StringBuilder();
        arg3.appendUsage(buffer3, null, null);

        assertEquals("arg [value]\n", buffer3.toString());

        final Argument arg4 = new ArgumentImpl("arg", "Description", 0, 1, 'a', 'b', null, "", Arrays.asList("value"), 0);

        final StringBuilder buffer4 = new StringBuilder();
        arg4.appendUsage(buffer4, null, null);

        assertEquals("arg [value]\n", buffer4.toString());

        final Argument arg5 = new ArgumentImpl("arg", "Description", 1, 1, 'a', 'b', null, "", Arrays.asList("value"), 1);

        final StringBuilder buffer5 = new StringBuilder();
        arg5.appendUsage(buffer5, null, null);

        assertEquals("arg [value]\n", buffer5.toString());

        final Argument arg6 = new ArgumentImpl("arg", "Description", 2, 1, 'a', 'b', null, "", Arrays.asList("value"), 2);

        final StringBuilder buffer6 = new StringBuilder();
        arg6.appendUsage(buffer6, null, null);

        assertEquals("arg [value]\narg [value]\n", buffer6.toString());

        final Argument arg7 = new ArgumentImpl("arg", "Description", 0, Integer.MAX_VALUE, 'a', 'b', null, "", Arrays.asList("value"), 0);

        final StringBuilder buffer7 = new StringBuilder();
        arg7.appendUsage(buffer7, null, null);

        assertEquals("arg [value] ...\n", buffer7.toString());
    }

    /**
     * Test for {@link ArgumentImpl#getDefaultValues()}.
     */
    @Test
    public void testDefaultValues() {
        final Argument arg = new ArgumentImpl("arg", "Description", 1, 1, 'a', 'b', null, "", Arrays.asList("value1"), 1);

        assertEquals(1, arg.getDefaultValues().size());
        assertEquals("value1", arg.getDefaultValues().get(0));

        final Argument arg2 = new ArgumentImpl("arg", "Description", 1, 1, 'a', 'b', null, "", null, 1);

        assertEquals(0, arg2.getDefaultValues().size());

        final Argument arg3 = new ArgumentImpl("arg", "Description", 1, 1, 'a', 'b', null, "", Arrays.asList("value1", "value2"), 1);

        assertEquals(2, arg3.getDefaultValues().size());
        assertEquals("value1", arg3.getDefaultValues().get(0));
        assertEquals("value2", arg3.getDefaultValues().get(1));

        final Argument arg4 = new ArgumentImpl("arg", "Description", 1, 1, 'a', 'b', null, "", Arrays.asList("value1", "value2"), 2);

        assertEquals(2, arg4.getDefaultValues().size());
        assertEquals("value1", arg4.getDefaultValues().get(0));
        assertEquals("value2", arg4.getDefaultValues().get(1));

        final Argument arg5 = new ArgumentImpl("arg", "Description", 1, 1, 'a', 'b', null, "", Arrays.asList("value1", "value2"), 3);

        assertEquals(2, arg5.getDefaultValues().size());
        assertEquals("value1", arg5.getDefaultValues().get(0));
        assertEquals("value2", arg5.getDefaultValues().get(1));
    }

    /**
     * Test for {@link ArgumentImpl#isRequired()}.
     */
    @Test
    public void testIsRequired() {
        final Argument arg1 = new ArgumentImpl("arg1", "Description", 1, 1, 'a', 'b', null, "", Arrays.asList("value"), 1);

        assertTrue(arg1.isRequired());

        final Argument arg2 = new ArgumentImpl("arg2", "Description", 0, 1, 'a', 'b', null, "", Arrays.asList("value"), 1);

        assertFalse(arg2.isRequired());
    }

    /**
     * Test for {@link ArgumentImpl#defaults(WriteableCommandLine)}.
     */
    @Test
    public void testDefaults() {
        final Argument arg = new ArgumentImpl("arg", "Description", 1, 1, 'a', 'b', null, "", Arrays.asList("value1"), 1);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        arg.defaults(commandLine);

        assertEquals(1, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
    }

    /**
     * Test for {@link ArgumentImpl#defaultValues(WriteableCommandLine, Option)}.
     */
    @Test
    public void testDefaultValuesOption() {
        final Argument arg = new ArgumentImpl("arg", "Description", 1, 1, 'a', 'b', null, "", Arrays.asList("value1"), 1);

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        arg.defaultValues(commandLine, arg);

        assertEquals(1, commandLine.getValues(arg).size());
        assertEquals("value1", commandLine.getValues(arg).get(0));
    }
}