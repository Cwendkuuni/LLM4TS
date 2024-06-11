import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class JoinerTest {

    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on('#');
        assertNotNull(joiner);
        assertEquals("#", joiner.separator);
    }

    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("one", "two", "three"));
        assertEquals("one,two,three", sb.toString());
    }

    @Test
    public void testAppendToWithIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("one", "two", "three").iterator());
        assertEquals("one,two,three", sb.toString());
    }

    @Test
    public void testAppendToWithObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, new Object[]{"one", "two", "three"});
        assertEquals("one,two,three", sb.toString());
    }

    @Test
    public void testAppendToWithObjects() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, "one", "two", "three");
        assertEquals("one,two,three", sb.toString());
    }

    @Test
    public void testAppendToWithNullValue() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("one", null, "three"));
        assertEquals("one,null,three", sb.toString());
    }

    @Test
    public void testAppendToStringBuilder() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("one", "two", "three"));
        assertEquals("one,two,three", sb.toString());
    }

    @Test
    public void testAppendToWithArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, new String[]{"one", "two", "three"});
        assertEquals("one,two,three", sb.toString());
    }

    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("one", "two", "three"));
        assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("one", "two", "three").iterator());
        assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinWithObjectArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new Object[]{"one", "two", "three"});
        assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinWithObjects() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("one", "two", "three");
        assertEquals("one,two,three", result);
    }

    @Test
    public void testJoinWithNullValue() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("one", null, "three"));
        assertEquals("one,null,three", result);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        assertEquals("null,two,null", joiner.join("one", null, "three"));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertEquals("one,three", joiner.join("one", null, "three"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithUseForNull() {
        Joiner joiner = Joiner.on(",").skipNulls().useForNull("null");
        // Should throw UnsupportedOperationException
        joiner.join("one", null, "three");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMapJoiner() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").skipNulls().withKeyValueSeparator(":");
        // Should throw UnsupportedOperationException
        mapJoiner.join(Collections.singletonMap("key", "value"));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        String result = mapJoiner.join(Collections.singletonMap("key", "value"));
        assertEquals("key:value", result);
    }

    @Test
    public void testToStringWithCharSequence() {
        Joiner joiner = Joiner.on(",");
        CharSequence result = joiner.toString("test");
        assertEquals("test", result);
    }

    @Test
    public void testToStringWithObject() {
        Joiner joiner = Joiner.on(",");
        CharSequence result = joiner.toString(123);
        assertEquals("123", result);
    }

}