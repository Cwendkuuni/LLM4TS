package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        options = mock(Options.class);
        parser = new PosixParser();
        parser.setOptions(options);
    }

    @Test
    public void testFlattenEmpty() {
        String[] arguments = {};
        String[] expected = {};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNoOptions() {
        String[] arguments = { "foo" };
        String[] expected = { "foo" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionShortForm() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(false);
        when(options.getOption("a")).thenReturn(option);
        String[] arguments = { "-a" };
        String[] expected = { "-a" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionLongForm() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(false);
        when(options.getOption("long-option")).thenReturn(option);
        String[] arguments = { "--long-option" };
        String[] expected = { "--long-option" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionWithValue() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(true);
        when(options.getOption("a")).thenReturn(option);
        String[] arguments = { "-a", "value" };
        String[] expected = { "-a", "value" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsShortForm() {
        Option option1 = mock(Option.class);
        when(option1.hasArg()).thenReturn(false);
        Option option2 = mock(Option.class);
        when(option2.hasArg()).thenReturn(false);
        when(options.getOption("a")).thenReturn(option1);
        when(options.getOption("b")).thenReturn(option2);
        String[] arguments = { "-a", "-b" };
        String[] expected = { "-a", "-b" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsLongForm() {
        Option option1 = mock(Option.class);
        when(option1.hasArg()).thenReturn(false);
        Option option2 = mock(Option.class);
        when(option2.hasArg()).thenReturn(false);
        when(options.getOption("long-option1")).thenReturn(option1);
        when(options.getOption("long-option2")).thenReturn(option2);
        String[] arguments = { "--long-option1", "--long-option2" };
        String[] expected = { "--long-option1", "--long-option2" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsShortFormWithValues() {
        Option option1 = mock(Option.class);
        when(option1.hasArg()).thenReturn(true);
        Option option2 = mock(Option.class);
        when(option2.hasArg()).thenReturn(true);
        when(options.getOption("a")).thenReturn(option1);
        when(options.getOption("b")).thenReturn(option2);
        String[] arguments = { "-a", "value1", "-b", "value2" };
        String[] expected = { "-a", "value1", "-b", "value2" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsLongFormWithValues() {
        Option option1 = mock(Option.class);
        when(option1.hasArg()).thenReturn(true);
        Option option2 = mock(Option.class);
        when(option2.hasArg()).thenReturn(true);
        when(options.getOption("long-option1")).thenReturn(option1);
        when(options.getOption("long-option2")).thenReturn(option2);
        String[] arguments = { "--long-option1", "value1", "--long-option2", "value2" };
        String[] expected = { "--long-option1", "value1", "--long-option2", "value2" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(false);
        when(options.getOption("a")).thenReturn(option);
        String[] arguments = { "-a", "foo", "-b" };
        String[] expected = { "-a", "foo", "-b" };
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleHyphen() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(false);
        when(options.getOption("--")).thenReturn(option);
        String[] arguments = { "--" };
        String[] expected = { "--" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleHyphenAndValue() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(true);
        when(options.getOption("--")).thenReturn(option);
        String[] arguments = { "--value" };
        String[] expected = { "--value" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithUnknownOption() {
        String[] arguments = { "unknown" };
        String[] expected = { "unknown" };
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithUnknownOptionAndStopAtNonOption() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(false);
        when(options.getOption("unknown")).thenReturn(option);
        String[] arguments = { "unknown" };
        String[] expected = { "unknown" };
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenSingleCharacterOption() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(false);
        when(options.getOption("a")).thenReturn(option);
        String[] token = { "-a" };
        String[] expected = { "-a" };
        parser.burstToken(token, false);
        assertArrayEquals(expected, tokens.toArray(new String[tokens.size()]));
    }

    @Test
    public void testBurstTokenMultiCharacterOptionShortForm() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(false);
        when(options.getOption("ab")).thenReturn(option);
        String[] token = { "-ab" };
        String[] expected = { "-a", "-b" };
        parser.burstToken(token, false);
        assertArrayEquals(expected, tokens.toArray(new String[tokens.size()]));
    }

    @Test
    public void testBurstTokenMultiCharacterOptionLongForm() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(false);
        when(options.getOption("long-option-ab")).thenReturn(option);
        String[] token = { "--long-option-ab" };
        String[] expected = { "--long-option-ab" };
        parser.burstToken(token, false);
        assertArrayEquals(expected, tokens.toArray(new String[tokens.size()]));
    }

    @Test
    public void testBurstTokenMultiCharacterOptionWithArguments() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(true);
        when(options.getOption("ab")).thenReturn(option);
        String[] token = { "-ab", "value" };
        String[] expected = { "-a", "-b", "value" };
        parser.burstToken(token, false);
        assertArrayEquals(expected, tokens.toArray(new String[tokens.size()]));
    }

    @Test
    public void testBurstTokenMultiCharacterOptionWithArgumentsAndStopAtNonOption() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(true);
        when(options.getOption("ab")).thenReturn(option);
        String[] token = { "-ab", "value" };
        String[] expected = { "-ab", "value" };
        parser.burstToken(token, true);
        assertArrayEquals(expected, tokens.toArray(new String[tokens.size()]));
    }

    @Test
    public void testBurstTokenUnknownTokenWithStopAtNonOption() {
        String[] token = { "unknown" };
        String[] expected = { "unknown" };
        parser.burstToken(token, true);
        assertArrayEquals(expected, tokens.toArray(new String[tokens.size()]));
    }

    @Test
    public void testBurstTokenUnknownTokenWithoutStopAtNonOption() {
        String[] token = { "unknown" };
        String[] expected = { "--unknown" };
        parser.burstToken(token, false);
        assertArrayEquals(expected, tokens.toArray(new String[tokens.size()]));
    }

    private List<String> tokens = new ArrayList<>();

}