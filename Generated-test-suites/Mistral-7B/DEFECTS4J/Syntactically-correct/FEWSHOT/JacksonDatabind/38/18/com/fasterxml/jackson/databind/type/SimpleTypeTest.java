package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct() {
        Class<Map<String, String>> cls = Map.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrow() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Class<Integer> subclass = Integer.class;
        JavaType narrowType = simpleType._narrow(subclass);
        assertNull(narrowType);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> cls = String.class;
        SimpleType originalSimpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType.getClass(), copiedSimpleType.getClass());
        assertEquals(originalSimpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(originalSimpleType.hashCode(), copiedSimpleType.hashCode());
    }

    @Test
    public void testIsContainerType() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<String>> cls = List.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructCollectionType(List.class, String.class);
        SimpleType simpleType = (SimpleType) javaType;
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<List<Map<String, String>>>> cls = List.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(new TypeReference<List<List<Map<String, String>>>>() {});
        SimpleType simpleType = (SimpleType) javaType;
        assertEquals("java.util.List[java.util.List[java.util.Map[<Ljava.lang.String;Ljava.lang.String;>]],java.lang.String]",
                simpleType.buildCanonicalName());
    }

    @Test
    public void testToString() {
        Class<List<List<Map<String, String>>>> cls = List.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(new TypeReference<List<List<Map<String, String>>>>() {});
        SimpleType simpleType = (SimpleType) javaType;
        assertEquals("[simple type, class java.util.List[java.util.List[java.util.Map[<Ljava.lang.String;Ljava.lang.String;>]],java.lang.String]]",
                simpleType.toString());
    }

    @Test
    public void testEquals() {
        Class<String> cls1 = String.class;
        Class<String> cls2 = String.class;
        SimpleType simpleType1 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls2, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType3 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}