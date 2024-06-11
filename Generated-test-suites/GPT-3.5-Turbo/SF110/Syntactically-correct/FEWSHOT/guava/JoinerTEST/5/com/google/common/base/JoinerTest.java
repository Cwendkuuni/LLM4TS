package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(new String[]{"a", "b", "c"}));
        assertNull(joiner.join((Object[]) null));
        assertEquals("a,b,c", joiner.join(new Object[]{"a", "b", "c"}));
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(';');
        assertEquals("a;b;c", joiner.join(new String[]{"a", "b", "c"}));
        assertNull(joiner.join((Object[]) null));
        assertEquals("a;b;c", joiner.join(new Object[]{"a", "b", "c"}));
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Arrays.asList("a", "b", "c").iterator();
            }
        });
        assertEquals("a-b-c", sb.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(";");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a;b;c", sb.toString());
    }

    @Test
    public void testAppendToArray() throws IOException {
        Joiner joiner = Joiner.on("~");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, new Object[]{"a", "b", "c"});
        assertEquals("a~b~c", sb.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, "a", "b", "c");
        assertEquals("a-b-c", sb.toString());
    }

    @Test
    public void testAppendToEmptyArray() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, new Object[]{});
        assertEquals("", sb.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(":");
        Iterable<String> iterable = Arrays.asList("a", "b", "c");
        assertEquals("a:b:c", joiner.join(iterable));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        Iterator<String> iterator = Arrays.asList("a", "b", "c").iterator();
        assertEquals("a,b,c", joiner.join(iterator));
    }

    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on("/");
        String[] array = {"a", "b", "c"};
        assertEquals("a/b/c", joiner.join(array));
    }

    @Test
    public void testJoinObjects() {
        Joiner joiner = Joiner.on("-");
        assertEquals("a-b-c", joiner.join("a", "b", "c"));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        assertEquals("a,null,c", joiner.join(new String[]{"a", null, "c"}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullTwice() {
        Joiner joiner = Joiner.on(",").useForNull("null").useForNull("null");
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertEquals("a,c", joiner.join(new String[]{"a", null, "c"}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithUseForNull() {
        Joiner joiner = Joiner.on(",").skipNulls().useForNull("null");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").skipNulls().withKeyValueSeparator(":");
    }

    @Test
    public void testMapJoinerAppendTo() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        StringBuilder sb = new StringBuilder();
        try {
            mapJoiner.appendTo(sb, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("key1:value1,key2:value2", sb.toString());
    }

    @Test
    public void testMapJoinerAppendToEmptyMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        try {
            mapJoiner.appendTo(sb, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testMapJoinerAppendToStringBuilder() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        StringBuilder sb = new StringBuilder();
        mapJoiner.appendTo(sb, map);
        assertEquals("key1:value1,key2:value2", sb.toString());
    }

    @Test
    public void testMapJoinerAppendToStringBuilderEmptyMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        mapJoiner.appendTo(sb, map);
        assertEquals("", sb.toString());
    }

    @Test
    public void testMapJoinerJoin() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals("key1:value1,key2:value2", mapJoiner.join(map));
    }

    @Test
    public void testMapJoinerJoinEmptyMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        assertEquals("", mapJoiner.join(map));
    }
}