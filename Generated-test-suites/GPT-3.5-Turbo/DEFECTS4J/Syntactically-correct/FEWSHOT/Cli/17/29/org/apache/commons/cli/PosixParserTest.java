import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");
        options.addOption("d", "directory", false, "Directory option");

        PosixParser parser = new PosixParser();

        String[] arguments = { "-f", "test.txt", "-d" };
        String[] expected = { "-f", "test.txt", "-d" };
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);

        String[] arguments2 = { "-f", "test.txt", "-d", "dir" };
        String[] expected2 = { "-f", "test.txt", "-d", "dir" };
        String[] result2 = parser.flatten(options, arguments2, true);
        assertArrayEquals(expected2, result2);

        String[] arguments3 = { "-f", "test.txt", "--directory", "dir" };
        String[] expected3 = { "-f", "test.txt", "--directory", "dir" };
        String[] result3 = parser.flatten(options, arguments3, true);
        assertArrayEquals(expected3, result3);

        String[] arguments4 = { "-f", "test.txt", "unknown", "--directory", "dir" };
        String[] expected4 = { "-f", "test.txt", "--", "unknown", "--directory", "dir" };
        String[] result4 = parser.flatten(options, arguments4, false);
        assertArrayEquals(expected4, result4);

        String[] arguments5 = { "-f", "test.txt", "--", "unknown", "--directory", "dir" };
        String[] expected5 = { "-f", "test.txt", "--", "unknown", "--directory", "dir" };
        String[] result5 = parser.flatten(options, arguments5, false);
        assertArrayEquals(expected5, result5);

        String[] arguments6 = { "-unknown", "value" };
        String[] expected6 = { "-unknown", "value" };
        String[] result6 = parser.flatten(options, arguments6, true);
        assertArrayEquals(expected6, result6);

        String[] arguments7 = { "--unknown", "value" };
        String[] expected7 = { "--unknown", "value" };
        String[] result7 = parser.flatten(options, arguments7, true);
        assertArrayEquals(expected7, result7);

        String[] arguments8 = { "-f", "test.txt", "-d", "dir", "extra" };
        String[] expected8 = { "-f", "test.txt", "-d", "dir", "extra" };
        String[] result8 = parser.flatten(options, arguments8, true);
        assertArrayEquals(expected8, result8);

        String[] arguments9 = { "-f", "test.txt", "-unknown" };
        String[] expected9 = { "-f", "test.txt", "-unknown" };
        String[] result9 = parser.flatten(options, arguments9, true);
        assertArrayEquals(expected9, result9);

        String[] arguments10 = { "-f", "test.txt", "--directory", "--unknown" };
        String[] expected10 = { "-f", "test.txt", "--directory", "--", "--unknown" };
        String[] result10 = parser.flatten(options, arguments10, false);
        assertArrayEquals(expected10, result10);
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}