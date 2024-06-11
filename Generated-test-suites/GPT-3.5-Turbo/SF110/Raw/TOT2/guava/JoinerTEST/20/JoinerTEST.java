package com.google.common.base;

import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", result);
    }

    @Test
    public void testJoinWithNull() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(null, "b", "c");
        assertEquals(",b,c", result);
    }

    @Test
    public void testJoinWithNullUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        String result = joiner.join(null, "b", "c");
        assertEquals("null,b,c", result);
    }

    @Test
    public void testJoinMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, Integer> map = Collections.singletonMap("key", 123);
        String result = mapJoiner.join(map);
        assertEquals("key:123", result);
    }

    @Test
    public void testJoinMapWithNull() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":").useForNull("null");
        Map<String, Integer> map = Collections.singletonMap("key", null);
        String result = mapJoiner.join(map);
        assertEquals("key:null", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullTwice() {
        Joiner joiner = Joiner.on(",");
        joiner.useForNull("null").useForNull("null");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        mapJoiner.skipNulls();
    }

    @Test(expected = IOException.class)
    public void testAppendToWithIOException() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, Arrays.asList("a", "b", "c").iterator());
    }
}