
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class StringUtilTest {
    
    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        
        String expectedResult = "Hello World";
        String actualResult = StringUtil.join(strings, " ");
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        
        String expectedResult = "Hello World";
        
        Iterator<String> iterator = strings.iterator();
        String actualResult = StringUtil.join(iterator, " ");
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World"};
        
        String expectedResult = "Hello World";
        String actualResult = StringUtil.join(strings, " ");
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testPadding() {
        int width = 5;
        
        String expectedResult = "     ";
        String actualResult = StringUtil.padding(width);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsBlankNull() {
        String string = null;
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.isBlank(string);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsBlankEmpty() {
        String string = "";
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.isBlank(string);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsBlankWhitespace() {
        String string = "     ";
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.isBlank(string);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsBlankNotWhitespace() {
        String string = "Hello";
        
        boolean expectedResult = false;
        boolean actualResult = StringUtil.isBlank(string);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsNumericEmpty() {
        String string = "";
        
        boolean expectedResult = false;
        boolean actualResult = StringUtil.isNumeric(string);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsNumericDigits() {
        String string = "12345";
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.isNumeric(string);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsNumericNotDigits() {
        String string = "Hello";
        
        boolean expectedResult = false;
        boolean actualResult = StringUtil.isNumeric(string);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsWhitespaceSpace() {
        int c = ' ';
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.isWhitespace(c);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsWhitespaceTab() {
        int c = '\t';
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.isWhitespace(c);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsWhitespaceNewline() {
        int c = '\n';
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.isWhitespace(c);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsWhitespaceNotWhitespace() {
        int c = 'A';
        
        boolean expectedResult = false;
        boolean actualResult = StringUtil.isWhitespace(c);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsActuallyWhitespaceSpace() {
        int c = ' ';
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.isActuallyWhitespace(c);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsActuallyWhitespaceTab() {
        int c = '\t';
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.isActuallyWhitespace(c);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsActuallyWhitespaceNewline() {
        int c = '\n';
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.isActuallyWhitespace(c);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        int c = 160;
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.isActuallyWhitespace(c);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIsActuallyWhitespaceNotWhitespace() {
        int c = 'A';
        
        boolean expectedResult = false;
        boolean actualResult = StringUtil.isActuallyWhitespace(c);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String string = "Hello     World";
        
        String expectedResult = "Hello World";
        String actualResult = StringUtil.normaliseWhitespace(string);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testNormaliseWhitespaceWhitespaceCharacters() {
        String string = "Hello\tWorld\r\n";
        
        String expectedResult = "Hello World ";
        String actualResult = StringUtil.normaliseWhitespace(string);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testInPresent() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.in(needle, haystack);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testInNotPresent() {
        String needle = "Hello";
        String[] haystack = {"Foo", "Bar"};
        
        boolean expectedResult = false;
        boolean actualResult = StringUtil.in(needle, haystack);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testInSortedPresent() {
        String needle = "Hello";
        String[] haystack = {"Hello", "World"};
        
        boolean expectedResult = true;
        boolean actualResult = StringUtil.inSorted(needle, haystack);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testInSortedNotPresent() {
        String needle = "Hello";
        String[] haystack = {"Foo", "Bar"};
        
        boolean expectedResult = false;
        boolean actualResult = StringUtil.inSorted(needle, haystack);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com/");
        String relUrl = "subpage.html";
        
        URL expectedResult = new URL("http://www.example.com/subpage.html");
        URL actualResult = StringUtil.resolve(base, relUrl);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testResolveWithQuery() throws MalformedURLException {
        URL base = new URL("http://www.example.com/path/file?foo=bar");
        String relUrl = "?baz=qux";
        
        URL expectedResult = new URL("http://www.example.com/path/file?foo=bar&baz=qux");
        URL actualResult = StringUtil.resolve(base, relUrl);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testResolveWithRelativePath() throws MalformedURLException {
        URL base = new URL("http://www.example.com/path/");
        String relUrl = "./file.html";
        
        URL expectedResult = new URL("http://www.example.com/path/file.html");
        URL actualResult = StringUtil.resolve(base, relUrl);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testResolveWithAbsolutePath() {
        String baseUrl = "http://www.example.com/path/";
        String relUrl = "http://www.example.com/otherpath/file.html";
        
        String expectedResult = "http://www.example.com/otherpath/file.html";
        String actualResult = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testResolveMalformedUrl() {
        String baseUrl = "http://www.example.com/path/";
        String relUrl = "not-a-url";
        
        String expectedResult = "";
        String actualResult = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        
        assertNotSame(sb1, sb2);
    }
    
}
