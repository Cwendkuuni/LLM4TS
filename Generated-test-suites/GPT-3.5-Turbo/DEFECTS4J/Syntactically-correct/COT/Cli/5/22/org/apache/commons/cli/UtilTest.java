
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test when str starts with two hyphens
        String str1 = "--testStr";
        String result1 = Util.stripLeadingHyphens(str1);
        assertEquals("testStr", result1);

        // Test when str starts with one hyphen
        String str2 = "-testStr";
        String result2 = Util.stripLeadingHyphens(str2);
        assertEquals("testStr", result2);

        // Test when str does not start with hyphen
        String str3 = "testStr";
        String result3 = Util.stripLeadingHyphens(str3);
        assertEquals("testStr", result3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test when str starts and ends with quotes
        String str1 = "\"testStr\"";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        assertEquals("testStr", result1);

        // Test when str starts with quote
        String str2 = "\"testStr";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        assertEquals("testStr", result2);

        // Test when str ends with quote
        String str3 = "testStr\"";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        assertEquals("testStr", result3);

        // Test when str does not start or end with quotes
        String str4 = "testStr";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        assertEquals("testStr", result4);
    }
}

