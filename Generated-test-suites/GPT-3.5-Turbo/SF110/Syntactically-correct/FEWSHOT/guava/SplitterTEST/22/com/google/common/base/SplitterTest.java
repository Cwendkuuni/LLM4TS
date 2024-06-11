import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SplitterTest {
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        assertEquals("abc", splitter.split("abc").iterator().next());
        assertEquals("abc", splitter.split("abc,def").iterator().next());
        assertEquals("abc", splitter.split("abc,def,ghi").iterator().next());
        assertFalse(splitter.split("").iterator().hasNext());
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        assertEquals("abc", splitter.split("abc").iterator().next());
        assertEquals("abc", splitter.split("abc,def").iterator().next());
        assertEquals("abc", splitter.split("abc,def,ghi").iterator().next());
        assertFalse(splitter.split("").iterator().hasNext());
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(";");
        assertEquals("abc", splitter.split("abc").iterator().next());
        assertEquals("abc", splitter.split("abc;def").iterator().next());
        assertEquals("abc", splitter.split("abc;def;ghi").iterator().next());
        assertFalse(splitter.split("").iterator().hasNext());
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        assertEquals("abc", splitter.split("abc").iterator().next());
        assertEquals("abc", splitter.split("abc def").iterator().next());
        assertEquals("abc", splitter.split("abc   def   ghi").iterator().next());
        assertFalse(splitter.split("").iterator().hasNext());
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        assertEquals("abc", splitter.split("abc").iterator().next());
        assertEquals("abc", splitter.split("abcdef").iterator().next());
        assertEquals("abc", splitter.split("abcdefghi").iterator().next());
        assertFalse(splitter.split("").iterator().hasNext());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(":").omitEmptyStrings();
        assertEquals("abc", splitter.split("abc").iterator().next());
        assertEquals("abc", splitter.split("abc:def").iterator().next());
        assertEquals("abc", splitter.split("abc::def::ghi").iterator().next());
        assertFalse(splitter.split("").iterator().hasNext());
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(":").limit(2);
        assertEquals("abc", splitter.split("abc").iterator().next());
        assertEquals("abc", splitter.split("abc:def").iterator().next());
        assertEquals("abc", splitter.split("abc:def:ghi").iterator().next());
        assertFalse(splitter.split("").iterator().hasNext());
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(":").trimResults();
        assertEquals("abc", splitter.split("abc").iterator().next());
        assertEquals("abc", splitter.split(" abc:def ").iterator().next());
        assertEquals("abc", splitter.split(" abc : def : ghi ").iterator().next());
        assertFalse(splitter.split("").iterator().hasNext());
    }
    
    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(":").trimResults(CharMatcher.WHITESPACE);
        assertEquals("abc", splitter.split("abc").iterator().next());
        assertEquals("abc", splitter.split(" abc:def ").iterator().next());
        assertEquals("abc", splitter.split(" abc : def : ghi ").iterator().next());
        assertFalse(splitter.split("").iterator().hasNext());
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(":");
        assertEquals("abc", splitter.split("abc").iterator().next());
        assertEquals("abc", splitter.split("abc:def").iterator().next());
        assertEquals("abc", splitter.split("abc:def:ghi").iterator().next());
        assertFalse(splitter.split("").iterator().hasNext());
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(":");
        assertEquals("abc", splitter.splitToList("abc").get(0));
        assertEquals("abc", splitter.splitToList("abc:def").get(0));
        assertEquals("def", splitter.splitToList("abc:def").get(1));
        assertEquals("abc", splitter.splitToList("abc:def:ghi").get(0));
        assertEquals("def", splitter.splitToList("abc:def:ghi").get(1));
        assertEquals("ghi", splitter.splitToList("abc:def:ghi").get(2));
        assertTrue(splitter.splitToList("").isEmpty());
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(";").withKeyValueSeparator(":");
        assertEquals("abc", mapSplitter.split("abc:def;ghi:jkl").get("abc"));
        assertEquals("jkl", mapSplitter.split("abc:def;ghi:jkl").get("ghi"));
    }
}