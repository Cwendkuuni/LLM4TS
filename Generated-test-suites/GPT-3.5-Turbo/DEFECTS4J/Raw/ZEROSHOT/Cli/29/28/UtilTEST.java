import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens_NullString_ReturnNull() {
        String result = Util.stripLeadingHyphens(null);
        assertEquals(null, result);
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphens_ReturnSubstring() {
        String result = Util.stripLeadingHyphens("--test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen_ReturnSubstring() {
        String result = Util.stripLeadingHyphens("-test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_NoHyphen_ReturnString() {
        String result = Util.stripLeadingHyphens("test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithQuotes_ReturnSubstring() {
        String result = Util.stripLeadingAndTrailingQuotes("\"one two\"");
        assertEquals("one two", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithoutQuotes_ReturnString() {
        String result = Util.stripLeadingAndTrailingQuotes("one two");
        assertEquals("one two", result);
    }
}