import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.ArgumentImpl;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {

    private ArgumentImpl argument;
    private static final String ARG_NAME = "arg";
    private static final String ARG_DESCRIPTION = "argument description";
    private static final int ARG_MIN = 2;
    private static final int ARG_MAX = 5;
    private static final char ARG_INITIAL_SEPARATOR = '-';
    private static final char ARG_SUBSEQUENT_SEPARATOR = ':';
    private static final boolean ARG_SUBSEQUENT_SPLIT = true;
    private static final Validator ARG_VALIDATOR = new ValidatorStub();
    private static final String ARG_CONSUME_REMAINING = "--";
    private static final List<String> ARG_DEFAULTS = new ArrayList<>();

    @Before
    public void setUp() {
        argument = new ArgumentImpl(ARG_NAME, ARG_DESCRIPTION, ARG_MIN, ARG_MAX, ARG_INITIAL_SEPARATOR,
                ARG_SUBSEQUENT_SEPARATOR, ARG_VALIDATOR, ARG_CONSUME_REMAINING, ARG_DEFAULTS, 0);
    }

    @Test
    public void testName() {
        assertEquals(ARG_NAME, argument.getPreferredName());
    }

    @Test
    public void testMin() {
        assertEquals(ARG_MIN, argument.getMinimum());
    }

    @Test
    public void testMax() {
        assertEquals(ARG_MAX, argument.getMaximum());
    }

    @Test
    public void testInitialSeparator() {
        assertEquals(ARG_INITIAL_SEPARATOR, argument.getInitialSeparator());
    }

    @Test
    public void testSubsequentSeparator() {
        assertEquals(ARG_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    @Test
    public void testSubsequentSplit() {
        assertEquals(ARG_SUBSEQUENT_SPLIT, argument.subsequentSplit);
    }

    @Test
    public void testValidator() {
        assertEquals(ARG_VALIDATOR, argument.getValidator());
    }

    @Test
    public void testConsumeRemaining() {
        assertEquals(ARG_CONSUME_REMAINING, argument.getConsumeRemaining());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine cmdLine = new CommandLineStub();
        argument.defaults(cmdLine);

        assertEquals(ARG_DEFAULTS, cmdLine.getDefaultValues(argument));
    }

    @Test
    public void testDescription() {
        assertEquals(ARG_DESCRIPTION, argument.getDescription());
    }

    @Test
    public void testProcessValues() {
        // Test case where remaining options should be consumed
        WriteableCommandLine cmdLine = new CommandLineStub();
        List<String> args = new ArrayList<>();
        args.add("arg1");
        args.add("--");
        args.add("arg2");
        args.add("arg3");
        argument.processValues(cmdLine, args.listIterator(), argument);

        assertEquals("arg1", cmdLine.getValues(argument).get(0));
        assertEquals("arg2", cmdLine.getValues(argument).get(1));
        assertEquals("arg3", cmdLine.getValues(argument).get(2));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine cmdLine = new CommandLineStub();
        String arg = "argValue";

        assertTrue(argument.canProcess(cmdLine, arg));
    }

    @Test
    public void testGetPrefixes() {
        assertTrue(argument.getPrefixes().isEmpty());
    }

    @Test
    public void testGetTriggers() {
        assertTrue(argument.getTriggers().isEmpty());
    }

    @Test
    public void testValidate() throws Exception {
        WriteableCommandLine cmdLine = new CommandLineStub();
        cmdLine.addValue(argument, "value1");
        cmdLine.addValue(argument, "value2");
        argument.validate(cmdLine, argument);

        try {
            cmdLine.addValue(argument, "value3");
            cmdLine.addValue(argument, "value4");
            argument.validate(cmdLine, argument);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals(ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE),
                    e.getMessage());
        }

        try {
            cmdLine.addValue(argument, "value5");
            argument.validate(cmdLine, argument);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals(ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_MISSING_VALUES),
                    e.getMessage());
        }

        try {
            cmdLine.addValue(argument, "value6");
            cmdLine.addValue(argument, "value7");
            cmdLine.addValue(argument, "invalidValue");
            argument.validate(cmdLine, argument);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals(ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE),
                    e.getMessage());
        }
    }

    private static class ValidatorStub implements Validator {
        @Override
        public void validate(List<?> values) throws InvalidArgumentException {
            if (values.contains("invalidValue")) {
                throw new InvalidArgumentException("Invalid value");
            }
        }
    }

    private static class CommandLineStub implements WriteableCommandLine {
        private List<String> values = new ArrayList<>();
        private List<String> defaultVals = new ArrayList<>();

        @Override
        public List<String> getValues(ArgumentImpl argument) {
            return values;
        }

        @Override
        public void addValue(Option option, String value) {
            if (option instanceof ArgumentImpl) {
                values.add(value);
            }
        }

        @Override
        public void removeValue(Option option, String value) {
            if (option instanceof ArgumentImpl) {
                values.remove(value);
            }
        }

        @Override
        public void setDefaultValues(Option option, List<String> defaultValues) {
            if (option instanceof ArgumentImpl) {
                defaultVals = defaultValues;
            }
        }

        @Override
        public List<String> getDefaultValues(Option option) {
            return defaultVals;
        }

    }

}