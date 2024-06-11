package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertEquals(0, simpleType.getContentTypeHandlers().size());
        assertEquals(0, simpleType.getValueHandlers().size());
        assertEquals(false, simpleType.isAsStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(simpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertEquals(simpleType.getContentTypeHandlers().size(), copiedSimpleType.getContentTypeHandlers().size());
        assertEquals(simpleType.getValueHandlers().size(), copiedSimpleType.getValueHandlers().size());
        assertEquals(simpleType.isAsStatic(), copiedSimpleType.isAsStatic());
    }

    @Test
    public void testSimpleTypeWithTypeHandlers() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType withTypeHandlers = simpleType.withTypeHandler(typeHandler).withValueHandler(valueHandler);
        assertEquals(valueHandler, withTypeHandlers.getValueHandler());
        assertEquals(typeHandler, withTypeHandlers.getTypeHandler());
        assertNotSame(simpleType, withTypeHandlers);
    }

    @Test
    public void testSimpleTypeConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType constructedType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, constructedType.getRawClass());
        assertNull(constructedType.getSuperClass());
        assertNull(constructedType.getSuperInterfaces());
        assertEquals(0, constructedType.getContentTypeHandlers().size());
        assertEquals(0, constructedType.getValueHandlers().size());
        assertFalse(constructedType.isAsStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructInvalidType() {
        Class<?> cls = Map.class;
        SimpleType.construct(cls);
    }

    @Test
    public void testSimpleTypeNarrow() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls);
        Class<?> subClass = Object.class;
        SimpleType narrowedType = baseType._narrow(subClass);
        assertNull(narrowedType);
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isAsStatic());
        assertFalse(simpleType.isAsStatic());
    }

    @Test
    public void testSimpleTypeToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals("[simple type, class " + cls.getName() + "]", simpleType.toString());
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = new SimpleType(cls);
        SimpleType simpleType3 = new SimpleType(Object.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testSimpleTypeGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("[simple type, class java.lang.String]", sb.toString());
    }

    @Test
    public void testSimpleTypeGetGenericSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[simple type, class java.lang.String]", sb.toString());
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new SimpleType[0];
        JavaType refinedType = simpleType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(refinedType);
    }
}