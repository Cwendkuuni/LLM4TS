package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        Assert.assertTrue(jsonPointer.matches());
        Assert.assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSegments() {
        JsonPointer jsonPointer = new JsonPointer("1/2/3", "1", new JsonPointer("1/2", "2", new JsonPointer("1", "3", null)));
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("1/2/3", jsonPointer.toString());
    }

    @Test
    public void testCompileValidJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/1/2/3");
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("/1/2/3", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("1/2/3");
    }

    @Test
    public void testValueOfValidJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/1/2/3");
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("/1/2/3", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalidJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.valueOf("1/2/3");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("1/2/3", "1", new JsonPointer("1/2", "2", new JsonPointer("1", "3", null)));
        Assert.assertNull(jsonPointer.matchProperty("0"));
        Assert.assertNotNull(jsonPointer.matchProperty("1"));
        Assert.assertEquals("1/2", jsonPointer.matchProperty("1").toString());
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("1/2/3", "1", new JsonPointer("1/2", "2", new JsonPointer("1", "3", null)));
        Assert.assertNull(jsonPointer.matchElement(0));
        Assert.assertNotNull(jsonPointer.matchElement(2));
        Assert.assertEquals("1/2/3", jsonPointer.matchElement(2).toString());
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer("1/2/3", "1", new JsonPointer("1/2", "2", new JsonPointer("1", "3", null)));
        Assert.assertEquals("1/2", jsonPointer.tail().toString());
        Assert.assertEquals("2", jsonPointer.tail().tail().toString());
        Assert.assertEquals("3", jsonPointer.tail().tail().tail().toString());
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("1/2/3", "1", new JsonPointer("1/2", "2", new JsonPointer("1", "3", null)));
        Assert.assertEquals("1/2/3", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = new JsonPointer("1/2/3", "1", new JsonPointer("1/2", "2", new JsonPointer("1", "3", null)));
        JsonPointer jsonPointer2 = new JsonPointer("1/2/3", "1", new JsonPointer("1/2", "2", new JsonPointer("1", "3", null)));
        Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("1/2/3", "1", new JsonPointer("1/2", "2", new JsonPointer("1", "3", null)));
        JsonPointer jsonPointer2 = new JsonPointer("1/2/3", "1", new JsonPointer("1/2", "2", new JsonPointer("1", "3", null)));
        JsonPointer jsonPointer3 = new JsonPointer();
        Assert.assertTrue(jsonPointer1.equals(jsonPointer1));
        Assert.assertTrue(jsonPointer1.equals(jsonPointer2));
        Assert.assertFalse(jsonPointer1.equals(jsonPointer3));
        Assert.assertFalse(jsonPointer1.equals(null));
        Assert.assertFalse(jsonPointer1.equals("String"));
    }

    @Test
    public void test_parseIndex() {
        int index1 = JsonPointer._parseIndex("12");
        int index2 = JsonPointer._parseIndex("1");
        int index3 = JsonPointer._parseIndex("123456789");
        int index4 = JsonPointer._parseIndex("012");
        int index5 = JsonPointer._parseIndex("01");
        int index6 = JsonPointer._parseIndex("0");
        Assert.assertEquals(12, index1);
        Assert.assertEquals(1, index2);
        Assert.assertEquals(123456789, index3);
        Assert.assertEquals(-1, index4);
        Assert.assertEquals(-1, index5);
        Assert.assertEquals(0, index6);
    }

    @Test
    public void test_parseTail() {
        JsonPointer jsonPointer = JsonPointer._parseTail("/1/2/3");
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("/1/2/3", jsonPointer.toString());
    }

    @Test
    public void test_parseQuotedTail() {
        JsonPointer jsonPointer = JsonPointer._parseQuotedTail("/1/2/~1", 7);
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("/1/2/~1", jsonPointer.toString());
    }
}