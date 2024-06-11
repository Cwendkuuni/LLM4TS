import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullInput_ReturnNull() {
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingHyphens_WithDoubleHyphens_ReturnStringWithoutHyphens() {
        String input = "--example";
        assertEquals("example", Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingHyphens_WithSingleHyphen_ReturnStringWithoutHyphen() {
        String input = "-example";
        assertEquals("example", Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithQuotes_ReturnStringWithoutQuotes() {
        String input = "\"example\"";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithoutQuotes_ReturnSameString() {
        String input = "example";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(input));
    }
}