import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = new String[] {"--opt1", "value1", "--opt2=value2", "-opt3", "-"};
        String[] expected = new String[] {"--opt1", "value1", "--opt2", "value2", "-opt3", "-"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();

        parser.init();

        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();

        parser.processNonOptionToken("value1");

        assertTrue(parser.eatTheRest);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value1", parser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("opt1", "option1", false, "option1");

        options.addOption(option);

        parser.options = options;

        parser.processOptionToken("--opt1", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("--opt1", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("opt1", "option1", false, "option1");

        option1.setArgs(1);
        options.addOption(option1);

        parser.options = options;

        parser.burstToken("-opt1value", false);

        assertEquals(2, parser.tokens.size());
        assertEquals("-opt1", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }
}