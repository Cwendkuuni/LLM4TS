package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        // Create a SimpleType object using constructUnsafe with a raw class
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        // Assert that the class is of type String
        Assert.assertEquals(String.class, simpleType.getRawClass());

        // Assert that the simpleType is not a container type
        Assert.assertFalse(simpleType.isContainerType());

        // Assert that the simpleType has no content types
        try {
            simpleType.getContentType();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Simple types have no content types; cannot call getContentType()", e.getMessage());
        }
    }

    @Test
    public void testConstruct() {
        // Create a SimpleType object using construct with a raw class
        SimpleType simpleType = SimpleType.construct(Integer.class);

        // Assert that the class is of type Integer
        Assert.assertEquals(Integer.class, simpleType.getRawClass());

        // Assert that the simpleType is not a container type
        Assert.assertFalse(simpleType.isContainerType());

        // Assert that the simpleType has no content types
        try {
            simpleType.getContentType();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Simple types have no content types; cannot call getContentType()", e.getMessage());
        }
    }

    @Test
    public void testNarrow() {
        // Create a SimpleType object using constructUnsafe with a raw class
        SimpleType simpleType = SimpleType.constructUnsafe(Number.class);

        // Narrow the class to Integer
        SimpleType narrowedType = (SimpleType) simpleType._narrow(Integer.class);

        // Assert that the narrowed type is of type Integer
        Assert.assertEquals(Integer.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        // Create a SimpleType object using construct with a raw class
        SimpleType simpleType = SimpleType.construct(Date.class);

        // Assert that the simpleType is not a container type
        Assert.assertFalse(simpleType.isContainerType());

        // Assert that the simpleType has no content types
        try {
            simpleType.getContentType();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Simple types have no content types; cannot call getContentType()", e.getMessage());
        }

        // Try to modify the content type of the simpleType
        try {
            simpleType.withContentType(SimpleType.construct(String.class));
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Simple types have no content types; cannot call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        // Create a SimpleType object using construct with a raw class
        SimpleType simpleType = SimpleType.construct(List.class);

        // Assert that the simpleType is not a container type
        Assert.assertFalse(simpleType.isContainerType());

        // Assert that the simpleType has no content types
        try {
            simpleType.getContentType();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Simple types have no content types; cannot call getContentType()", e.getMessage());
        }

        // Try to modify the content type handler of the simpleType
        try {
            simpleType.withContentTypeHandler(new Object());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Simple types have no content types; cannot call withContentTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        // Create a SimpleType object using construct with a raw class
        SimpleType simpleType = SimpleType.construct(List.class);

        // Assert that the simpleType is not a static typing
        Assert.assertFalse(simpleType.withStaticTyping().isStaticTyping());

        // Assert that the simpleType is not a container type
        Assert.assertFalse(simpleType.isContainerType());

        // Assert that the simpleType has no content types
        try {
            simpleType.getContentType();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Simple types have no content types; cannot call getContentType()", e.getMessage());
        }
    }

    @Test
    public void testRefine() {
        // Create a SimpleType object using construct with a raw class
        SimpleType simpleType = SimpleType.construct(List.class);

        // Try to refine the simpleType
        Assert.assertNull(simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        // Create a SimpleType object using construct with a raw class
        SimpleType simpleType = SimpleType.construct(List.class);

        // Assert that the canonical name is built correctly
        Assert.assertEquals("java.util.List", simpleType.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        // Create a SimpleType object using construct with a raw class
        SimpleType simpleType1 = SimpleType.construct(List.class);

        // Create another SimpleType object using construct with the same raw class
        SimpleType simpleType2 = SimpleType.construct(List.class);

        // Objects should be equal
        Assert.assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testToString() {
        // Create a SimpleType object using construct with a raw class
        SimpleType simpleType = SimpleType.construct(List.class);

        // Assert that the toString method does not return null
        Assert.assertNotNull(simpleType.toString());
    }
}