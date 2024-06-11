package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        boolean result = pointer.matches();
        Assert.assertTrue(result);
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        String result = pointer.getMatchingProperty();
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        int result = pointer.getMatchingIndex();
        Assert.assertEquals(-1, result);
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        boolean result = pointer.mayMatchProperty();
        Assert.assertTrue(result);
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        boolean result = pointer.mayMatchElement();
        Assert.assertFalse(result);
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer result = pointer.matchProperty("abc");
        Assert.assertNull(result);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer result = pointer.matchElement(0);
        Assert.assertNull(result);
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer result = pointer.tail();
        Assert.assertNull(result);
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        String result = pointer.toString();
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        int hashCode1 = pointer1.hashCode();
        int hashCode2 = pointer2.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        boolean result = pointer1.equals(pointer2);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testCompile() {
        JsonPointer result = JsonPointer.compile("");
        Assert.assertEquals(JsonPointer.EMPTY, result);
    }
    
    @Test
    public void testValueOf() {
        JsonPointer result = JsonPointer.valueOf("");
        Assert.assertEquals(JsonPointer.EMPTY, result);
    }
    
    @Test
    public void test_parseIndex() {
        int result = JsonPointer._parseIndex("");
        Assert.assertEquals(-1, result);
    }
    
    @Test
    public void test_parseTail() {
        JsonPointer result = JsonPointer._parseTail("/");
        Assert.assertEquals(JsonPointer.EMPTY, result);
    }
    
    @Test
    public void test_parseQuotedTail() {
        JsonPointer result = JsonPointer._parseQuotedTail("~", 1);
        Assert.assertEquals(JsonPointer.EMPTY, result);
    }
    
    @Test
    public void test_appendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        String result = sb.toString();
        Assert.assertEquals("~", result);
    }
    
}