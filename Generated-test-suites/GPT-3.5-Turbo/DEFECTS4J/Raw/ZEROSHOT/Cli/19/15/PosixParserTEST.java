import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

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
        String[] args = {};
        String[] result = parser.flatten(options, args, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_SingleOption_ReturnsOptionToken() {
        options.addOption(Option.builder("a").build());

        String[] args = {"-a"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_SingleOptionWithArgument_ReturnsOptionAndArgumentTokens() {
        options.addOption(Option.builder("a").hasArg().build());

        String[] args = {"-a", "arg"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg", result[1]);
    }

    @Test
    public void testFlatten_SingleOptionWithMultipleArguments_ReturnsOptionAndAllArgumentTokens() {
        options.addOption(Option.builder("a").hasArgs().build());

        String[] args = {"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue_ReturnsTokensUntilNonOption() {
        options.addOption(Option.builder("a").build());

        String[] args = {"-a", "-b"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse_ReturnsAllTokens() {
        options.addOption(Option.builder("a").build());

        String[] args = {"-a", "-b"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }
}