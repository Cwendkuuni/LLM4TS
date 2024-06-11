package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class JoinerTest {
    
    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }
    
    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on(' ');
        assertNotNull(joiner);
        assertEquals(" ", joiner.separator);
    }
    
    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        Iterable<String> parts = Arrays.asList("A", "B", "C");
        joiner.appendTo(sb, parts);
        assertEquals("A,B,C", sb.toString());
    }
    
    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(",");
        String[] parts = {"D", "E", "F"};
        String result = joiner.join(parts);
        assertEquals("D,E,F", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        joiner = joiner.useForNull("null");
        String result = joiner.join("G", null, "H");
        assertEquals("G,null,H", result);
    }
    
    @Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        Object[] parts = {null, "I", null, "J"};
        joiner.skipNulls().appendTo(sb, parts);
        assertEquals("I,J", sb.toString());
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        assertNotNull(mapJoiner);
        assertEquals(",", mapJoiner.joiner.separator);
        assertEquals(":", mapJoiner.keyValueSeparator);
    }
}