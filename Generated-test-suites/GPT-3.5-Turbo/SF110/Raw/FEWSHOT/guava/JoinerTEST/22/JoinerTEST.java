
package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.IOException;
import java.util.*;

import javax.annotation.Nullable;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public class JoinerTest {

    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3,4,5", joiner.join(Arrays.asList(1, 2, 3, 4, 5)));
        assertEquals("foo,bar,baz", joiner.join(Arrays.asList("foo", "bar", "baz")));
    }

    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on('-');
        assertEquals("1-2-3-4-5", joiner.join(Arrays.asList(1, 2, 3, 4, 5)));
        assertEquals("foo-bar-baz", joiner.join(Arrays.asList("foo", "bar", "baz")));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(',').useForNull("null");
        assertEquals("1,null,3,null,5", joiner.join(Arrays.asList(1, null, 3, null, 5)));
        assertEquals("foo,null,baz,null,qux", joiner.join(Arrays.asList("foo", null, "baz", null, "qux")));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(',').skipNulls();
        assertEquals("1,3,5", joiner.join(Arrays.asList(1, null, 3, null, 5)));
        assertEquals("foo,baz,qux", joiner.join(Arrays.asList("foo", null, "baz", null, "qux")));
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals("1,2,3,4,5", joiner.join(numbers));
        Set<String> names = new HashSet<>(Arrays.asList("John", "Mary", "David"));
        assertEquals("John,Mary,David", joiner.join(names));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals("1,2,3,4,5", joiner.join(numbers.iterator()));
        Set<String> names = new HashSet<>(Arrays.asList("John", "Mary", "David"));
        assertEquals("John,Mary,David", joiner.join(names.iterator()));
    }

    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(",");
        Integer[] numbers = {1, 2, 3, 4, 5};
        assertEquals("1,2,3,4,5", joiner.join(numbers));
        String[] names = {"John", "Mary", "David"};
        assertEquals("John,Mary,David", joiner.join(names));
    }

    @Test
    public void testJoinWithNulls() {
        Joiner joiner = Joiner.on(", ");
        assertNull(joiner.join((Iterable<?>)null));
        assertNull(joiner.join((Iterator<?>)null));
        assertNull(joiner.join((Object[])null));
        assertNull(joiner.join((Object)null, null, (Object)null));
    }

    @Test
    public void testJoinWithFirstSecondAndRest() {
        Joiner joiner = Joiner.on(", ");
        assertEquals("1, 2, 3, 4, 5", joiner.join(1, 2, 3, 4, 5));
        assertEquals("foo, bar, baz", joiner.join("foo", "bar", "baz"));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator("=");
        Map<String, Integer> ages = new LinkedHashMap<>();
        ages.put("John", 25);
        ages.put("Mary", 30);
        ages.put("David", 35);
        assertEquals("John=25, Mary=30, David=35", mapJoiner.join(ages));
    }

    @Beta
    @Test
    public void testMapJoiner() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator("=");
        Map<String, Integer> ages = new LinkedHashMap<>();
        ages.put("John", 25);
        ages.put("Mary", 30);
        ages.put("David", 35);
        assertEquals("John=25, Mary=30, David=35", mapJoiner.join(ages.entrySet()));
    }

    @Beta
    @Test
    public void testMapJoinerWithNulls() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator("=");
        assertNull(mapJoiner.join((Map<?, ?>)null));
    }

}
