package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testEmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertNull(result);
    }

    @Test
    public void testSingleOption() {
        Options options = new Options();
        options.addOption("o", false, "Option o");
        String[] args = {"-o"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-o"}, result);
    }

    @Test
    public void testSingleLongOption() {
        Options options = new Options();
        options.addOption("longOption", false, "Long option");
        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testOptionWithArg() {
        Options options = new Options();
        options.addOption("o", true, "Option o with arg");
        String[] args = {"-o", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-o", "arg"}, result);
    }

    @Test
    public void testOptionWithMultipleArgs() {
        Options options = new Options();
        options.addOption("o", true, "Option o with multiple args");
        options.addOption("m", true, "Option m with multiple args");
        String[] args = {"-o", "arg1", "-o", "arg2", "--m", "arg3", "arg4"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-o", "arg1", "-o", "arg2", "--m", "arg3", "arg4"}, result);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", false, "Option o");
        String[] args = {"-o", "arg", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(new String[]{"-o", "arg"}, result);
    }

    @Test
    public void testBurstOption() {
        Options options = new Options();
        options.addOption("ab", false, "Option ab");
        options.addOption("ac", false, "Option ac");
        options.addOption("ad", false, "Option ad");
        String[] args = {"-abcd"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-ab", "-c", "-d"}, result);
    }

    @Test
    public void testBurstOptionWithArg() {
        Options options = new Options();
        options.addOption("ab", true, "Option ab with arg");
        options.addOption("ac", true, "Option ac with arg");
        String[] args = {"-abcd", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-ab", "arg1", "-c", "arg2"}, result);
    }

    @Test
    public void testBurstOptionWithLongArg() {
        Options options = new Options();
        options.addOption("ab", true, "Option ab with long arg");
        options.addOption("ac", true, "Option ac with long arg");
        String[] args = {"--abcd", "arg1", "--ac", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"--ab", "arg1", "--c", "arg2"}, result);
    }

    @Test
    public void testBurstOptionWithArgAndNonOption() {
        Options options = new Options();
        options.addOption("ab", true, "Option ab with arg");
        options.addOption("nonOption", true, "Non option with arg");
        String[] args = {"-abcd", "arg1", "nonOption", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(new String[]{"-ab", "arg1"}, result);
    }
}