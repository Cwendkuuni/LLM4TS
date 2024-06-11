
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class JoinerTest {

    private Joiner joiner;

    @Before
    public void setup() {
        joiner = Joiner.on(",");
    }

    @Test
    public void testOnString() {
        assertEquals("abc,def,ghi", joiner.join("abc", "def", "ghi"));
    }

    @Test
    public void testOnChar() {
        assertEquals("a,b,c", Joiner.on(',').join("a", "b", "c"));
    }

    @Test
    public void testAppendToIterable() throws IOException {
        List<Integer> list = Arrays.asList(1, 2, 3);
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list);
        assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        List<Integer> list = Arrays.asList(1, 2, 3);
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());
        assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        String[] array = {"hello", "world"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);
        assertEquals("hello,world", builder.toString());
    }

    @Test
    public void testAppendToOneObject() throws IOException {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "hello");
        assertEquals("hello", builder.toString());
    }

    @Test
    public void testAppendToTwoObjects() throws IOException {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "hello", "world");
        assertEquals("hello,world", builder.toString());
    }

    @Test
    public void testAppendToThreeObjects() throws IOException {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "hello", "world", "!");
        assertEquals("hello,world,!", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithIterable() {
        List<String> list = Arrays.asList("hello", "world");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list);
        assertEquals("hello,world", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithIterator() {
        List<String> list = Arrays.asList("hello", "world");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());
        assertEquals("hello,world", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithObjectArray() {
        String[] array = {"hello", "world"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);
        assertEquals("hello,world", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithOneObject() {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "hello");
        assertEquals("hello", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithTwoObjects() {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "hello", "world");
        assertEquals("hello,world", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithThreeObjects() {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "hello", "world", "!");
        assertEquals("hello,world,!", builder.toString());
    }

    @Test
    public void testJoinWithIterable() {
        List<String> list = Arrays.asList("hello", "world");
        assertEquals("hello,world", joiner.join(list));
    }

    @Test
    public void testJoinWithIterator() {
        List<String> list = Arrays.asList("hello", "world");
        assertEquals("hello,world", joiner.join(list.iterator()));
    }

    @Test
    public void testJoinWithObjectArray() {
        String[] array = {"hello", "world"};
        assertEquals("hello,world", joiner.join(array));
    }

    @Test
    public void testJoinWithOneObject() {
        assertEquals("hello", joiner.join("hello"));
    }

    @Test
    public void testJoinWithTwoObjects() {
        assertEquals("hello,world", joiner.join("hello", "world"));
    }

    @Test
    public void testJoinWithThreeObjects() {
        assertEquals("hello,world,!", joiner.join("hello", "world", "!"));
    }

    @Test
    public void testUseForNull() {
        joiner = joiner.useForNull("null");
        List<String> list = Arrays.asList(null, "world");
        assertEquals("null,world", joiner.join(list));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullTwice() {
        joiner.useForNull("null").useForNull("null2");
    }

    @Test
    public void testSkipNulls() throws IOException {
        joiner = joiner.skipNulls();
        List<String> list = Arrays.asList(null, "world", null, "!");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());
        assertEquals("world,!", builder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithUseForNull() {
        joiner.skipNulls().useForNull("null");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMapJoiner() {
        joiner.skipNulls().withKeyValueSeparator(":");
    }

    @Test
    public void testMapJoinerAppendToWithMap() throws IOException {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, map);
        assertEquals("a:1,b:2", builder.toString());
    }

    @Test
    public void testMapJoinerAppendToWithMapEntries() throws IOException {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, map.entrySet());
        assertEquals("a:1,b:2", builder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapJoinerUseForNull() {
        joiner.withKeyValueSeparator(":").useForNull("null");
    }

    @Test
    public void testCompleteArray() throws IOException {
        List<String> list = Arrays.asList("hello", "world");
        StringBuilder builder = new StringBuilder();
        String[] array = {"!", "Java"};
        joiner.appendTo(builder, list);
        joiner.appendTo(builder, array);
        joiner.appendTo(builder, list.iterator());
        joiner.appendTo(builder, array);
        assertEquals("hello,world,!,Java,hello,world,!,Java", builder.toString());
    }

}
