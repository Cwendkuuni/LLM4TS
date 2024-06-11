import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // test case for when str starts with two hyphens
        String str1 = "--example";
        String expected1 = "example";
        String result1 = Util.stripLeadingHyphens(str1);
        assertEquals(expected1, result1);
        
        // test case for when str starts with one hyphen
        String str2 = "-example";
        String expected2 = "example";
        String result2 = Util.stripLeadingHyphens(str2);
        assertEquals(expected2, result2);
        
        // test case for when str does not start with hyphen
        String str3 = "example";
        String expected3 = "example";
        String result3 = Util.stripLeadingHyphens(str3);
        assertEquals(expected3, result3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // test case for when str starts and ends with quotes
        String str1 = "\"example\"";
        String expected1 = "example";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        assertEquals(expected1, result1);
        
        // test case for when str starts with quote and does not end with quote
        String str2 = "\"example";
        String expected2 = "\"example";
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        assertEquals(expected2, result2);
        
        // test case for when str ends with quote and does not start with quote
        String str3 = "example\"";
        String expected3 = "example\"";
        String result3 = Util.stripLeadingAndTrailingQuotes(str3);
        assertEquals(expected3, result3);
        
        // test case for when str does not start or end with quotes
        String str4 = "example";
        String expected4 = "example";
        String result4 = Util.stripLeadingAndTrailingQuotes(str4);
        assertEquals(expected4, result4);
    }

}