package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ValidationResult;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for ArgumentImpl class.
 */
public class ArgumentImplTest {

    private Argument argument;
    private Option option;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        option = OptionBuilder.withName("arg")
                .withDescription("A test argument")
                .withMinValues(1)
                .withMaxValues(3)
                .withValidator(new Validator() {
                    @Override
                    public void validate(List values) throws InvalidArgumentException {
                        for (Object value : values) {
                            assertTrue(value instanceof String);
                            assertTrue(value.toString().length() > 0);
                        }
                    }
                })
                .create();

        argument = new ArgumentImpl("arg", "A test argument", 1, 3, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, new Validator() {
                    @Override
                    public void validate(List values) throws InvalidArgumentException {
                        for (Object value : values) {
                            assertTrue(value instanceof String);
                            assertTrue(value.toString().length() > 0);
                        }
                    }
                }, "--", Arrays.asList("value1", "value2"), 0);

        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testGetName() {
        assertEquals("arg", argument.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("A test argument", argument.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testProcessValues_ValidArgument() {
        List<String> arguments = Arrays.asList("--arg", "value1", "value2");

        argument.process(commandLine, arguments.listIterator());

        List<String> values = commandLine.getValues(option);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testProcessValues_ConsumeRemaining() {
        List<String> arguments = Arrays.asList("--arg", "--consume", "value1", "value2");

        argument.process(commandLine, arguments.listIterator());

        List<String> values = commandLine.getValues(option);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testProcessValues_SubsequentSplit() {
        List<String> arguments = Arrays.asList("--arg", "value1,value2");

        argument.process(commandLine, arguments.listIterator());

        List<String> values = commandLine.getValues(option);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testProcessValues_InvalidArgument() {
        List<String> arguments = Arrays.asList("--arg", "value1,value2", "--consume");

        assertThrows(OptionException.class, () -> argument.process(commandLine, arguments.listIterator()));
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "--arg"));
    }

    @Test
    public void testCanProcess_InvalidArgument() {
        assertFalse(argument.canProcess(commandLine, "--invalid"));
    }

    @Test
    public void testValidate_ValidArguments() {
        List<String> arguments = Arrays.asList("--arg", "value1", "value2");

        ValidationResult result = argument.validate(commandLine);

        assertEquals(0, result.getErrors().size());
    }

    @Test
    public void testValidate_InvalidArgument() {
        List<String> arguments = Arrays.asList("--arg", "value1", "value2", "--invalid");

        ValidationResult result = argument.validate(commandLine);

        assertEquals(1, result.getErrors().size());
    }

    @Test
    public void testValidate_InvalidArgumentCount() {
        List<String> arguments = Arrays.asList("--arg", "value1");

        assertThrows(OptionException.class, () -> argument.validate(commandLine));
    }

    @Test
    public void testAppendUsage() {
        StringBuilder buffer = new StringBuilder();

        argument.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals("arg [value1] [value2] ...\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        HelpFormatter helpFormatter = new HelpFormatter();

        List<HelpLine> helpLines = argument.helpLines(1, Collections.emptySet(), null);

        String helpText = helpFormatter.printHelp(".", Arrays.asList(option), true);

        assertEquals(helpText, helpLines.get(0).toString());
    }
}