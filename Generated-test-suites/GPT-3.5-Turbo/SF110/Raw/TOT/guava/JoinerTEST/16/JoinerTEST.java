package com.google.common.base;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        Assert.assertNotNull(joiner);
    }
    
    @Test
    public void testAppendTo_withAnEmptyIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<?> parts = Arrays.asList();
        StringBuilder result = joiner.appendTo(builder, parts);
        Assert.assertEquals("", result.toString());
    }
    
    @Test
    public void testAppendTo_withAnIterableOfStrings() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<?> parts = Arrays.asList("one", "two", "three");
        StringBuilder result = joiner.appendTo(builder, parts);
        Assert.assertEquals("one,two,three", result.toString());
    }
    
    @Test(expected = IOException.class)
    public void testAppendTo_withAppendableThrowingIOException() throws IOException {
        Joiner joiner = Joiner.on(",");
        Appendable appendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("IOException");
            }
            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("IOException");
            }
            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("IOException");
            }
        };
        Iterable<?> parts = Arrays.asList("one", "two", "three");
        joiner.appendTo(appendable, parts);
    }
    
    @Test
    public void testJoin_withAnEmptyIterable() {
        Joiner joiner = Joiner.on(",");
        Iterable<?> parts = Arrays.asList();
        String result = joiner.join(parts);
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testJoin_withAnIterableOfStrings() {
        Joiner joiner = Joiner.on(",");
        Iterable<?> parts = Arrays.asList("one", "two", "three");
        String result = joiner.join(parts);
        Assert.assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoin_withAnArray() {
        Joiner joiner = Joiner.on(",");
        Object[] parts = { "one", "two", "three" };
        String result = joiner.join(parts);
        Assert.assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoin_withNullValues() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        Iterable<?> parts = Arrays.asList("one", null, "three");
        String result = joiner.join(parts);
        Assert.assertEquals("one,null,three", result);
    }
    
    @Test
    public void testSkipNulls_withIterableContainingNullValues() {
        Joiner joiner = Joiner.on(",").skipNulls();
        Iterable<?> parts = Arrays.asList("one", null, "three");
        String result = joiner.join(parts);
        Assert.assertEquals("one,three", result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withMapJoiner() {
        Joiner joiner = Joiner.on(",").skipNulls().withKeyValueSeparator(":");
        Iterable<? extends Map.Entry<?, ?>> parts = Arrays.asList(
                Map.entry("one", 1),
                Map.entry("two", null),
                Map.entry("three", 3)
        );
        joiner.join(parts);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_withSkipNullsAlreadySpecified() {
        Joiner joiner = Joiner.on(",").skipNulls().useForNull("null");
    }
    
    @Test
    public void testUseForNull_withAnIterableBetweenNullValues() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        Iterable<?> parts = Arrays.asList(null, "one", null, "two");
        String result = joiner.join(parts);
        Assert.assertEquals("null,one,null,two", result);
    }
    
    @Test
    public void testWithKeyValueSeparator_withAnEmptyMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        Map<?, ?> map = Map.of();
        StringBuilder result = mapJoiner.appendTo(builder, map);
        Assert.assertEquals("", result.toString());
    }
    
    @Test
    public void testWithKeyValueSeparator_withSingleEntryMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        Map<?, ?> map = Map.of("one", 1);
        StringBuilder result = mapJoiner.appendTo(builder, map);
        Assert.assertEquals("one:1", result.toString());
    }
    
    @Test
    public void testWithKeyValueSeparator_withMultipleEntryMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        Map<?, ?> map = Map.of(
                "one", 1,
                "two", 2,
                "three", 3
        );
        StringBuilder result = mapJoiner.appendTo(builder, map);
        Assert.assertEquals("one:1,two:2,three:3", result.toString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testWithKeyValueSeparator_withSkipNulls() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").skipNulls().withKeyValueSeparator(":");
        Map<?, ?> map = Map.of(
                "one", 1,
                "two", null,
                "three", 3
        );
        mapJoiner.join(map);
    }
    
    @Test
    public void testUseForNull_withKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":").useForNull("null");
        Map<?, ?> map = Map.of(
                "one", 1,
                "two", null,
                "three", 3
        );
        String result = mapJoiner.join(map);
        Assert.assertEquals("one:1,two:null,three:3", result);
    }
}