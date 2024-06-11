import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

public class StringUtilTest {
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("foo", "bar", "baz");
        assertEquals("foo, bar, baz", StringUtil.join(strings, ", "));
        assertNull(StringUtil.join(null, ", "));
        assertEquals("", StringUtil.join(Arrays.asList(), ", "));
    }
    
    @Test
    public void testJoinIterator() {
        Iterator<String> strings = Arrays.asList("foo", "bar", "baz").iterator();
        assertEquals("foo, bar, baz", StringUtil.join(strings, ", "));
        assertNull(StringUtil.join(null, ", "));
        assertEquals("", StringUtil.join(Arrays.asList().iterator(), ", "));
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = new String[] {"foo", "bar", "baz"};
        assertEquals("foo, bar, baz", StringUtil.join(strings, ", "));
        assertNull(StringUtil.join(null, ", "));
        assertEquals("", StringUtil.join(new String[] {}, ", "));
    }
    
    @Test
    public void testPadding() {
        assertEquals("", StringUtil.padding(0));
        assertEquals(" ", StringUtil.padding(1));
        assertEquals("      ", StringUtil.padding(6));
        try {
            StringUtil.padding(-1);
            assertFalse("IllegalArgumentException not thrown", true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testIsBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("foo"));
    }
    
    @Test
    public void testIsNumeric() {
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
        assertTrue(StringUtil.isNumeric("123"));
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
        assertEquals("foo  bar", StringUtil.normaliseWhitespace("foo  bar"));
        assertEquals("foo bar", StringUtil.normaliseWhitespace("foo    bar"));
        assertEquals("foo bar", StringUtil.normaliseWhitespace("foo\n   \tbar"));
        assertEquals("", StringUtil.normaliseWhitespace(""));
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "foo  bar", false);
        assertEquals("foo bar", sb.toString());
        
        StringBuilder sb2 = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb2, "foo\n   \tbar", true);
        assertEquals("foo bar", sb2.toString());
    }
    
    @Test
    public void testIn() {
        assertTrue(StringUtil.in("foo", "foo", "bar", "baz"));
        assertFalse(StringUtil.in("qux", "foo", "bar", "baz"));
        assertFalse(StringUtil.in(null, "foo", "bar", "baz"));
        assertFalse(StringUtil.in("foo", null, "bar", "baz"));
    }
    
    @Test
    public void testInSorted() {
        assertTrue(StringUtil.inSorted("bar", new String[] {"foo", "bar", "baz"}));
        assertFalse(StringUtil.inSorted("qux", new String[] {"foo", "bar", "baz"}));
        assertFalse(StringUtil.inSorted(null, new String[] {"foo", "bar", "baz"}));
        assertFalse(StringUtil.inSorted("foo", null));
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        assertEquals("http://www.example.com/foo?bar", StringUtil.resolve(base, "/foo?bar").toString());
        assertEquals("http://www.example.com/foo", StringUtil.resolve(base, "./foo").toString());
        
        assertEquals("http://www.example.com/foo?bar", StringUtil.resolve("http://www.example.com", "/foo?bar"));
        assertEquals("http://www.example.com/foo", StringUtil.resolve("http://www.example.com", "./foo"));
        assertEquals("", StringUtil.resolve("http://www.example.com", "foo")); // MalformedURLException thrown should return empty string
    }
    
    @Test
    public void testStringResolve() {
        assertEquals("http://www.example.com/foo?bar", StringUtil.resolve("http://www.example.com", "/foo?bar"));
        assertEquals("http://www.example.com/foo", StringUtil.resolve("http://www.example.com", "./foo"));
        assertEquals("", StringUtil.resolve("http://www.example.com", "foo")); // MalformedURLException thrown should return empty string
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        sb1.append("foo");
        StringBuilder sb2 = StringUtil.stringBuilder();
        assertEquals("", sb2.toString());
    }
}