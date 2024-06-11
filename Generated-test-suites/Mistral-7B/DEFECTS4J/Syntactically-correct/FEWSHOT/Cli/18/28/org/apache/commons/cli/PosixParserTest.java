package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmptyArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {};

        String[] result = parser.flatten(options, arguments, false);

        assertNull(result);
    }

    @Test
    public void testSingleHyphen() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[]{"-"}, result);
    }

    @Test
    public void testSingleOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[]{"-o"}, result);
        assertEquals(options.findOption("o"), parser.getCurrentOption());
    }

    @Test
    public void testSingleOptionWithValue() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[]{"-o", "value"}, result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testOptionWithValueAndArgument() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        options.addOption("a", "argument", true, "Argument description");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o", "value", "--", "-a", "arg"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[]{"-o", "value", "--", "-a", "arg"}, result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption("o1", "option1", false, "Option 1 description");
        options.addOption("o2", "option2", false, "Option 2 description");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o1", "-o2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[]{"-o1", "-o2"}, result);
        assertSame(options.findOption("o1"), parser.getCurrentOption());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testMultipleOptionsWithValues() {
        Options options = new Options();
        options.addOption("o1", "option1", true, "Option 1 description");
        options.addOption("o2", "option2", true, "Option 2 description");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o1", "value1", "-o2", "value2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[]{"-o1", "value1", "-o2", "value2"}, result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testMultipleOptionsWithArguments() {
        Options options = new Options();
        options.addOption("o1", "option1", true, "Option 1 description");
        options.addOption("o2", "option2", true, "Option 2 description");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o1", "value1", "--", "-o2", "value2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[]{"-o1", "value1", "--", "-o2", "value2"}, result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testMultiOptionWithSingleValue() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");
        options.addOption("a", "argument", true, "Argument description");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-o", "--", "-a", "arg"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[]{"-o", "--", "-a", "arg"}, result);
        assertNull(parser.getCurrentOption());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"-i"}; // invalid option

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");
        PosixParser parser = new PosixParser();
        String[] arguments = {"--", "-o", "value"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(new String[]{"--", "-o"}, result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstOption() {
        Options options = new Options();
        options.addOption("lo", "longoption", true, "Long option description");
        options.addOption("s", "shortoption", false, "Short option description");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-losomething", "shortoptionvalue"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[]{"-lo", "s", "omething", "shortoptionvalue"}, result);
        assertSame(options.findOption("lo"), parser.getCurrentOption());
    }

    @Test
    public void testBurstOptionWithValue() {
        Options options = new Options();
        options.addOption("lo", "longoption", true, "Long option description");
        options.addOption("s", "shortoption", false, "Short option description");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-losomething=value", "shortoptionvalue"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[]{"-lo", "value", "shortoptionvalue"}, result);
        assertSame(options.findOption("lo"), parser.getCurrentOption());
    }

    @Test
    public void testBurstOptionWithValueAndArguments() {
        Options options = new Options();
        options.addOption("lo", "longoption", true, "Long option description");
        options.addOption("s", "shortoption", false, "Short option description");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-losomething=value", "--", "-s", "arg"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(new String[]{"-lo", "value", "--", "-s", "arg"}, result);
        assertSame(options.findOption("lo"), parser.getCurrentOption());
    }
}