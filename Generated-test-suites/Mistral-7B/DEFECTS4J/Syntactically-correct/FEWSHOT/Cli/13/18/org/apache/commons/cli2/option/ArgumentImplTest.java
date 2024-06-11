package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Test;

public class ArgumentImplTest {
    @Test
    public void testArgumentImplCreation() {
        Argument arg = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ', null, "--", Collections.emptyList(), 0);
        assertNotNull(arg);
        assertEquals("testArg", arg.getPreferredName());
        assertEquals("Test Argument", arg.getDescription());
        assertEquals(1, arg.getMinimum());
        assertEquals(1, arg.getMaximum());
        assertEquals(' ', arg.getInitialSeparator());
        assertEquals(' ', arg.getSubsequentSeparator());
        assertFalse(arg.getSubsequentSplit());
        assertNull(arg.getValidator());
        assertNull(arg.getConsumeRemaining());
        assertNotNull(arg.getDefaultValues());
        assertEquals(0, arg.getDefaultValues().size());
    }

    @Test
    public void testArgumentImplCreationWithValidator() {
        Validator validator = new Validator() {
            @Override
            public void validate(List values) throws InvalidArgumentException {
                assertEquals(1, values.size());
                String value = (String) values.get(0);
                assertTrue(value.length() > 5);
            }
        };
        Argument arg = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ', validator, "--", Collections.emptyList(), 0);
        assertNotNull(arg);
        assertEquals("testArg", arg.getPreferredName());
        assertEquals("Test Argument", arg.getDescription());
        assertEquals(1, arg.getMinimum());
        assertEquals(1, arg.getMaximum());
        assertEquals(' ', arg.getInitialSeparator());
        assertEquals(' ', arg.getSubsequentSeparator());
        assertTrue(arg.getSubsequentSplit());
        assertNotNull(arg.getValidator());
        assertNull(arg.getConsumeRemaining());
        assertNotNull(arg.getDefaultValues());
        assertEquals(0, arg.getDefaultValues().size());
    }

    @Test
    public void testArgumentImplCreationWithDefaultValues() {
        Argument arg = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ', null, "--", Collections.singletonList("default"), 0);
        assertNotNull(arg);
        assertEquals("testArg", arg.getPreferredName());
        assertEquals("Test Argument", arg.getDescription());
        assertEquals(1, arg.getMinimum());
        assertEquals(1, arg.getMaximum());
        assertEquals(' ', arg.getInitialSeparator());
        assertEquals(' ', arg.getSubsequentSeparator());
        assertFalse(arg.getSubsequentSplit());
        assertNull(arg.getValidator());
        assertNotNull(arg.getConsumeRemaining());
        assertEquals("--", arg.getConsumeRemaining());
        assertNotNull(arg.getDefaultValues());
        assertEquals(1, arg.getDefaultValues().size());
        assertEquals("default", arg.getDefaultValues().get(0));
    }

    @Test
    public void testArgumentImplProcessValues() throws OptionException {
        Argument arg = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ', null, "--", Collections.emptyList(), 0);
        WriteableCommandLine commandLine = new CommandLine();

        List arguments = new ArrayList();
        arguments.add("--testArg arg1");
        arguments.add("arg2");
        arguments.add("--consumeRemaining");

        arg.processValues(commandLine, arguments.listIterator(), arg);

        CommandLine parsedCommandLine = commandLine.parse();
        assertEquals(2, parsedCommandLine.getArgList().size());
        assertEquals("arg1", parsedCommandLine.getArgList().get(0));
        assertEquals("arg2", parsedCommandLine.getArgList().get(1));
    }

    @Test
    public void testArgumentImplProcessValuesWithConsumingOption()
        throws OptionException {
        Argument arg = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ', null, "--", Collections.emptyList(), 0);

        WriteableCommandLine commandLine = new CommandLine();
        List arguments = new ArrayList();

        arguments.add("--testArg arg1");
        arguments.add("--consumeRemaining");
        arguments.add("arg3");
        arguments.add("arg4");

        arg.processValues(commandLine, arguments.listIterator(), arg);

        CommandLine parsedCommandLine = commandLine.parse();
        assertEquals(3, parsedCommandLine.getArgList().size());
        assertEquals("arg3", parsedCommandLine.getArgList().get(1));
        assertEquals("arg4", parsedCommandLine.getArgList().get(2));
    }

    @Test
    public void testArgumentImplProcessValuesWithValidator()
        throws OptionException, InvalidArgumentException {
        Validator validator = new Validator() {
            @Override
            public void validate(List values) throws InvalidArgumentException {
                assertEquals(1, values.size());
                String value = (String) values.get(0);
                assertTrue(value.length() > 5);
            }
        };

        Argument arg = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ', validator, "--", Collections.emptyList(), 0);

        WriteableCommandLine commandLine = new CommandLine();
        List arguments = new ArrayList();

        arguments.add("--testArg test");
        arguments.add("--testArg longValueThatIsLongEnough");

        arg.processValues(commandLine, arguments.listIterator(), arg);

        CommandLine parsedCommandLine = commandLine.parse();
        assertEquals(1, parsedCommandLine.getArgList().size());
        assertEquals("longValueThatIsLongEnough", parsedCommandLine.getArgList().get(0));
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplProcessValuesWithInvalidArguments()
        throws OptionException {
        Argument arg = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ', null, "--", Collections.emptyList(), 0);

        WriteableCommandLine commandLine = new CommandLine();
        List arguments = new ArrayList();

        arguments.add("--testArg arg1 arg2");

        arg.processValues(commandLine, arguments.listIterator(), arg);

        try {
            commandLine.parse();
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplProcessValuesWithInvalidArgumentsAndConsumingOption()
        throws OptionException {
        Argument arg = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ', null, "--", Collections.emptyList(), 0);

        WriteableCommandLine commandLine = new CommandLine();
        List arguments = new ArrayList();

        arguments.add("--testArg arg1 arg2");
        arguments.add("--consumeRemaining");

        arg.processValues(commandLine, arguments.listIterator(), arg);

        try {
            commandLine.parse();
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testArgumentImplProcessValuesWithInvalidArgumentsAndValidator()
        throws OptionException, InvalidArgumentException {
        Validator validator = new Validator() {
            @Override
            public void validate(List values) throws InvalidArgumentException {
                assertEquals(1, values.size());
                String value = (String) values.get(0);
                assertTrue(value.length() > 5);
            }
        };

        Argument arg = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ', validator, "--", Collections.emptyList(), 0);

        WriteableCommandLine commandLine = new CommandLine();
        List arguments = new ArrayList();

        arguments.add("--testArg test");

        arg.processValues(commandLine, arguments.listIterator(), arg);

        try {
            commandLine.parse();
            fail();
        } catch (OptionException e) {
            try {
                validator.validate(Collections.singletonList("test"));
                fail();
            } catch (InvalidArgumentException ive) {
                // expected
                throw e.getCause();
            }
        }
    }

    @Test
    public void testArgumentImplGetters() {
        Argument arg = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ', null, "--", Collections.emptyList(), 0);
        assertEquals(' ', arg.getInitialSeparator());
        assertEquals(' ', arg.getSubsequentSeparator());
        assertFalse(arg.getSubsequentSplit());
        assertNull(arg.getValidator());
        assertNull(arg.getConsumeRemaining());
        assertNotNull(arg.getDefaultValues());
        assertEquals(0, arg.getDefaultValues().size());
    }

    @Test
    public void testArgumentImplHelpLines() {
        Argument arg = new ArgumentImpl("testArg", "Test Argument", 1, 1, ' ', ' ', null, "--", Collections.emptyList(), 0);

        List helpLines = arg.helpLines(1, Collections.emptySet(), null);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = (HelpLine) helpLines.get(0);
        assertEquals("--testArg <argument>\n", helpLine.getShortDescription());
        assertEquals("Test Argument\n", helpLine.getLongDescription());
        assertNull(helpLine.getValues());
    }
}