package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;

        Assert.assertTrue(jsonPointer.matches());
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(!jsonPointer.mayMatchProperty());
        Assert.assertTrue(!jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("test"));
        Assert.assertNull(jsonPointer.matchElement(0));

        JsonPointer tail = jsonPointer.tail();
        Assert.assertNull(tail);
    }

    @Test
    public void testNonEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar", "foo", null);

        Assert.assertTrue(!jsonPointer.matches());
        Assert.assertEquals("foo", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertTrue(!jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("test"));
        Assert.assertNull(jsonPointer.matchElement(0));

        JsonPointer tail = jsonPointer.tail();
        Assert.assertNull(tail);
    }

    @Test
    public void testNonEmptyJsonPointerWithNextSegment() {
        JsonPointer nextSegment = new JsonPointer("/baz/qux", "baz", null);
        JsonPointer jsonPointer = new JsonPointer("/foo/bar", "foo", nextSegment);

        Assert.assertTrue(!jsonPointer.matches());
        Assert.assertEquals("foo", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertTrue(!jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("test"));
        Assert.assertNull(jsonPointer.matchElement(0));

        JsonPointer tail = jsonPointer.tail();
        Assert.assertEquals(nextSegment, tail);
    }

    @Test
    public void testMatchProperty() {
        JsonPointer nextSegment = new JsonPointer("/baz/qux", "baz", null);
        JsonPointer jsonPointer = new JsonPointer("/foo/bar", "foo", nextSegment);

        JsonPointer matchProperty = jsonPointer.matchProperty("foo");
        Assert.assertNull(matchProperty);

        matchProperty = jsonPointer.matchProperty("baz");
        Assert.assertEquals(nextSegment, matchProperty);
    }

    @Test
    public void testMatchElement() {
        JsonPointer nextSegment = new JsonPointer("/baz/qux", "baz", null);
        JsonPointer jsonPointer = new JsonPointer("/foo/bar", "foo", nextSegment);

        JsonPointer matchElement = jsonPointer.matchElement(0);
        Assert.assertNull(matchElement);

        matchElement = nextSegment.matchElement(1);
        Assert.assertNull(matchElement);

        matchElement = nextSegment.matchElement(-1);
        Assert.assertNull(matchElement);

        matchElement = nextSegment.matchElement(2);
        Assert.assertNull(matchElement);

        matchElement = nextSegment.matchElement(0);
        Assert.assertEquals(nextSegment, matchElement);
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar", "foo", null);
        Assert.assertEquals("/foo/bar", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = new JsonPointer("/foo/bar", "foo", null);
        JsonPointer jsonPointer2 = new JsonPointer("/foo/bar", "foo", null);

        Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("/foo/bar", "foo", null);
        JsonPointer jsonPointer2 = new JsonPointer("/foo/bar", "foo", null);
        JsonPointer jsonPointer3 = new JsonPointer("/foo/bar", "bar", null);

        Assert.assertTrue(jsonPointer1.equals(jsonPointer2));
        Assert.assertFalse(jsonPointer1.equals(jsonPointer3));
        Assert.assertFalse(jsonPointer1.equals(null));
        Assert.assertFalse(jsonPointer1.equals(new Object()));
    }

    @Test
    public void testCompileEmpty() {
        JsonPointer jsonPointer = JsonPointer.compile("");

        Assert.assertTrue(jsonPointer.matches());
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(!jsonPointer.mayMatchProperty());
        Assert.assertTrue(!jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("test"));
        Assert.assertNull(jsonPointer.matchElement(0));

        JsonPointer tail = jsonPointer.tail();
        Assert.assertNull(tail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidStart() {
        JsonPointer.compile("foo");
    }

    @Test
    public void testCompileValidJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");

        Assert.assertTrue(!jsonPointer.matches());
        Assert.assertEquals("foo", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertTrue(!jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("test"));
        Assert.assertNull(jsonPointer.matchElement(0));

        JsonPointer tail = jsonPointer.tail();
        Assert.assertNull(tail);
    }

    @Test
    public void testCompileWithNextSegment() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar/baz/qux");

        Assert.assertTrue(!jsonPointer.matches());
        Assert.assertEquals("foo", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertTrue(!jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("test"));
        Assert.assertNull(jsonPointer.matchElement(0));

        JsonPointer tail = jsonPointer.tail();
        Assert.assertNotNull(tail);

        Assert.assertTrue(!tail.matches());
        Assert.assertEquals("bar", tail.getMatchingProperty());
        Assert.assertEquals(-1, tail.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertTrue(!jsonPointer.mayMatchElement());
        Assert.assertNull(jsonPointer.matchProperty("test"));
        Assert.assertNull(jsonPointer.matchElement(0));

        JsonPointer next = tail.tail();
        Assert.assertNull(next);

        JsonPointer match = jsonPointer.matchProperty("foo");
        Assert.assertNull(match);
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer1 = JsonPointer.valueOf("/foo");
        JsonPointer jsonPointer2 = JsonPointer.compile("/foo");

        Assert.assertEquals(jsonPointer1, jsonPointer2);
    }

    @Test
    public void testAppendEscape() {
        StringBuilder stringBuilder = new StringBuilder();
        JsonPointer._appendEscape(stringBuilder, '0');
        Assert.assertEquals("~0", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        JsonPointer._appendEscape(stringBuilder, '1');
        Assert.assertEquals("~1", stringBuilder.toString());

        stringBuilder = new StringBuilder();
        JsonPointer._appendEscape(stringBuilder, '2');
        Assert.assertEquals("~2", stringBuilder.toString());
    }

    @Test
    public void testParseIndex() {
        int index = JsonPointer._parseIndex("");
        Assert.assertEquals(-1, index);

        index = JsonPointer._parseIndex("foo");
        Assert.assertEquals(-1, index);

        index = JsonPointer._parseIndex("12345678901");
        Assert.assertEquals(-1, index);

        index = JsonPointer._parseIndex("1234567890");
        Assert.assertEquals(1234567890, index);

        index = JsonPointer._parseIndex("2147483647");
        Assert.assertEquals(2147483647, index);
    }
}