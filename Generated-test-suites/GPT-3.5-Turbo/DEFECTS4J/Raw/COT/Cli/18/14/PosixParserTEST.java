import org.apache.commons.cli.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "--", "-c"};
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-a", "-b", "--", "-c"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "-c"};
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = {"-a", "-b", "-c"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessSingleHyphen() {
        String hyphen = "-";
        parser.processSingleHyphen(hyphen);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        String token = "-a";
        boolean stopAtNonOption = true;
        parser.processOptionToken(token, stopAtNonOption);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
        assertNotNull(parser.getCurrentOption());
    }

    @Test
    public void testProcessOptionTokenNonOption() {
        Options options = new Options();
        String token = "-a";
        boolean stopAtNonOption = true;
        parser.processOptionToken(token, stopAtNonOption);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        String[] expected = {};
        assertArrayEquals(expected, result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testProcessOptionTokenNoOption() {
        Options options = new Options();
        String token = "-a";
        boolean stopAtNonOption = false;
        parser.processOptionToken(token, stopAtNonOption);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testProcessNonOption() {
        String value = "value";
        parser.process(value);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        String[] expected = {"--", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessNonOptionWithOption() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        String value = "value";
        parser.setCurrentOption(options.getOption("a"));
        parser.process(value);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        String[] expected = {};
        assertArrayEquals(expected, result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testProcessNonOptionWithOptionAndArgs() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        String value = "value";
        parser.setCurrentOption(options.getOption("a"));
        parser.process(value);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        String[] expected = {"value"};
        assertArrayEquals(expected, result);
        assertNotNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        String token = "-ab";
        boolean stopAtNonOption = true;
        parser.burstToken(token, stopAtNonOption);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        String[] expected = {"-a", "b"};
        assertArrayEquals(expected, result);
        assertNotNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstTokenNonOption() {
        Options options = new Options();
        String token = "-ab";
        boolean stopAtNonOption = true;
        parser.burstToken(token, stopAtNonOption);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        String[] expected = {};
        assertArrayEquals(expected, result);
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstTokenNoOptionStopAtNonOption() {
        Options options = new Options();
        String token = "-ab";
        boolean stopAtNonOption = false;
        parser.burstToken(token, stopAtNonOption);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        String[] expected = {"-ab"};
        assertArrayEquals(expected, result);
        assertNull(parser.getCurrentOption());
    }

}