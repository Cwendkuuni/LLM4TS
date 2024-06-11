import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        // Test 1
        String[] arguments1 = {"-f", "file.txt", "-d", "path"};
        String[] expected1 = {"-f", "file.txt", "-d", "path"};
        String[] result1 = parser.flatten(options, arguments1, false);
        assertArrayEquals(expected1, result1);

        // Test 2
        String[] arguments2 = {"--verbose", "-o", "output.txt", "input.txt"};
        String[] expected2 = {"--verbose", "-o", "output.txt", "input.txt"};
        String[] result2 = parser.flatten(options, arguments2, false);
        assertArrayEquals(expected2, result2);

        // Test 3
        String[] arguments3 = {"-Dproperty=value", "--option"};
        String[] expected3 = {"-D", "property=value", "--option"};
        String[] result3 = parser.flatten(options, arguments3, false);
        assertArrayEquals(expected3, result3);

        // Test 4
        String[] arguments4 = {"--", "-f", "file.txt"};
        String[] expected4 = {"--", "-f", "file.txt"};
        String[] result4 = parser.flatten(options, arguments4, false);
        assertArrayEquals(expected4, result4);

        // Test 5
        String[] arguments5 = {"-f", "-o", "-d"};
        String[] expected5 = {"-f", "-o", "-d"};
        String[] result5 = parser.flatten(options, arguments5, true);
        assertArrayEquals(expected5, result5);
    }
}