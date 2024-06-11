import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("h", "help", false, "Print help message");

        // Typical use case
        String[] arguments = {"-h", "file1", "file2"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(3, result.length);
        assertEquals("-h", result[0]);
        assertEquals("file1", result[1]);
        assertEquals("file2", result[2]);

        // Edge case: Empty arguments
        arguments = new String[]{};
        result = parser.flatten(options, arguments, false);
        assertEquals(0, result.length);

        // Edge case: Stop at non-option
        arguments = new String[]{"-h", "file1", "--", "-x"};
        result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertEquals("-h", result[0]);
        assertEquals("file1", result[1]);
        assertEquals("--", result[2]);
    }
}