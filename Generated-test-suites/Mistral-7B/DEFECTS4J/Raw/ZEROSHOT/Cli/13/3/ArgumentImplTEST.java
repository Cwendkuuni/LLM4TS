import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ArgumentImplTest {

    private CommandLine commandLine;
    private Argument argument;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
        argument = new ArgumentImpl("option", "Option description", 1, 1, ' ', ' ', null, null, Arrays.asList("value"), 0);
    }

    @After
    public void tearDown() {
        commandLine = null;
        argument = null;
    }

    @Test
    public void testArgumentName() {
        assertEquals("option", argument.getPreferredName());
    }

    @Test
    public void testArgumentDescription() {
        assertEquals("Option description", argument.getDescription());
    }

    @Test
    public void testArgumentProcessValues() throws Exception {
        List arguments = Arrays.asList("value1", "value2", "value3");
        argument.process(commandLine, arguments.listIterator(0));

        assertEquals(3, commandLine.getValues(argument).size());
        assertSame("value1", commandLine.getValues(argument).get(0));
        assertSame("value2", commandLine.getValues(argument).get(1));
        assertSame("value3", commandLine.getValues(argument).get(2));
    }

    @Test
    public void testArgumentCanProcess() {
        assertTrue(argument.canProcess(commandLine, "value1"));
    }

    @Test(expected = OptionException.class)
    public void testArgumentCanProcessOption() throws Exception {
        argument.canProcess(commandLine, "--option");
    }

    @Test
    public void testArgumentGetInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testArgumentGetSubsequentSeparator() {
        assertEquals(' ', argument.getSubsequentSeparator());
    }

    @Test
    public void testArgumentGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testArgumentGetConsumeRemaining() {
        assertNull(argument.getConsumeRemaining());
    }

    @Test
    public void testArgumentgetDefaultValues() {
        assertEquals(Arrays.asList("value"), argument.getDefaultValues());
    }

    @Test
    public void testArgumentGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testArgumentValidate() throws Exception {
        List values = Arrays.asList("value1", "value2", "value3");
        argument.validate(commandLine, argument);

        assertEquals(3, commandLine.getValues(argument).size());
        assertSame("value1", commandLine.getValues(argument).get(0));
        assertSame("value2", commandLine.getValues(argument).get(1));
        assertSame("value3", commandLine.getValues(argument).get(2));
    }

    @Test(expected = OptionException.class)
    public void testArgumentValidateInvalidArgument() throws Exception {
        List values = Arrays.asList("value1", "value2", "invalidValue");
        argument.validate(commandLine, argument);
    }

    @Test
    public void testArgumentValidateEmptyList() throws Exception {
        List<String> values = new ArrayList<>();
        argument.validate(commandLine, argument);
    }

    @Test
    public void testArgumentAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        assertEquals("  [option]                   Option description                   ", buffer.toString());
    }

    @Test
    public void testArgumentHelpLines() {
        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> lines = argument.helpLines(0, null, null);

        String expected = "  [option]                   Option description                   ";
        HelpLine line = lines.get(0);

        assertEquals(expected, line.getLeftValue());
        assertEquals(expected, line.getLongValue());
    }

    @Test
    public void testArgumentMinimum() {
        Argument minArgument = new ArgumentImpl("minArgument", "Minimum argument", 1, 1, ' ', ' ', null, null, null, 0);
        Argument maxArgument = new ArgumentImpl("maxArgument", "Maximum argument", 1, 1, ' ', ' ', null, null, null, 0);

        assertEquals(1, minArgument.getMinimum());
        assertEquals(1, minArgument.getMaximum());
        assertEquals(1, maxArgument.getMinimum());
        assertEquals(1, maxArgument.getMaximum());

        Argument invalidMinArgument = new ArgumentImpl("invalidMinArgument", "Minimum argument", 0, 1, ' ', ' ', null, null, null, 0);

        assertEquals(0, invalidMinArgument.getMinimum());
        assertEquals(1, invalidMinArgument.getMaximum());
    }

    @Test
    public void testArgumentMaximum() {
        Argument minArgument = new ArgumentImpl("minArgument", "Minimum argument", 1, 1, ' ', ' ', null, null, null, 0);
        Argument maxArgument = new ArgumentImpl("maxArgument", "Maximum argument", 1, 2, ' ', ' ', null, null, null, 0);

        assertEquals(1, minArgument.getMinimum());
        assertEquals(1, minArgument.getMaximum());
        assertEquals(1, maxArgument.getMinimum());
        assertEquals(2, maxArgument.getMaximum());

        Argument invalidMaxArgument = new ArgumentImpl("invalidMaxArgument", "Maximum argument", 1, 0, ' ', ' ', null, null, null, 0);

        assertEquals(1, invalidMaxArgument.getMinimum());
        assertEquals(0, invalidMaxArgument.getMaximum());
    }

    @Test
    public void testArgumentDefaults() throws Exception {
        Argument argumentWithDefaults = new ArgumentImpl("option", "Option description", 1, 1, ' ', ' ', null, null, Arrays.asList("value1", "value2"), 0);

        TestUtils.runCommandLine(ArgumentImplTest.class, new String[]{"-o", "option", "value3"});
        assertEquals(3, commandLine.getValues(argumentWithDefaults).size());
        assertSame("value3", commandLine.getValues(argumentWithDefaults).get(0));
        assertSame("value1", commandLine.getValues(argumentWithDefaults).get(1));
        assertSame("value2", commandLine.getValues(argumentWithDefaults).get(2));

        TestUtils.runCommandLine(ArgumentImplTest.class, new String[]{"-o", "option"});
        assertEquals(2, commandLine.getValues(argumentWithDefaults).size());
        assertSame("value1", commandLine.getValues(argumentWithDefaults).get(0));
        assertSame("value2", commandLine.getValues(argumentWithDefaults).get(1));

        TestUtils.runCommandLine(ArgumentImplTest.class, new String[]{"-o", "option", "value1"});
        assertEquals(2, commandLine.getValues(argumentWithDefaults).size());
        assertSame("value1", commandLine.getValues(argumentWithDefaults).get(0));
        assertSame("value2", commandLine.getValues(argumentWithDefaults).get(1));
    }

    @Test
    public void testArgumentSubsequentSeparator() throws Exception {
        Argument argumentWithSubsequentSeparator = new ArgumentImpl("option", "Option description", 1, 1, ' ', '|', null, null, null, 0);

        TestUtils.runCommandLine(ArgumentImplTest.class, new String[]{"-o", "option", "value1|value2|value3"});
        assertEquals(3, commandLine.getValues(argumentWithSubsequentSeparator).size());
        assertSame("value1", commandLine.getValues(argumentWithSubsequentSeparator).get(0));
        assertSame("value2", commandLine.getValues(argumentWithSubsequentSeparator).get(1));
        assertSame("value3", commandLine.getValues(argumentWithSubsequentSeparator).get(2));

        TestUtils.runCommandLine(ArgumentImplTest.class, new String[]{"-o", "option", "value1|value2"});
        assertEquals(2, commandLine.getValues(argumentWithSubsequentSeparator).size());
        assertSame("value1", commandLine.getValues(argumentWithSubsequentSeparator).get(0));
        assertSame("value2", commandLine.getValues(argumentWithSubsequentSeparator).get(1));

        TestUtils.runCommandLine(ArgumentImplTest.class, new String[]{"-o", "option", "value1"});
        assertEquals(1, commandLine.getValues(argumentWithSubsequentSeparator).size());
        assertSame("value1", commandLine.getValues(argumentWithSubsequentSeparator).get(0));
    }

    @Test
    public void testArgumentValidator() throws Exception {
        Argument validatorArgument = new ArgumentImpl("validatorArgument", "Validator argument", 1, 1, ' ', ' ', ValidatorTests.newIntValidator(), null, null, 0);

        TestUtils.runCommandLine(ArgumentImplTest.class, new String[]{"-o", "validatorArgument", "1", "2"});
        assertEquals(2, commandLine.getValues(validatorArgument).size());
        assertEquals(1, (Integer)commandLine.getValues(validatorArgument).get(0));
        assertEquals(2, (Integer)commandLine.getValues(validatorArgument).get(1));

        TestUtils.runCommandLine(ArgumentImplTest.class, new String[]{"-o", "validatorArgument", "1"});
        assertEquals(1, commandLine.getValues(validatorArgument).size());
        assertEquals(1, (Integer)commandLine.getValues(validatorArgument).get(0));

        TestUtils.runCommandLine(ArgumentImplTest.class, new String[]{"-o", "validatorArgument", "invalid"});
        assertFalse(commandLine.isSet("validatorArgument"));
    }

    @Test(expected = OptionException.class)
    public void testArgumentInvalidArgumentValidator() throws Exception {
        Argument validatorArgument = new ArgumentImpl("invalidValidatorArgument", "Validator argument", 1, 1, ' ', ' ', ValidatorTests.newIntValidator(), null, null, 0);

        TestUtils.runCommandLine(ArgumentImplTest.class, new String[]{"-o", "invalidValidatorArgument", "invalid"});
    }
}