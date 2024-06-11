import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class PosixParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();

        Option option1 = new Option("a", "option a");
        option1.setArgs(1);
        options.addOption(option1);

        Option option2 = new Option("b", "option b");
        option2.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(option2);

        parser = new PosixParser();
    }

    @Test
    public void testFlatten_withLongOptions() {
        String[] args = new String[]{"--option1", "--option2=value"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("--option2=value", result[1]);
    }

    @Test
    public void testFlatten_withSingleHyphen() {
        String[] args = new String[]{"-"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_withSingleOption() {
        String[] args = new String[]{"-a", "value"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_withExistingOption() {
        String[] args = new String[]{"-b", "value1", "value2"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }

    @Test
    public void testFlatten_withInvalidOption() {
        String[] args = new String[]{"-c", "value"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-c", result[1]);
    }

    @Test
    public void testFlatten_withStopAtNonOption() {
        String[] args = new String[]{"-a", "value1", "value2", "-c", "value3"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
        assertEquals("--", result[3]);
    }

    @Test
    public void testFlatten_withRemainingTokens() {
        String[] args = new String[]{"-a", "value1", "value2"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
        assertEquals("--", result[3]);
    }

    @Test
    public void testBurstToken_withValidOption() {
        String token = "-b";

        parser.burstToken(token, false);

        assertEquals(1, tokens.size());
        assertEquals("-b", tokens.get(0));
    }

    @Test
    public void testBurstToken_withOptionAndArgument() {
        String token = "-bvalue";

        parser.burstToken(token, false);

        assertEquals(1, tokens.size());
        assertEquals("-bvalue", tokens.get(0));
    }

    @Test
    public void testBurstToken_withNonOptionAndStopAtNonOptionTrue() {
        String token = "-c";

        parser.burstToken(token, true);

        assertEquals(2, tokens.size());
        assertEquals("--", tokens.get(0));
        assertEquals("-c", tokens.get(1));
    }

    @Test
    public void testBurstToken_withNonOptionAndStopAtNonOptionFalse() {
        String token = "-c";

        parser.burstToken(token, false);

        assertEquals(1, tokens.size());
        assertEquals("-c", tokens.get(0));
    }

}