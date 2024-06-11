
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;
    private Options options;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "value", "-b", "--", "-c"};
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", false, "option c");

        String[] result = posixParser.flatten(options, args, true);

        String[] expected = {"-a", "value", "-b"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_DontStopAtNonOption() {
        String[] args = {"-a", "value", "-b", "--", "-c"};
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", false, "option c");

        String[] result = posixParser.flatten(options, args, false);

        String[] expected = {"-a", "value", "-b", "--", "-c"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_HandlerSingleHyphen() {
        String[] args = {"-"};
        options.addOption("a", false, "option a");

        String[] result = posixParser.flatten(options, args, false);

        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_HandlerOptionToken() {
        String[] args = {"-a"};
        options.addOption("a", false, "option a");

        String[] result = posixParser.flatten(options, args, false);

        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_HandlerBurstToken() {
        String[] args = {"-abc"};
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", false, "option c");

        String[] result = posixParser.flatten(options, args, false);

        String[] expected = {"-a", "-b", "-c"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_HandlerNoOption() {
        String[] args = {"value"};
        options.addOption("a", false, "option a");

        String[] result = posixParser.flatten(options, args, false);

        String[] expected = {"value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_HandlerNoOption_StopAtNonOption() {
        String[] args = {"value"};
        options.addOption("a", false, "option a");

        String[] result = posixParser.flatten(options, args, true);

        String[] expected = {"--", "value"};
        assertArrayEquals(expected, result);
    }
}
