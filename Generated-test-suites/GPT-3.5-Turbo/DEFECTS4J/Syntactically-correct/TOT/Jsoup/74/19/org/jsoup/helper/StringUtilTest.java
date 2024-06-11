import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("hello", "world");
        String sep = ",";
        String expectedResult = "hello,world";
        
        String result = StringUtil.join(strings, sep);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("hello", "world").iterator();
        String sep = ",";
        String expectedResult = "hello,world";
        
        String result = StringUtil.join(strings, sep);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world"};
        String sep = ",";
        String expectedResult = "hello,world";
        
        String result = StringUtil.join(strings, sep);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testPadding() {
        int width = 5;
        String expectedResult = "     ";
        
        String result = StringUtil.padding(width);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsBlank_NullString() {
        String string = null;
        boolean expectedResult = true;
        
        boolean result = StringUtil.isBlank(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsBlank_EmptyString() {
        String string = "";
        boolean expectedResult = true;
        
        boolean result = StringUtil.isBlank(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsBlank_WhitespaceString() {
        String string = "     ";
        boolean expectedResult = true;
        
        boolean result = StringUtil.isBlank(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsBlank_ValidString() {
        String string = "hello";
        boolean expectedResult = false;
        
        boolean result = StringUtil.isBlank(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsNumeric_NullString() {
        String string = null;
        boolean expectedResult = false;
        
        boolean result = StringUtil.isNumeric(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsNumeric_EmptyString() {
        String string = "";
        boolean expectedResult = false;
        
        boolean result = StringUtil.isNumeric(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsNumeric_NumberString() {
        String string = "123";
        boolean expectedResult = true;
        
        boolean result = StringUtil.isNumeric(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsNumeric_NonNumberString() {
        String string = "abc";
        boolean expectedResult = false;
        
        boolean result = StringUtil.isNumeric(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWhitespace_WhitespaceCharacter() {
        int c = ' ';
        boolean expectedResult = true;
        
        boolean result = StringUtil.isWhitespace(c);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWhitespace_NonWhitespaceCharacter() {
        int c = 'a';
        boolean expectedResult = false;
        
        boolean result = StringUtil.isWhitespace(c);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsActuallyWhitespace_WhitespaceCharacter() {
        int c = ' ';
        boolean expectedResult = true;
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsActuallyWhitespace_NonWhitespaceCharacter() {
        int c = 'a';
        boolean expectedResult = false;
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String string = "   hello    world  ";
        String expectedResult = " hello world ";
        
        String result = StringUtil.normaliseWhitespace(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testAppendNormalisedWhitespace_StripLeadingTrue() {
        StringBuilder accum = new StringBuilder();
        String string = "   hello    world  ";
        boolean stripLeading = true;
        String expectedResult = "hello world ";
        
        StringUtil.appendNormalisedWhitespace(accum, string, stripLeading);
        
        assertEquals(expectedResult, accum.toString());
    }
    
    @Test
    public void testAppendNormalisedWhitespace_StripLeadingFalse() {
        StringBuilder accum = new StringBuilder();
        String string = "   hello    world  ";
        boolean stripLeading = false;
        String expectedResult = " hello world ";
        
        StringUtil.appendNormalisedWhitespace(accum, string, stripLeading);
        
        assertEquals(expectedResult, accum.toString());
    }
    
    @Test
    public void testIn_NeedleExistsInHaystack() {
        String needle = "hello";
        String[] haystack = {"hello", "world"};
        boolean expectedResult = true;
        
        boolean result = StringUtil.in(needle, haystack);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIn_NeedleDoesNotExistInHaystack() {
        String needle = "abc";
        String[] haystack = {"hello", "world"};
        boolean expectedResult = false;
        
        boolean result = StringUtil.in(needle, haystack);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testInSorted_NeedleExistsInHaystack() {
        String needle = "hello";
        String[] haystack = {"apple", "banana", "hello", "world"};
        boolean expectedResult = true;
        
        boolean result = StringUtil.inSorted(needle, haystack);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testInSorted_NeedleDoesNotExistInHaystack() {
        String needle = "abc";
        String[] haystack = {"apple", "banana", "hello", "world"};
        boolean expectedResult = false;
        
        boolean result = StringUtil.inSorted(needle, haystack);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testResolve_RelativeUrlStartsWithQuestionMark() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "?foo";
        String expectedResult = "http://example.com?foo";
        
        URL result = StringUtil.resolve(base, relUrl);
        
        assertEquals(expectedResult, result.toExternalForm());
    }
    
    @Test
    public void testResolve_RelativeUrlStartsWithPeriodAndBaseFileDoesNotStartWithSlash() throws MalformedURLException {
        URL base = new URL("http://example.com/page");
        String relUrl = "./foo";
        String expectedResult = "http://example.com/./foo";
        
        URL result = StringUtil.resolve(base, relUrl);
        
        assertEquals(expectedResult, result.toExternalForm());
    }
    
    @Test
    public void testResolve_ValidRelativeUrl() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String relUrl = "/page";
        String expectedResult = "http://example.com/page";
        
        URL result = StringUtil.resolve(base, relUrl);
        
        assertEquals(expectedResult, result.toExternalForm());
    }
    
    @Test
    public void testResolve_InvalidRelativeUrl() {
        String baseUrl = "http://example.com";
        String relUrl = ":page";
        String expectedResult = "";
        
        String result = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("test");
        
        StringBuilder result = StringUtil.stringBuilder();
        result.append("test");
        
        assertEquals(expectedResult.toString(), result.toString());
    }
}