package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        Class<?> cls = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        SimpleType simpleType = new SimpleType(cls);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructorWithBindings() {
        Class<?> cls = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructorWithCopy() {
        TypeBase base = new TypeBase() {
        };

        SimpleType simpleType = new SimpleType(base);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructorWithAllAttributes() {
        Class<?> cls = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts, valueHandler, typeHandler, asStatic);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructorWithExtraHash() {
        Class<?> cls = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        int extraHash = 0;

        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts, extraHash, valueHandler, typeHandler, asStatic);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> raw = Object.class;

        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        assertNotNull(simpleType);
    }

    @Test
    @Deprecated
    public void testConstructDeprecated() {
        Class<?> cls = Object.class;

        SimpleType simpleType = SimpleType.construct(cls);
        assertNotNull(simpleType);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;

        SimpleType simpleType = new SimpleType(Object.class);
        JavaType narrowedType = simpleType._narrow(subclass);

        assertNotNull(narrowedType);
        assertEquals(subclass, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        JavaType contentType = null;

        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentType(contentType);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newType = simpleType.withTypeHandler(h);

        assertNotNull(newType);
        assertNotEquals(simpleType, newType);
        assertEquals(h, newType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        Object h = null;

        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentTypeHandler(h);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newType = simpleType.withValueHandler(h);

        assertNotNull(newType);
        assertNotEquals(simpleType, newType);
        assertEquals(h, newType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        Object h = null;

        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentValueHandler(h);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newType = simpleType.withStaticTyping();

        assertNotNull(newType);
        assertNotEquals(simpleType, newType);
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        SimpleType simpleType = new SimpleType(Object.class);
        JavaType refinedType = simpleType.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();

        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder result = simpleType.getErasedSignature(sb);

        assertNotNull(result);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();

        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder result = simpleType.getGenericSignature(sb);

        assertNotNull(result);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNotNull(simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);

        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithDifferentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentBindings() {
        TypeBindings bindings1 = new TypeBindings();
        bindings1.addBinding(Object.class, new SimpleType(Object.class));

        TypeBindings bindings2 = new TypeBindings();
        bindings2.addBinding(Collection.class, new SimpleType(Collection.class));

        SimpleType simpleType1 = new SimpleType(Object.class, bindings1, null, null);
        SimpleType simpleType2 = new SimpleType(Object.class, bindings2, null, null);

        assertFalse(simpleType1.equals(simpleType2));
    }
}