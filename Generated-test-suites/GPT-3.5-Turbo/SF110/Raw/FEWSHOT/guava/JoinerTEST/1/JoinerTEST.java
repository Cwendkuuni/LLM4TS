package com.google.common.base;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JoinerTest {
    @Test
    public void testOnWithStringSeparator() {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, new Object[]{1, 2, 3});
        assertEquals("1, 2, 3", builder.toString());

        builder = joiner.appendTo(builder, new Object[]{});
        assertEquals("1, 2, 3", builder.toString());

        builder = joiner.appendTo(builder, new Object[]{4});
        assertEquals("1, 2, 3, 4", builder.toString());
    }

    @Test
    public void testOnWithCharSeparator() {
        Joiner joiner = Joiner.on(',');
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, new Object[]{1, 2, 3});
        assertEquals("1,2,3", builder.toString());

        builder = joiner.appendTo(builder, new Object[]{});
        assertEquals("1,2,3", builder.toString());

        builder = joiner.appendTo(builder, new Object[]{4});
        assertEquals("1,2,3,4", builder.toString());
    }
    
    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        Iterable<Integer> iterable = Arrays.asList(1, 2, 3);

        builder = joiner.appendTo(builder, iterable);
        assertEquals("1, 2, 3", builder.toString());
    }

    @Test
    public void testAppendToWithIterator() throws IOException {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();

        builder = joiner.appendTo(builder, iterator);
        assertEquals("1, 2, 3", builder.toString());
    }

    @Test
    public void testAppendToWithArray() throws IOException {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        Object[] array = new Object[]{1, 2, 3};

        builder = joiner.appendTo(builder, array);
        assertEquals("1, 2, 3", builder.toString());
    }

    @Test
    public void testAppendToWithObjects() throws IOException {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, 1, 2, 3);
        assertEquals("1, 2, 3", builder.toString());
    }

    @Test
    public void testAppendToWithFirstAndSecond() throws IOException {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, 1, 2);
        assertEquals("1, 2", builder.toString());
    }

    @Test
    public void testAppendToWithFirstAndSecondAndRest() throws IOException {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, 1, 2, 3, 4);
        assertEquals("1, 2, 3, 4", builder.toString());
    }

    @Test
    public void testAppendToWithNullAppendable() {
        Joiner joiner = Joiner.on(", ");
        Appendable appendable = null;

        assertThrows(NullPointerException.class, () -> {
            joiner.appendTo(appendable, new Object[]{1, 2, 3});
        });
    }

    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(", ");
        Iterable<Integer> iterable = Arrays.asList(1, 2, 3);

        assertEquals("1, 2, 3", joiner.join(iterable));
    }

    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(", ");
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();

        assertEquals("1, 2, 3", joiner.join(iterator));
    }

    @Test
    public void testJoinWithArray() {
        Joiner joiner = Joiner.on(", ");
        Object[] array = new Object[]{1, 2, 3};

        assertEquals("1, 2, 3", joiner.join(array));
    }

    @Test
    public void testJoinWithObjects() {
        Joiner joiner = Joiner.on(", ");

        assertEquals("1, 2, 3", joiner.join(1, 2, 3));
    }

    @Test
    public void testJoinWithFirstAndSecond() {
        Joiner joiner = Joiner.on(", ");

        assertEquals("1, 2", joiner.join(1, 2));
    }

    @Test
    public void testJoinWithFirstAndSecondAndRest() {
        Joiner joiner = Joiner.on(", ");

        assertEquals("1, 2, 3, 4", joiner.join(1, 2, 3, 4));
    }
}