package org.apache.commons.cli;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] arguments = {"-D", "foo", "-Dbar", "--", "-f", "arg1", "arg2"};
        String[] expected = { "-D", "foo", "-D", "bar", "--", "-f", "arg1", "arg2" };
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOption() {
        Options options = new Options();
        String[] arguments = {"-D", "foo", "--myoption", "bar", "--", "-f", "arg1", "arg2"};
        String[] expected = { "-D", "foo", "--myoption", "bar", "--", "-f", "arg1", "arg2" };
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithTwoOptions() {
        Options options = new Options();
        options.addOption("a", true, "Prints A.");
        options.addOption("b", true, "Prints B.");
        String[] arguments = {"-D", "--a", "--b", "--", "-f", "arg1", "arg2"};
        String[] expected = { "-D", "--a", "--b", "--", "-f", "arg1", "arg2" };
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionAndArg() {
        Options options = new Options();
        options.addOption("a", true, "Prints A.");
        String[] arguments = {"-D", "--a=value", "--", "-f", "arg1", "arg2"};
        String[] expected = { "-D", "--a=value", "--", "-f", "arg1", "arg2" };
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Prints Long Option.");
        String[] arguments = {"-D", "--longOption", "--", "-f", "arg1", "arg2"};
        String[] expected = { "-D", "--longOption", "--", "-f", "arg1", "arg2" };
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithTwoLongOptions() {
        Options options = new Options();
        options.addOption("longOption1", true, "Prints Long Option 1.");
        options.addOption("longOption2", true, "Prints Long Option 2.");
        String[] arguments = {"-D", "--longOption1=value1", "--longOption2=value2", "--", "-f", "arg1", "arg2"};
        String[] expected = { "-D", "--longOption1=value1", "--longOption2=value2", "--", "-f", "arg1", "arg2" };
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Prints A.");
        String[] arguments = {"-D", "--a", "--", "-f", "arg1", "arg2", "nonOption"};
        String[] expected = { "-D", "--a", "--", "-f", "arg1", "arg2" };
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithNullArguments() {
        Options options = new Options();
        String[] arguments = null;
        String[] expected = {};
        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithEmptyList() {
        Options options = new Options();
        List tokens = new ArrayList();
        GnuParser parser = new GnuParser();
        String[] result = (String[]) parser.flatten(options, (String[]) tokens.toArray(new String[0]), false).toArray(new String[0]);
        assertArrayEquals(new String[0], result);
    }
}