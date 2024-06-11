package com.google.common.base;
import static org.junit.Assert.*;
import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<String> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply("Test"));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(5));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> predicate = Predicates.isNull();
        assertTrue(predicate.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<String> predicate = Predicates.notNull();
        assertFalse(predicate.apply(null));
        assertTrue(predicate.apply("Test"));
    }

    @Test
    public void testNot() {
        Predicate<Integer> lessThanTen = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input < 10;
            }
        };
        Predicate<Integer> notLessThanTen = Predicates.not(lessThanTen);
        assertFalse(notLessThanTen.apply(5));
        assertTrue(notLessThanTen.apply(15));
    }

    // Additional test cases for other methods can be added here

}