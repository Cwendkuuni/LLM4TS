import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@PowerMockIgnore({"junit.internal.reflect.DelegatingMethodAccessor$1"})
@RunWith(PowerMockRunner.class)
public class ArgumentImplTest {

    private Option option;
    private Argument argument;
    private CommandLine commandLine;

    @Before
    public void setUp() throws Exception {
        option = OptionBuilder.withLongOpt("test")
                .withDescription("Test argument")
                .create();
        argument = new ArgumentImpl("arg", "Test argument", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, null, null, 0);
        commandLine = new CommandLine();
    }

    @Test
    public void testArgumentImplCreation() {
        assertNotNull(argument);
    }

    @Test
    public void testGetName() {
        assertEquals("arg", argument.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test argument", argument.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    @Test
    public void testGetSubsequentSplit() {
        assertFalse(argument.getSubsequentSplit());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals(ArgumentImpl.DEFAULT_CONSUME_REMAINING, argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testProcessValues() throws Exception {
        String[] args = {"test", "value1", "value2"};
        TestUtils.parseArguments(ArgString.class, commandLine, args);

        List<String> values = commandLine.getValues(option);
        assertNotNull(values);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesInvalidArgument() throws Exception {
        String[] args = {"test", "value1", "value2", "invalid"};
        TestUtils.parseArguments(ArgString.class, commandLine, args);
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesConsumeRemaining() throws Exception {
        String[] args = {"test", "--", "value1", "value2"};
        TestUtils.parseArguments(ArgString.class, commandLine, args);

        List<String> values = commandLine.getValues(option);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals("value1,value2", values.get(0));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesInvalidConsumeRemaining() throws Exception {
        String[] args = {"test", "--", "value1", "value2", "invalid"};
        TestUtils.parseArguments(ArgString.class, commandLine, args);
    }

    @Test
    public void testProcess() throws Exception {
        String[] args = {"test", "value1", "value2"};
        PowerMockito.doThrow(new OptionException(option, "Test error")).when(commandLine).addValue(option, "value3");
        TestUtils.parseArguments(ArgString.class, commandLine, args);

        List<String> values = commandLine.getValues(option);
        assertNotNull(values);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        assertFalse(commandLine.hasOption("test", "value3"));
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "value"));
    }

    @Test
    public void testGetPrefixes() {
        assertSame(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testValidate() throws Exception {
        String[] args = {"test", "value1", "value2", "value3"};
        TestUtils.parseArguments(ArgString.class, commandLine, args);

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalidArgument() throws Exception {
        String[] args = {"test", "value1", "value2", "invalid"};
        TestUtils.parseArguments(ArgString.class, commandLine, args);

        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        assertEquals("  [arg]            Test argument", buffer.toString());
    }

    @Test
    public void testHelpLines() throws Exception {
        StringBuffer buffer = new StringBuffer();
        HelpFormatter formatter = new HelpFormatter();
        argument.appendUsage(buffer, null, null);

        List<HelpLine> helpLines = argument.helpLines(0, null, null);

        formatter.printHelp(System.out, "", buffer.toString(), null, helpLines, 0);
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testGetValueString() throws Exception {
        String[] args = {"test", "value1", "value2"};
        TestUtils.parseArguments(ArgString.class, commandLine, args);

        assertEquals("value1,value2", argument.getValueString(commandLine));
    }

    @Test
    public void testGetValue() throws Exception {
        String[] args = {"test", "value1", "value2"};
        TestUtils.parseArguments(ArgString.class, commandLine, args);

        assertEquals(Arrays.asList("value1", "value2"), argument.getValue(commandLine));
    }

    @Test
    public void testDefaults() throws Exception {
        String[] args = {};
        TestUtils.parseArguments(ArgString.class, commandLine, args);

        List<String> values = commandLine.getValues(option);
        assertNotNull(values);
        assertEquals(0, values.size());

        argument.defaults(commandLine);

        values = commandLine.getValues(option);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals("", values.get(0));
    }

    @Test
    public void testDefaultsWithValues() throws Exception {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        argument = new ArgumentImpl("arg", "Test argument", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, null, defaultValues, 0);

        String[] args = {};
        TestUtils.parseArguments(ArgString.class, commandLine, args);

        List<String> values = commandLine.getValues(option);
        assertNotNull(values);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testValidateWithValidator() throws Exception {
        String[] args = {"test", "value1", "value2", "invalid"};
        TestUtils.parseArguments(ArgString.class, commandLine, args);

        Validator validator = new Validator() {
            @Override
            public void validate(List<String> values) throws InvalidArgumentException {
                if (values.size() > 2) {
                    throw new InvalidArgumentException("Test error");
                }
            }
        };

        argument = new ArgumentImpl("arg", "Test argument", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, validator, null, null, 0);

        try {
            argument.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Test error", e.getMessage());
        }
    }
}

class ArgString {
}