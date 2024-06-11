import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        assertEquals("[a, b, c]", splitter.split("a,b,c").toString());
        assertEquals("[a, b, c]", splitter.split("a,b,c,").toString());
        assertEquals("[a, b, c]", splitter.split(",a,b,c").toString());
        assertEquals("[a,  b,  c]", splitter.split("a,  b,  c").toString());
        assertEquals("[a, b, c]", splitter.split("a,,b,c").toString());
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.WHITESPACE);
        assertEquals("[a, b, c]", splitter.split("a b c").toString());
        assertEquals("[a, b, c]", splitter.split("  a b c").toString());
        assertEquals("[a, b, c]", splitter.split("a b c  ").toString());
        assertEquals("[a, b, c]", splitter.split("a     b     c").toString());
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(";");
        assertEquals("[a, b, c]", splitter.split("a;b;c").toString());
        assertEquals("[a, b, c]", splitter.split("a;b;c;").toString());
        assertEquals("[a, b, c]", splitter.split(";a;b;c").toString());
        assertEquals("[a, b, c]", splitter.split("a;;b;c").toString());
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("[\\s,;]+");
        assertEquals("[a, b, c]", splitter.split("a b c").toString());
        assertEquals("[a, b, c]", splitter.split("a, b, c").toString());
        assertEquals("[a, b, c]", splitter.split("a;b;c").toString());
        assertEquals("[a, b, c]", splitter.split("a;;b;;;c").toString());
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        assertEquals("[abc, def, ghi]", splitter.split("abcdefghi").toString());
        assertEquals("[abc, def, ghi]", splitter.split("abcdefghi ").toString());
        assertEquals("[abc, def, ghi]", splitter.split(" abcdefghi").toString());
        assertEquals("[abc, def, ghi]", splitter.split("abc  def  ghi").toString());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        assertEquals("[a, b, c]", splitter.split("a,,b,c").toString());
        assertEquals("[a, b, c]", splitter.split(",a,b,c").toString());
        assertEquals("[a, b, c]", splitter.split("a,b,c,").toString());
        assertEquals("[a, b, c]", splitter.split(",a,b,c,").toString());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        assertEquals("[a, b,c]", splitter.split("a,b,c").toString());
        assertEquals("[a, b]", splitter.split("a,b,c,d").toString());
        assertEquals("[a, b, c,d]", splitter.split("a,b,c,d").toString());
        assertEquals("[a, b, c,d, e]", splitter.split("a,b,c,d,e").toString());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        assertEquals("[a, b, c]", splitter.split("a ,b ,c").toString());
        assertEquals("[a, b, c]", splitter.split("a, b ,c").toString());
        assertEquals("[a, b, c]", splitter.split(" a,b, c").toString());
        assertEquals("[a, b, c]", splitter.split("a, b , c ").toString());
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> iterable = splitter.split("a,b,c");
        assertEquals("a", iterable.iterator().next());
        assertFalse(iterable.iterator().hasNext());
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        assertEquals("[a, b, c]", splitter.splitToList("a,b,c").toString());
        assertEquals("[a, b, c]", splitter.splitToList("a,b,c,,").toString());
        assertEquals("[a, b, c]", splitter.splitToList(",a,b,c").toString());
        assertEquals("[a, b, c]", splitter.splitToList("a,,b,c").toString());
    }

    @Test
    public void testWithKeyValueSeparator_String() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(':');
        assertEquals("{a=b, c=d}", mapSplitter.split("a:b,c:d").toString());
        assertEquals("{ a = b ,  c = d }", mapSplitter.split(" a = b , c = d ").toString());
        assertEquals("{a= b = c}", mapSplitter.split("a: b = c").toString());
    }

    @Test
    public void testWithKeyValueSeparator_Char() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(':');
        assertEquals("{a=b, c=d}", mapSplitter.split("a:b,c:d").toString());
        assertEquals("{ a = b ,  c = d }", mapSplitter.split(" a = b , c = d ").toString());
        assertEquals("{a= b = c}", mapSplitter.split("a: b = c").toString());
    }

    @Test
    public void testWithKeyValueSeparator_Splitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(Splitter.on(':'));
        assertEquals("{a=b, c=d}", mapSplitter.split("a:b,c:d").toString());
        assertEquals("{ a = b ,  c = d }", mapSplitter.split(" a = b , c = d ").toString());
        assertEquals("{a= b = c}", mapSplitter.split("a: b = c").toString());
    }

}