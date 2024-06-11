import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens_nullString() {
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    @Test
    public void testStripLeadingHyphens_startsWithDoubleHyphen() {
        assertEquals("testString", Util.stripLeadingHyphens("--testString"));
    }
    
    @Test
    public void testStripLeadingHyphens_startsWithSingleHyphen() {
        assertEquals("testString", Util.stripLeadingHyphens("-testString"));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_startsWithQuotes() {
        assertEquals("testString", Util.stripLeadingAndTrailingQuotes("\"testString"));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_endsWithQuotes() {
        assertEquals("testString", Util.stripLeadingAndTrailingQuotes("testString\""));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_startAndEndWithQuotes() {
        assertEquals("testString", Util.stripLeadingAndTrailingQuotes("\"testString\""));
    }
}