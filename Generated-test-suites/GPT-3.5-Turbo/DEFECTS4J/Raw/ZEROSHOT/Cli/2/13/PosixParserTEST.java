import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "test", "-b", "value1", "-c"};
        String[] expected = {"-a", "test", "-b", "value1", "-c"};
        assertArrayEquals(expected, parser.flatten(options, arguments, true));
    }

    @Test
    public void testProcessSingleHyphen() {
        String hyphen = "-";
        String[] expected = {"-"};
        parser.processSingleHyphen(hyphen);
        assertArrayEquals(expected, parser.getTokens().toArray());
    }

    @Test
    public void testProcessOptionToken() {
        boolean stopAtNonOption = true;
        String token = "-b";
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("c", false, "Option c");
        parser.setOptions(options);

        String[] expected = {"-b"};
        parser.processOptionToken(token, stopAtNonOption);
        assertArrayEquals(expected, parser.getTokens().toArray());
        assertSame(options.getOption("b"), parser.getCurrentOption());
    }

    @Test
    public void testProcessOptionTokenWithInvalidOption() {
        boolean stopAtNonOption = false;
        String token = "-d";
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("c", false, "Option c");
        parser.setOptions(options);

        String[] expected = {"-d"};
        parser.processOptionToken(token, stopAtNonOption);
        assertArrayEquals(expected, parser.getTokens().toArray());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstToken() {
        boolean stopAtNonOption = true;
        String token = "-abc";
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        parser.setOptions(options);

        String[] expected = {"-a", "-b", "c"};
        parser.burstToken(token, stopAtNonOption);
        assertArrayEquals(expected, parser.getTokens().toArray());
        assertSame(options.getOption("c"), parser.getCurrentOption());
    }

    @Test
    public void testBurstTokenWithNonOption() {
        boolean stopAtNonOption = false;
        String token = "-abcd";
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        parser.setOptions(options);

        String[] expected = {"-a", "-b", "-c", "-d"};
        parser.burstToken(token, stopAtNonOption);
        assertArrayEquals(expected, parser.getTokens().toArray());
        assertSame(options.getOption("c"), parser.getCurrentOption());
    }

    @Test
    public void testProcess() {
        String value = "test";
        parser.setCurrentOption(new Option("b", true, "Option b"));

        String[] expected = {"test"};
        parser.process(value);
        assertArrayEquals(expected, parser.getTokens().toArray());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testProcessWithNoOption() {
        String value = "test";
        boolean stopAtNonOption = false;

        String[] expected = {"--", "test"};
        parser.process(value);
        assertArrayEquals(expected, parser.getTokens().toArray());
        assertTrue(parser.isEatTheRest());
    }

    @Test
    public void testGobble() {
        parser.setEatTheRest(true);
        String[] remainingTokens = {"token1", "token2"};

        String[] expected = {"token1", "token2"};
        parser.gobble(Arrays.asList(remainingTokens).iterator());
        assertArrayEquals(expected, parser.getTokens().toArray());
    }

}