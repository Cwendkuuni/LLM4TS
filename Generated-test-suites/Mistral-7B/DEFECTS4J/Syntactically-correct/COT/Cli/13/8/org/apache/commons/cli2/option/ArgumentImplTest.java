package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.validator.MockValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the ArgumentImpl class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ArgumentImplTest {

    private WriteableCommandLine commandLine;
    private Argument argument;

    @Before
    public void setUp() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        argument = new ArgumentImpl("argName", "description", 1, 1, '.', ':', new MockValidator(), "--", Collections.emptyList(), 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("argName", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        List values = Arrays.asList("value1", "value2");
        Mockito.when(commandLine.getArguments()).thenReturn(values.iterator());

        argument.process(commandLine, commandLine.getArguments());

        Mockito.verify(commandLine, Mockito.times(1)).addValue(argument, "value1");
        Mockito.verify(commandLine, Mockito.times(1)).addValue(argument, "value2");
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithMoreValuesThanMaximum() {
        List values = Arrays.asList("value1", "value2", "value3");
        Mockito.when(commandLine.getArguments()).thenReturn(values.iterator());

        argument.process(commandLine, commandLine.getArguments());

        Mockito.verify(commandLine, Mockito.times(2)).addValue(argument, "value1");
        Mockito.verify(commandLine, Mockito.times(1)).addValue(argument, "value2");
        Mockito.verify(commandLine, Mockito.times(1)).addValue(argument, "value3");
        Mockito.verify(commandLine, Mockito.times(1)).addOption(Mockito.any(Option.class));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithConsumeRemaining() {
        List values = Arrays.asList("value1", "--", "value3");
        Mockito.when(commandLine.getArguments()).thenReturn(values.iterator());

        argument.process(commandLine, commandLine.getArguments());

        Mockito.verify(commandLine, Mockito.times(2)).addValue(argument, "value1");
        Mockito.verify(commandLine, Mockito.times(1)).addOption(Mockito.any(Option.class));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithInvalidValues() {
        List values = Arrays.asList("value1", "--optionName", "value3");
        Mockito.when(commandLine.getArguments()).thenReturn(values.iterator());

        argument.process(commandLine, commandLine.getArguments());

        Mockito.verify(commandLine, Mockito.times(1)).addValue(argument, "value1");
        Mockito.verify(commandLine, Mockito.times(1)).addOption(Mockito.any(Option.class));
    }

    @Test
    public void testCanProcess() {
        Mockito.when(commandLine.looksLikeOption("value1")).thenReturn(false);
        Mockito.when(commandLine.looksLikeOption("--optionName")).thenReturn(true);

        assertEquals(true, argument.canProcess(commandLine, "value1"));
        assertEquals(false, argument.canProcess(commandLine, "--optionName"));
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('.', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(':', argument.getSubsequentSeparator());
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
        assertEquals(Mockito.mock(Validator.class), argument.getValidator());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValues() {
        List values = Arrays.asList("value1", "value2", "invalidValue");
        Mockito.when(commandLine.getValues(argument)).thenReturn(values);

        argument.validate(commandLine);

        Mockito.verify(commandLine, Mockito.times(1)).addOption(Mockito.any(Option.class));
    }

    @Test
    public void testValidateWithValidValues() {
        List values = Arrays.asList("value1", "value2");
        Mockito.when(commandLine.getValues(argument)).thenReturn(values);

        argument.validate(commandLine);

        Mockito.verifyNoMoreInteractions(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuilder buffer = new StringBuilder();
        argument.appendUsage(buffer, null, null);

        assertEquals("argName [--] [argName]", buffer.toString());
    }

    @Test
    public void testDefaultValues() {
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");

        argument = new ArgumentImpl("argName", "description", 1, 1, '.', ':', new MockValidator(), "--", defaultValues, 0);

        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);

        argument.defaults(commandLine);

        Mockito.verify(commandLine, Mockito.times(1)).setDefaultValues(argument, defaultValues);
    }
}

class MockValidator implements Validator {
    @Override
    public void validate(List values) throws InvalidArgumentException {
        // noop
    }
}

class HelpLineImpl implements HelpLine {
    private Argument argument;
    private int depth;

    public HelpLineImpl(Argument argument, int depth) {
        this.argument = argument;
        this.depth = depth;
    }

    @Override
    public String getHeader() {
        return "";
    }

    @Override
    public String getShortDescription() {
        return argument.getDescription();
    }

    @Override
    public String getLongDescription() {
        return argument.getDescription();
    }

    @Override
    public String getUsage() {
        StringBuilder buffer = new StringBuilder();
        argument.appendUsage(buffer, null, null);
        return buffer.toString();
    }

    @Override
    public void print(int indentLevel, String prefix) {
        // noop
    }
}