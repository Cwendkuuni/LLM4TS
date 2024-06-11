import org.junit.Test;
import static org.junit.Assert.*;



public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullString_ReturnsNull() {
        String result = Util.stripLeadingHyphens(null);
        assertNull(result);
    }

    @Test
    public void testStripLeadingHyphens_WithDoubleHyphens_ReturnsSubstringWithoutHyphens() {
        String result = Util.stripLeadingHyphens("--test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_WithSingleHyphen_ReturnsSubstringWithoutHyphen() {
        String result = Util.stripLeadingHyphens("-test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_WithoutHyphens_ReturnsStringUnchanged() {
        String result = Util.stripLeadingHyphens("test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithLeadingAndTrailingQuotes_ReturnsStringWithoutQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithLeadingQuotes_ReturnsStringWithoutQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithTrailingQuotes_ReturnsStringWithoutQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("test\"");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithoutQuotes_ReturnsStringUnchanged() {
        String result = Util.stripLeadingAndTrailingQuotes("test");
        assertEquals("test", result);
    }

}

