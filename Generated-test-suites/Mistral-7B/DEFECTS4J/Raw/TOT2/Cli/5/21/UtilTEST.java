import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--string";
        String expectedOutput = "string";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    // Test cases for edge cases and exceptions
}