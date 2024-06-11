package com.google.common.base;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.io.*;
import javax.annotation.*;
import java.util.*;
import com.google.common.annotations.*;

public class JoinerTest {
    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        String[] input = {"one", "two", "three"};
        String expectedOutput = "one,two,three";
        String actualOutput = joiner.join(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(';');
        String[] input = {"one", "two", "three"};
        String expectedOutput = "one;two;three";
        String actualOutput = joiner.join(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> input = Arrays.asList("one", "two", "three");
        StringBuilder builder = new StringBuilder();
        String expectedOutput = "one,two,three";
        joiner.appendTo(builder, input);
        String actualOutput = builder.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> input = Arrays.asList("one", "two", "three");
        StringBuilder builder = new StringBuilder();
        String expectedOutput = "one,two,three";
        joiner.appendTo(builder, input.iterator());
        String actualOutput = builder.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        String[] input = {"one", "two", "three"};
        StringBuilder builder = new StringBuilder();
        String expectedOutput = "one,two,three";
        joiner.appendTo(builder, input);
        String actualOutput = builder.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAppendToObjectVarargs() throws IOException {
        Joiner joiner = Joiner.on(",");
        String expectedOutput = "one,two,three";
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "one", "two", "three");
        String actualOutput = builder.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAppendToStringBuilderIterable() {
        Joiner joiner = Joiner.on(",");
        List<String> input = Arrays.asList("one", "two", "three");
        StringBuilder builder = new StringBuilder();
        String expectedOutput = "one,two,three";
        joiner.appendTo(builder, input);
        String actualOutput = builder.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAppendToStringBuilderIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> input = Arrays.asList("one", "two", "three");
        StringBuilder builder = new StringBuilder();
        String expectedOutput = "one,two,three";
        joiner.appendTo(builder, input.iterator());
        String actualOutput = builder.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAppendToStringBuilderObjectArray() {
        Joiner joiner = Joiner.on(",");
        String[] input = {"one", "two", "three"};
        StringBuilder builder = new StringBuilder();
        String expectedOutput = "one,two,three";
        joiner.appendTo(builder, input);
        String actualOutput = builder.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAppendToStringBuilderObjectVarargs() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "one", "two", "three");
        String expectedOutput = "one,two,three";
        String actualOutput = builder.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        List<String> input = Arrays.asList("one", "two", "three");
        String expectedOutput = "one,two,three";
        String actualOutput = joiner.join(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> input = Arrays.asList("one", "two", "three");
        String expectedOutput = "one,two,three";
        String actualOutput = joiner.join(input.iterator());
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        String[] input = {"one", "two", "three"};
        String expectedOutput = "one,two,three";
        String actualOutput = joiner.join(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testJoinObjectVarargs() {
        Joiner joiner = Joiner.on(",");
        String expectedOutput = "one,two,three";
        String actualOutput = joiner.join("one", "two", "three");
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        String[] input = {"one", null, "three"};
        String expectedOutput = "one,null,three";
        String actualOutput = joiner.join(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String[] input = {"one", null, "three"};
        String expectedOutput = "one,three";
        String actualOutput = joiner.join(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsMapJoiner() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").skipNulls().withKeyValueSeparator("=");
        Map<String, String> input = new HashMap<>();
        input.put("one", "1");
        input.put("two", null);
        input.put("three", "3");
        mapJoiner.join(input);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, String> input = new HashMap<>();
        input.put("one", "1");
        input.put("two", "2");
        input.put("three", "3");
        String expectedOutput = "one=1,two=2,three=3";
        String actualOutput = mapJoiner.join(input);
        assertEquals(expectedOutput, actualOutput);
    }
}