import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;

import static org.junit.Assert.*;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_LongOption_ShouldAddToken() {
        options.addOption("f", false, "file");

        String[] arguments = {"--file"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--file"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue_ShouldAddTokenAndValue() {
        options.addOption("f", true, "file");

        String[] arguments = {"--file=file.txt"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--file", "file.txt"}, result);
    }

    @Test
    public void testFlatten_SingleHyphen_ShouldAddToken() {
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_ShortOption_ShouldAddToken() {
        options.addOption("v", false, "verbose");

        String[] arguments = {"-v"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-v"}, result);
    }

    @Test
    public void testFlatten_ShortOptionWithValue_ShouldAddTokenAndValue() {
        options.addOption("n", true, "name");

        String[] arguments = {"-n", "John"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-n", "John"}, result);
    }

    @Test
    public void testFlatten_InvalidShortOption_StopAtNonOption_ShouldAddRemainingTokens() {
        String[] arguments = {"-a", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"--", "-a", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlatten_InvalidShortOption_NotStopAtNonOption_ShouldAddToken() {
        String[] arguments = {"-a", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_NonOptionToken_ShouldAddToken() {
        String[] arguments = {"value"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"value"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_ShouldAddSpecialTokenAndRemainingTokens() {
        String[] arguments = {"--", "args"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"--", "args"}, result);
    }

    @Test
    public void testFlatten_NotStopAtNonOption_ShouldAddSpecialTokenAndRemainingTokens() {
        String[] arguments = {"--", "args"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--", "args"}, result);
    }

    @Test
    public void testFlatten_EmptyArguments_ShouldReturnEmptyArray() {
        String[] arguments = {};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_NullOptions_ShouldThrowException() {
        String[] arguments = {"--file"};

        try {
            parser.flatten(null, arguments, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Options must not be null", e.getMessage());
        }
    }

    @Test
    public void testFlatten_NullArguments_ShouldThrowException() {
        try {
            parser.flatten(options, null, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Arguments must not be null", e.getMessage());
        }
    }

}