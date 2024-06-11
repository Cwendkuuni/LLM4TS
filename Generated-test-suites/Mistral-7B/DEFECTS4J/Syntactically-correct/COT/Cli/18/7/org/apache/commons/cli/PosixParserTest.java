package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    

    @Test
    public void testFlatten_EmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};

        try {
            String[] result = parser.flatten(new Options(), args, false);
            Assert.assertArrayEquals(new String[0], result);
        } catch (ParseException e) {
            // empty arguments should not throw an exception
            Assert.fail();
        }
    }

    @Test
    public void testFlatten_SingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("A", true, "Option A");
        String[] args = {"-A"};

        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--", "-A");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testFlatten_SingleArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("A", true, "Option A");
        String[] args = {"-A", "arg"};

        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--", "-A", "arg");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testFlatten_MultipleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("A", true, "Option A");
        options.addOption("B", false, "Option B");
        String[] args = {"-A", "--", "-B"};

        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--", "-A", "--", "-B");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testFlatten_SingleDash() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"-"};

        try {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[0], result);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testFlatten_DoubleDash() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"--"};

        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testFlatten_SingleDashAfterOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("A", true, "Option A");
        String[] args = {"-A", "--"};

        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--", "-A");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] args = {"-X"};

        try {
            parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            // expected exception
        }
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("A", true, "Option A");
        String[] args = {"-A", "-B", "nonOption"};

        try {
            String[] result = parser.flatten(options, args, true);
            List<String> expected = Arrays.asList("--", "-A", "-B", "nonOption");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testFlatten_LongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("long-option", true, "Long option");
        String[] args = {"--long-option", "arg"};

        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--long-option", "arg");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("long-option", true, "Long option");
        options.addOption("long-option2", true, "Another long option");
        options.addOption("long-option3", true, "Yet another long option");
        String[] args = {"--long-option=arg1", "--long-option2=arg2", "--long-option3=arg3"};

        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--long-option=arg1", "--long-option2=arg2", "--long-option3=arg3");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testFlatten_LongOptionWithNoArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("long-option", false, "Long option without argument");
        String[] args = {"--long-option"};

        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--long-option");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("invalid-long-option", true, "Invalid long option");
        String[] args = {"--invalid-long-option"};

        try {
            parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            // expected exception
        }
    }

    @Test
    public void testFlatten_LongOptionWithArgumentAndAfterOtherOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("A", true, "Option A");
        options.addOption("long-option", true, "Long option");
        String[] args = {"-A", "--long-option=arg"};

        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--", "-A", "--long-option=arg");
            Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    
}