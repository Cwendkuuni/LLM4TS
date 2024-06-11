package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(new Object()));
        assertTrue(predicate.apply(null));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(new Object()));
        assertFalse(predicate.apply(null));
    }

    // Additional test cases for other methods can be added here

    // Edge cases and exception handling scenarios should be tested here

}