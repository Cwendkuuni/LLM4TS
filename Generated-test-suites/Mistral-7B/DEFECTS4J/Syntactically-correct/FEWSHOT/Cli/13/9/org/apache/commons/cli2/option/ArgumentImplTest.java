package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.MockValidator;
import org.apache.commons.cli2.validation.Validator;

public class ArgumentImplTest {

    @Test
    public void testArgumentImpl() {
        Argument arg = new ArgumentImpl("arg", "arg description", 0, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);
        assertEquals("arg", arg.getPreferredName());
        assertEquals("arg description", arg.getDescription());
        assertEquals(0, arg.getMinimum());
        assertEquals(1, arg.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, arg.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, arg.getSubsequentSeparator());
        assertFalse(arg.getSubsequentSplit());
        assertNull(arg.getValidator());
        assertEquals(DEFAULT_CONSUME_REMAINING, arg.getConsumeRemaining());
        assertNull(arg.getDefaultValues());

        Argument arg1 = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, new ArrayList<String>(), 0);
        assertEquals("arg", arg1.getPreferredName());
        assertEquals("arg description", arg1.getDescription());
        assertEquals(1, arg1.getMinimum());
        assertEquals(1, arg1.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, arg1.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, arg1.getSubsequentSeparator());
        assertFalse(arg1.getSubsequentSplit());
        assertNotNull(arg1.getValidator());
        assertNotNull(arg1.getDefaultValues());
    }

    @Test
    public void testProcessValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An example option")
                .isRequired(false)
                .create();

        WriteableCommandLine commandLine = new CommandLine();
        commandLine.setParser(OptionParserBuilder.getDefaultParser());

        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        List<String> arguments = new ArrayList<>();
        arguments.add("--option value1");
        arguments.add("value2");
        arguments.add("--arg value1 value2");
        arguments.add("value3");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");

        arg.processValues(commandLine, arguments.listIterator(), option);

        assertEquals(expectedValues, commandLine.getValues(option));
    }

    @Test
    public void testCanProcess() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An example option")
                .isRequired(false)
                .create();

        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        WriteableCommandLine commandLine = new CommandLine();
        commandLine.setParser(OptionParserBuilder.getDefaultParser());

        assertTrue(arg.canProcess(commandLine, "--option value1"));
        assertTrue(arg.canProcess(commandLine, "value1"));
        assertTrue(arg.canProcess(commandLine, "value1 value2"));
        assertFalse(arg.canProcess(commandLine, "--option"));
        assertFalse(arg.canProcess(commandLine, "--anotherOption"));
    }

    @Test
    public void testGetPrefixes() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        Set<String> prefixes = arg.getPrefixes();

        assertTrue(Collections.isEmpty(prefixes));
    }

    @Test
    public void testProcess() throws OptionException {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An example option")
                .isRequired(false)
                .create();

        WriteableCommandLine commandLine = new CommandLine();
        commandLine.setParser(OptionParserBuilder.getDefaultParser());

        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        List<String> arguments = new ArrayList<>();
        arguments.add("--option value1");
        arguments.add("--arg value1 value2");

        arg.process(commandLine, arguments.listIterator(), option);

        assertEquals("value1 value2", commandLine.getValues(option).get(0));
    }

    @Test
    public void testGetInitialSeparator() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        assertEquals(DEFAULT_INITIAL_SEPARATOR, arg.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, arg.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        Set<String> triggers = arg.getTriggers();

        assertTrue(Collections.isEmpty(triggers));
    }

    @Test
    public void testGetConsumeRemaining() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        assertEquals(DEFAULT_CONSUME_REMAINING, arg.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, new ArrayList<String>(), 0);

        assertEquals(arg.getDefaultValues(), commandLine.getDefaultValues(arg));
    }

    @Test
    public void testValidate() throws OptionException {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An example option")
                .isRequired(false)
                .create();

        WriteableCommandLine commandLine = new CommandLine();
        commandLine.setParser(OptionParserBuilder.getDefaultParser());

        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        List<String> arguments = new ArrayList<>();
        arguments.add("--option value1");
        arguments.add("--arg value1 value2 value3");

        List<String> values = arg.getValues(commandLine, option);

        arg.validate(commandLine, option);

        assertEquals(2, values.size());
        assertEquals("value1 value2", values.get(0));
        assertEquals("value3", values.get(1));
    }

    @Test
    public void testAppendUsage() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        StringBuffer buffer = new StringBuffer();
        arg.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals("arg [value]", buffer.toString());
    }

    @Test
    public void testIsRequired() {
        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        assertFalse(arg.isRequired());

        arg = new ArgumentImpl("arg", "arg description", 1, 0, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        assertTrue(arg.isRequired());
    }

    @Test
    public void testDefaults() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An example option")
                .isRequired(false)
                .create();

        WriteableCommandLine commandLine = new CommandLine();
        commandLine.setParser(OptionParserBuilder.getDefaultParser());

        Argument arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        arg.defaults(commandLine);

        assertEquals(Collections.emptyList(), commandLine.getValues(option));

        arg = new ArgumentImpl("arg", "arg description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MockValidator(), DEFAULT_CONSUME_REMAINING, Arrays.asList("value1", "value2"), 0);

        arg.defaults(commandLine);

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option));
    }

    private static class HelpLineImpl implements HelpLine {

        private final Argument arg;
        private final int depth;

        public HelpLineImpl(Argument arg, int depth) {
            this.arg = arg;
            this.depth = depth;
        }

        @Override
        public String getHeadText(String name) {
            StringBuilder headText = new StringBuilder();

            for (int i = 0; i < depth; ++i) {
                headText.append("  ");
            }

            headText.append(String.format("%-15s", name));

            return headText.toString();
        }

        @Override
        public String getOptionSpec() {
            StringBuilder optionSpec = new StringBuilder();

            if (arg.getInitialSeparator() != DEFAULT_INITIAL_SEPARATOR) {
                optionSpec.append(arg.getInitialSeparator());
            }

            optionSpec.append(arg.getName());

            if (arg.getSubsequentSeparator() != DEFAULT_SUBSEQUENT_SEPARATOR) {
                optionSpec.append(arg.getSubsequentSeparator());
            }

            if (arg.getSubsequentSplit()) {
                optionSpec.append(" [");
                optionSpec.append(arg.getSubsequentSeparator());
                optionSpec.append("]*");
            }

            return optionSpec.toString();
        }

        @Override
        public String getDescription() {
            return arg.getDescription();
        }

        @Override
        public String getLongOpt() {
            return "--" + arg.getName();
        }

        @Override
        public String getShortOpt() {
            return null;
        }

        @Override
        public String getValueDescription() {
            return "";
        }

        @Override
        public String getValuePattern() {
            return "";
        }
    }

    private static class MockValidator implements Validator {

        @Override
        public void validate(List<String> values) throws InvalidArgumentException {
            // Do nothing
        }
    }
}