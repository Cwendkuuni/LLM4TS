import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-a", "--", "-b"};
        String[] expected = {"-a", "--", "-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNoStopAtNonOption() {
        String[] arguments = {"-a", "--", "-b"};
        String[] expected = {"-a", "--", "-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] arguments = {"-"};
        String[] expected = {"-"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionExists() {
        options.addOption("a", false, "Option a");
        String[] arguments = {"-a"};
        String[] expected = {"-a"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionDoesNotExistStopAtNonOption() {
        String[] arguments = {"-a"};
        String[] expected = {"--", "-a"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionDoesNotExistNoStopAtNonOption() {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBurstToken() {
        options.addOption("a", false, "Option a");
        String[] arguments = {"-ab"};
        String[] expected = {"-a", "-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        org.junit.Assert.assertArrayEquals(expected, actual);
    }
}