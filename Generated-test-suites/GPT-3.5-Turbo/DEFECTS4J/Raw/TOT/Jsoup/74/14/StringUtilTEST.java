import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");
        String sep = " ";
        String expectedResult = "Hello World !";
        
        String result = StringUtil.join(strings, sep);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testJoinIterator() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Hello");
        stringList.add("World");
        stringList.add("!");
        Iterator<String> strings = stringList.iterator();
        String sep = " ";
        String expectedResult = "Hello World !";
        
        String result = StringUtil.join(strings, sep);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        String sep = " ";
        String expectedResult = "Hello World !";
        
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
    public void testIsBlank() {
        String string = "   ";
        boolean expectedResult = true;
        
        boolean result = StringUtil.isBlank(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsNumeric() {
        String string = "12345";
        boolean expectedResult = true;
        
        boolean result = StringUtil.isNumeric(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsWhitespace() {
        int c = ' ';
        boolean expectedResult = true;
        
        boolean result = StringUtil.isWhitespace(c);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        int c = 160;
        boolean expectedResult = true;
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String string = "Hello    World!";
        String expectedResult = "Hello World!";
        
        String result = StringUtil.normaliseWhitespace(string);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testIn() {
        String needle = "World";
        String[] haystack = {"Hello", "World", "!"};
        boolean expectedResult = true;
        
        boolean result = StringUtil.in(needle, haystack);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testInSorted() {
        String needle = "World";
        String[] haystack = {"!", "Hello", "World"};
        boolean expectedResult = true;
        
        boolean result = StringUtil.inSorted(needle, haystack);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testResolve() {
        try {
            URL base = new URL("http://example.com");
            String relUrl = "../page.html";
            String expectedResult = "http://example.com/page.html";
            
            URL result = StringUtil.resolve(base, relUrl);
            
            assertEquals(expectedResult, result.toString());
        } catch (MalformedURLException e) {
            fail();
        }
    }
    
    @Test
    public void testResolveInvalid() {
        String baseUrl = "http://example.com";
        String relUrl = "page.html";
        String expectedResult = "";
        
        String result = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testStringBuilder() {
        int maxCachedBuilderSize = 8 * 1024;
        StringBuilder expectedResult = new StringBuilder(maxCachedBuilderSize);
        
        StringBuilder result = StringUtil.stringBuilder();
        
        assertEquals(expectedResult.capacity(), result.capacity());
    }
    
    
}