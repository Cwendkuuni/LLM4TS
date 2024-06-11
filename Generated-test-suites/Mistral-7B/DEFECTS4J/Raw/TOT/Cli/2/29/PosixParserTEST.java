import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PosixParserTest {

    @Test
    public void testSimpleOption() {
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");

        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"-a"};

        try {
            String[] result = parser.parse(options, args, false);
            Assert.assertArrayEquals(expected, result);
            Assert.assertTrue(options.isSet("a"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionWithValue() {
        Options options = new Options();
        options.addOption("b", true, "An option with a value");

        PosixParser parser = new PosixParser();
        String[] args = {"-b", "value"};
        String[] expected = {"-b", "value"};

        try {
            String[] result = parser.parse(options, args, false);
            Assert.assertArrayEquals(expected, result);
            Assert.assertEquals("value", options.getOptionValue("b"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-b"};
        String[] expected = {"-a", "-b"};

        try {
            String[] result = parser.parse(options, args, false);
            Assert.assertArrayEquals(expected, result);
            Assert.assertTrue(options.isSet("a"));
            Assert.assertTrue(options.isSet("b"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "A long option");

        PosixParser parser = new PosixParser();
        String[] args = {"--longOption"};
        String[] expected = {"--longOption"};

        try {
            String[] result = parser.parse(options, args, false);
            Assert.assertArrayEquals(expected, result);
            Assert.assertTrue(options.isSet("longOption"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"-x"};
        String[] expected = {};

        try {
            String[] result = parser.parse(options, args, false);
            Assert.assertArrayEquals(expected, result);
            Assert.assertFalse(options.isSet("x"));
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--", "c", "-b"};
        String[] expected = {"-a"};

        try {
            String[] result = parser.parse(options, args, true);
            Assert.assertArrayEquals(expected, result);
            Assert.assertTrue(options.isSet("a"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testMalformedOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"-ab"};

        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testMixedLongAndShortOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("longOption", true, "A long option");

        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "-a"};

        parser.parse(options, args, false);
    }
}