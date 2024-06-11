package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.validation.StringValidator;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

/**
 * Test class for ArgumentImpl.
 */
public class ArgumentImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testArgumentImplConstructor() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, 'a', 'b',
                new StringValidator(), "", Collections.singletonList("default"), 0);
        assertNotNull(arg);
        assertSame(arg.getClass(), ArgumentImpl.class);
    }

    /**
     * Test getters.
     */
    @Test
    public void testGetters() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, 'a', 'b',
                new StringValidator(), "", Arrays.asList("default1", "default2"), 0);
        assertSame(arg.getName(), "arg");
        assertSame(arg.getDescription(), "Argument test");
        assertSame(arg.getMinimum(), 1);
        assertSame(arg.getMaximum(), 1);
        assertSame(arg.getInitialSeparator(), 'a');
        assertSame(arg.getSubsequentSeparator(), 'b');
        assertFalse(arg.getSubsequentSplit());
        assertNull(arg.getValidator());
        assertSame(arg.getConsumeRemaining(), "");
        assertSame(arg.getDefaultValues(), Arrays.asList("default1", "default2"));
    }

    /**
     * test processValues method.
     */
    @Test
    public void testProcessValues() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, 'a', 'b',
                new StringValidator(), "", Collections.singletonList("default"), 0);
        WriteableCommandLine cmdLine = new CommandLine();
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1 arg2 arg3");
        List<Option> options = new ArrayList<>();
        Option option = OptionBuilder.withName("testOption")
                .withDescription("Test option")
                .withArgument(arg)
                .build();
        options.add(option);
        cmdLine.setArgs(arguments.toArray(new String[0]));
        cmdLine.setOptions(options.toArray(new Option[0]));

        arg.process(cmdLine, arguments.listIterator());

        List<String> values = cmdLine.getValues("testOption");
        assertNotNull(values);
        assertSame(values.size(), 3);
        assertSame(values.get(0), "arg1");
        assertSame(values.get(1), "arg2");
        assertSame(values.get(2), "arg3");
    }

    /**
     * Test canProcess method.
     */
    @Test
    public void testCanProcess() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, 'a', 'b',
                new StringValidator(), "", Collections.singletonList("default"), 0);
        WriteableCommandLine cmdLine = new CommandLine();
        List<String> arguments = new ArrayList<>();

        // test valid value
        arguments.add("arg1");
        assertTrue(arg.canProcess(cmdLine, "arg1"));

        // test invalid value
        arguments.set(0, "testOption");
        assertFalse(arg.canProcess(cmdLine, "testOption"));
    }

    /**
     * Test validate method.
     */
    @Test
    public void testValidate() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, 'a', 'b',
                new StringValidator(), "", Collections.singletonList("default"), 0);
        WriteableCommandLine cmdLine = new CommandLine();
        List<String> arguments = new ArrayList<>();

        // test valid values
        arguments.add("arg1");
        arguments.add("arg2");
        try {
            arg.validate(cmdLine);
        } catch (OptionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // test invalid value
        arguments.set(0, "invalidArg");
        try {
            arg.validate(cmdLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals("Argument arg: invalidArg is not a valid value for argument test",
                    e.getMessage());
        }
    }

    /**
     * Test appendUsage method.
     */
    @Test
    public void testAppendUsage() {
        Argument arg = new ArgumentImpl("arg", "Argument test", 1, 1, 'a', 'b',
                new StringValidator(), "", Collections.singletonList("default"), 0);

        StringBuilder buffer = new StringBuilder();
        arg.appendUsage(buffer, null, null);
        assertSame(buffer.toString(), "arg [optional] <default>");

        arg = new ArgumentImpl("arg1", "Argument test1", 1, 2, 'a', 'b',
                new StringValidator(), "", Collections.singletonList("default1"), 0);
        buffer.setLength(0);
        arg.appendUsage(buffer, null, null);
        assertSame(buffer.toString(), "arg1 [optional] <default1>");

        arg = new ArgumentImpl("arg2", "Argument test2", 2, 2, 'a', 'b',
                new StringValidator(), "", Arrays.asList("default1", "default2"), 0);
        buffer.setLength(0);
        arg.appendUsage(buffer, null, null);
        assertSame(buffer.toString(), "arg2 [optional] <default1> <default2>");

        arg = new ArgumentImpl("arg3", "Argument test3", 1, Integer.MAX_VALUE, 'a', 'b',
                new StringValidator(), "", Arrays.asList("default1"), 0);
        buffer.setLength(0);
        arg.appendUsage(buffer, null, null);
        assertSame(buffer.toString(), "arg3 [optional] <default1> ...");
    }
}