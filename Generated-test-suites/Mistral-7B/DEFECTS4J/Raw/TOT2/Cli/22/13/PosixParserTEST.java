package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test basic functionality of flatten method with no options and no arguments.
     */
    @Test
    public void testEmpty() {
        PosixParser parser = new PosixParser();
        String[] args = new String[0];
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    /**
     * Test basic functionality of flatten method with single option.
     */
    @Test
    public void testSingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    /**
     * Test basic functionality of flatten method with single value option.
     */
    @Test
    public void testSingleValueOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "value"}, result);
    }

    /**
     * Test basic functionality of flatten method with multiple options.
     */
    @Test
    public void testMultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "-c"}, result);
    }

    /**
     * Test functionality of flatten method with long option.
     */
    @Test
    public void testLongOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
    }

    /**
     * Test functionality of flatten method with long option value.
     */
    @Test
    public void testLongOptionValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    /**
     * Test functionality of flatten method with multiple long options.
     */
    @Test
    public void testMultipleLongOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption1", "--longOption2"};
        Options options = new Options();
        options.addOption("longOption1", true, "Long Option 1");
        options.addOption("longOption2", true, "Long Option 2");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption1", "--longOption2"}, result);
    }

    /**
     * Test functionality of flatten method with option requiring no value.
     */
    @Test
    public void testNoValueOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    /**
     * Test functionality of flatten method with stopAtNonOption.
     */
    @Test
    public void testStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--nonOption", "value", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("nonOption", false, "Non Option");
        options.addOption("c", true, "Option C");
        try {
            String[] result = parser.flatten(options, args, true);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
    }

    /**
     * Test functionality of flatten method with stopAtNonOption and multiple non-options.
     */
    @Test
    public void testStopAtNonOptionMultiple() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--nonOption1", "--nonOption2", "value", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("nonOption1", false, "Non Option 1");
        options.addOption("nonOption2", false, "Non Option 2");
        options.addOption("c", true, "Option C");
        try {
            String[] result = parser.flatten(options, args, true);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
    }

    /**
     * Test functionality of flatten method with bursting.
     */
    @Test
    public void testBursting() {
        PosixParser parser = new PosixParser();
        String[] args = {"-foo", "bar", "-baZ"};
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("baZ", true, "Option baZ");
        options.addOption("b", true, "Option B");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-foo", "bar", "-b", "a", "z"}, result);
    }

    /**
     * Test functionality of flatten method with stopAtNonOption and bursting.
     */
    @Test
    public void testStopAtNonOptionBursting() {
        PosixParser parser = new PosixParser();
        String[] args = {"-foo", "bar", "--nonOption", "baz", "-baZ"};
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("baZ", true, "Option baZ");
        options.addOption("b", true, "Option B");
        try {
            String[] result = parser.flatten(options, args, true);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
    }

    /**
     * Test functionality of flatten method with multiple options and bursting.
     */
    @Test
    public void testMultipleOptionsBursting() {
        PosixParser parser = new PosixParser();
        String[] args = {"-foo", "--option1", "value1", "--option2", "value2"};
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("option1", true, "Option option1");
        options.addOption("option2", true, "Option option2");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-foo", "--option1", "value1", "--option2", "value2"}, result);
    }

    /**
     * Test functionality of flatten method with long options and bursting.
     */
    @Test
    public void testLongOptionsBursting() {
        PosixParser parser = new PosixParser();
        String[] args = {"--longOption1", "--longOption2", "value1", "--longOption3", "value2"};
        Options options = new Options();
        options.addOption("longOption1", true, "Option longOption1");
        options.addOption("longOption2", true, "Option longOption2");
        options.addOption("longOption3", true, "Option longOption3");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption1", "--longOption2", "value1", "--longOption3", "value2"}, result);
    }
}