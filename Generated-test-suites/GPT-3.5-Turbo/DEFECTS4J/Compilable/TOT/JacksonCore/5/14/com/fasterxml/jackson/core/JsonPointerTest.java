package com.fasterxml.jackson.core;
import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testCompile() {
        // Typical use case
        JsonPointer pointer = JsonPointer.compile("/foo");
        Assert.assertEquals("foo", pointer.getMatchingProperty());

        // Edge case: empty input
        pointer = JsonPointer.compile("");
        Assert.assertTrue(pointer.matches());

        // Error scenario: input does not start with '/'
        try {
            pointer = JsonPointer.compile("foo");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testValueOf() {
        // Typical use case
        JsonPointer pointer = JsonPointer.valueOf("/bar");
        Assert.assertEquals("bar", pointer.getMatchingProperty());

        // Edge case: empty input
        pointer = JsonPointer.valueOf("");
        Assert.assertTrue(pointer.matches());

        // Error scenario: input does not start with '/'
        try {
            pointer = JsonPointer.valueOf("bar");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMatchProperty() {
        // Typical use case
        JsonPointer pointer = JsonPointer.compile("/foo");
        pointer = pointer.matchProperty("foo");
        Assert.assertNull(pointer);

        // Edge case: pointer matches property
        pointer = JsonPointer.compile("/bar");
        pointer = pointer.matchProperty("bar");
        Assert.assertTrue(pointer.matches());

        // Error scenario: property name does not match
        pointer = JsonPointer.compile("/baz");
        pointer = pointer.matchProperty("notBaz");
        Assert.assertNull(pointer);
    }

    @Test
    public void testMatchElement() {
        // Typical use case
        JsonPointer pointer = JsonPointer.compile("/0");
        pointer = pointer.matchElement(0);
        Assert.assertNull(pointer);

        // Edge case: pointer matches element
        pointer = JsonPointer.compile("/1");
        pointer = pointer.matchElement(1);
        Assert.assertTrue(pointer.matches());

        // Error scenario: element index does not match
        pointer = JsonPointer.compile("/2");
        pointer = pointer.matchElement(1);
        Assert.assertNull(pointer);
    }

    @Test
    public void testTail() {
        // Typical use case
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        pointer = pointer.tail();
        Assert.assertEquals("bar", pointer.getMatchingProperty());

        // Edge case: single segment pointer
        pointer = JsonPointer.compile("/baz");
        pointer = pointer.tail();
        Assert.assertTrue(pointer.matches());

        // Error scenario: empty pointer
        pointer = JsonPointer.compile("");
        pointer = pointer.tail();
        Assert.assertTrue(pointer.matches());
    }

    @Test
    public void testToString() {
        // Typical use case
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        Assert.assertEquals("/foo/bar", pointer.toString());

        // Edge case: empty pointer
        pointer = JsonPointer.compile("");
        Assert.assertEquals("", pointer.toString());
    }

    @Test
    public void testEquals() {
        // Typical use case
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.valueOf("/foo");
        Assert.assertEquals(pointer1, pointer2);

        // Edge case: empty pointer
        pointer1 = JsonPointer.compile("");
        pointer2 = JsonPointer.valueOf("");
        Assert.assertEquals(pointer1, pointer2);

        // Error scenario: different pointers
        pointer1 = JsonPointer.compile("/bar");
        pointer2 = JsonPointer.valueOf("/foo");
        Assert.assertNotEquals(pointer1, pointer2);

        // Error scenario: different objects
        pointer1 = JsonPointer.compile("/baz");
        Assert.assertNotEquals(pointer1, "notPointer");
    }

    // Additional test cases can be added for other methods if necessary

}