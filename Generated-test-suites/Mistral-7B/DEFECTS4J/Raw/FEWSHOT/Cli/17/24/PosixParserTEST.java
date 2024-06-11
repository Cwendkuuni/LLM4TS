package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlattenEmpty() {
        Options options = new Options();
        String[] arguments = {};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option test");
        options.addOption(option);
        String[] arguments = {"-o", "arg"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"o", "arg"}, result);
        assertEquals(option, parser.getCurrentOption());
        assertNull(parser.getCurrentOption().getValue());
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", true, "Option 1 test");
        Option option2 = new Option("o2", "opt2", false, "Option 2 test");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-o1", "arg1", "--opt2", "arg2"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"o1", "arg1", "o2"}, result);
        assertSame(option1, parser.getCurrentOption());
        assertNull(parser.getCurrentOption().getValue());
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        Option longOption = new Option("long-opt", "Long option test", false, "Long option test");
        options.addOption(longOption);
        String[] arguments = {"--long-opt"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"long-opt"}, result);
        assertSame(longOption, parser.getCurrentOption());
        assertNull(parser.getCurrentOption().getValue());
    }

    @Test
    public void testFlattenArgWithOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option test");
        options.addOption(option);
        String[] arguments = {"--opt", "arg"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"o", "arg"}, result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlattenArgWithLongOption() {
        Options options = new Options();
        Option longOption = new Option("long-opt", "Long option test", false, "Long option test");
        options.addOption(longOption);
        String[] arguments = {"--long-opt=arg"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"long-opt", "arg"}, result);
        assertSame(longOption, parser.getCurrentOption());
        assertEquals("arg", parser.getCurrentOption().getValue());
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlattenWithDoubleHyphen() {
        Options options = new Options();
        String[] arguments = {"--"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option test");
        options.addOption(option);
        String[] arguments = {"-o", "arg1", "--", "arg2"};
        boolean stopAtNonOption = true;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"o", "arg1", "--", "arg2"}, result);
    }

    @Test
    public void testBurstTokenSingleOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option test");
        options.addOption(option);
        String[] arguments = {"-oo"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"o", "o"}, result);
        assertSame(option, parser.getCurrentOption());
        assertNull(parser.getCurrentOption().getValue());
    }

    @Test
    public void testBurstTokenMultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", true, "Option 1 test");
        Option option2 = new Option("o2", "opt2", true, "Option 2 test");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"-o1o2"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"o1", "o2"}, result);
        assertSame(option1, parser.getCurrentOption());
        assertNull(parser.getCurrentOption().getValue());
    }

    @Test
    public void testBurstTokenLongOption() {
        Options options = new Options();
        Option longOption = new Option("long-opt", "Long option test", false, "Long option test");
        options.addOption(longOption);
        String[] arguments = {"--long-opt-long"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"long-opt", "long"}, result);
        assertSame(longOption, parser.getCurrentOption());
        assertNull(parser.getCurrentOption().getValue());
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option test");
        options.addOption(option);
        String[] arguments = {"-oo", "--"};
        boolean stopAtNonOption = true;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(new String[]{"o", "--"}, result);
    }
}