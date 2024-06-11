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
    public void testFlatten_OptionsWithLongOptions() {
        Options options = new Options();
        options.addOption("foo", false, "Option foo");
        options.addOption("bar", true, "Option bar");

        String[] arguments = {"--foo", "--bar=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(result.length, 4);
        assertEquals(result[0], "--foo");
        assertEquals(result[1], "--");
        assertEquals(result[2], "--bar");
        assertEquals(result[3], "value");
    }

    @Test
    public void testFlatten_OptionsWithShortOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "-bvalue"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(result.length, 3);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "-b");
        assertEquals(result[2], "value");
    }

    @Test
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        options.addOption("foo", false, "Option foo");

        String[] arguments = {"-bar"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(result.length, 1);
        assertEquals(result[0], "-bar");
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("foo", false, "Option foo");

        String[] arguments = {"-foo", "value", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(result.length, 3);
        assertEquals(result[0], "-foo");
        assertEquals(result[1], "value");
        assertEquals(result[2], "nonOption");
    }
}