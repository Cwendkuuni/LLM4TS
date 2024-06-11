import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");
        options.addOption("d", 3, "Option D");
        parser = new PosixParser();
    }

    @After
    public void tearDown() {
        parser = null;
        options = null;
    }

    @Test
    public void testEmptyArgumentArray() {
        String[] arguments = {};
        String[] expected = {};
        String[] flattened = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testStopAtNonOption() {
        String[] arguments = {"--non-option", "optionA", "optionB", "--non-option"};
        String[] expected = {"--non-option", "optionA", "--non-option"};
        String[] flattened = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testSingleHyphen() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testDoubleHyphen() {
        String[] arguments = {"--"};
        String[] expected = {"--"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testSingleCharacterOption() {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testMultiCharacterOption() {
        String[] arguments = {"-ab"};
        String[] expected = {"-a", "-b"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testOptionWithValue() {
        String[] arguments = {"-a", "valueA"};
        String[] expected = {"-a", "valueA"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testOptionWithValueAndArgs() {
        String[] arguments = {"-a", "valueA", "--", "optionB", "optionC"};
        String[] expected = {"-a", "valueA", "--", "optionB", "optionC"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, flattened);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOption() {
        String[] arguments = {"-z"};
        String[] flattened = parser.flatten(options, arguments, false);
    }

    @Test
    public void testBursting() {
        String[] arguments = {"-abc"};
        String[] expected = {"-a", "-b", "-c"};
        String[] flattened = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testBurstingWithStopAtNonOption() {
        String[] arguments = {"-abc", "nonOption"};
        String[] expected = {"-a", "--", "nonOption"};
        String[] flattened = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, flattened);
    }
}