package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JoinerTest {
    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(new Integer[] {1, 2, 3}));
        assertEquals("", joiner.join(new Integer[] {}));
        assertNull(joiner.join((Integer[]) null));
    }

    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on(',');
        assertEquals("a,b,c", joiner.join(new Character[] {'a', 'b', 'c'}));
        assertEquals("", joiner.join(new Character[] {}));
        assertNull(joiner.join((Character[]) null));
    }

    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), new Character[] {'a', 'b', 'c'}).toString());
        assertEquals("", joiner.appendTo(new StringBuilder(), new Character[] {}).toString());
        assertEquals("abc", joiner.appendTo(new StringBuilder(), new Character[] {'a', 'b', 'c'}).toString());
        assertNull(joiner.appendTo(new StringBuilder(), (Character[]) null));
    }

    @Test
    public void testAppendToIterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder(), new Character[] {'a', 'b', 'c'}).toString());
        assertEquals("", joiner.appendTo(new StringBuilder(), new Character[] {}).toString());
        assertEquals("abc", joiner.appendTo(new StringBuilder(), new Character[] {'a', 'b', 'c'}).toString());
        assertNull(joiner.appendTo(new StringBuilder(), (Character[]) null));
    }

    @Test
    public void testAppendToObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.appendTo(new StringBuilder(), new Integer[] {1, 2, 3}).toString());
        assertEquals("", joiner.appendTo(new StringBuilder(), new Integer[] {}).toString());
        assertNull(joiner.appendTo(new StringBuilder(), (Integer[]) null));
    }

    @Test
    public void testAppendWithObjects() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.appendTo(new StringBuilder(), 1, 2, 3).toString());
        assertEquals("a,b,1,2,3", joiner.appendTo(new StringBuilder(), "a", "b", 1, 2, 3).toString());
        assertEquals("a,b,1,2,3", joiner.appendTo(new StringBuilder(), "a", "b", new Integer[] {1, 2, 3}).toString());
        assertNull(joiner.appendTo(new StringBuilder(), (Object[]) null));
    }

    @Test
    public void testAppendToStringBuilderIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder("abc"), new Character[] {'a', 'b', 'c'}).toString());
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder("abc"), new Character[] {'a', 'b', 'c'}).toString());
        assertEquals("abc", joiner.appendTo(new StringBuilder("abc"), new Character[] {}).toString());
        assertNull(joiner.appendTo(new StringBuilder("abc"), (Character[]) null));
    }

    @Test
    public void testAppendToStringBuilderIterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder("abc"), new Character[] {'a', 'b', 'c'}).toString());
        assertEquals("a,b,c", joiner.appendTo(new StringBuilder("abc"), new Character[] {'a', 'b', 'c'}).toString());
        assertEquals("abc", joiner.appendTo(new StringBuilder("abc"), new Character[] {}).toString());
        assertNull(joiner.appendTo(new StringBuilder("abc"), (Character[]) null));
    }

    @Test
    public void testAppendToStringBuilderObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.appendTo(new StringBuilder("abc"), new Integer[] {1, 2, 3}).toString());
        assertEquals("1,2,3", joiner.appendTo(new StringBuilder("abc"), new Integer[] {1, 2, 3}).toString());
        assertEquals("abc", joiner.appendTo(new StringBuilder("abc"), new Integer[] {}).toString());
        assertNull(joiner.appendTo(new StringBuilder("abc"), (Integer[]) null));
    }

    @Test
    public void testAppendToStringBuilderWithObjects() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.appendTo(new StringBuilder("abc"), 1, 2, 3).toString());
        assertEquals("a,b,1,2,3", joiner.appendTo(new StringBuilder("abc"), "a", "b", 1, 2, 3).toString());
        assertEquals("a,b,1,2,3", joiner.appendTo(new StringBuilder("abc"), "a", "b", new Integer[] {1, 2, 3}).toString());
        assertNull(joiner.appendTo(new StringBuilder("abc"), (Object[]) null));
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList(new Character[] {'a', 'b', 'c'})));
        assertEquals("", joiner.join(Arrays.asList(new Character[] {})));
        assertNull(joiner.join((Iterable<?>) null));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList(new Character[] {'a', 'b', 'c'}).iterator()));
        assertEquals("", joiner.join(Arrays.asList(new Character[] {}).iterator()));
        assertNull(joiner.join((Iterator<?>) null));
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(new Integer[] {1, 2, 3}));
        assertEquals("", joiner.join(new Integer[] {}));
        assertNull(joiner.join((Integer[]) null));
    }

    @Test
    public void testJoinWithObjects() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(1, 2, 3));
        assertEquals("a,b,1,2,3", joiner.join("a", "b", 1, 2, 3));
        assertEquals("a,b,1,2,3", joiner.join("a", "b", new Integer[] {1, 2, 3}));
        assertNull(joiner.join((Object[]) null));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        assertEquals("a,null,c", joiner.join(Arrays.asList(new Character[] {'a', null, 'c'})));
        assertEquals("null,null,null", joiner.join(Arrays.asList(new String[] {null, null, null})));
        assertEquals("null,null,null", joiner.join((String[]) null));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertEquals("a,c", joiner.join(Arrays.asList(new Character[] {'a', null, 'c'})));
        assertEquals("", joiner.join(Arrays.asList(new String[] {null, null, null})));
        assertNull(joiner.join((String[]) null));
    }
}