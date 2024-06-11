package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArgumentImplTest {

    @Test
    public void testArgumentImplCreation() {
        Argument arg = new ArgumentImpl("arg1", "", 0, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                null, null, Collections.emptyList(), 0);
        assertEquals("arg1", arg.getPreferredName());
        assertTrue(arg.isRequired());
        assertNull(arg.getDescription());
        assertFalse(arg.canProcess(null, ""));
        assertEquals(Collections.EMPTY_LIST, arg.getDefaultValues());
        assertNull(arg.getValidator());

        Argument arg2 = new ArgumentImpl("arg2", "an argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                null, null, Collections.singletonList("default1"), 0);
        assertEquals("arg2", arg2.getPreferredName());
        assertFalse(arg2.isRequired());
        assertEquals("an argument", arg2.getDescription());
        assertTrue(arg2.canProcess(null, "default1"));
        assertSame("default1", arg2.getDefaultValues().get(0));
        assertNull(arg2.getValidator());

        Argument arg3 = new ArgumentImpl("arg3", "an argument", 0, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                null, null, Collections.singletonList("default1", "default2"), 0);
        assertEquals("arg3", arg3.getPreferredName());
        assertFalse(arg3.isRequired());
        assertEquals("an argument", arg3.getDescription());
        assertTrue(arg3.canProcess(null, "default1"));
        assertTrue(arg3.canProcess(null, "default2"));
        assertSame("default1", arg3.getDefaultValues().get(0));
        assertSame("default2", arg3.getDefaultValues().get(1));
        assertNull(arg3.getValidator());

        Argument arg4 = new ArgumentImpl("arg4", "an argument", 1, 3, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                null, null, Collections.singletonList("default1", "default2", "default3"), 0);
        assertEquals("arg4", arg4.getPreferredName());
        assertFalse(arg4.isRequired());
        assertEquals("an argument", arg4.getDescription());
        assertTrue(arg4.canProcess(null, "default1"));
        assertTrue(arg4.canProcess(null, "default2"));
        assertTrue(arg4.canProcess(null, "default3"));
        assertSame("default1", arg4.getDefaultValues().get(0));
        assertSame("default2", arg4.getDefaultValues().get(1));
        assertSame("default3", arg4.getDefaultValues().get(2));
        assertNull(arg4.getValidator());
    }

    @Test
    public void testArgumentImplProcessValues() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                null, null, Collections.singletonList("default1"), 0);

        WriteableCommandLine cmdLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--arg", "arg1", "--arg", "arg2");
        ListIterator<String> argIt = arguments.listIterator();

        try {
            arg.processValues(cmdLine, argIt, arg);
            assertEquals(2, cmdLine.getValues(arg).size());
            assertEquals("arg1", cmdLine.getValues(arg).get(0));
            assertEquals("arg2", cmdLine.getValues(arg).get(1));
        } catch (OptionException e) {
            // Should not reach here
            throw new RuntimeException("Unexpected error", e);
        }
    }

    @Test
    public void testArgumentImplProcessValuesWithConsume() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                null, "--consume", Collections.singletonList(), 0);

        WriteableCommandLine cmdLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--arg", "--consume", "arg1", "arg2", "arg3");
        ListIterator<String> argIt = arguments.listIterator();

        try {
            arg.processValues(cmdLine, argIt, arg);
            assertEquals(3, cmdLine.getValues(arg).size());
            assertEquals("arg1", cmdLine.getValues(arg).get(0));
            assertEquals("arg2", cmdLine.getValues(arg).get(1));
            assertEquals("arg3", cmdLine.getValues(arg).get(2));
        } catch (OptionException e) {
            // Should not reach here
            throw new RuntimeException("Unexpected error", e);
        }
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplProcessValuesWithInvalidValue() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                null, null, Collections.singletonList("default1"), 0);

        WriteableCommandLine cmdLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--arg", "arg1", "--arg", "arg2", "--arg", "invalid");
        ListIterator<String> argIt = arguments.listIterator();

        arg.processValues(cmdLine, argIt, arg);
    }

    @Test
    public void testArgumentImplValidate() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                null, null, Collections.singletonList("default1"), 0);
        List<String> values = Arrays.asList("arg1", "arg2", "arg3");

        try {
            arg.validate(new WriteableCommandLine(), arg);
            arg.validate(new WriteableCommandLine(), arg, values);
        } catch (OptionException e) {
            // Should not reach here
            throw new RuntimeException("Unexpected error", e);
        }

        try {
            Argument arg2 = new ArgumentImpl("arg2", "an argument", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                    new Validator() {
                        @Override
                        public void validate(List values) throws InvalidArgumentException {
                            String firstValue = (String) values.get(0);
                            if (firstValue.equals("invalid")) {
                                throw new InvalidArgumentException("Invalid value", new Exception());
                            }
                        }
                    }, null, Collections.singletonList("default1"), 0);
            List<String> values2 = Arrays.asList("default1", "invalid");

            arg2.validate(new WriteableCommandLine(), arg2);
            arg2.validate(new WriteableCommandLine(), arg2, values2);
        } catch (OptionException e) {
            // Expected
        }
    }

    @Test
    public void testArgumentImplHelpLines() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, Integer.MAX_VALUE, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                null, null, Collections.singletonList(), 0);
        List<HelpLine> helpLines = arg.helpLines(0, null, null);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("--arg an argument\n", helpLine.getShortDescription());
        assertEquals("an argument\n", helpLine.getLongDescription());
    }
}