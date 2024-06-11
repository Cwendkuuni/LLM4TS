import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;
    private Options options;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b with argument");
    }

    @Test
    public void testFlatten_StopAtNonOption_True() {
        String[] arguments = {"--", "-a", "-b", "value", "non-option"};
        String[] flattened = posixParser.flatten(options, arguments, true);
        String[] expected = {"--", "-a", "-b", "value", "non-option"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        String[] arguments = {"-abvalue", "non-option"};
        String[] flattened = posixParser.flatten(options, arguments, false);
        String[] expected = {"-a", "-b", "value", "non-option"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlatten_WithEqualsSign() {
        String[] arguments = {"--option=value"};
        String[] flattened = posixParser.flatten(options, arguments, false);
        String[] expected = {"--option", "value"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};
        String[] flattened = posixParser.flatten(options, arguments, false);
        String[] expected = {"-"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlatten_InvalidOption() {
        String[] arguments = {"-c"};
        String[] flattened = posixParser.flatten(options, arguments, false);
        String[] expected = {"-c"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOption_True() {
        String[] arguments = {"-c", "non-option"};
        String[] flattened = posixParser.flatten(options, arguments, true);
        String[] expected = {"-c", "non-option"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlatten_BurstToken() {
        String[] arguments = {"-abcvalue"};
        String[] flattened = posixParser.flatten(options, arguments, false);
        String[] expected = {"-a", "-b", "-c", "value"};
        assertArrayEquals(expected, flattened);
    }

}