import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertEquals(parser.tokens.size(), 0);
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "a option");
        options.addOption("b", true, "b option");
        options.addOption("c", false, "c option");

        String[] arguments = {"-a", "valueA", "--b=valueB", "argC", "-d", "-e"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(result.length, 6);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "valueA");
        assertEquals(result[2], "--b");
        assertEquals(result[3], "valueB");
        assertEquals(result[4], "--");
        assertEquals(result[5], "-d");
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("x", false, "x option");

        String token = "-xy";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, true);

        assertEquals(parser.tokens.size(), 2);
        assertEquals(parser.tokens.get(0), "-x");
        assertEquals(parser.tokens.get(1), "y");
    }
}