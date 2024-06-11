package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        list.add("baz");

        assertEquals("foo,bar,baz", joiner.join(list));
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(';');
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        list.add("baz");

        assertEquals("foo;bar;baz", joiner.join(list));
    }

    @Test
    public void testOnNullSeparator() {
        Joiner joiner = Joiner.on((String) null);
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        list.add("baz");

        assertEquals("foo,bar,baz", joiner.join(list));
    }

    @Test
    public void testAppendToAppendable() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        list.add("baz");

        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("foo,bar,baz", builder.toString());
    }

    @Test
    public void testAppendToIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        list.add("baz");

        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());

        assertEquals("foo,bar,baz", builder.toString());
    }

    @Test
    public void testAppendToArray() {
        Joiner joiner = Joiner.on(",");
        String[] array = {"foo", "bar", "baz"};

        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, array);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("foo,bar,baz", builder.toString());
    }

    @Test
    public void testAppendToWithNulls() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add(null);
        list.add("baz");

        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("foo,null,baz", builder.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        list.add("baz");

        assertEquals("foo,bar,baz", joiner.join(list));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        list.add("baz");

        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());

        assertEquals("foo,bar,baz", builder.toString());
    }

    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(",");
        String[] array = {"foo", "bar", "baz"};

        assertEquals("foo,bar,baz", joiner.join(array));
    }

    @Test
    public void testJoinWithNulls() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add(null);
        list.add("baz");

        assertEquals("foo,null,baz", joiner.join(list));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("N/A");
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add(null);
        list.add("baz");

        assertEquals("foo,N/A,baz", joiner.join(list));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add(null);
        list.add("baz");

        assertEquals("foo,baz", joiner.join(list));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        assertEquals("key1=value1,key2=value2", mapJoiner.join(map));
    }

    @Test
    public void testUseForNullWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=").useForNull("N/A");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);

        assertEquals("key1=value1,key2=N/A", mapJoiner.join(map));
    }

}