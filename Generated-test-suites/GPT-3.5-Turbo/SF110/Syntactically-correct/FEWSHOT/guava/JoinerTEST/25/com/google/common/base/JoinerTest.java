package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class JoinerTest {

    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on('-');
        assertEquals("1-2-3", joiner.join(Arrays.asList(1, 2, 3)));
        assertEquals("a-b-c", joiner.join(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, Arrays.asList(1, 2, 3));
        assertEquals("1,2,3", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", stringBuilder.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, Arrays.asList(1, 2, 3).iterator());
        assertEquals("1,2,3", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", stringBuilder.toString());
    }

    @Test
    public void testAppendToArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, new Object[]{1, 2, 3});
        assertEquals("1,2,3", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", stringBuilder.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, 1, 2, 3);
        assertEquals("1,2,3", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, "a", "b", "c");
        assertEquals("a,b,c", stringBuilder.toString());
    }

    @Test
    public void testAppendToStringBuilderIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = joiner.appendTo(stringBuilder, Arrays.asList(1, 2, 3));
        assertEquals("1,2,3", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        stringBuilder = joiner.appendTo(stringBuilder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", stringBuilder.toString());
    }

    @Test
    public void testAppendToStringBuilderIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = joiner.appendTo(stringBuilder, Arrays.asList(1, 2, 3).iterator());
        assertEquals("1,2,3", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        stringBuilder = joiner.appendTo(stringBuilder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", stringBuilder.toString());
    }

    @Test
    public void testAppendToStringBuilderArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = joiner.appendTo(stringBuilder, new Object[]{1, 2, 3});
        assertEquals("1,2,3", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        stringBuilder = joiner.appendTo(stringBuilder, new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", stringBuilder.toString());
    }

    @Test
    public void testAppendToStringBuilderObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = joiner.appendTo(stringBuilder, 1, 2, 3);
        assertEquals("1,2,3", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        stringBuilder = joiner.appendTo(stringBuilder, "a", "b", "c");
        assertEquals("a,b,c", stringBuilder.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3).iterator()));
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c").iterator()));
    }

    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(new Object[]{1, 2, 3}));
        assertEquals("a,b,c", joiner.join(new Object[]{"a", "b", "c"}));
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(1, 2, 3));
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        assertEquals("1,null,3", joiner.join(Arrays.asList(1, null, 3)));
        assertEquals("a,null,c", joiner.join(Arrays.asList("a", null, "c")));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertEquals("1,3", joiner.join(Arrays.asList(1, null, 3)));
        assertEquals("a,c", joiner.join(Arrays.asList("a", null, "c")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMaps() {
        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.withKeyValueSeparator("=").join(Collections.singletonMap("key", null));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        assertEquals("key1:value1,key2:value2,key3:value3", joiner.join(Arrays.asList(
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2"),
                new AbstractMap.SimpleEntry<>("key3", "value3")
        )));
    }

}