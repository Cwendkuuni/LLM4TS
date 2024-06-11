package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test(expected = ParseException.class)
    public void testFlattenEmpty() throws ParseException {
        String[] arguments = {};
        new GnuParser().flatten(new Options(), arguments, false);
    }

    @Test
    public void testFlattenSingleArgument() {
        String[] arguments = {"argument"};
        Options options = new Options();
        String[] expected = {"argument"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleArguments() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        Options options = new Options();
        String[] expected = {"arg1", "arg2", "arg3"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenArgumentWithDoubleDash() {
        String[] arguments = {"--arg"};
        Options options = new Options();
        String[] expected = {"--arg"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenArgumentWithSingleDash() {
        String[] arguments = {"-arg"};
        Options options = new Options();
        String[] expected = {"-arg"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenArgumentWithValue() {
        String[] arguments = {"-Dproperty=value"};
        Options options = new Options();
        String[] expected = {"-D", "property", "value"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleArgumentsWithOptionsAndValues() {
        String[] arguments = {"-Dproperty1=value1", "-Dproperty2=value2"};
        Options options = new Options();
        String[] expected = {"-D", "property1", "value1", "-D", "property2", "value2"};
        String[] result = new GnuParser().flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenArgumentsWithStopAtNonOption() {
        String[] arguments = {"-a", "--option", "value", "-Dproperty=value", "arg", "-nonOption"};
        Options options = new Options();
        String[] expected = {"-a", "--option", "value", "-D", "property", "value", "arg"};
        String[] result = new GnuParser().flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
}