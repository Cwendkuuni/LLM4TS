import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
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
        options.addOption(new Option("a", false));
        options.addOption(new Option("b", true));
        options.addOption(new Option("c", false, "description"));

        String[] arguments = new String[]{"--option1", "-b", "argument", "-a", "--option2"};

        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals("Expected length of result", 5, result.length);
        assertEquals("Expected value of result[0]", "--option1", result[0]);
        assertEquals("Expected value of result[1]", "-b", result[1]);
        assertEquals("Expected value of result[2]", "argument", result[2]);
        assertEquals("Expected value of result[3]", "-a", result[3]);
        assertEquals("Expected value of result[4]", "--option2", result[4]);
    }

    @Test
    public void testInit() {
        parser.init();
        List tokens = parser.getTokens();

        assertTrue("Expected tokens list to be empty", tokens.isEmpty());
        assertNull("Expected currentOption to be null", parser.getCurrentOption());
        assertFalse("Expected eatTheRest to be false", parser.isEatTheRest());
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption(new Option("a", false));

        parser.setOptions(options);
        parser.processOptionToken("-a", true);

        List tokens = parser.getTokens();
        Option currentOption = parser.getCurrentOption();

        assertEquals("Expected size of tokens list", 1, tokens.size());
        assertEquals("Expected value of tokens[0]", "-a", tokens.get(0));
        assertNotNull("Expected currentOption to be not null", currentOption);
        assertEquals("Expected id of currentOption", "a", currentOption.getOpt());
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        Option option = new Option("a", true);
        options.addOption(option);

        parser.setOptions(options);
        parser.process("value");

        List tokens = parser.getTokens();
        Option currentOption = parser.getCurrentOption();

        assertEquals("Expected size of tokens list", 1, tokens.size());
        assertEquals("Expected value of tokens[0]", "value", tokens.get(0));
        assertNull("Expected currentOption to be null", currentOption);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption(new Option("a", false));
        options.addOption(new Option("b", true));

        parser.setOptions(options);
        parser.burstToken("-ab", true);

        List tokens = parser.getTokens();
        Option currentOption = parser.getCurrentOption();

        assertEquals("Expected size of tokens list", 2, tokens.size());
        assertEquals("Expected value of tokens[0]", "-a", tokens.get(0));
        assertEquals("Expected value of tokens[1]", "-b", tokens.get(1));
        assertNull("Expected currentOption to be null", currentOption);
    }

    @Test
    public void testGobble() {
        List<String> input = Arrays.asList("token1", "token2", "token3");
        Iterator<String> iterator = input.iterator();

        parser.setEatTheRest(true);
        parser.gobble(iterator);

        List tokens = parser.getTokens();
        assertEquals("Expected size of tokens list", 3, tokens.size());
        assertEquals("Expected value of tokens[0]", "token1", tokens.get(0));
        assertEquals("Expected value of tokens[1]", "token2", tokens.get(1));
        assertEquals("Expected value of tokens[2]", "token3", tokens.get(2));
    }

}