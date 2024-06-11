package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.*;

public class JoinerTest {
    
    @Test
    public void testOn_withStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("test1,test2,test3", joiner.join(Arrays.asList("test1", "test2", "test3")));
    }
    
    @Test
    public void testOn_withCharSeparator() {
        Joiner joiner = Joiner.on(',');
        assertEquals("test1,test2,test3", joiner.join(Arrays.asList("test1", "test2", "test3")));
    }
    
    @Test
    public void testAppendTo_withIterableParts() {
        Joiner joiner = Joiner.on(",");
        List<String> partsList = Arrays.asList("test1", "test2", "test3");
        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, partsList);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        assertEquals("test1,test2,test3", builder.toString());
    }
    
    @Test
    public void testAppendTo_withIteratorParts() {
        Joiner joiner = Joiner.on(",");
        List<String> partsList = Arrays.asList("test1", "test2", "test3");
        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, partsList.iterator());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        assertEquals("test1,test2,test3", builder.toString());
    }
    
    @Test
    public void testAppendTo_withArrayParts() {
        Joiner joiner = Joiner.on(",");
        String[] partsArray = {"test1", "test2", "test3"};
        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, partsArray);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        assertEquals("test1,test2,test3", builder.toString());
    }
    
    @Test
    public void testAppendTo_withMultipleParts() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, "test1", "test2", "test3");
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        assertEquals("test1,test2,test3", builder.toString());
    }
    
    @Test
    public void testJoin_withIterableParts() {
        Joiner joiner = Joiner.on(",");
        List<String> partsList = Arrays.asList("test1", "test2", "test3");
        assertEquals("test1,test2,test3", joiner.join(partsList));
    }
    
    @Test
    public void testJoin_withIteratorParts() {
        Joiner joiner = Joiner.on(",");
        List<String> partsList = Arrays.asList("test1", "test2", "test3");
        assertEquals("test1,test2,test3", joiner.join(partsList.iterator()));
    }
    
    @Test
    public void testJoin_withArrayParts() {
        Joiner joiner = Joiner.on(",");
        String[] partsArray = {"test1", "test2", "test3"};
        assertEquals("test1,test2,test3", joiner.join(partsArray));
    }
    
    @Test
    public void testJoin_withMultipleParts() {
        Joiner joiner = Joiner.on(",");
        assertEquals("test1,test2,test3", joiner.join("test1", "test2", "test3"));
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        List<String> partsList = Arrays.asList("test1", null, "test3");
        assertEquals("test1,null,test3", joiner.join(partsList));
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        List<String> partsList = Arrays.asList("test1", null, "test3");
        assertEquals("test1,test3", joiner.join(partsList));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withMap() {
        Joiner joiner = Joiner.on(",").skipNulls();
        Map<String, String> partsMap = new HashMap<>();
        partsMap.put("key1", "value1");
        partsMap.put("key2", null);
        joiner.appendTo(new StringBuilder(), partsMap);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, String> partsMap = new HashMap<>();
        partsMap.put("key1", "value1");
        partsMap.put("key2", "value2");
        assertEquals("key1:value1,key2:value2", joiner.join(partsMap));
    }
    
    @Test
    public void testToString() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Joiner joiner = Joiner.on(",");
        Method toStringMethod = joiner.getClass().getDeclaredMethod("toString", Object.class);
        toStringMethod.setAccessible(true);
        
        assertEquals("test", toStringMethod.invoke(joiner, "test"));
        assertEquals("null", toStringMethod.invoke(joiner, null));
    }
}