
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class StringUtilTest {
    
    @Test
    public void testJoinCollection() {
        Collection<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");
        String result = StringUtil.join(strings, " ");
        assertEquals("Hello World !", result);
    }
    
    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");
        Iterator<String> iterator = strings.iterator();
        String result = StringUtil.join(iterator, " ");
        assertEquals("Hello World !", result);
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        String result = StringUtil.join(strings, " ");
        assertEquals("Hello World !", result);
    }
    
    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }
    
    @Test
    public void testIsBlank() {
        boolean result1 = StringUtil.isBlank(null);
        assertTrue(result1);
        
        boolean result2 = StringUtil.isBlank("");
        assertTrue(result2);
        
        boolean result3 = StringUtil.isBlank(" ");
        assertTrue(result3);
        
        boolean result4 = StringUtil.isBlank("Hello");
        assertFalse(result4);
    }
    
    @Test
    public void testIsNumeric() {
        boolean result1 = StringUtil.isNumeric(null);
        assertFalse(result1);
        
        boolean result2 = StringUtil.isNumeric("");
        assertFalse(result2);
        
        boolean result3 = StringUtil.isNumeric("123");
        assertTrue(result3);
        
        boolean result4 = StringUtil.isNumeric("abc");
        assertFalse(result4);
    }
    
    @Test
    public void testIsWhitespace() {
        boolean result1 = StringUtil.isWhitespace(' ');
        assertTrue(result1);
        
        boolean result2 = StringUtil.isWhitespace('\t');
        assertTrue(result2);
        
        boolean result3 = StringUtil.isWhitespace('\n');
        assertTrue(result3);
        
        boolean result4 = StringUtil.isWhitespace('\f');
        assertTrue(result4);
        
        boolean result5 = StringUtil.isWhitespace('\r');
        assertTrue(result5);
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        boolean result1 = StringUtil.isActuallyWhitespace(' ');
        assertTrue(result1);
        
        boolean result2 = StringUtil.isActuallyWhitespace('\t');
        assertTrue(result2);
        
        boolean result3 = StringUtil.isActuallyWhitespace('\n');
        assertTrue(result3);
        
        boolean result4 = StringUtil.isActuallyWhitespace('\f');
        assertTrue(result4);
        
        boolean result5 = StringUtil.isActuallyWhitespace('\r');
        assertTrue(result5);
        
        boolean result6 = StringUtil.isActuallyWhitespace((int) 160);
        assertTrue(result6);
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String string = "Hello  World!\nHow\tare\t    you?";
        String result = StringUtil.normaliseWhitespace(string);
        assertEquals("Hello World! How are you?", result);
    }
    
    @Test
    public void testIn() {
        String needle = "World";
        String[] haystack = {"Hello", "World", "!"};
        boolean result1 = StringUtil.in(needle, haystack);
        assertTrue(result1);
        
        String needle2 = "Goodbye";
        boolean result2 = StringUtil.in(needle2, haystack);
        assertFalse(result2);
    }
    
    @Test
    public void testInSorted() {
        String needle = "Def";
        String[] haystack = {"Abc", "Def", "Ghi"};
        boolean result1 = StringUtil.inSorted(needle, haystack);
        assertTrue(result1);
        
        String needle2 = "Klm";
        boolean result2 = StringUtil.inSorted(needle2, haystack);
        assertFalse(result2);
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "/page.html";
        URL result = StringUtil.resolve(base, relUrl);
        assertEquals("http://www.example.com/page.html", result.toString());
    }
    
    @Test
    public void testResolveInvalid() {
        String baseUrl = "www.example.com";
        String relUrl = "/page.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals("", result);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);
        sb.append("Hello");
        assertEquals("Hello", sb.toString());
    }
}
