package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[0], result);
    }

    @Test
    public void testFlatten_SingleArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"arg1"}, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"opt1"};
        Options options = new Options();
        options.addOption("opt1", false, "o", "opt");
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"opt", "opt1"}, result);
    }

    @Test
    public void testFlatten_SingleOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"opt1", "arg1"};
        Options options = new Options();
        options.addOption("opt1", true, "o", "opt");
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"opt", "opt1", "arg1"}, result);
    }

    @Test
    public void testFlatten_DoubleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"opt1", "opt2"};
        Options options = new Options();
        options.addOption("opt1", false, "o", "opt1");
        options.addOption("opt2", false, "o", "opt2");
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"opt1", "opt2"}, result);
    }

    @Test
    public void testFlatten_DoubleOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"opt1", "arg1", "opt2", "arg2"};
        Options options = new Options();
        options.addOption("opt1", true, "o", "opt1");
        options.addOption("opt2", true, "o", "opt2");
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"opt1", "opt1", "opt2", "opt2"}, result);
    }

    @Test
    public void testFlatten_LongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"long-option", "arg1"};
        Options options = new Options();
        options.addOption("longOption", true, null, "long");
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"long", "long-option", "arg1"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"long-option", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("longOption", true, null, "long");
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"long", "long-option", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "opt1", "arg2"};
        Options options = new Options();
        options.addOption("opt1", false, "o", "opt");
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"arg1", "opt1", "arg2"}, result);
    }

    @Test
    public void testFlatten_Complex() {
        PosixParser parser = new PosixParser();
        String[] args = {"--long-option", "--long-option=value", "opt1", "-o", "opt2=value2", "arg2"};
        Options options = new Options();
        options.addOption("longOption", true, null, "long");
        options.addOption("opt1", true, "o", "opt");
        options.addOption("opt2", true, null, "opt2");
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("long", "longOption", "opt", "opt1", "opt2", "value2", "arg2");
        List actual = Arrays.asList(result);
        assertEquals(expected, actual);
    }
}