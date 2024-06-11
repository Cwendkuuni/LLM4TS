import org.junit.Before;
import org.junit.Test;
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
    public void testFlatten_NoArguments_ReturnsEmptyArray() {
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_SingleArgumentStartingWithDash_ReturnsUnprocessedToken() {
        String[] arguments = {"-singleArgument"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-singleArgument", result[0]);
    }

    @Test
    public void testFlatten_SingleArgumentStartingWithDoubleDash_ReturnsUnprocessedToken() {
        String[] arguments = {"--singleArgument"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("--singleArgument", result[0]);
    }

    @Test
    public void testFlatten_OptionWithNoArgs_ReturnsProcessedOption() {
        options.addOption("a", false, null);
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_OptionWithArgs_ReturnsProcessedOptionAndArgs() {
        options.addOption("b", true, null);
        String[] arguments = {"-b", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(3, result.length);
        assertEquals("-b", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_OptionWithMultipleArgs_ReturnsProcessedOptionAndArgs() {
        options.addOption("c", true, null);
        String[] arguments = {"-c", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(3, result.length);
        assertEquals("-c", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_OptionWithArgsAndUnprocessedTokens_ReturnsProcessedOptionAndUnprocessedTokens() {
        options.addOption("d", true, null);
        String[] arguments = {"-d", "arg1", "arg2", "unprocessed"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(4, result.length);
        assertEquals("-d", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
        assertEquals("unprocessed", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnsProcessedOptionsAndNonOptions() {
        options.addOption("e", false, null);
        String[] arguments = {"-e", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertEquals("-e", result[0]);
        assertEquals("nonOption", result[1]);
    }

    @Test
    public void testFlatten_BurstToken_ReturnsProcessedOptionsAndUnprocessedTokens() {
        options.addOption("f", false, null);
        options.addOption("g", true, null);
        options.addOption("h", true, null);
        String[] arguments = {"-fg", "arg1", "-h", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(5, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-g", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("-h", result[3]);
        assertEquals("arg2", result[4]);
    }
}