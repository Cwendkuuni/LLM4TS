import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;

import static org.junit.Assert.*;

public class PosixParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
    }

    @Test
    public void testFlatten_withNoArguments_shouldReturnEmptyArray() throws Exception {
        String[] args = {};
        CommandLine cmd = parser.parse(options, args);
        String[] result = cmd.getArgs();
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_withSingleOption_shouldReturnOptionInArgs() throws Exception {
        String[] args = {"-a"};
        CommandLine cmd = parser.parse(options, args);
        String[] result = cmd.getArgs();
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_withOptionAndValue_shouldReturnOptionAndValueInArgs() throws Exception {
        String[] args = {"-b", "value"};
        CommandLine cmd = parser.parse(options, args);
        String[] result = cmd.getArgs();
        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_withMultipleOptions_shouldReturnOptionsInArgs() throws Exception {
        String[] args = {"-a", "-b", "value", "-c"};
        CommandLine cmd = parser.parse(options, args);
        String[] result = cmd.getArgs();
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-c", result[3]);
    }

    @Test
    public void testFlatten_withNonOptionArgument_shouldReturnNonOptionArgumentInArgs() throws Exception {
        String[] args = {"-a", "nonOption"};
        CommandLine cmd = parser.parse(options, args);
        String[] result = cmd.getArgs();
        assertEquals(2, result.length);
        assertEquals("nonOption", result[1]);
    }

    @Test
    public void testFlatten_withStopAtNonOptionOption_shouldNotIncludeNonOptionArgumentInArgs() throws Exception {
        String[] args = {"-a", "-b", "value", "-c", "nonOption"};
        CommandLine cmd = parser.parse(options, args, true);
        String[] result = cmd.getArgs();
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-c", result[3]);
    }

    @Test
    public void testFlatten_withSpecialTokenArgument_shouldIncludeSpecialTokenInArgs() throws Exception {
        String[] args = {"--", "specialToken"};
        CommandLine cmd = parser.parse(options, args);
        String[] result = cmd.getArgs();
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("specialToken", result[1]);
    }

    @Test
    public void testFlatten_withOptionAndSpecialTokenArgument_shouldIncludeOptionAndSpecialTokenInArgs() throws Exception {
        String[] args = {"-a", "--", "specialToken"};
        CommandLine cmd = parser.parse(options, args);
        String[] result = cmd.getArgs();
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("specialToken", result[2]);
    }

    @Test
    public void testFlatten_withOptionAndSpecialTokenArgument_shouldNotIncludeNonOptionArgumentInArgs() throws Exception {
        String[] args = {"-a", "--", "-b"};
        CommandLine cmd = parser.parse(options, args);
        String[] result = cmd.getArgs();
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlatten_withOptionAndSpecialTokenArgument_shouldIncludeNonOptionArgumentInArgsWhenStopAtNonOptionOptionIsFalse() throws Exception {
        String[] args = {"-a", "--", "-b"};
        CommandLine cmd = parser.parse(options, args, false);
        String[] result = cmd.getArgs();
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("-b", result[3]);
    }

}