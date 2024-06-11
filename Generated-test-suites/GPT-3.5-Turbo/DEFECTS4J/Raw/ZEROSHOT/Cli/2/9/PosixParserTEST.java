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
    public void testFlatten() {
        String[] arguments = {"--arg1", "value1", "-a", "value2", "-b"};

        Options options = new Options();
        options.addOption("arg1", true, "Argument 1");
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] result = parser.flatten(options, arguments, true);

        assertNotNull(result);
        assertEquals(5, result.length);
        assertEquals("--arg1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-a", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("-b", result[4]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"--arg1", "value1", "-a", "value2", "-b", "nonOption"};

        Options options = new Options();
        options.addOption("arg1", true, "Argument 1");
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] result = parser.flatten(options, arguments, true);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("--arg1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-a", result[2]);
    }
}