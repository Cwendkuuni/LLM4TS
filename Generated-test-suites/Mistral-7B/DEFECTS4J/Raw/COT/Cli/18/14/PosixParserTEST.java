package org.apache.commons.cli;

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
        String[] args = new String[0];
        try {
            String[] result = parser.flatten(new Options(), args, false);
            Assert.assertArrayEquals(new String[0], result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_SingleArg() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"arg"};
        try {
            String[] result = parser.flatten(new Options(), args, false);
            Assert.assertArrayEquals(new String[]{"arg"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_SingleOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");

        String[] args = new String[]{"o"};
        try {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"o"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_SingleOptionWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");

        String[] args = new String[]{"o", "arg"};
        try {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"o", "arg"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_MultipleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o1", "option1", false, "Option1 description");
        options.addOption("o2", "option2", true, "Option2 description");

        String[] args = new String[]{"o1", "o2", "arg"};
        try {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"o1", "o2", "arg"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_MultipleOptionsWithArgs() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o1", "option1", true, "Option1 description");
        options.addOption("o2", "option2", true, "Option2 description");

        String[] args = new String[]{"o1", "arg1", "o2", "arg2"};
        try {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"o1", "arg1", "o2", "arg2"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");

        String[] args = new String[]{"arg1", "o", "arg2"};
        try {
            String[] result = parser.flatten(options, args, true);
            Assert.assertArrayEquals(new String[]{"arg1", "o"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_DoubleDash() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");

        String[] args = new String[]{"--"};
        try {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"--"}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] args = new String[]{"x"};
        try {
            parser.flatten(options, args, false);
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_OptionWithInvalidArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");

        String[] args = new String[]{"o", "invalidArg"};
        try {
            parser.flatten(options, args, false);
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testBurstToken_ValidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");

        String token = "-o";
        try {
            parser.burstToken(token, false);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        List expectedTokens = Arrays.asList("-o");
        List actualTokens = parser.getTokens();
        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testBurstToken_ValidOptionWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");

        String token = "-oarg";
        try {
            parser.burstToken(token, false);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        List expectedTokens = Arrays.asList("-o", "arg");
        List actualTokens = parser.getTokens();
        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test(expected = ParseException.class)
    public void testBurstToken_InvalidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String token = "-x";
        try {
            parser.burstToken(token, false);
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test(expected = ParseException.class)
    public void testBurstToken_LongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", "longOptionDescription", false, "Long option description");

        String token = "--longOption";
        try {
            parser.burstToken(token, false);
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test(expected = ParseException.class)
    public void testBurstToken_OptionWithInvalidArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o", "option", true, "Option description");

        String token = "-oinvalidArg";
        try {
            parser.burstToken(token, false);
        } catch (ParseException e) {
            // Expected exception
        }
    }

    
}