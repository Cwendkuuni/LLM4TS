import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", "option-a", true, "Option A");
        options.addOption("b", "option-b", false, "Option B");
        options.addOption("c", "option-c", 1, "Option C");
        options.addOption("d", "option-d", true, "Option D");
        options.addOption("e", "option-e", false, "Option E");
        options.addOption("f", "option-f", true, "Option F");
        options.addOption("g", "option-g", true, "Option G");
    }

    @Test
    public void testFlattenWithEmptyArguments() {
        String[] arguments = {};
        String[] expected = {};
        try {
            List<String> result = new PosixParser().flatten(options, arguments, false);
            Assert.assertArrayEquals(expected, (String[]) result.toArray());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithSingleArgument() {
        String[] arguments = {"--option-a"};
        String[] expected = {"--option-a"};
        try {
            List<String> result = new PosixParser().flatten(options, arguments, false);
            Assert.assertArrayEquals(expected, (String[]) result.toArray());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithSingleShortOption() {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        try {
            List<String> result = new PosixParser().flatten(options, arguments, false);
            Assert.assertArrayEquals(expected, (String[]) result.toArray());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithSingleLongOption() {
        String[] arguments = {"--option-d"};
        String[] expected = {"--option-d"};
        try {
            List<String> result = new PosixParser().flatten(options, arguments, false);
            Assert.assertArrayEquals(expected, (String[]) result.toArray());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithSingleOptionWithValue() {
        String[] arguments = {"--option-a", "value"};
        String[] expected = {"--option-a", "value"};
        try {
            List<String> result = new PosixParser().flatten(options, arguments, false);
            Assert.assertArrayEquals(expected, (String[]) result.toArray());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        String[] arguments = {"-a", "--option-d", "-e", "--option-f=value", "--option-g"};
        String[] expected = {"-a", "--option-d", "-e", "--option-f", "value", "--option-g"};
        try {
            List<String> result = new PosixParser().flatten(options, arguments, false);
            Assert.assertArrayEquals(expected, (String[]) result.toArray());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithMixedOptions() {
        String[] arguments = {"--option-a", "--option-b", "-c", "--option-d=value", "-e", "--", "option", "value"};
        String[] expected = {"--option-a", "--option-b", "--option-d", "value", "-c", "--", "option", "value"};
        try {
            List<String> result = new PosixParser().flatten(options, arguments, false);
            Assert.assertArrayEquals(expected, (String[]) result.toArray());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] arguments = {"--invalid-option"};
        new PosixParser().flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithUnsupportedOption() {
        String[] arguments = {"--option-h"};
        new PosixParser().flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidLongOption() {
        String[] arguments = {"--invalid-long-option"};
        new PosixParser().flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithUnsupportedLongOption() {
        String[] arguments = {"--option-i"};
        new PosixParser().flatten(options, arguments, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"a", "--option-a", "b", "--option-d", "c"};
        String[] expected = {"a", "--option-a", "b"};
        try {
            List<String> result = new PosixParser().flatten(options, arguments, true);
            Assert.assertArrayEquals(expected, (String[]) result.toArray());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBurstTokenWithSingleOption() {
        String token = "-a";
        String[] expected = {"-a"};
        testBurstToken(new PosixParser(), options, token, expected, false);
    }

    @Test
    public void testBurstTokenWithLongOption() {
        String token = "--option-a";
        String[] expected = {"--option-a"};
        testBurstToken(new PosixParser(), options, token, expected, false);
    }

    @Test
    public void testBurstTokenWithOptionWithValue() {
        String token = "--option-a=value";
        String[] expected = {"--option-a", "value"};
        testBurstToken(new PosixParser(), options, token, expected, false);
    }

    @Test
    public void testBurstTokenWithOptionAndArguments() {
        String token = "--option-a value1 value2";
        String[] expected = {"--option-a", "value1", "value2"};
        testBurstToken(new PosixParser(), options, token, expected, false);
    }

    @Test
    public void testBurstTokenWithInvalidOption() {
        String token = "--invalid-option";
        try {
            testBurstToken(new PosixParser(), options, token, null, true);
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test
    public void testBurstTokenWithUnsupportedOption() {
        String token = "--option-h";
        try {
            testBurstToken(new PosixParser(), options, token, null, true);
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test
    public void testBurstTokenWithInvalidLongOption() {
        String token = "--invalid-long-option";
        try {
            testBurstToken(new PosixParser(), options, token, null, true);
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test
    public void testBurstTokenWithUnsupportedLongOption() {
        String token = "--option-i";
        try {
            testBurstToken(new PosixParser(), options, token, null, true);
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String token = "-a --option-a b";
        String[] expected = {"-a", "--option-a", "b"};
        testBurstToken(new PosixParser(), options, token, expected, true);
    }

    private void testBurstToken(PosixParser parser, Options options, String token, String[] expected, boolean stopAtNonOption) {
        List<String> result = new ArrayList<>();
        try {
            parser.burstToken(token, stopAtNonOption);
            for (String option : parser.tokens) {
                result.add(option);
                parser.tokens.remove(0);
            }
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertEquals(Arrays.asList(expected), result);
    }
}