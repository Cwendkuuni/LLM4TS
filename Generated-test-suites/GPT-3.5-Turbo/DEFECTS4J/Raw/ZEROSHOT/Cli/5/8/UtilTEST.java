import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String hyphenated = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(hyphenated);
        assertEquals(expected, result);
        
        String dashSeparated = "-test";
        result = Util.stripLeadingHyphens(dashSeparated);
        assertEquals(expected, result);
        
        String noHyphen = "test";
        result = Util.stripLeadingHyphens(noHyphen);
        assertEquals(noHyphen, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String quoted = "\"test\"";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(quoted);
        assertEquals(expected, result);
        
        String noQuotes = "test";
        result = Util.stripLeadingAndTrailingQuotes(noQuotes);
        assertEquals(noQuotes, result);
        
        String startQuote = "\"test";
        result = Util.stripLeadingAndTrailingQuotes(startQuote);
        assertEquals(startQuote, result);
        
        String endQuote = "test\"";
        result = Util.stripLeadingAndTrailingQuotes(endQuote);
        assertEquals(endQuote, result);
    }
}