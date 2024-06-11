package org.apache.commons.cli.test;

import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testSimpleOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a flag.");
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expectedArgs = {"-a"};
        String[] actualArgs = parser.parse(options, args, false);
        assertArrayEquals(expectedArgs, actualArgs);
        assertTrue(options.isSet("a"));
        assertNull(options.getOptionValue("b"));
    }

    @Test
    public void testOptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a flag.");
        options.addOption("b", true, "An option with an argument.");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--b", "value"};
        String[] expectedArgs = {"-a", "--b", "value"};
        String[] actualArgs = parser.parse(options, args, false);
        assertArrayEquals(expectedArgs, actualArgs);
        assertFalse(options.isSet("a"));
        assertEquals("value", options.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--invalid"};
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testMultipleInvalidOptions() throws ParseException {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--invalid1", "--invalid2"};
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testMissingOptionArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a flag.");
        options.addOption("b", true, "An option with an argument.");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--b"};
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testExtraArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a flag.");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "extra"};
        parser.parse(options, args, false);
    }

    @Test
    public void testLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "A long option.");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption"};
        String[] expectedArgs = {"--longOption"};
        String[] actualArgs = parser.parse(options, args, false);
        assertArrayEquals(expectedArgs, actualArgs);
        assertTrue(options.isSet("longOption"));
        assertNull(options.getOptionValue("shortOption"));
    }

    @Test
    public void testLongOptionWithArgument() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "A long option.");
        options.addOption("shortOption", true, "A short option.");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption=value", "--shortOption=value2"};
        String[] expectedArgs = {"--longOption=value", "--shortOption=value2"};
        String[] actualArgs = parser.parse(options, args, false);
        assertArrayEquals(expectedArgs, actualArgs);
        assertFalse(options.isSet("longOption"));
        assertEquals("value", options.getOptionValue("longOption"));
        assertEquals("value2", options.getOptionValue("shortOption"));
    }

    @Test
    public void testLongOptionWithShortOption() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "A long option.");
        options.addOption("o", true, "A short option.");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "--o"};
        String[] expectedArgs = {"--longOption", "--o"};
        String[] actualArgs = parser.parse(options, args, false);
        assertArrayEquals(expectedArgs, actualArgs);
        assertTrue(options.isSet("longOption"));
        assertTrue(options.isSet("o"));
    }

    @Test
    public void testLongOptionWithLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("longOption1", true, "First long option.");
        options.addOption("longOption2", true, "Second long option.");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption1", "--longOption2"};
        String[] expectedArgs = {"--longOption1", "--longOption2"};
        String[] actualArgs = parser.parse(options, args, false);
        assertArrayEquals(expectedArgs, actualArgs);
        assertTrue(options.isSet("longOption1"));
        assertTrue(options.isSet("longOption2"));
    }

    @Test
    public void testLongOptionWithShortOptionAndArgument() throws ParseException {
        Options options = new Options();
        options.addOption("l", true, "An option with a flag.");
        options.addOption("s", true, "An option with an argument.");
        PosixParser parser = new PosixParser();
        String[] args = {"--l", "--s=value"};
        String[] expectedArgs = {"--l", "--s=value"};
        String[] actualArgs = parser.parse(options, args, false);
        assertArrayEquals(expectedArgs, actualArgs);
        assertTrue(options.isSet("l"));
        assertEquals("value", options.getOptionValue("s"));
    }

    @Test
    public void testLongOptionWithLongOptionAndArgument() throws ParseException {
        Options options = new Options();
        options.addOption("longOption1", true, "First long option.");
        options.addOption("longOption2", true, "Second long option.");
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption1=value1", "--longOption2=value2"};
        String[] expectedArgs = {"--longOption1=value1", "--longOption2=value2"};
        String[] actualArgs = parser.parse(options, args, false);
        assertArrayEquals(expectedArgs, actualArgs);
        assertFalse(options.isSet("longOption1"));
        assertEquals("value1", options.getOptionValue("longOption1"));
        assertFalse(options.isSet("longOption2"));
        assertEquals("value2", options.getOptionValue("longOption2"));
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a flag.");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "nonOption"};
        String[] expectedArgs = {"-a", "nonOption"};
        String[] actualArgs = parser.parse(options, args, true);
        assertArrayEquals(expectedArgs, actualArgs);
    }

    @Test
    public void testStopAtNonOptionWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a flag.");
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "nonOption", "arg1", "arg2"};
        String[] expectedArgs = {"-a", "nonOption", "arg1", "arg2"};
        String[] actualArgs = parser.parse(options, args, true);
        assertArrayEquals(expectedArgs, actualArgs);
    }
}