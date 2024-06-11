import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class PosixParserTest {

    private PosixParser parser;
    private Option option;
    private String[] arguments;

    @Before
    public void setUp() {
        parser = new PosixParser();
        option = new Option("f", "file", true, "input file");
        arguments = new String[]{"-f", "input.txt"};
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(option);

        String[] result = parser.flatten(options, arguments, true);
        String[] expected = {"-f", "input.txt"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testInit() {
        parser.init();

        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        boolean stopAtNonOption = true;
        parser.processOptionToken("-f", stopAtNonOption);

        assertEquals(option, parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("-f", parser.tokens.get(0));

        stopAtNonOption = false;
        parser.processOptionToken("-f", stopAtNonOption);

        assertEquals(option, parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-f", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        boolean stopAtNonOption = true;
        parser.burstToken("-fabc", stopAtNonOption);

        assertEquals(option, parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("-f", parser.tokens.get(0));

        parser.burstToken("-fx", stopAtNonOption);

        assertEquals(option, parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("-f", parser.tokens.get(1));

        stopAtNonOption = false;
        parser.burstToken("-fabc", stopAtNonOption);

        assertEquals(option, parser.currentOption);
        assertEquals(3, parser.tokens.size());
        assertEquals("-fabc", parser.tokens.get(2));

        parser.burstToken("-fx", stopAtNonOption);

        assertEquals(option, parser.currentOption);
        assertEquals(4, parser.tokens.size());
        assertEquals("-fx", parser.tokens.get(3));
    }

    @Test
    public void testProcess() {
        parser.currentOption = option;

        parser.process("input.txt");
        assertEquals(1, parser.tokens.size());
        assertEquals("input.txt", parser.tokens.get(0));

        parser.currentOption = new Option("x", "example");
        parser.currentOption.setArgs(2);

        parser.process("arg1");
        assertEquals(2, parser.tokens.size());
        assertEquals("arg1", parser.tokens.get(1));

        parser.currentOption.setArgName("arg2");

        parser.process("arg2");
        assertEquals(3, parser.tokens.size());
        assertEquals("arg2", parser.tokens.get(2));

        parser.currentOption = null;

        parser.process("input.txt");
        assertTrue(parser.eatTheRest);
        assertEquals(5, parser.tokens.size());
        assertEquals("--", parser.tokens.get(3));
        assertEquals("input.txt", parser.tokens.get(4));
    }

    @Test
    public void testGobble() {
        Iterator iterator = Arrays.asList("-f", "input.txt").iterator();

        parser.eatTheRest = false;
        parser.gobble(iterator);

        assertEquals(0, parser.tokens.size());

        parser.eatTheRest = true;
        parser.gobble(iterator);

        assertEquals(2, parser.tokens.size());
        assertEquals("-f", parser.tokens.get(0));
        assertEquals("input.txt", parser.tokens.get(1));
    }

}