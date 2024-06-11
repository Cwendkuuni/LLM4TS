package org.apache.commons.cli.option;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;


public class ArgumentImplTest {
    
    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;
    private Option testOption;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "Argument Description", 1, 2, '-', ',', null, "--", new ArrayList<>(), 0);
        commandLine = mock(WriteableCommandLine.class);
        testOption = new OptionImpl(0, false);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('-', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        argument = new ArgumentImpl("arg", "Argument Description", 1, 2, '-', ',', null, "--", defaultValues, 0);

        assertEquals(defaultValues, argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "test"));
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
    public void testProcess() {
        ListIterator<String> args = Arrays.asList("test1").listIterator();
        when(commandLine.looksLikeOption("test1")).thenReturn(false);

        try {
            argument.process(commandLine, args);
            verify(commandLine, times(1)).addValue(testOption, "test1");
        } catch (OptionException e) {
            fail("Unexpected OptionException: " + e.getMessage());
        }
    }

    @Test
    public void testProcessValues() {
        ListIterator<String> args = Arrays.asList("test1", "test2").listIterator();
        when(commandLine.looksLikeOption("test1")).thenReturn(false);
        when(commandLine.looksLikeOption("test2")).thenReturn(false);

        try {
            argument.processValues(commandLine, args, testOption);
            verify(commandLine, times(1)).addValue(testOption, "test1");
            verify(commandLine, times(1)).addValue(testOption, "test2");
        } catch (OptionException e) {
            fail("Unexpected OptionException: " + e.getMessage());
        }
    }

    @Test
    public void testProcessValuesWithConsumeRemaining() {
        ListIterator<String> args = Arrays.asList("value1", "value2", "--", "value3").listIterator();
        when(commandLine.looksLikeOption("value1")).thenReturn(false);
        when(commandLine.looksLikeOption("value2")).thenReturn(false);
        when(commandLine.looksLikeOption("--")).thenReturn(false);
        when(commandLine.looksLikeOption("value3")).thenReturn(false);

        try {
            argument.processValues(commandLine, args, testOption);
            verify(commandLine, times(1)).addValue(testOption, "value1");
            verify(commandLine, times(1)).addValue(testOption, "value2");
            verify(commandLine, times(1)).addValue(testOption, "value3");
        } catch (OptionException e) {
            fail("Unexpected OptionException: " + e.getMessage());
        }
    }

    @Test
    public void testProcessValuesWithSubsequentSplit() {
        ListIterator<String> args = Arrays.asList("value1,value2", "value3").listIterator();
        when(commandLine.looksLikeOption("value1,value2")).thenReturn(false);
        when(commandLine.looksLikeOption("value3")).thenReturn(false);

        try {
            argument.processValues(commandLine, args, testOption);
            verify(commandLine, times(1)).addValue(testOption, "value1");
            verify(commandLine, times(1)).addValue(testOption, "value2");
            verify(commandLine, times(1)).addValue(testOption, "value3");
        } catch (OptionException e) {
            fail("Unexpected OptionException: " + e.getMessage());
        }
    }

    @Test
    public void testProcessValuesWithSubsequentSplitAndUnexpectedValue() {
        ListIterator<String> args = Arrays.asList("value1,value2,value3").listIterator();
        when(commandLine.looksLikeOption("value1,value2,value3")).thenReturn(false);

        try {
            argument.processValues(commandLine, args, testOption);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals(ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE), e.getMessage());
        }
    }

    @Test
    public void testIsRequired() {
        argument = new ArgumentImpl("arg", "Argument Description", 1, 2, '-', ',', null, "--", new ArrayList<>(), 0);
        assertTrue(argument.isRequired());

        argument = new ArgumentImpl("arg", "Argument Description", 0, 2, '-', ',', null, "--", new ArrayList<>(), 0);
        assertFalse(argument.isRequired());
    }

    @Test
    public void testValidate() {
        List<String> values = new ArrayList<>();
        values.add("test1");
        values.add("test2");
        when(commandLine.getValues(testOption)).thenReturn(values);

        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            fail("Unexpected OptionException: " + e.getMessage());
        }
    }
}