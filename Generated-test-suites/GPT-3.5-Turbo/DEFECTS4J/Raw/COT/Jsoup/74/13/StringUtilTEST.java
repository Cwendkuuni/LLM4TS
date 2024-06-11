
import static org.junit.Assert.*;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {
    
    @Test
    public void testJoinWithCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("hello");
        strings.add("world");
        strings.add("!");

        String joinedString = StringUtil.join(strings, ", ");

        assertEquals("hello, world, !", joinedString);
    }
    
    @Test
    public void testJoinWithIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("hello");
        strings.add("world");
        strings.add("!");

        Iterator<String> iterator = strings.iterator();

        String joinedString = StringUtil.join(iterator, ", ");

        assertEquals("hello, world, !", joinedString);
    }
    
    @Test
    public void testJoinWithArray() {
        String[] strings = {"hello", "world", "!"};

        String joinedString = StringUtil.join(strings, ", ");

        assertEquals("hello, world, !", joinedString);
    }
    
    @Test
    public void testPadding() {
        String padding = StringUtil.padding(5);

        assertEquals("     ", padding);
    }
    
    @Test
    public void testIsBlankWithBlankString() {
        assertTrue(StringUtil.isBlank("   "));
    }
    
    @Test
    public void testIsBlankWithNullString() {
        assertTrue(StringUtil.isBlank(null));
    }
    
    @Test
    public void testIsBlankWithNonBlankString() {
        assertFalse(StringUtil.isBlank("hello world"));
    }
    
    @Test
    public void testIsNumericWithNumericString() {
        assertTrue(StringUtil.isNumeric("12345"));
    }
    
    @Test
    public void testIsNumericWithNonNumericString() {
        assertFalse(StringUtil.isNumeric("hello123"));
    }
    
    @Test
    public void testIsNumericWithEmptyString() {
        assertFalse(StringUtil.isNumeric(""));
    }
    
    @Test
    public void testIsNumericWithNullString() {
        assertFalse(StringUtil.isNumeric(null));
    }
    
    @Test
    public void testIsWhitespaceWithWhitespaceCharacter() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
    }
    
    @Test
    public void testIsWhitespaceWithNonWhitespaceCharacter() {
        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('1'));
        assertFalse(StringUtil.isWhitespace('@'));
    }
    
    @Test
    public void testIsActuallyWhitespaceWithWhitespaceCharacter() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace('\u00A0'));
    }
    
    @Test
    public void testIsActuallyWhitespaceWithNonWhitespaceCharacter() {
        assertFalse(StringUtil.isActuallyWhitespace('a'));
        assertFalse(StringUtil.isActuallyWhitespace('1'));
        assertFalse(StringUtil.isActuallyWhitespace('@'));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String input = "   hello\n\nworld!   ";
        String normalized = StringUtil.normaliseWhitespace(input);
        
        assertEquals(" hello world! ", normalized);
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "   hello\n\nworld!   ", true);
        
        assertEquals(" hello world! ", sb.toString());
    }
    
    @Test
    public void testInWithMatch() {
        assertTrue(StringUtil.in("hello", "hello", "world", "!"));
    }
    
    @Test
    public void testInWithNoMatch() {
        assertFalse(StringUtil.in("foo", "hello", "world", "!"));
    }
    
    @Test
    public void testInSortedWithMatch() {
        String[] haystack = {"!", "hello", "world"};
        assertTrue(StringUtil.inSorted("hello", haystack));
    }
    
    @Test
    public void testInSortedWithNoMatch() {
        String[] haystack = {"!", "hello", "world"};
        assertFalse(StringUtil.inSorted("foo", haystack));
    }
    
    @Test
    public void testResolveWithBaseUrlAndRelativeUrl() throws MalformedURLException {
        URL baseUrl = new URL("http://www.example.com");
        String relUrl = "./foo/bar.html";
        
        URL resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals("http://www.example.com/foo/bar.html", resolvedUrl.toString());
    }
    
    @Test
    public void testResolveWithInvalidBaseUrl() {
        String baseUrl = "not-a-url";
        String relUrl = "./foo/bar.html";
        
        String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals("", resolvedUrl);
    }
    
    @Test
    public void testResolveWithAbsoluteRelUrl() {
        String baseUrl = "http://www.example.com";
        String relUrl = "http://www.example2.com/foo/bar.html";
        
        String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
        
        assertEquals("http://www.example2.com/foo/bar.html", resolvedUrl);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        
        assertEquals("", sb.toString());
    }
}
