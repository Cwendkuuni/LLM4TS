package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JoinerTest {
    @Test
    public void testOn_String() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
    }
    
    @Test
    public void testOn_Char() {
        Joiner joiner = Joiner.on(',');
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
    }
    
    @Test
    public void testAppendTo_Iterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), Arrays.asList("a", "b", "c")).toString());
    }
    
    @Test
    public void testAppendTo_Iterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), Arrays.asList("a", "b", "c").iterator()).toString());
    }
    
    @Test
    public void testAppendTo_ObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), new Object[]{"a", "b", "c"}).toString());
    }
    
    @Test
    public void testAppendTo_Objects() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), "a", "b", "c").toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_Iterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_Iterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_ObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_Objects() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", "b", "c");
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testJoin_Iterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
    }
    
    @Test
    public void testJoin_Iterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c").iterator()));
    }
    
    @Test
    public void testJoin_ObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(new Object[]{"a", "b", "c"}));
    }
    
    @Test
    public void testJoin_Objects() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        assertEquals("a,null,c", joiner.join(Arrays.asList("a", null, "c")));
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertEquals("a,c", joiner.join(Arrays.asList("a", null, "c")));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.withKeyValueSeparator(":");
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        assertEquals("a:1,b:2,c:3", joiner.join(map));
    }
}