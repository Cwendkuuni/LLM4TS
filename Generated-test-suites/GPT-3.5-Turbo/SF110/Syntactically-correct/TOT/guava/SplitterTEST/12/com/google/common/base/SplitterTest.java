import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;
import java.util.*;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('.');
        Iterable<String> result = splitter.split("hello.world");
        Iterator<String> iterator = result.iterator();
        assertEquals("hello", iterator.next());
        assertEquals("world", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(':'));
        Iterable<String> result = splitter.split("one:two:three");
        Iterator<String> iterator = result.iterator();
        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        assertEquals("three", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(" | ");
        Iterable<String> result = splitter.split("one | two | three");
        Iterator<String> iterator = result.iterator();
        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        assertEquals("three", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("one  two   three");
        Iterator<String> iterator = result.iterator();
        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        assertEquals("three", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("123456");
        Iterator<String> iterator = result.iterator();
        assertEquals("12", iterator.next());
        assertEquals("34", iterator.next());
        assertEquals("56", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("one,,two,,,three");
        Iterator<String> iterator = result.iterator();
        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        assertEquals("three", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        Iterable<String> result = splitter.split("one,two,three,four");
        Iterator<String> iterator = result.iterator();
        assertEquals("one", iterator.next());
        assertEquals("two,three,four", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(" ").trimResults();
        Iterable<String> result = splitter.split("  one  two    three   ");
        Iterator<String> iterator = result.iterator();
        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        assertEquals("three", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.anyOf(".-"));
        Iterable<String> result = splitter.split(".one,-two,--three");
        Iterator<String> iterator = result.iterator();
        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        assertEquals("three", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(":");
        Iterable<String> result = splitter.split("one:two:three");
        Iterator<String> iterator = result.iterator();
        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        assertEquals("three", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on("|");
        List<String> result = splitter.splitToList("one|two|three");
        assertEquals("one", result.get(0));
        assertEquals("two", result.get(1));
        assertEquals("three", result.get(2));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter splitter = Splitter.on(",").withKeyValueSeparator(":");
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2,key3=value3");
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }
}