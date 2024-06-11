package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        // Arrange
        Class<?> cls = String.class;

        // Act
        SimpleType simpleType = new SimpleType(cls);

        // Assert
        assertEquals(cls, simpleType.getContentType().getRawClass());
    }

    @Test
    public void testConstructor2() {
        // Arrange
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;

        // Act
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts);

        // Assert
        assertEquals(cls, simpleType.getContentType().getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInts, simpleType.getInterfaces());
    }

    @Test
    public void testConstructor3() {
        // Arrange
        TypeBase base = new SimpleType(String.class);

        // Act
        SimpleType simpleType = new SimpleType(base);

        // Assert
        assertEquals(base.getRawClass(), simpleType.getContentType().getRawClass());
        assertEquals(base.getBindings(), simpleType.getBindings());
        assertEquals(base.getSuperClass(), simpleType.getSuperClass());
        assertEquals(base.getInterfaces(), simpleType.getInterfaces());
    }

    @Test
    public void testConstructor4() {
        // Arrange
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        // Act
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts, valueHandler, typeHandler, asStatic);

        // Assert
        assertEquals(cls, simpleType.getContentType().getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInts, simpleType.getInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(asStatic, simpleType.isStaticTyping());
    }

    @Test
    public void testConstructor5() {
        // Arrange
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        int extraHash = 0;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        // Act
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts, extraHash, valueHandler, typeHandler, asStatic);

        // Assert
        assertEquals(cls, simpleType.getContentType().getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInts, simpleType.getInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(asStatic, simpleType.isStaticTyping());
    }

    @Test
    public void testConstructUnsafe() {
        // Arrange
        Class<?> raw = String.class;

        // Act
        SimpleType simpleType = SimpleType.constructUnsafe(raw);

        // Assert
        assertEquals(raw, simpleType.getContentType().getRawClass());
    }

    @Test
    public void testConstruct() {
        // Arrange
        Class<?> cls = String.class;

        try {
            // Act
            SimpleType simpleType = SimpleType.construct(cls);

            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Can not construct SimpleType for a Collection (class: java.lang.String)", e.getMessage());
        }
    }

    @Test
    public void testNarrow() {
        // Arrange
        Class<?> subclass = Object.class;
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        JavaType narrowedType = simpleType._narrow(subclass);

        // Assert
        assertEquals(subclass, narrowedType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        // Arrange
        JavaType contentType = null;
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        simpleType.withContentType(contentType);
    }

    @Test
    public void testWithTypeHandler() {
        // Arrange
        Object h = new Object();
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        SimpleType typeWithHandler = simpleType.withTypeHandler(h);

        // Assert
        assertNotSame(simpleType, typeWithHandler);
        assertEquals(h, typeWithHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        // Arrange
        Object h = new Object();
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        simpleType.withContentTypeHandler(h);
    }

    @Test
    public void testWithValueHandler() {
        // Arrange
        Object h = new Object();
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        SimpleType typeWithValueHandler = simpleType.withValueHandler(h);

        // Assert
        assertNotSame(simpleType, typeWithValueHandler);
        assertEquals(h, typeWithValueHandler.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        // Arrange
        Object h = new Object();
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        simpleType.withContentValueHandler(h);
    }

    @Test
    public void testWithStaticTyping() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        SimpleType staticType = simpleType.withStaticTyping();

        // Assert
        assertNotSame(simpleType, staticType);
        assertTrue(staticType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        // Arrange
        Class<?> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        SimpleType simpleType = new SimpleType(rawType, bindings, superClass, superInterfaces);

        // Act
        JavaType refinedType = simpleType.refine(rawType, bindings, superClass, superInterfaces);

        // Assert
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        boolean isContainerType = simpleType.isContainerType();

        // Assert
        assertFalse(isContainerType);
    }

    @Test
    public void testGetErasedSignature() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());

        // Assert
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());

        // Assert
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        String canonicalName = simpleType.buildCanonicalName();

        // Assert
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testEquals() {
        // Arrange
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);

        // Act
        boolean isEqual = simpleType1.equals(simpleType2);

        // Assert
        assertTrue(isEqual);
    }
}