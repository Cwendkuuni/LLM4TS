import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testInit() {
        parser.init();
        assertNull(parser.getCurrentOption());
        assertFalse(parser.isEatTheRest());
        assertEquals(0, parser.getTokens().size());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-a", "value1", "-b", "-c", "value2"};
        boolean stopAtNonOption = false;

        String[] flattened = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(arguments, flattened);
    }

    @Test
    public void testGobble() {
        List<String> remaining = Arrays.asList("-x", "value1", "-y", "value2");

        Iterator<String> iter = remaining.iterator();

        parser.setEatTheRest(true);
        parser.gobble(iter);

        assertTrue(parser.getTokens().containsAll(remaining));
    }

    @Test
    public void testProcess() {
        Option option = new Option("a", "optionA", false, "Option A");
        parser.setCurrentOption(option);

        String value = "value1";
        parser.process(value);

        assertNull(parser.getCurrentOption());
        assertFalse(parser.isEatTheRest());
        assertTrue(parser.getTokens().contains(value));

        Option optionWithArgument = new Option("b", "optionB", true, "Option B");
        parser.setCurrentOption(optionWithArgument);

        parser.process(value);
        assertTrue(parser.getTokens().contains(value));

        Option optionWithArguments = new Option("c", "optionC", true, "Option C");
        parser.setCurrentOption(optionWithArguments);

        parser.process(value);
        assertTrue(parser.getTokens().contains(value));

        option.setArgs(Option.UNLIMITED_VALUES);
        parser.setCurrentOption(option);

        parser.process(value);
        assertTrue(parser.getTokens().contains(value));
    }

    @Test
    public void testProcessSingleHyphen() {
        String hyphen = "-";
        parser.processSingleHyphen(hyphen);
        assertTrue(parser.getTokens().contains(hyphen));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Option A");
        options.addOption(option);

        String token = "-a";
        boolean stopAtNonOption = false;

        parser.setOptions(options);
        parser.processOptionToken(token, stopAtNonOption);

        assertSame(option, parser.getCurrentOption());
        assertTrue(parser.getTokens().contains(token));

        token = "-b";
        stopAtNonOption = true;

        parser.processOptionToken(token, stopAtNonOption);

        assertTrue(parser.isEatTheRest());
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        String token = "-ab";
        boolean stopAtNonOption = false;

        parser.setOptions(options);
        parser.burstToken(token, stopAtNonOption);

        assertTrue(parser.getTokens().contains("-a"));
        assertTrue(parser.getTokens().contains("-b"));

        stopAtNonOption = true;

        parser.burstToken(token, stopAtNonOption);

        assertTrue(parser.isEatTheRest());
    }

}