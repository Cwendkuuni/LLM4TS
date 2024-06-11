
package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {
    // Test constructor with cls parameter
    @Test
    public void testConstructorWithClass() {
        Class<?> cls = Integer.class;
        SimpleType simpleType = new SimpleType(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    // Test constructor with cls, bindings, superClass, superInts parameters
    @Test
    public void testConstructorWithParams() {
        Class<?> cls = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts, valueHandler, typeHandler, asStatic);
        assertNotNull(simpleType);

        assertEquals(cls, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInts, simpleType.getInterfaces().toArray());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(asStatic, simpleType.isStatic());
    }

    // Test copy constructor
    @Test
    public void testCopyConstructor() {
        SimpleType base = new SimpleType(Integer.class);
        SimpleType simpleType = new SimpleType(base);
        assertNotNull(simpleType);
        assertEquals(base.getRawClass(), simpleType.getRawClass());
        assertEquals(base.getBindings(), simpleType.getBindings());
        assertEquals(base.getSuperClass(), simpleType.getSuperClass());
        assertArrayEquals(base.getInterfaces().toArray(), simpleType.getInterfaces().toArray());
        assertEquals(base.getValueHandler(), simpleType.getValueHandler());
        assertEquals(base.getTypeHandler(), simpleType.getTypeHandler());
        assertEquals(base.isStatic(), simpleType.isStatic());
    }

    // Test narrow method
    @Test
    public void testNarrow() {
        SimpleType base = new SimpleType(Number.class);
        Class<?> subclass = Integer.class;
        JavaType narrowType = base.narrow(subclass);
        assertNotNull(narrowType);
        assertEquals(subclass, narrowType.getRawClass());
        assertEquals(base.getBindings(), narrowType.getBindings());
        assertEquals(base.getSuperClass(), narrowType.getSuperClass());
        assertArrayEquals(base.getInterfaces().toArray(), narrowType.getInterfaces().toArray());
        assertEquals(base.getValueHandler(), narrowType.getValueHandler());
        assertEquals(base.getTypeHandler(), narrowType.getTypeHandler());
        assertEquals(base.isStatic(), narrowType.isStatic());
    }

    // Test withContentType method
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        JavaType contentType = new SimpleType(String.class);
        simpleType.withContentType(contentType);
    }

    // Test withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        SimpleType base = new SimpleType(Integer.class);
        Object typeHandler = new Object();
        SimpleType simpleType = base.withTypeHandler(typeHandler);
        assertNotNull(simpleType);
        assertEquals(base.getRawClass(), simpleType.getRawClass());
        assertEquals(base.getBindings(), simpleType.getBindings());
        assertEquals(base.getSuperClass(), simpleType.getSuperClass());
        assertArrayEquals(base.getInterfaces().toArray(), simpleType.getInterfaces().toArray());
        assertEquals(base.getValueHandler(), simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(base.isStatic(), simpleType.isStatic());
    }

    // Test withContentTypeHandler method
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        simpleType.withContentTypeHandler(new Object());
    }

    // Test withValueHandler method
    @Test
    public void testWithValueHandler() {
        SimpleType base = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        SimpleType simpleType = base.withValueHandler(valueHandler);
        assertNotNull(simpleType);
        assertEquals(base.getRawClass(), simpleType.getRawClass());
        assertEquals(base.getBindings(), simpleType.getBindings());
        assertEquals(base.getSuperClass(), simpleType.getSuperClass());
        assertArrayEquals(base.getInterfaces().toArray(), simpleType.getInterfaces().toArray());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(base.getTypeHandler(), simpleType.getTypeHandler());
        assertEquals(base.isStatic(), simpleType.isStatic());
    }

    // Test withContentValueHandler method
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        simpleType.withContentValueHandler(new Object());
    }

    // Test withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        SimpleType base = new SimpleType(Integer.class);
        SimpleType simpleType = base.withStaticTyping();
        assertNotNull(simpleType);
        assertEquals(base.getRawClass(), simpleType.getRawClass());
        assertEquals(base.getBindings(), simpleType.getBindings());
        assertEquals(base.getSuperClass(), simpleType.getSuperClass());
        assertArrayEquals(base.getInterfaces().toArray(), simpleType.getInterfaces().toArray());
        assertEquals(base.getValueHandler(), simpleType.getValueHandler());
        assertEquals(base.getTypeHandler(), simpleType.getTypeHandler());
        assertTrue(simpleType.isStatic());
    }

    // Test refine method
    @Test
    public void testRefine() {
        SimpleType base = new SimpleType(Integer.class);
        JavaType refinedType = base.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    // Test isContainerType method
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertFalse(simpleType.isContainerType());
    }

    // Test getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Integer.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        assertNotNull(erasedSignature);
        assertEquals(sb, erasedSignature);
    }

    // Test getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Integer.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        assertNotNull(genericSignature);
        assertEquals(sb, genericSignature);
    }

    // Test toString method
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Integer.class);
        String str = simpleType.toString();
        assertNotNull(str);
        assertTrue(str.contains(Integer.class.getName()));
    }

    // Test equals method
    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(Integer.class);
        SimpleType type2 = new SimpleType(Integer.class);
        assertTrue(type1.equals(type2));

        SimpleType type3 = new SimpleType(String.class);
        assertFalse(type1.equals(type3));
    }
}
