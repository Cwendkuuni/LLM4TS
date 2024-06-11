import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 3, "Option C with an optional argument");
    }

    @After
    public void teardown() {
        parser = null;
    }

    @Test
    public void testEmptyArgs() {
        String[] args = {};
        String[] expected = {};

        try {
            parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testSimpleArgs() {
        String[] args = {"--optionA"};
        String[] expected = {"--optionA"};

        try {
            parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testOptionWithArg() {
        String[] args = {"--optionB", "value"};
        String[] expected = {"--optionB", "value"};

        try {
            parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testOptionWithOptionalArg() {
        String[] args = {"--optionC", "value1"};
        String[] expected = {"--optionC", "value1"};

        try {
            parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        String[] args2 = {"--optionC"};
        String[] expected2 = {"--optionC"};

        try {
            parser = new PosixParser();
            String[] result = parser.flatten(options, args2, false);
            Assert.assertArrayEquals(expected2, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        String[] args = {"--invalidOption"};

        try {
            parser = new PosixParser();
            parser.flatten(options, args, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptionWithArg() {
        String[] args = {"--invalidOption", "value"};

        try {
            parser = new PosixParser();
            parser.flatten(options, args, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testMultiOptions() {
        String[] args = {"--optionA", "--optionB", "valueB"};
        String[] expected = {"--optionA", "--optionB", "valueB"};

        try {
            parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testMixedOptionsAndValues() {
        String[] args = {"--optionA", "--optionB=valueB", "valueC"};
        String[] expected = {"--optionA", "--optionB=valueB", "valueC"};

        try {
            parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testLongOptionWithArg() {
        options.addOption("longOption", true, "Long option with an argument");
        String[] args = {"--longOption", "value"};
        String[] expected = {"--longOption", "value"};

        try {
            parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidLongOptionWithArg() {
        options.addOption("longInvalidOption", true, "Invalid long option with an argument");
        String[] args = {"--longInvalidOption", "value"};

        try {
            parser = new PosixParser();
            parser.flatten(options, args, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }
}