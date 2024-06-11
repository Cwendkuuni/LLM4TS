package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

public class JoinerTest {

    @Test
    public void testOnString() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        assertEquals("one,two,three", joiner.join(list));
    }

    @Test
    public void testOnChar() throws IOException {
        Joiner joiner = Joiner.on(',');
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        assertEquals("one,two,three", joiner.join(list));
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list);
        assertEquals("one,two,three", builder.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());
        assertEquals("one,two,three", builder.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        String[] array = {"one", "two", "three"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);
        assertEquals("one,two,three", builder.toString());
    }

    @Test
    public void testAppendToObjects() throws IOException {
        Joiner joiner = Joiner.on(",");
        String first = "one";
        String second = "two";
        String third = "three";
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, first, second, third);
        assertEquals("one,two,three", builder.toString());
    }

    @Test
    public void testAppendToStringBuilder() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list);
        assertEquals("one,two,three", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());
        assertEquals("one,two,three", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithArray() {
        Joiner joiner = Joiner.on(",");
        String[] array = {"one", "two", "three"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);
        assertEquals("one,two,three", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithObjects() {
        Joiner joiner = Joiner.on(",");
        String first = "one";
        String second = "two";
        String third = "three";
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, first, second, third);
        assertEquals("one,two,three", builder.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        assertEquals("one,two,three", joiner.join(list));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        assertEquals("one,two,three", joiner.join(list.iterator()));
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        String[] array = {"one", "two", "three"};
        assertEquals("one,two,three", joiner.join(array));
    }

    @Test
    public void testJoinObjects() {
        Joiner joiner = Joiner.on(",");
        String first = "one";
        String second = "two";
        String third = "three";
        assertEquals("one,two,three", joiner.join(first, second, third));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add(null);
        list.add("three");
        assertEquals("one,NULL,three", joiner.join(list));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add(null);
        list.add("three");
        assertEquals("one,three", joiner.join(list));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMap() {
        Joiner joiner = Joiner.on(",").skipNulls();
        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        map.put("two", null);
        map.put("three", "3");
        joiner.join(map);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");
        assertEquals("one:1,two:2,three:3", joiner.join(map));
    }

}