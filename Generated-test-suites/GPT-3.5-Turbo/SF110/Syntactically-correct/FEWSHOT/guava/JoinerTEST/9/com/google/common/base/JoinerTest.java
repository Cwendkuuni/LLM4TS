package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JoinerTest {
    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
    }
    
    @Test 
    public void testAppendTo() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test 
    public void testAppendTo_NullList() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, null);
        assertEquals("", builder.toString());
    }
    
    @Test 
    public void testAppendTo_NullElement() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", null, "c"));
        assertEquals("a,,c", builder.toString());
    }
    
    @Test 
    public void testAppendTo_Varargs() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", "b", "c");
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendTo_NullVarargs() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, (Object[]) null);
        assertEquals("", builder.toString());
    }
    
    @Test 
    public void testAppendTo_NullElementVarargs() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", null, "c");
        assertEquals("a,,c", builder.toString());
    }
    
    @Test 
    public void testJoin() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
    }
    
    @Test 
    public void testJoin_NullList() {
        Joiner joiner = Joiner.on(",");
        assertNull(joiner.join((Iterable<?>) null));
    }
    
    @Test 
    public void testJoin_NullElement() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,,c", joiner.join(Arrays.asList("a", null, "c")));
    }
    
    @Test 
    public void testJoin_Varargs() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
    }
    
    @Test 
    public void testJoin_NullVarargs() {
        Joiner joiner = Joiner.on(",");
        assertNull(joiner.join((Object[]) null));
    }
    
    @Test 
    public void testJoin_NullElementVarargs() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,,c", joiner.join("a", null, "c"));
    }
    
    @Test 
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        assertEquals("a,null,c", joiner.join(Arrays.asList("a", null, "c")));
        assertEquals("a,null,c", joiner.join("a", null, "c"));
    }
    
    @Test 
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertEquals("a,c", joiner.join(Arrays.asList("a", null, "c")));
    }
    
    @Test 
    public void testJoinWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals("key1=value1,key2=value2", mapJoiner.join(map));
    }
    
    @Test 
    public void testJoinWithKeyValueSeparator_NullMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        assertNull(mapJoiner.join((Map<?, ?>) null));
    }
    
    @Test 
    public void testJoinWithKeyValueSeparator_NullKey() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put(null, "value1");
        map.put("key2", "value2");
        assertEquals("=value1,key2=value2", mapJoiner.join(map));
    }
    
    @Test 
    public void testJoinWithKeyValueSeparator_NullValue() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", null);
        map.put("key2", "value2");
        assertEquals("key1=,key2=value2", mapJoiner.join(map));
    }
}