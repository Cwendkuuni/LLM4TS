import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String testStr1 = "--test";
        String testStr2 = "-test";
        String testStr3 = "test";
        
        assertEquals("test", Util.stripLeadingHyphens(testStr1));
        assertEquals("test", Util.stripLeadingHyphens(testStr2));
        assertEquals("test", Util.stripLeadingHyphens(testStr3));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String testStr1 = "\"test\"";
        String testStr2 = "test";
        
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(testStr1));
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(testStr2));
    }
}