import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;
    private String[] arguments;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        arguments = new String[0];
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        options.addOption("a", false, "Option a");

        arguments = new String[]{"-a", "value"};

        String[] result = parser.flatten(options, arguments, true);
        String[] expected = new String[]{"-a", "value"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        options.addOption("a", false, "Option a");

        arguments = new String[]{"-a", "value"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-a", "value"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithSpecialToken() {
        options.addOption("a", true, "Option a");
        options.addOption("b", false, "Option b");

        arguments = new String[]{"--a=value1", "--b"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"--a", "value1", "--b"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        options.addOption("a", false, "Option a");

        arguments = new String[]{"-"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOptionToken() {
        options.addOption("a", true, "Option a");
        options.addOption("b", false, "Option b");

        arguments = new String[]{"-a", "value1"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-a", "value1"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOptionToken_StopAtNonOptionTrue() {
        options.addOption("a", false, "Option a");

        arguments = new String[]{"-b", "value"};

        String[] result = parser.flatten(options, arguments, true);
        String[] expected = new String[]{"-b", "value"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_InvalidOptionToken_StopAtNonOptionFalse() {
        options.addOption("a", false, "Option a");

        arguments = new String[]{"-b", "value"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-b", "value"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOptionTrue() {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        arguments = new String[]{"-ab", "value"};

        String[] result = parser.flatten(options, arguments, true);
        String[] expected = new String[]{"-a", "-b", "value"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_BurstToken_StopAtNonOptionFalse() {
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        arguments = new String[]{"-ab", "value"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-a", "-b", "value"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithNonOptionToken_StopAtNonOptionTrue() {
        options.addOption("a", false, "Option a");

        arguments = new String[]{"-a", "value1", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);
        String[] expected = new String[]{"-a", "value1"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithNonOptionToken_StopAtNonOptionFalse() {
        options.addOption("a", false, "Option a");

        arguments = new String[]{"-a", "value1", "nonOption"};

        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-a", "value1", "nonOption"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue_MultipleNonOptions() {
        options.addOption("a", false, "Option a");

        arguments = new String[]{"-a", "value1", "nonOption1", "nonOption2"};

        String[] result = parser.flatten(options, arguments, true);
        String[] expected = new String[]{"-a", "value1"};

        assertArrayEquals(expected, result);
    }
}