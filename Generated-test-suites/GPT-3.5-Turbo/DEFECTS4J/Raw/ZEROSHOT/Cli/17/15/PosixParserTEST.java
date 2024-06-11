import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
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
    public void testFlatten_emptyArguments_returnsEmptyArray() {
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_singleDash_returnsProcessedTokens() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_doubleDash_returnsProcessedTokens() {
        String[] arguments = {"--"};
        String[] expected = {"--"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleCharacterOption_returnsProcessedTokens() {
        options.addOption(Option.builder("a").build());
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleCharacterOptionWithValue_returnsProcessedTokens() {
        options.addOption(Option.builder("a").hasArg().build());
        String[] arguments = {"-a", "value"};
        String[] expected = {"-a", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleCharacterOptionWithMultipleValues_returnsProcessedTokens() {
        options.addOption(Option.builder("a").hasArgs().build());
        String[] arguments = {"-a", "value1", "value2"};
        String[] expected = {"-a", "value1", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleCharacterOptionNotExistsAndStopAtNonOption_returnsProcessedTokens() {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleCharacterOptionNotExistsAndNotStopAtNonOption_returnsProcessedTokens() {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_longOptionWithValue_returnsProcessedTokens() {
        options.addOption(Option.builder().longOpt("longoption").hasArg().build());
        String[] arguments = {"--longoption", "value"};
        String[] expected = {"--longoption", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_longOptionWithMultipleValues_returnsProcessedTokens() {
        options.addOption(Option.builder().longOpt("longoption").hasArgs().build());
        String[] arguments = {"--longoption", "value1", "value2"};
        String[] expected = {"--longoption", "value1", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_longOptionNotExistsAndStopAtNonOption_returnsProcessedTokens() {
        String[] arguments = {"--longoption"};
        String[] expected = {"--longoption"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_longOptionNotExistsAndNotStopAtNonOption_returnsProcessedTokens() {
        String[] arguments = {"--longoption"};
        String[] expected = {"--longoption"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_mixedOptionsAndValues_returnsProcessedTokens() {
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder().longOpt("longoption").hasArg().build());
        String[] arguments = {"-a", "--longoption", "value"};
        String[] expected = {"-a", "--longoption", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
}