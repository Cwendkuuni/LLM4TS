import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    // Test case for flatten method
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        GnuParser parser = new GnuParser();

        // Test case 1: No arguments
        String[] arguments1 = {};
        String[] result1 = parser.flatten(options, arguments1, true);
        assertEquals(0, result1.length);

        // Test case 2: Only options
        String[] arguments2 = {"-a", "-b", "value"};
        String[] result2 = parser.flatten(options, arguments2, true);
        assertEquals(3, result2.length);
        assertEquals("-a", result2[0]);
        assertEquals("-b", result2[1]);
        assertEquals("value", result2[2]);

        // Test case 3: Options and non-options
        String[] arguments3 = {"-a", "file1", "file2"};
        String[] result3 = parser.flatten(options, arguments3, true);
        assertEquals(3, result3.length);
        assertEquals("-a", result3[0]);
        assertEquals("file1", result3[1]);
        assertEquals("file2", result3[2]);

        // Test case 4: Stop at non-option
        String[] arguments4 = {"-a", "file1", "-b", "value", "file2"};
        String[] result4 = parser.flatten(options, arguments4, false);
        assertEquals(3, result4.length);
        assertEquals("-a", result4[0]);
        assertEquals("file1", result4[1]);
        assertEquals("-b", result4[2]);
    }

}