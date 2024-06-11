import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    

    @Test
    public void testFlattenEmpty() {
        String[] input = {};
        String[] expected = {};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithArguments() {
        String[] input = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        options.addOption("longOption1", true, "l1");
        options.addOption("longOption2", false, "l2");
        String[] input = {"--longOption1", "--longOption2"};
        String[] expected = {"longOption1", "longOption2"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithShortOptions() {
        options.addOption("o1", true, "o");
        String[] input = {"-o"};
        String[] expected = {"o1"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        String[] input = {"-"};
        String[] expected = {};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleHyphenAndLongOptions() {
        options.addOption("longOption1", true, "l1");
        String[] input = {"--", "-l1"};
        String[] expected = {"--", "longOption1"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSingleHyphenAndShortOptions() {
        options.addOption("o1", true, "o");
        String[] input = {"--", "-o"};
        String[] expected = {"--", "o1"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] input = {"--invalidOption"};
        String[] result = parser.flatten(options, input, false);
        Assert.fail("Expected ParseException but got: " + Arrays.toString(result));
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        options.addOption("o1", true, "o");
        String[] input = {"-o", "nonOption"};
        String[] expected = {"o1"};
        String[] result = parser.flatten(options, input, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleInvalidOptions() {
        options.addOption("o1", true, "o");
        String[] input = {"--invalidOption1", "--invalidOption2"};
        String[] expected = {"--invalidOption1", "--invalidOption2"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleValidOptions() {
        options.addOption("o1", true, "o");
        options.addOption("o2", true, "o");
        String[] input = {"-o1", "-o2"};
        String[] expected = {"o1", "o2"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        options.addOption("longOption", true, "l");
        String[] input = {"--longOption=value"};
        String[] expected = {"longOption", "value"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionValueAndArguments() {
        options.addOption("longOption", true, "l");
        String[] input = {"--longOption=value", "arg1", "arg2"};
        String[] expected = {"longOption", "value", "arg1", "arg2"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleLongOptions() {
        options.addOption("longOption1", true, "l1");
        options.addOption("longOption2", true, "l2");
        String[] input = {"--longOption1=value1", "--longOption2=value2"};
        String[] expected = {"longOption1", "value1", "longOption2", "value2"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionalLongOption() {
        options.addOption("longOption", true, "l");
        options.addOption("noLongOption", false, null);
        String[] input = {"--longOption", "--noLongOption"};
        String[] expected = {"longOption", "noLongOption"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMixedOptions() {
        options.addOption("o1", true, "o1");
        options.addOption("o2", true, "o2");
        String[] input = {"--o1", "-o2"};
        String[] expected = {"o1", "o2"};
        String[] result = parser.flatten(options, input, false);
        Assert.assertArrayEquals(expected, result);
    }

    
}