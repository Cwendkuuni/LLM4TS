import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("Test 1", "abcd", Util.stripLeadingHyphens("---abcd"));
        assertEquals("Test 2", "efgh", Util.stripLeadingHyphens("-efgh"));
        assertEquals("Test 3", "ijkl", Util.stripLeadingHyphens("ijkl"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("Test 1", "one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("Test 2", "three four", Util.stripLeadingAndTrailingQuotes("\"three four\""));
        assertEquals("Test 3", "five six", Util.stripLeadingAndTrailingQuotes("five six"));
    }
}