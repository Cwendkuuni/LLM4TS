import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] arguments = {"-a", "-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] arguments = {"-a", "nonoption", "-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("nonoption", result[1]);
    }
}