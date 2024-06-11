package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value"};
        String[] expected = {"-o", "option", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option1 value");
        Option option2 = new Option("o2", "option2", false, "Option2");
        options.addOption(option1);
        options.addOption(option2);
        PosixParser parser = new PosixParser();
        String[] args = {"-o1", "value1", "--option2"};
        String[] expected = {"-o1", "option1", "value1", "--option2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = new Options();
        Option option = new Option("long-option", "long option", false, "Long option");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option"};
        String[] expected = {"--long-option"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        Option option = new Option("long-option", "long option", true, "Long option value");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option", "value"};
        String[] expected = {"--long-option", "long option", "value"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithArgStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "value1", "-a", "arg1", "--", "value2"};
        String[] expected = {"-o", "option", "value1", "--", "--", "value2"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"-i", "invalidOption"};
        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_EmptyOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--"};
        parser.flatten(options, args, false);
    }

}