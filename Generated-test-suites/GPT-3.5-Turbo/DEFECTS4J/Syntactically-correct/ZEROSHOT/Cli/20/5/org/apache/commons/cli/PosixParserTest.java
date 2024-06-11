import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testInit() {
        parser.init();
        List tokens = parser.getTokens();
        assertTrue(tokens.isEmpty());
        assertFalse(parser.isEatTheRest());
        assertNull(parser.getCurrentOption());
    }

    @Test
    public void testFlatten_stopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");

        String[] arguments = {"-a", "value", "-b", "value", "nonOption"};

        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value", flattened[3]);
        assertEquals("nonOption", flattened[4]);
    }

    @Test
    public void testFlatten_stopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");

        String[] arguments = {"-a", "value", "-b", "value", "nonOption"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value", flattened[3]);
        assertEquals("nonOption", flattened[4]);
    }

    @Test
    public void testProcessOptionToken_optionExists() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");

        boolean stopAtNonOption = false;

        parser.init();
        parser.setOptions(options);

        parser.processOptionToken("-a", stopAtNonOption);
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertEquals("a", currentOption.getOpt());
        assertEquals("-a", tokens.get(0));
    }

    @Test
    public void testProcessOptionToken_optionDoesNotExist_stopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", false, "");

        boolean stopAtNonOption = true;

        parser.init();
        parser.setOptions(options);

        parser.processOptionToken("-b", stopAtNonOption);
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertNull(currentOption);
        assertTrue(tokens.isEmpty());
        assertEquals("-b", tokens.get(0));
    }

    @Test
    public void testProcessOptionToken_optionDoesNotExist_stopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", false, "");

        boolean stopAtNonOption = false;

        parser.init();
        parser.setOptions(options);

        parser.processOptionToken("-b", stopAtNonOption);
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertNull(currentOption);
        assertEquals("-b", tokens.get(0));
    }

    @Test
    public void testProcess_longOptionWithValue() {
        Options options = new Options();
        options.addOption("a", false, "");

        String value = "value";
        parser.init();
        parser.setOptions(options);

        parser.process("--a=" + value);
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertNull(currentOption);
        assertEquals("--a", tokens.get(0));
        assertEquals(value, tokens.get(1));
    }

    @Test
    public void testProcess_longOptionWithoutValue() {
        Options options = new Options();
        options.addOption("a", false, "");

        parser.init();
        parser.setOptions(options);

        parser.process("--a");
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertNull(currentOption);
        assertEquals("--a", tokens.get(0));
    }

    @Test
    public void testProcess_singleHyphen() {
        parser.init();

        parser.process("-");
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertNull(currentOption);
        assertEquals("-", tokens.get(0));
    }

    @Test
    public void testProcess_shortOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "");

        String value = "value";
        parser.init();
        parser.setOptions(options);

        parser.process("-a" + value);
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertNull(currentOption);
        assertEquals("-a", tokens.get(0));
        assertEquals(value, tokens.get(1));
    }

    @Test
    public void testProcess_shortOptionWithoutValue() {
        Options options = new Options();
        options.addOption("a", false, "");

        parser.init();
        parser.setOptions(options);

        parser.process("-a");
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertNull(currentOption);
        assertEquals("-a", tokens.get(0));
    }

    @Test
    public void testProcess_nonOption_stopAtNonOptionTrue() {
        boolean stopAtNonOption = true;

        parser.init();

        parser.process("nonOption");
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertNull(currentOption);
        assertEquals("--", tokens.get(0));
        assertEquals("nonOption", tokens.get(1));
    }

    @Test
    public void testProcess_nonOption_stopAtNonOptionFalse() {
        boolean stopAtNonOption = false;

        parser.init();

        parser.process("nonOption");
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertNull(currentOption);
        assertEquals("nonOption", tokens.get(0));
    }

    @Test
    public void testBurstToken_optionExists() {
        Options options = new Options();
        options.addOption("a", false, "");
        options.addOption("b", true, "");

        boolean stopAtNonOption = false;

        parser.init();
        parser.setOptions(options);

        parser.burstToken("-ab", stopAtNonOption);
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertEquals("a", currentOption.getOpt());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
    }

    @Test
    public void testBurstToken_optionDoesNotExist_stopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", false, "");

        boolean stopAtNonOption = true;

        parser.init();
        parser.setOptions(options);

        parser.burstToken("-bc", stopAtNonOption);
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertNull(currentOption);
        assertEquals("--", tokens.get(0));
        assertEquals("-bc", tokens.get(1));
    }

    @Test
    public void testBurstToken_optionDoesNotExist_stopAtNonOptionFalse() {
        Options options = new Options();
        options.addOption("a", false, "");

        boolean stopAtNonOption = false;

        parser.init();
        parser.setOptions(options);

        parser.burstToken("-bc", stopAtNonOption);
        Option currentOption = parser.getCurrentOption();
        List tokens = parser.getTokens();

        assertNull(currentOption);
        assertEquals("-bc", tokens.get(0));
    }
}