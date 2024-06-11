import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;
    private Options options;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "value1", "arg1"};
        options.addOption("a", true, "Option a");

        String[] result = posixParser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlatten_WithLongOption() {
        String[] args = {"--option", "value1", "arg1"};
        options.addOption(Option.builder().longOpt("option").build());

        String[] result = posixParser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlatten_WithShortOption() {
        String[] args = {"-a", "-b", "arg1"};
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        String[] result = posixParser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlatten_WithInvalidOption_NoStopAtNonOption() {
        String[] args = {"-a", "-c", "arg1"};
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        String[] result = posixParser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-c", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlatten_WithInvalidOption_StopAtNonOption() {
        String[] args = {"-a", "-c", "arg1"};
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        String[] result = posixParser.flatten(options, args, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-c", result[1]);
        assertEquals("--", result[2]);
        assertEquals("arg1", result[3]);
    }

    @Test
    public void testFlatten_WithSingleHyphen() {
        String[] args = {"-"};
        options.addOption(Option.builder("a").build());

        String[] result = posixParser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_WithArgumentValue() {
        String[] args = {"-a", "value1"};
        options.addOption(Option.builder("a").hasArg().build());

        String[] result = posixParser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_WithMultipleArgumentValues() {
        String[] args = {"-a", "value1", "value2", "arg1"};
        options.addOption(Option.builder("a").hasArgs().build());

        String[] result = posixParser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
        assertEquals("arg1", result[3]);
    }

    @Test
    public void testFlatten_WithRemainingTokens() {
        String[] args = {"-a", "value1", "arg1", "--", "remaining1", "remaining2"};
        options.addOption(Option.builder("a").hasArg().build());

        String[] result = posixParser.flatten(options, args, false);

        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("--", result[3]);
        assertEquals("remaining1", result[4]);
        assertEquals("remaining2", result[5]);
    }

}