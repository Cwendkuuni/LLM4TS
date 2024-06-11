package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JoinerTest {
    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
        assertEquals("a,b,c", joiner.join(new String[]{"a", "b", "c"}));
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), new String[]{"a", "b", "c"}).toString());
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Arrays.asList("a", "b", "c").iterator();
            }
        }).toString());
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), Arrays.asList("a", "b", "c")).toString());
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), Arrays.asList("a", "b", "c").iterator()).toString());
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(',');
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
        assertEquals("a,b,c", joiner.join(new String[]{"a", "b", "c"}));
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), new String[]{"a", "b", "c"}).toString());
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Arrays.asList("a", "b", "c").iterator();
            }
        }).toString());
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), Arrays.asList("a", "b", "c")).toString());
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), Arrays.asList("a", "b", "c").iterator()).toString());
    }

    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        assertEquals("a,null,c", joiner.join("a", null, "c"));
        assertEquals("a,null,c", joiner.join(new String[]{"a", null, "c"}));
        assertEquals("a,null,c", joiner.appendTo(new StringBuilder(), new String[]{"a", null, "c"}).toString());
        assertEquals("a,null,c", joiner.appendTo(new StringBuilder(), new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Arrays.asList("a", null, "c").iterator();
            }
        }).toString());
        assertEquals("a,null,c", joiner.appendTo(new StringBuilder(), Arrays.asList("a", null, "c")).toString());
        assertEquals("a,null,c", joiner.appendTo(new StringBuilder(), Arrays.asList("a", null, "c").iterator()).toString());
    }

    @Test
    public void testJoinWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        assertEquals("one:1,two:2,three:3", joiner.join(map));
        assertEquals("one:1,two:2,three:3", joiner.appendTo(new StringBuilder(), map).toString());
        assertEquals("one:1,two:2,three:3", joiner.appendTo(new StringBuilder(), map.entrySet()).toString());
        assertEquals("one:1,two:2,three:3", joiner.appendTo(new StringBuilder(), map.entrySet().iterator()).toString());
    }
}