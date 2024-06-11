import static org.junit.Assert.*;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten_WithExistingOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] arguments = {"-a", "-b"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testFlatten_WithNonExistingOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("x", false, "Option x");

        String[] arguments = {"-y"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-y", result[0]);
    }

    @Test
    public void testFlatten_WithSpecialPropertyOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", false, "Special property option");

        String[] arguments = {"-Dproperty=value"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }

    @Test
    public void testFlatten_WithStopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"-a", "nonOption1", "-b", "nonOption2"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("nonOption1", result[1]);
    }

    @Test
    public void testFlatten_WithDoubleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"--", "-a", "-b"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlatten_WithSingleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"-", "-a"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-", result[0]);
        assertEquals("-a", result[1]);
    }
}