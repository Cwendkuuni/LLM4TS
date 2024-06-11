package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.validator.MockValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class ArgumentImplTest {

    private Argument argument;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        commandLine = PowerMockito.mock(WriteableCommandLine.class);
        argument = new ArgumentImpl("name", "description", 1, 1, ' ', ' ', new MockValidator(), "--", Collections.emptyList(), 0);
    }

    @Test
    public void testConstructor() {
        assertNotNull(argument);
        assertEquals("name", argument.getPreferredName());
        assertEquals("description", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertEquals(' ', argument.getInitialSeparator());
        assertEquals(' ', argument.getSubsequentSeparator());
        assertFalse(argument.isSubsequentSplit());
        assertNull(argument.getValidator());
        assertEquals("--", argument.getConsumeRemaining());
        assertNotNull(argument.getDefaultValues());
        assertEquals(0, argument.getId());
    }

    @Test
    public void testProcessValues() {
        final List values = Arrays.asList("value1", "value2");
        PowerMockito.when(commandLine.getValues(argument)).thenReturn(values);

        argument.process(commandLine, Arrays.asList("--test", "arg1", "arg2", "--name", "value1", "value2").iterator());

        assertEquals(2, argument.getMinimum());
        assertEquals(2, argument.getMaximum());
        assertEquals(2, commandLine.getValues(argument).size());
        assertEquals("value1", commandLine.getValue(argument, 0));
        assertEquals("value2", commandLine.getValue(argument, 1));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithTooFewArguments() {
        PowerMockito.when(commandLine.getValues(argument)).thenReturn(Collections.emptyList());

        argument.processValues(commandLine, Arrays.asList("--test", "arg1").iterator(), argument);
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithTooManyArguments() {
        final List values = Arrays.asList("value1", "value2", "value3");
        PowerMockito.when(commandLine.getValues(argument)).thenReturn(values);

        argument.processValues(commandLine, Arrays.asList("--test", "arg1", "arg2", "--name", "value1", "value2", "value3").iterator(), argument);
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithInvalidArgument() {
        final List values = Arrays.asList("value1", "value2");
        PowerMockito.when(commandLine.getValues(argument)).thenReturn(values);

        PowerMockito.when(commandLine.looksLikeOption("value3"))
                .thenReturn(true);

        argument.processValues(commandLine, Arrays.asList("--test", "arg1", "arg2", "--name", "value1", "value2", "value3").iterator(), argument);
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "value"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testProcess() {
        final List values = Arrays.asList("value1", "value2");
        PowerMockito.when(commandLine.getValues(argument)).thenReturn(values);

        argument.process(commandLine, Arrays.asList("--test", "arg1", "--name", "value1", "value2").iterator());

        assertEquals(2, argument.getMinimum());
        assertEquals(2, argument.getMaximum());
        assertEquals(2, commandLine.getValues(argument).size());
        assertEquals("value1", commandLine.getValue(argument, 0));
        assertEquals("value2", commandLine.getValue(argument, 1));
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(' ', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(Collections.emptyList(), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testValidate() {
        final List values = Arrays.asList("value1", "value2");
        PowerMockito.when(commandLine.getValues(argument)).thenReturn(values);

        argument.validate(commandLine);

        assertEquals(2, commandLine.getValues(argument).size());
        assertEquals("value1", commandLine.getValue(argument, 0));
        assertEquals("value2", commandLine.getValue(argument, 1));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewArguments() {
        PowerMockito.when(commandLine.getValues(argument)).thenReturn(Collections.emptyList());

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyArguments() {
        final List values = Arrays.asList("value1", "value2", "value3");
        PowerMockito.when(commandLine.getValues(argument)).thenReturn(values);

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidArgument() {
        final List values = Arrays.asList("value1", "value2");
        PowerMockito.when(commandLine.getValues(argument)).thenReturn(values);

        PowerMockito.when(commandLine.looksLikeOption("value3"))
                .thenReturn(true);

        argument.validate(commandLine);
    }
}