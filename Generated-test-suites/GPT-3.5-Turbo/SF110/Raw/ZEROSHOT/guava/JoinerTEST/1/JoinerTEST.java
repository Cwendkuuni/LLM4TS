import org.junit.*;
import java.io.*;
import java.util.*;
import com.google.common.annotations.*;

public class JoinerTest {

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, list);
        Assert.assertEquals("hello, world", writer.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, list.iterator());
        Assert.assertEquals("hello, world", writer.toString());
    }

    @Test
    public void testAppendToArray() throws IOException {
        Joiner joiner = Joiner.on(", ");
        String[] array = {"hello", "world"};
        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, array);
        Assert.assertEquals("hello, world", writer.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(", ");
        String[] array = {"hello", "world"};
        StringWriter writer = new StringWriter();
        joiner.appendTo(writer, "first", "second", array);
        Assert.assertEquals("first, second, hello, world", writer.toString());
    }

    @Test
    public void testAppendToStringBuilderIterable() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list);
        Assert.assertEquals("hello, world", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderIterator() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());
        Assert.assertEquals("hello, world", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderArray() {
        Joiner joiner = Joiner.on(", ");
        String[] array = {"hello", "world"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);
        Assert.assertEquals("hello, world", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderObjectArray() {
        Joiner joiner = Joiner.on(", ");
        String[] array = {"hello", "world"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "first", "second", array);
        Assert.assertEquals("first, second, hello, world", builder.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        String result = joiner.join(list);
        Assert.assertEquals("hello, world", result);
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(", ");
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        String result = joiner.join(list.iterator());
        Assert.assertEquals("hello, world", result);
    }

    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(", ");
        String[] array = {"hello", "world"};
        String result = joiner.join(array);
        Assert.assertEquals("hello, world", result);
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(", ");
        String[] array = {"hello", "world"};
        String result = joiner.join("first", "second", array);
        Assert.assertEquals("first, second, hello, world", result);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(", ").useForNull("null");
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add(null);
        list.add("world");
        String result = joiner.join(list);
        Assert.assertEquals("hello, null, world", result);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(", ").skipNulls();
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add(null);
        list.add("world");
        String result = joiner.join(list);
        Assert.assertEquals("hello, world", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsUseForNull() {
        Joiner joiner = Joiner.on(", ").skipNulls();
        joiner.useForNull("null");
    }

    @Test
    public void testMapJoinerAppendTo() throws IOException {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("One", 1);
        map.put("Two", 2);
        StringWriter writer = new StringWriter();
        mapJoiner.appendTo(writer, map);
        Assert.assertEquals("One:1, Two:2", writer.toString());
    }

    @Test
    public void testMapJoinerAppendToStringBuilder() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("One", 1);
        map.put("Two", 2);
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, map);
        Assert.assertEquals("One:1, Two:2", builder.toString());
    }

    @Test
    public void testMapJoinerJoin() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("One", 1);
        map.put("Two", 2);
        String result = mapJoiner.join(map);
        Assert.assertEquals("One:1, Two:2", result);
    }

    @Test
    public void testMapJoinerAppendToIterable() throws IOException {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        list.add(new AbstractMap.SimpleEntry<>("One", 1));
        list.add(new AbstractMap.SimpleEntry<>("Two", 2));
        StringWriter writer = new StringWriter();
        mapJoiner.appendTo(writer, list);
        Assert.assertEquals("One:1, Two:2", writer.toString());
    }

    @Test
    public void testMapJoinerAppendToIterator() throws IOException {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        list.add(new AbstractMap.SimpleEntry<>("One", 1));
        list.add(new AbstractMap.SimpleEntry<>("Two", 2));
        StringWriter writer = new StringWriter();
        mapJoiner.appendTo(writer, list.iterator());
        Assert.assertEquals("One:1, Two:2", writer.toString());
    }

    @Test
    public void testMapJoinerAppendToStringBuilderIterable() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        list.add(new AbstractMap.SimpleEntry<>("One", 1));
        list.add(new AbstractMap.SimpleEntry<>("Two", 2));
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, list);
        Assert.assertEquals("One:1, Two:2", builder.toString());
    }

    @Test
    public void testMapJoinerAppendToStringBuilderIterator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        list.add(new AbstractMap.SimpleEntry<>("One", 1));
        list.add(new AbstractMap.SimpleEntry<>("Two", 2));
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, list.iterator());
        Assert.assertEquals("One:1, Two:2", builder.toString());
    }

    @Test
    public void testMapJoinerJoinIterable() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        list.add(new AbstractMap.SimpleEntry<>("One", 1));
        list.add(new AbstractMap.SimpleEntry<>("Two", 2));
        String result = mapJoiner.join(list);
        Assert.assertEquals("One:1, Two:2", result);
    }

    @Test
    public void testMapJoinerJoinIterator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        list.add(new AbstractMap.SimpleEntry<>("One", 1));
        list.add(new AbstractMap.SimpleEntry<>("Two", 2));
        String result = mapJoiner.join(list.iterator());
        Assert.assertEquals("One:1, Two:2", result);
    }

    @Test
    public void testMapJoinerUseForNull() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":").useForNull("null");
        Map<String, Integer> map = new HashMap<>();
        map.put("One", 1);
        map.put("Two", null);
        String result = mapJoiner.join(map);
        Assert.assertEquals("One:1, Two:null", result);
    }

}