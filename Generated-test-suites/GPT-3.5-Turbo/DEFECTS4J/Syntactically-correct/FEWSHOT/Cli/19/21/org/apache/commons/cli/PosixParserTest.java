import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = new String[]{"-a"};
        String[] expected = new String[]{"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"--a"};
        expected = new String[]{"--a"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-ab"};
        expected = new String[]{"-a", "-b"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-ab", "-c"};
        expected = new String[]{"-a", "-b", "-c"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-abc"};
        expected = new String[]{"-a", "-b", "-c"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-abc", "-d"};
        expected = new String[]{"-a", "-b", "-c", "-d"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-abc", "--d"};
        expected = new String[]{"-a", "-b", "-c", "--d"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-ab", "--c"};
        expected = new String[]{"-a", "-b", "--c"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-a", "--b"};
        expected = new String[]{"-a", "--b"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-a", "--b", "value"};
        expected = new String[]{"-a", "--b", "value"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-a", "value", "--b"};
        expected = new String[]{"-a", "value", "--b"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-a", "--", "value"};
        expected = new String[]{"-a", "--", "value"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-a", "--", "value", "other"};
        expected = new String[]{"-a", "--", "value", "other"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);

        arguments = new String[]{"-a", "--", "value", "other", "-b"};
        expected = new String[]{"-a", "--", "value", "other", "-b"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}