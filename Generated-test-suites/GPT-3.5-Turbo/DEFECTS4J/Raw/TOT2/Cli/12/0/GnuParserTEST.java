import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class GnuParserTest {

    @Test
    public void testFlattenWithExistingOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlattenWithNonOption() {
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] arguments = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlattenWithSpecialOptionFormat() {
        Options options = new Options();
        options.addOption("D", true, "Special Option");
        GnuParser parser = new GnuParser();
        String[] arguments = {"-Dproperty=value"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "arg1", "-b"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }
}