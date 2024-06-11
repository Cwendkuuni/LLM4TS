package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.junit.Test;

public class JoinerTest {
    
    @Test
    public void testOnStringSeparator() {
        Joiner j = Joiner.on(",");
        assertEquals("a,b,c", j.join("a", "b", "c"));
        assertEquals("a,b,c", j.join(new String[] {"a", "b", "c"}));
        assertEquals("a,b,c", j.join(new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private int index = 0;
                    
                    @Override
                    public boolean hasNext() {
                        return index < 3;
                    }
                    
                    @Override
                    public String next() {
                        switch (index) {
                            case 0:
                                index++;
                                return "a";
                            case 1:
                                index++;
                                return "b";
                            case 2:
                                index++;
                                return "c";
                            default:
                                return null;
                        }
                    }
                };
            }
        }));
    }
    
    @Test
    public void testOnCharSeparator() {
        Joiner j = Joiner.on(',');
        assertEquals("a,b,c", j.join("a", "b", "c"));
    }
    
    @Test
    public void testUseForNull() {
        Joiner j = Joiner.on(',').useForNull("null");
        assertEquals("a,null,c", j.join("a", null, "c"));
    }
    
    @Test
    public void testSkipNulls() {
        Joiner j = Joiner.on(',').skipNulls();
        assertEquals("a,c", j.join("a", null, "c"));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMap() {
        Joiner j = Joiner.on(',').skipNulls();
        j.withKeyValueSeparator("=");
    }
    
    @Test
    public void testAppendTo() throws IOException {
        StringBuilder sb = new StringBuilder();
        Joiner j = Joiner.on(',');
        j.appendTo(sb, "a");
        j.appendTo(sb, new String[] {"b", "c"});
        assertEquals("a,b,c", sb.toString());
    }
    
    @Test
    public void testJoin() {
        Joiner j = Joiner.on(',');
        assertEquals("a,b,c", j.join("a", "b", "c"));
        assertEquals("a,b,c", j.join(new String[] {"a", "b", "c"}));
        assertEquals("a,b,c", j.join(new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private int index = 0;
                    
                    @Override
                    public boolean hasNext() {
                        return index < 3;
                    }
                    
                    @Override
                    public String next() {
                        switch (index) {
                            case 0:
                                index++;
                                return "a";
                            case 1:
                                index++;
                                return "b";
                            case 2:
                                index++;
                                return "c";
                            default:
                                return null;
                        }
                    }
                };
            }
        }));
    }
    
    @Test
    public void testAppendToMap() throws IOException {
        StringBuilder sb = new StringBuilder();
        Joiner.MapJoiner j = Joiner.on(',').withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        j.appendTo(sb, map);
        assertEquals("a=1,b=2,c=3", sb.toString());
    }
    
    @Test
    public void testJoinMap() {
        Joiner.MapJoiner j = Joiner.on(',').withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        assertEquals("a=1,b=2,c=3", j.join(map));
    }
    
    @Test
    public void testJoinNull() {
        Joiner j = Joiner.on(',');
        assertNull(j.join((Object[]) null));
    }
    
    @Test
    public void testAppendToNull() throws IOException {
        StringBuilder sb = new StringBuilder();
        Joiner j = Joiner.on(',');
        j.appendTo(sb, (Object[]) null);
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testUseForNullNullText() {
        Joiner j = Joiner.on(',').useForNull(null);
        assertNull(j.join("a", null, "c"));
    }
}