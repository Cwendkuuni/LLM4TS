package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(10));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<String> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply("hello"));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> predicate = Predicates.isNull();
        assertTrue(predicate.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> predicate = Predicates.notNull();
        assertTrue(predicate.apply(5));
    }

    @Test
    public void testNot() {
        Predicate<Integer> predicate = Predicates.notNull();
        Predicate<Integer> notPredicate = Predicates.not(predicate);
        assertFalse(notPredicate.apply(null));
    }

    // Add more test cases for other methods in Predicates class

}