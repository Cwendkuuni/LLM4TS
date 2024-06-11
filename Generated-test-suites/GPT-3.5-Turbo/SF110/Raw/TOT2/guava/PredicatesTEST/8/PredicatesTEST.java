package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(5));
        assertTrue(predicate.apply(null));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<String> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply("test"));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> predicate = Predicates.isNull();
        assertTrue(predicate.apply(null));
        assertFalse(predicate.apply("not null"));
    }

    @Test
    public void testNotNull() {
        Predicate<String> predicate = Predicates.notNull();
        assertTrue(predicate.apply("not null"));
        assertFalse(predicate.apply(null));
    }

    // Additional test cases for other methods should be added here

}