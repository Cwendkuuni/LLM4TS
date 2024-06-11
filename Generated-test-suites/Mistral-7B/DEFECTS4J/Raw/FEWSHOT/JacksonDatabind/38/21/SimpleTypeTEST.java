package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(simpleType1);
        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testUnsafeSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.hasBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalMapClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalCollectionClass() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArrayClass() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testNarrowType() {
        Class<?> baseClass = String.class;
        Class<?> subClass = Integer.class;
        SimpleType baseType = new SimpleType(baseClass, TypeBindings.emptyBindings(), null, null);
        SimpleType narrowedType = baseType._narrow(subClass);
        assertTrue(narrowedType instanceof SimpleType);
        assertNotSame(baseType, narrowedType);
        assertEquals(subClass, narrowedType.getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        Object handler = new Object() {};
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertEquals(simpleType.getRawClass(), withHandler.getRawClass());
        assertEquals(handler, withHandler._typeHandler);
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals("[simple type, class " + cls.getName() + "]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType3 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType3.equals(simpleType1));
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        TypeParameter<?> stringTypeParameter = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null).withGenericBinding(0, String.class);
        SimpleType listType = new SimpleType(cls, Arrays.asList(stringTypeParameter), null, null);
        assertEquals("[Ljava.util.List;", listType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = List.class;
        TypeParameter<?> stringTypeParameter = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null).withGenericBinding(0, String.class);
        SimpleType listType = new SimpleType(cls, Arrays.asList(stringTypeParameter), null, null);
        assertEquals("java.util.List", listType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        TypeParameter<?> stringTypeParameter = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null).withGenericBinding(0, String.class);
        SimpleType listType = new SimpleType(cls, Arrays.asList(stringTypeParameter), null, null);
        StringBuilder sb = new StringBuilder();
        listType.getGenericSignature(sb);
        assertEquals("[Ljava.util.List<Ljava.lang.String;>;", sb.toString());
    }
}