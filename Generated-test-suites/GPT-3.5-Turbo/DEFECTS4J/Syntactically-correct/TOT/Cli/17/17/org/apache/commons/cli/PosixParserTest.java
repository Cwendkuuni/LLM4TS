import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
    }

    @Test
    public void testFlatten_noArguments() {
        String[] args = new String[0];
        String[] result = parser.flatten(options, args, false);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_singleHyphens() {
        String[] args = new String[]{"-", "-", "a", "--"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(4, result.length);
        assertEquals("-", result[0]);
        assertEquals("-", result[1]);
        assertEquals("a", result[2]);
        assertEquals("--", result[3]);
    }

    @Test
    public void testFlatten_singleDashWithOption() {
        String[] args = new String[]{"-a"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_singleDashWithOptionsAndArg() {
        String[] args = new String[]{"-b", "value"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_singleDashWithoutOption() {
        String[] args = new String[]{"-c"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(1, result.length);
        assertEquals("-c", result[0]);
    }

    @Test
    public void testFlatten_multipleDashesWithOption() {
        String[] args = new String[]{"--a"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--a", result[0]);
    }

    @Test
    public void testFlatten_multipleDashesWithOptionsAndArg() {
        String[] args = new String[]{"--b", "value"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--b", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_multipleDashesWithoutOption() {
        String[] args = new String[]{"--c"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(1, result.length);
        assertEquals("--c", result[0]);
    }

    @Test
    public void testFlatten_multipleDashesWithOptionAndEqualSign() {
        String[] args = new String[]{"--b=value"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--b", result[0]);
    }

    @Test
    public void testFlatten_multipleDashesWithOptionsAndArgAndEqualSign() {
        String[] args = new String[]{"--b=value1", "--b=value2"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--b", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        String[] args = new String[]{"-a", "nonOption", "-b", "value"};
        String[] result = parser.flatten(options, args, true);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("nonOption", result[1]);
        assertEquals("-b", result[2]);
    }

    @Test
    public void testFlatten_skipNonOption() {
        String[] args = new String[]{"-a", "nonOption", "-b", "value"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("nonOption", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value", result[3]);
    }

    @Test
    public void testFlatten_burstToken() {
        String[] args = new String[]{"-abc"};
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

}