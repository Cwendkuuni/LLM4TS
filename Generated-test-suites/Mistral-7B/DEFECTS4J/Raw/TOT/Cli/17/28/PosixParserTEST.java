package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testSimpleOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-a"};
        DefaultParser parser = new DefaultParser();
        try {
            parser.parse(options, args, false);
            Assert.assertTrue(options.isSet("a"));
        } catch (ParseException e) {
            Assert.fail("Should not reach here");
        }
    }

    @Test
    public void testOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-a", "value"};
        DefaultParser parser = new DefaultParser();
        try {
            parser.parse(options, args, false);
            Assert.assertEquals("value", options.getOptionValue("a"));
        } catch (ParseException e) {
            Assert.fail("Should not reach here");
        }
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-a", "-b"};
        DefaultParser parser = new DefaultParser();
        try {
            parser.parse(options, args, false);
            Assert.assertTrue(options.isSet("a"));
            Assert.assertTrue(options.isSet("b"));
        } catch (ParseException e) {
            Assert.fail("Should not reach here");
        }
    }

    @Test
    public void testInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-z"}; // Invalid option
        DefaultParser parser = new DefaultParser();
        try {
            parser.parse(options, args, false);
            Assert.fail("Should not reach here");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");

        String[] args = {"--longOption"};
        DefaultParser parser = new DefaultParser();
        try {
            parser.parse(options, args, false);
            Assert.assertTrue(options.isSet("longOption"));
        } catch (ParseException e) {
            Assert.fail("Should not reach here");
        }
    }

    @Test
    public void testLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");

        String[] args = {"--longOption", "value"};
        DefaultParser parser = new DefaultParser();
        try {
            parser.parse(options, args, false);
            Assert.assertEquals("value", options.getOptionValue("longOption"));
        } catch (ParseException e) {
            Assert.fail("Should not reach here");
        }
    }

    @Test
    public void testBurstedOption() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");

        String[] args = {"-ab", "value"};
        DefaultParser parser = new DefaultParser();
        try {
            parser.parse(options, args, false);
            Assert.assertEquals("value", options.getOptionValue("ab"));
        } catch (ParseException e) {
            Assert.fail("Should not reach here");
        }
    }

    @Test
    public void testBurstedOptionWithValue() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");

        String[] args = {"-a", "-b", "value"};
        DefaultParser parser = new DefaultParser();
        try {
            parser.parse(options, args, false);
            Assert.assertEquals("value", options.getOptionValue("ab"));
        } catch (ParseException e) {
            Assert.fail("Should not reach here");
        }
    }

    @Test
    public void testMixedOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("ab", true, "Option AB");

        String[] args = {"-a", "-ab", "value"};
        DefaultParser parser = new DefaultParser();
        try {
            parser.parse(options, args, false);
            Assert.assertTrue(options.isSet("a"));
            Assert.assertEquals("value", options.getOptionValue("ab"));
        } catch (ParseException e) {
            Assert.fail("Should not reach here");
        }
    }

    @Test
    public void testHelp() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("ab", true, "Option AB");

        String[] args = {"--help"};
        DefaultParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            parser.parse(options, args, false);
            Assert.fail("Should not reach here");
        } catch (ParseException e) {
            // Expected
        }

        String help = formatter.printHelp("test", Arrays.asList(options.getOptions()), true);
        System.out.println(help);
    }

    @Test
    public void testInvalidLongOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--invalidLongOption"};
        DefaultParser parser = new DefaultParser();
        try {
            parser.parse(options, args, false);
            Assert.fail("Should not reach here");
        } catch (ParseException e) {
            // Expected
        }
    }
}