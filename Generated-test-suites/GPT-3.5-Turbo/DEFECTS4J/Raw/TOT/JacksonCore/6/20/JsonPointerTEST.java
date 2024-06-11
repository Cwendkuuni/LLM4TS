package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        // Arrange
        JsonPointer jsonPointer = JsonPointer.EMPTY;

        // Act
        boolean matches = jsonPointer.matches();

        // Assert
        Assert.assertTrue(matches);
    }

    @Test
    public void testNonEmptyJsonPointer() {
        // Arrange
        JsonPointer jsonPointer = JsonPointer.compile("/property");

        // Act
        boolean matches = jsonPointer.matches();

        // Assert
        Assert.assertFalse(matches);
    }

    @Test
    public void testMatchingProperty() {
        // Arrange
        JsonPointer jsonPointer = JsonPointer.compile("/property");
        String name = "property";

        // Act
        JsonPointer match = jsonPointer.matchProperty(name);

        // Assert
        Assert.assertNotNull(match);
        Assert.assertTrue(match.matches());
    }

    @Test
    public void testNonMatchingProperty() {
        // Arrange
        JsonPointer jsonPointer = JsonPointer.compile("/property");
        String name = "nonProperty";

        // Act
        JsonPointer match = jsonPointer.matchProperty(name);

        // Assert
        Assert.assertNull(match);
    }

    @Test
    public void testMatchingIndex() {
        // Arrange
        JsonPointer jsonPointer = JsonPointer.compile("/2");
        int index = 2;

        // Act
        JsonPointer match = jsonPointer.matchElement(index);

        // Assert
        Assert.assertNotNull(match);
        Assert.assertTrue(match.matches());
    }

    @Test
    public void testNonMatchingIndex() {
        // Arrange
        JsonPointer jsonPointer = JsonPointer.compile("/2");
        int index = 1;

        // Act
        JsonPointer match = jsonPointer.matchElement(index);

        // Assert
        Assert.assertNull(match);
    }

    @Test
    public void testTail() {
        // Arrange
        JsonPointer jsonPointer = JsonPointer.compile("/parent/child");

        // Act
        JsonPointer tail = jsonPointer.tail();

        // Assert
        Assert.assertNotNull(tail);
        Assert.assertEquals("/child", tail.toString());
    }

    @Test
    public void testEquals() {
        // Arrange
        JsonPointer jsonPointer1 = JsonPointer.compile("/property");
        JsonPointer jsonPointer2 = JsonPointer.compile("/property");

        // Act
        boolean equals = jsonPointer1.equals(jsonPointer2);

        // Assert
        Assert.assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        // Arrange
        JsonPointer jsonPointer1 = JsonPointer.compile("/property");
        JsonPointer jsonPointer2 = JsonPointer.compile("/property");

        // Act
        int hashCode1 = jsonPointer1.hashCode();
        int hashCode2 = jsonPointer2.hashCode();

        // Assert
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        // Act
        JsonPointer.compile("property");
    }

}