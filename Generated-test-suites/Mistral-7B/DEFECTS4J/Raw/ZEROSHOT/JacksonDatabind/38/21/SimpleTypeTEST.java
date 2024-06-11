package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new Class[]{}, simpleType.getSuperTypes());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentException() {
        SimpleType.construct(Map.class);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleTypeWithTypeHandler = simpleType.withTypeHandler(new ObjectMapper.StdDateSerializer(null));
        assertNotSame(simpleType, simpleTypeWithTypeHandler);
        assertEquals(simpleType.getRawClass(), simpleTypeWithTypeHandler.getRawClass());
        assertNotSame(simpleType.getTypeHandler(), simpleTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        Class<List<String>> listClass = List.class.cast(TypeFactory.defaultInstance().constructTypeCollectionType(String.class));
        SimpleType simpleTypeString = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleTypeList = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleTypeString.isContainerType());
        assertTrue(simpleTypeList.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<String>> listClass = List.class.cast(TypeFactory.defaultInstance().constructTypeCollectionType(String.class));
        SimpleType simpleType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[Ljava.util.List;<TT;>;", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<List<String>>> listOfListStringClass = List.class.cast(TypeFactory.defaultInstance().constructTypeCollectionType(List.class.cast(TypeFactory.defaultInstance().constructTypeCollectionType(String.class))));
        SimpleType simpleType = new SimpleType(listOfListStringClass, TypeBindings.emptyBindings(), null, null);
        assertEquals("java.util.List<java.util.List<java.lang.String>>", simpleType.buildCanonicalName());
    }

    @Test
    public void testRefine() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertNull(simpleType.refine(String[].class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testEquals() {
        Class<List<String>> listClass = List.class.cast(TypeFactory.defaultInstance().constructTypeCollectionType(String.class));
        SimpleType simpleType1 = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType3 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}