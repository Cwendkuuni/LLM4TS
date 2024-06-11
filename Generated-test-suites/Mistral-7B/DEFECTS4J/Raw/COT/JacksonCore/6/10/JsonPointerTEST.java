package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {
    

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer._nextSegment);
        assertEquals("", pointer._asString);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertTrue(pointer.matches());
    }

    @Test
    public void testConstructorWithSegments() {
        String fullString = "/foo/bar/baz";
        String segment = "bar";
        JsonPointer pointer = new JsonPointer(fullString, segment, new JsonPointer("/qux"));
        assertEquals(fullString, pointer._asString);
        assertEquals("/qux", pointer._nextSegment._asString);
        assertEquals("bar", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertFalse(pointer.matches());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenInputDoesNotStartWithSlash() {
        JsonPointer.compile("foo");
    }

    @Test
    public void testCompileMethod() throws IllegalArgumentException {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer._asString);
        assertEquals("/baz", pointer.tail()._asString);
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.matches());
    }

    @Test
    public void testValueOfMethod() throws IllegalArgumentException {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        assertEquals("/foo/bar/baz", JsonPointer.valueOf(pointer._asString)._asString);
    }

    @Test
    public void testFromSegmentMethod() {
        // TODO: Implement this method and write test cases.
    }

    @Test
    public void testMatchesMethod() {
        JsonPointer pointer = new JsonPointer("/foo/bar/baz");
        assertTrue(pointer.matches());
        pointer = new JsonPointer("/foo/bar");
        assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingPropertyMethod() {
        JsonPointer pointer = new JsonPointer("/foo/bar/baz");
        assertEquals("bar", pointer.getMatchingProperty());
        pointer = new JsonPointer("/foo/qux");
        assertNull(pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndexMethod() {
        JsonPointer pointer = new JsonPointer("/foo/0/bar");
        assertEquals(0, pointer.getMatchingIndex());
        pointer = new JsonPointer("/foo/1/bar");
        assertEquals(1, pointer.getMatchingIndex());
        pointer = new JsonPointer("/foo/bar");
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchPropertyMethod() {
        JsonPointer pointer = new JsonPointer("/foo/bar/baz");
        JsonPointer match = pointer.matchProperty("bar");
        assertNotNull(match);
        assertEquals("/baz", match._asString);
        match = pointer.matchProperty("qux");
        assertNull(match);
    }

    @Test
    public void testMatchElementMethod() {
        JsonPointer pointer = new JsonPointer("/foo/0/bar");
        JsonPointer match = pointer.matchElement(0);
        assertNotNull(match);
        assertEquals("/bar", match._asString);
        match = pointer.matchElement(1);
        assertNull(match);
    }

    @Test
    public void testTailMethod() {
        JsonPointer pointer = new JsonPointer("/foo/bar/baz");
        assertEquals("/bar", pointer.tail()._asString);
        assertEquals("/baz", pointer.tail().tail()._asString);
        pointer = JsonPointer.EMPTY;
        assertEquals(null, pointer.tail());
    }

    @Test
    public void testToStringMethod() {
        JsonPointer pointer = new JsonPointer("/foo/bar/baz");
        assertEquals("/foo/bar/baz", pointer.toString());
        pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
    }

    @Test
    public void testHashCodeMethod() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar/baz");
        JsonPointer pointer2 = new JsonPointer("/foo/bar/baz");
        JsonPointer pointer3 = new JsonPointer("/qux/quux/quux");
        int hashCode1 = pointer1.hashCode();
        int hashCode2 = pointer2.hashCode();
        int hashCode3 = pointer3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testEqualsMethod() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar/baz");
        JsonPointer pointer2 = new JsonPointer("/foo/bar/baz");
        JsonPointer pointer3 = new JsonPointer("/qux/quux/quux");
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        JsonPointer pointer4 = new JsonPointer("/foo/bar/baz");
        assertTrue(pointer1.equals(pointer4));
    }

    @Test
    public void testParseIndexMethod() {
        assertEquals(0, JsonPointer._parseIndex("0"));
        assertEquals(42, JsonPointer._parseIndex("42"));
        assertEquals(-1, JsonPointer._parseIndex(""));
        assertEquals(-1, JsonPointer._parseIndex("foo"));
        assertEquals(-1, JsonPointer._parseIndex("~"));
        assertEquals(-1, JsonPointer._parseIndex("~/"));
        assertEquals(-1, JsonPointer._parseIndex("/qux"));
    }

    @Test
    public void testParseTailMethod() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer._parseTail(input);
        assertEquals("/baz", pointer._asString);
        assertEquals("/", pointer.tail()._asString);

        input = "/foo/~/bar/baz";
        pointer = JsonPointer._parseTail(input);
        assertEquals("/bar/baz", pointer._asString);
        assertEquals("/", pointer.tail()._asString);

        input = "/foo/bar/~/baz";
        pointer = JsonPointer._parseTail(input);
        assertEquals("/baz", pointer._asString);
        assertEquals("/bar", pointer.tail()._asString);

        input = "/foo/bar/~/~/baz";
        pointer = JsonPointer._parseTail(input);
        assertEquals("/baz", pointer._asString);
        assertEquals("/bar", pointer.tail()._asString);
        assertEquals("/", pointer.tail().tail()._asString);

        input = "/foo/bar/~/~/~/baz";
        pointer = JsonPointer._parseTail(input);
        assertEquals("/baz", pointer._asString);
        assertEquals("/bar", pointer.tail()._asString);
        assertEquals("/", pointer.tail().tail()._asString);
        assertEquals("/", pointer.tail().tail().tail()._asString);
    }

    @Test
    public void testParseQuotedTailMethod() {
        String input = "/foo/~0/bar/baz";
        JsonPointer pointer = JsonPointer._parseQuotedTail(input, 3);
        assertEquals("/bar/baz", pointer._asString);
        assertEquals("/", pointer.tail()._asString);

        input = "/foo/~1/bar/baz";
        pointer = JsonPointer._parseQuotedTail(input, 3);
        assertEquals("/bar/baz", pointer._asString);
        assertEquals("/1", pointer.tail()._asString);
        assertEquals("/foo/", pointer.tail().tail()._asString);

        input = "/foo/~/bar/baz";
        pointer = JsonPointer._parseQuotedTail(input, 2);
        assertEquals("/bar/baz", pointer._asString);
        assertEquals("/", pointer.tail()._asString);
        assertEquals("/foo", pointer.tail().tail()._asString);

        input = "/foo/~/~/bar/baz";
        pointer = JsonPointer._parseQuotedTail(input, 3);
        assertEquals("/bar/baz", pointer._asString);
        assertEquals("/", pointer.tail()._asString);
        assertEquals("/foo", pointer.tail().tail()._asString);
        assertEquals("/", pointer.tail().tail().tail()._asString);
    }

    @Test
    public void testAppendEscapeMethod() {
        StringBuilder sb = new StringBuilder();
        _appendEscape(sb, '0');
        assertEquals("~0", sb.toString());

        _appendEscape(sb, '1');
        assertEquals("/", sb.toString());

        _appendEscape(sb, '~');
        assertEquals("~", sb.toString());
    }

    
}