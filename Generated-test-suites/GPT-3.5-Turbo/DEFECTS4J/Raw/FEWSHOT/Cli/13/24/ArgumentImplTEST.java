package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.cli2.Value;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ArgumentImplTest {

    private ArgumentImpl argumentImpl;
    private WriteableCommandLine commandLine;
    private ListIterator arguments;
    private Option option;

    @Test
    public void testProcessValues_withEmptyArguments() throws Exception {
        while (arguments.hasNext()) {
            arguments.remove();
        }
        argumentImpl.processValues(commandLine, arguments, option);
        Mockito.verifyZeroInteractions(commandLine);
    }

    @Test
    public void testProcessValues_withMixedValues() throws Exception {
        arguments.add("-f");
        arguments.add("value1,value2");
        argumentImpl.processValues(commandLine, arguments, option);
        Mockito.verify(commandLine).addValue(option, "value1");
        Mockito.verify(commandLine).addValue(option, "value2");
    }

    @Test
    public void testProcessValues_withTooManyValues() throws Exception {
        arguments.add("-f");
        arguments.add("value1,value2,value3");
        argumentImpl.processValues(commandLine, arguments, option);
        Mockito.verify(commandLine).addValue(option, "value1");
        Mockito.verify(commandLine).addValue(option, "value2");
        Mockito.verify(commandLine).addValue(option, "value3");
    }

    @Test
    public void testProcessValues_withConsumeRemaining() throws Exception {
        arguments.add("-f");
        arguments.add("--");
        arguments.add("value1");
        arguments.add("value2");
        argumentImpl.processValues(commandLine, arguments, option);
        Mockito.verify(commandLine).addValue(option, "value1");
        Mockito.verify(commandLine).addValue(option, "value2");
    }

    @Test
    public void testCanProcess() throws Exception {
        String arg = "-f";
        assertTrue(argumentImpl.canProcess(commandLine, arg));
    }

    @Test
    public void testProcess() throws Exception {
        Mockito.spy(argumentImpl).process(commandLine, arguments);
        Mockito.verify(argumentImpl).processValues(commandLine, arguments, argumentImpl);
    }

    @Test
    public void testGetInitialSeparator() throws Exception {
        assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argumentImpl.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() throws Exception {
        assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argumentImpl.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() throws Exception {
        assertTrue(argumentImpl.getTriggers().isEmpty());
    }

    @Test
    public void testGetConsumeRemaining() throws Exception {
        assertEquals(ArgumentImpl.DEFAULT_CONSUME_REMAINING, argumentImpl.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() throws Exception {
        assertNull(argumentImpl.getDefaultValues());
    }

    @Test
    public void testGetValidator() throws Exception {
        assertNull(argumentImpl.getValidator());
    }

    @Test
    public void testValidate() throws Exception {
        Mockito.spy(argumentImpl).validate(commandLine);
        Mockito.verify(argumentImpl).validate(commandLine, argumentImpl);
    }

    @Test(expected = OptionException.class)
    public void testValidate_withInvalidValidator() throws Exception {
        List<Value> values = new ArrayList<>();
        values.add(new Value("value"));
        Mockito.when(commandLine.getValues(option)).thenReturn(values);
        argumentImpl.validate(commandLine);
    }

    @Test
    public void testAppendUsage() throws Exception {
        StringBuffer buffer = new StringBuffer();
        argumentImpl.appendUsage(buffer, null, null);
        assertEquals("arg", buffer.toString());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals("A description of the argument", argumentImpl.getDescription());
    }

    @Test
    public void testHelpLines() throws Exception {
        List<String> helpLineList = argumentImpl.helpLines(0, null, null);
        assertEquals(1, helpLineList.size());
        assertEquals("arg: A description of the argument", helpLineList.get(0));
    }

    @Test
    public void testGetMaximum() throws Exception {
        assertEquals(2, argumentImpl.getMaximum());
    }

    @Test
    public void testGetMinimum() throws Exception {
        assertEquals(1, argumentImpl.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() throws Exception {
        String token = "\"value\"";
        assertEquals("value", argumentImpl.stripBoundaryQuotes(token));
    }

    @Test
    public void testIsRequired() throws Exception {
        assertTrue(argumentImpl.isRequired());
    }

    @Test
    public void testDefaults() throws Exception {
        Mockito.spy(argumentImpl).defaults(commandLine);
        Mockito.verify(argumentImpl).defaultValues(commandLine, argumentImpl);
    }

    @Test
    public void testDefaultValues() throws Exception {
        Value value = new Value("default");
        List<Value> values = new ArrayList<>();
        values.add(value);
        Mockito.when(option.getDefaultValues()).thenReturn(values);
        argumentImpl.defaultValues(commandLine, option);
        Mockito.verify(commandLine).setDefaultValues(option, values);
    }

}