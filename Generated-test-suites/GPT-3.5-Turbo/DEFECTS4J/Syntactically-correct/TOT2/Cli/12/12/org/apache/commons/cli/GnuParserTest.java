import org.junit.Test;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

public class GnuParserTest {

    @Test
    public void testFlatten_basic() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-D", "value", "--", "-A", "arg"};
        String[] expected = {"-D", "value", "--", "-A", "arg"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-D", "value", "-A", "arg"};
        String[] expected = {"-D", "value"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_emptyArguments() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {};
        String[] expected = {};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_singleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-"};
        String[] expected = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_doubleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"--"};
        String[] expected = {"--"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}