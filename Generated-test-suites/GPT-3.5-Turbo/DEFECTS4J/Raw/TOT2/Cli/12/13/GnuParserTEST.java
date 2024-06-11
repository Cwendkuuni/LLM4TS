import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class GnuParserTest {

    @Test
    public void testFlatten_Options() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "-b"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "-b"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        options.addOption("x", false, "Option x");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-x", "arg1", "nonOption", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-x", "arg1", "nonOption", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_Characters() {
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] arguments = {"-abc"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "-b", "-c"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DoubleDash() {
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] arguments = {"--", "-x", "-y"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--", "-x", "-y"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_PropertyOption() {
        Options options = new Options();
        options.addOption("D", true, "Property option");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-Dkey=value"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-D", "key=value"};
        assertArrayEquals(expected, result);
    }
}