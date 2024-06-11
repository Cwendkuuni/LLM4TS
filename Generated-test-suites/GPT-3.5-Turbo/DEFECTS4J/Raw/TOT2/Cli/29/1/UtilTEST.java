import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens_NullString() {
        String actual = Util.stripLeadingHyphens(null);
        assertEquals(null, actual);
    }
    
    @Test
    public void testStripLeadingHyphens_DoubleHyphen() {
        String actual = Util.stripLeadingHyphens("--test");
        assertEquals("test", actual);
    }
    
    @Test
    public void testStripLeadingHyphens_SingleHyphen() {
        String actual = Util.stripLeadingHyphens("-test");
        assertEquals("test", actual);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_Quotes() {
        String actual = Util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", actual);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_MissingTrailingQuote() {
        String actual = Util.stripLeadingAndTrailingQuotes("\"test");
        assertEquals("\"test", actual);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_MissingLeadingQuote() {
        String actual = Util.stripLeadingAndTrailingQuotes("test\"");
        assertEquals("test\"", actual);
    }
}