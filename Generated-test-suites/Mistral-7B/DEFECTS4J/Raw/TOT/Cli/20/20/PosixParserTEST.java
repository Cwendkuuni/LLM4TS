package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test for parsing simple options.
     */
    @Test
    public void testSimpleOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] args = {"-a", "--b"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{"-a"}, parsedArgs);
            Assert.assertTrue(options.isSet("a"));
            Assert.assertFalse(options.isSet("b"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for parsing options with arguments.
     */
    @Test
    public void testOptionsWithArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B with argument");

        String[] args = {"-a", "--b=value"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{"-a", "--b=value"}, parsedArgs);
            Assert.assertTrue(options.isSet("a"));
            Assert.assertEquals("value", options.getOptionValue("b"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for parsing long options.
     */
    @Test
    public void testLongOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");

        String[] args = {"--longOptionA"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{"--longOptionA"}, parsedArgs);
            Assert.assertTrue(options.isSet("longOptionA"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for parsing options with arg lists.
     */
    @Test
    public void testOptionsWithArgLists() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("a", true, "Option A with arg list");

        String[] args = {"-a", "--", "arg1", "arg2", "arg3"};
        try {
            String[] parsedArgs = parser.parse(options, args, true);
            Assert.assertArrayEquals(new String[]{"-a", "--", "arg1", "arg2", "arg3"}, parsedArgs);
            Assert.assertFalse(options.isSet("a"));
            Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, options.getValues("a"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for handling stopAtNonOption.
     */
    @Test
    public void testStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-a", "nonOption"};
        try {
            String[] parsedArgs = parser.parse(options, args, true);
            Assert.assertArrayEquals(new String[]{"-a"}, parsedArgs);
            Assert.assertTrue(options.isSet("a"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for parsing invalid options.
     */
    @Test
    public void testInvalidOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--invalidOption", "-a"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assert.fail("Unexpected success");
        } catch (ParseException e) {
            // Expected
        }
    }

    /**
     * Test for parsing empty argument list.
     */
    @Test
    public void testEmptyArgumentList() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}