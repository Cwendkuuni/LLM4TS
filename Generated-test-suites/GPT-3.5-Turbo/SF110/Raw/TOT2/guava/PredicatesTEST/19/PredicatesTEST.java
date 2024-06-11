package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import javax.annotation.Nullable;
import java.util.regex.Pattern;
import java.util.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(5));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(10));
    }

    @Test
    public void testIsNull() {
        Predicate<String> predicate = Predicates.isNull();
        assertTrue(predicate.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<String> predicate = Predicates.notNull();
        assertFalse(predicate.apply(null));
    }

    @Test
    public void testNot() {
        Predicate<Integer> predicate = Predicates.not(Predicates.isNull());
        assertTrue(predicate.apply(5));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> predicate1 = Predicates.notNull();
        Predicate<Integer> predicate2 = Predicates.alwaysTrue();
        Predicate<Integer> predicate = Predicates.and(predicate1, predicate2);
        assertTrue(predicate.apply(10));
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicate1 = Predicates.isNull();
        Predicate<Integer> predicate2 = Predicates.alwaysFalse();
        Predicate<Integer> predicate = Predicates.or(predicate1, predicate2);
        assertFalse(predicate.apply(null));
    }

    @Test
    public void testEqualTo() {
        Predicate<String> predicate = Predicates.equalTo("test");
        assertTrue(predicate.apply("test"));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> predicate = Predicates.instanceOf(Integer.class);
        assertTrue(predicate.apply(5));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> predicate = Predicates.assignableFrom(Number.class);
        assertTrue(predicate.apply(Integer.class));
    }

    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Predicate<Integer> predicate = Predicates.in(list);
        assertTrue(predicate.apply(2));
    }

    @Test
    public void testCompose() {
        Predicate<String> predicate1 = Predicates.notNull();
        Function<String, Integer> function = new Function<String, Integer>() {
            public Integer apply(String input) {
                return Integer.parseInt(input);
            }
        };
        Predicate<String> predicate = Predicates.compose(predicate1, function);
        assertTrue(predicate.apply("10"));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> predicate = Predicates.containsPattern("\\d+");
        assertTrue(predicate.apply("There are 5 apples"));
    }

    @Test
    public void testContains() {
        Pattern pattern = Pattern.compile("\\d+");
        Predicate<String> predicate = Predicates.contains(pattern);
        assertTrue(predicate.apply("There are 5 apples"));
    }
}