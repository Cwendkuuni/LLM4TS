package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.*;
import java.io.*;

public class JoinerTest {
    
    @Test
    public void testOnString() {
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        
        String[] parts = {"apple", "banana", "cherry"};
        String expected = "apple,banana,cherry";
        
        String actual = joiner.join(parts);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOnChar() {
        char separator = '-';
        Joiner joiner = Joiner.on(separator);
        
        String[] parts = {"apple", "banana", "cherry"};
        String expected = "apple-banana-cherry";
        
        String actual = joiner.join(parts);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testUseForNull() {
        String separator = ",";
        Joiner joiner = Joiner.on(separator).useForNull("null");
        
        String[] parts = {"apple", null, "cherry"};
        String expected = "apple,null,cherry";
        
        String actual = joiner.join(parts);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSkipNulls() {
        String separator = ",";
        Joiner joiner = Joiner.on(separator).skipNulls();
        
        String[] parts = {"apple", null, "cherry"};
        String expected = "apple,cherry";
        
        String actual = joiner.join(parts);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppendToIterable() throws IOException {
        StringBuilder builder = new StringBuilder();
        
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        
        List<String> parts = new ArrayList<>();
        parts.add("apple");
        parts.add("banana");
        parts.add("cherry");
        
        String expected = "apple,banana,cherry";
        
        joiner.appendTo(builder, parts);
        
        String actual = builder.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppendToIterator() throws IOException {
        StringBuilder builder = new StringBuilder();
        
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        
        List<String> parts = new ArrayList<>();
        parts.add("apple");
        parts.add("banana");
        parts.add("cherry");
        
        Iterator<String> iterator = parts.iterator();
        
        String expected = "apple,banana,cherry";
        
        joiner.appendTo(builder, iterator);
        
        String actual = builder.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppendToObjectArray() throws IOException {
        StringBuilder builder = new StringBuilder();
        
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        
        String[] parts = {"apple", "banana", "cherry"};
        
        String expected = "apple,banana,cherry";
        
        joiner.appendTo(builder, parts);
        
        String actual = builder.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppendToObjectFirstSecondRest() throws IOException {
        StringBuilder builder = new StringBuilder();
        
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        
        String expected = "apple,banana,cherry";
        
        joiner.appendTo(builder, "apple", "banana", "cherry");
        
        String actual = builder.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testJoinIterable() {
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        
        List<String> parts = new ArrayList<>();
        parts.add("apple");
        parts.add("banana");
        parts.add("cherry");
        
        String expected = "apple,banana,cherry";
        
        String actual = joiner.join(parts);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testJoinIterator() {
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        
        List<String> parts = new ArrayList<>();
        parts.add("apple");
        parts.add("banana");
        parts.add("cherry");
        
        Iterator<String> iterator = parts.iterator();
        
        String expected = "apple,banana,cherry";
        
        String actual = joiner.join(iterator);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testJoinObjectArray() {
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        
        String[] parts = {"apple", "banana", "cherry"};
        
        String expected = "apple,banana,cherry";
        
        String actual = joiner.join(parts);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testJoinTwoObjectsRestArray() {
        String separator = ",";
        Joiner joiner = Joiner.on(separator);
        
        String expected = "apple,banana,cherry";
        
        String actual = joiner.join("apple", "banana", "cherry");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        String separator = ",";
        String keyValueSeparator = "=";
        Joiner joiner = Joiner.on(separator).withKeyValueSeparator(keyValueSeparator);
        
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        
        String expected = "key1=value1,key2=value2,key3=value3";
        
        String actual = joiner.join(map.entrySet());
        
        assertEquals(expected, actual);
    }
}