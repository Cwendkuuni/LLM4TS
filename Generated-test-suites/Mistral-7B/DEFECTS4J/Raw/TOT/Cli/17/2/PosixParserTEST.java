package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link PosixParser} class.
 */
public class PosixParserTest {

    @Test
    public void testSimpleOption() {
        String[] args = {"-a", "arg1", "--optionB=valueB", "--optionC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", null, "Option C");

        DefaultParser parser = new PosixParser();
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{"arg1", "valueB", "--optionC"}, parsedArgs);
            Assert.assertEquals(true, options.isSetOption("a"));
            Assert.assertEquals(true, options.isSetOption("b"));
            Assert.assertNull(options.getOptionValue("c"));
            Assert.assertEquals(0, options.getOptionIndex("c", args));
        } catch (ParseException e) {
            new HelpFormatter().printHelp("PosixParserTest", "java -jar PosixParserTest.jar", "", options, true);
            Assert.fail("ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testNoOption() {
        String[] args = {"--noOption", "arg1", "--optionB=valueB", "--optionC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", null, "Option C");

        DefaultParser parser = new PosixParser();
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{"arg1", "--noOption", "--optionB=valueB", "--optionC"}, parsedArgs);
            Assert.assertFalse(options.isSetOption("a"));
            Assert.assertEquals(true, options.isSetOption("b"));
            Assert.assertEquals("valueB", options.getOptionValue("b"));
            Assert.assertEquals(1, options.getOptionIndex("b", args));
        } catch (ParseException e) {
            new HelpFormatter().printHelp("PosixParserTest", "java -jar PosixParserTest.jar", "", options, true);
            Assert.fail("ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testLongOption() {
        String[] args = {"--longOptionA=valueA", "--longOptionB=valueB", "--optionC"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");
        options.addOption("c", null, "Option C");

        DefaultParser parser = new PosixParser();
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{"--longOptionA=valueA", "--longOptionB=valueB", "--optionC"}, parsedArgs);
            Assert.assertEquals(true, options.isSetOption("longOptionA"));
            Assert.assertEquals(true, options.isSetOption("longOptionB"));
            Assert.assertEquals("valueA", options.getOptionValue("longOptionA"));
            Assert.assertEquals("valueB", options.getOptionValue("longOptionB"));
            Assert.assertEquals(0, options.getOptionIndex("c", args));
        } catch (ParseException e) {
            new HelpFormatter().printHelp("PosixParserTest", "java -jar PosixParserTest.jar", "", options, true);
            Assert.fail("ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testSingleDash() {
        String[] args = {"-", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        DefaultParser parser = new PosixParser();
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{null, "--optionB=valueB"}, parsedArgs);
            Assert.assertFalse(options.isSetOption("a"));
            Assert.assertEquals(true, options.isSetOption("b"));
            Assert.assertEquals("valueB", options.getOptionValue("b"));
            Assert.assertEquals(1, options.getOptionIndex("b", args));
        } catch (ParseException e) {
            new HelpFormatter().printHelp("PosixParserTest", "java -jar PosixParserTest.jar", "", options, true);
            Assert.fail("ParseException: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() {
        String[] args = {"--unknownOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        String[] args = {"--invalidOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testDoubleDash() {
        String[] args = {"--", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);
    }
}