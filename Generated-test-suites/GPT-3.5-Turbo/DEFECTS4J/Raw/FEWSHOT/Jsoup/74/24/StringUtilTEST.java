import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringUtilTest {
    @Test
    public void testJoinCollection() {
        assertEquals("", StringUtil.join(Collections.emptyList(), ""));
        assertEquals("a", StringUtil.join(Collections.singletonList("a"), ""));
        assertEquals("a,b,c", StringUtil.join(Arrays.asList("a", "b", "c"), ","));
    }
    
    @Test
    public void testJoinIterator() {
        assertEquals("", StringUtil.join(Collections.emptyIterator(), ""));
        assertEquals("a", StringUtil.join(Collections.singletonList("a").iterator(), ""));
        assertEquals("a,b,c", StringUtil.join(Arrays.asList("a", "b", "c").iterator(), ","));
    }
    
    @Test
    public void testJoinArray() {
        assertEquals("", StringUtil.join(new String[]{}, ""));
        assertEquals("a", StringUtil.join(new String[]{"a"}, ""));
        assertEquals("a,b,c", StringUtil.join(new String[]{"a", "b", "c"}, ","));
    }
    
    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("          ", StringUtil.padding(10));
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\n"));
        assertFalse(StringUtil.isBlank("a"));
        assertFalse(StringUtil.isBlank(" a "));
    }
    
    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("12345"));
        assertTrue(StringUtil.isNumeric("-12345"));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("a123"));
        assertFalse(StringUtil.isNumeric("123a"));
    }
    
    @Test
    public void testIsWhitespace() {
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));
        assertFalse(StringUtil.isWhitespace('a'));
    }
    
    @Test
    public void testIsActuallyWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));
        assertFalse(StringUtil.isActuallyWhitespace('a'));
    }
    
    @Test
    public void testNormaliseWhitespace() {
        assertEquals("", StringUtil.normaliseWhitespace(""));
        assertEquals(" ", StringUtil.normaliseWhitespace("  "));
        assertEquals("a b c", StringUtil.normaliseWhitespace("a  b   c"));
        assertEquals("a b c", StringUtil.normaliseWhitespace("a\nb\nc"));
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb1 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb1, "", true);
        assertEquals("", sb1.toString());
        
        StringBuilder sb2 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb2, " a b ", true);
        assertEquals("a b", sb2.toString());
        
        StringBuilder sb3 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb3, " a b ", false);
        assertEquals(" a b ", sb3.toString());
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("a", "a", "b", "c"));
        assertTrue(StringUtil.in("a", "b", "a", "c"));
        assertFalse(StringUtil.in("d", "a", "b", "c"));
    }
    
    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("a", new String[]{"a", "b", "c"}));
        assertFalse(StringUtil.inSorted("d", new String[]{"a", "b", "c"}));
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        URL resolved = StringUtil.resolve(base, "/path/file.html");
        assertEquals("http://www.example.com/path/file.html", resolved.toString());
        
        String baseUrl = "http://www.example.com";
        String resolvedUrl = StringUtil.resolve(baseUrl, "/path/file.html");
        assertEquals("http://www.example.com/path/file.html", resolvedUrl);
    }
    
    @Test
    public void testResolveInvalidUrl() {
        String baseUrl = "invalid-url";
        String resolvedUrl = StringUtil.resolve(baseUrl, "/path/file.html");
        assertEquals("", resolvedUrl);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        sb1.append("test");
        assertEquals("test", sb1.toString());
        
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertEquals("", sb2.toString());
    }
}