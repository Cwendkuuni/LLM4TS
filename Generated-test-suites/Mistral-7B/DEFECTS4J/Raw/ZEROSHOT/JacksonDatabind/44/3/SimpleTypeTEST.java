package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());

        SimpleType simpleTypeWithBindings = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(String.class, simpleTypeWithBindings.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithBindings.getBindings());
        assertNull(simpleTypeWithBindings.getSuperClass());
        assertNull(simpleTypeWithBindings.getSuperInterfaces());
        assertFalse(simpleTypeWithBindings.isStatic());

        SimpleType simpleTypeWithSuperClass = new SimpleType(stringClass, TypeBindings.emptyBindings(), TypeFactory.unknownType(), null);
        assertEquals(String.class, simpleTypeWithSuperClass.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithSuperClass.getBindings());
        assertEquals(TypeFactory.unknownType(), simpleTypeWithSuperClass.getSuperClass());
        assertNull(simpleTypeWithSuperClass.getSuperInterfaces());
        assertFalse(simpleTypeWithSuperClass.isStatic());

        SimpleType simpleTypeWithSuperClassAndInterfaces = new SimpleType(stringClass, TypeBindings.emptyBindings(), TypeFactory.unknownType(), new JavaType[] { TypeFactory.unknownType(), TypeFactory.unknownType() });
        assertEquals(String.class, simpleTypeWithSuperClassAndInterfaces.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithSuperClassAndInterfaces.getBindings());
        assertEquals(TypeFactory.unknownType(), simpleTypeWithSuperClassAndInterfaces.getSuperClass());
        assertArrayEquals(new JavaType[] { TypeFactory.unknownType(), TypeFactory.unknownType() }, simpleTypeWithSuperClassAndInterfaces.getSuperInterfaces());
        assertFalse(simpleTypeWithSuperClassAndInterfaces.isStatic());

        SimpleType simpleTypeWithValueHandler = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, "valueHandler", false);
        assertEquals(String.class, simpleTypeWithValueHandler.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithValueHandler.getBindings());
        assertNull(simpleTypeWithValueHandler.getSuperClass());
        assertNull(simpleTypeWithValueHandler.getSuperInterfaces());
        assertFalse(simpleTypeWithValueHandler.isStatic());
        assertEquals("valueHandler", simpleTypeWithValueHandler.getValueHandler());

        SimpleType simpleTypeWithTypeHandler = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, "typeHandler", false);
        assertEquals(String.class, simpleTypeWithTypeHandler.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithTypeHandler.getBindings());
        assertNull(simpleTypeWithTypeHandler.getSuperClass());
        assertNull(simpleTypeWithTypeHandler.getSuperInterfaces());
        assertFalse(simpleTypeWithTypeHandler.isStatic());
        assertEquals("typeHandler", simpleTypeWithTypeHandler.getTypeHandler());

        SimpleType simpleTypeWithStaticTyping = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null, true);
        assertEquals(String.class, simpleTypeWithStaticTyping.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleTypeWithStaticTyping.getBindings());
        assertNull(simpleTypeWithStaticTyping.getSuperClass());
        assertNull(simpleTypeWithStaticTyping.getSuperInterfaces());
        assertTrue(simpleTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testConstruct() {
        try {
            new SimpleType(int[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new SimpleType(ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new SimpleType(HashMap.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new SimpleType(List.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new SimpleType(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new SimpleType(Array.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new SimpleType(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType narrowedType = simpleType._narrow(intClass);
        assertTrue(narrowedType instanceof SimpleType);
        assertEquals(intClass, narrowedType.getRawClass());
        assertEquals(simpleType.getBindings(), narrowedType.getBindings());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertFalse(narrowedType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        try {
            new SimpleType(String.class).withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithContentTypeHandler = simpleType.withContentTypeHandler("contentTypeHandler");
        assertEquals(simpleType.getRawClass(), simpleTypeWithContentTypeHandler.getRawClass());
        assertEquals(simpleType.getBindings(), simpleTypeWithContentTypeHandler.getBindings());
        assertNull(simpleTypeWithContentTypeHandler.getSuperClass());
        assertNull(simpleTypeWithContentTypeHandler.getSuperInterfaces());
        assertFalse(simpleTypeWithContentTypeHandler.isStatic());
        assertEquals("contentTypeHandler", simpleTypeWithContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithTypeHandler() {
        try {
            new SimpleType(String.class).withTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithTypeHandler = simpleType.withTypeHandler("typeHandler");
        assertEquals(simpleType.getRawClass(), simpleTypeWithTypeHandler.getRawClass());
        assertEquals(simpleType.getBindings(), simpleTypeWithTypeHandler.getBindings());
        assertNull(simpleTypeWithTypeHandler.getSuperClass());
        assertNull(simpleTypeWithTypeHandler.getSuperInterfaces());
        assertFalse(simpleTypeWithTypeHandler.isStatic());
        assertEquals("typeHandler", simpleTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        try {
            new SimpleType(String.class).withValueHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithValueHandler = simpleType.withValueHandler("valueHandler");
        assertEquals(simpleType.getRawClass(), simpleTypeWithValueHandler.getRawClass());
        assertEquals(simpleType.getBindings(), simpleTypeWithValueHandler.getBindings());
        assertNull(simpleTypeWithValueHandler.getSuperClass());
        assertNull(simpleTypeWithValueHandler.getSuperInterfaces());
        assertFalse(simpleTypeWithValueHandler.isStatic());
        assertEquals("valueHandler", simpleTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithStaticTyping = simpleType.withStaticTyping();
        assertEquals(simpleType.getRawClass(), simpleTypeWithStaticTyping.getRawClass());
        assertEquals(simpleType.getBindings(), simpleTypeWithStaticTyping.getBindings());
        assertNull(simpleTypeWithStaticTyping.getSuperClass());
        assertNull(simpleTypeWithStaticTyping.getSuperInterfaces());
        assertTrue(simpleTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType refinedType = simpleType.refine(intClass.getClass(), TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName);

        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        SimpleType listType = new SimpleType(listClass);
        String listCanonicalName = listType.buildCanonicalName();
        assertEquals("java.util.List<java.lang.Integer>", listCanonicalName);

        Class<Map<String, Integer>> mapClass = new HashMap<String, Integer>().getClass();
        SimpleType mapType = new SimpleType(mapClass);
        String mapCanonicalName = mapType.buildCanonicalName();
        assertEquals("java.util.Map<java.lang.String,java.lang.Integer>", mapCanonicalName);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());

        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        SimpleType listType = new SimpleType(listClass);
        StringBuilder listSb = new StringBuilder();
        listType.getGenericSignature(listSb);
        assertEquals("java.util.List<java.lang.Integer>", listSb.toString());

        Class<Map<String, Integer>> mapClass = new HashMap<String, Integer>().getClass();
        SimpleType mapType = new SimpleType(mapClass);
        StringBuilder mapSb = new StringBuilder();
        mapType.getGenericSignature(mapSb);
        assertEquals("java.util.Map<java.lang.String,java.lang.Integer>", mapSb.toString());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String toString = simpleType.toString();
        assertEquals("[simple type, class java.lang.String]", toString);
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertFalse(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType4));

        SimpleType simpleType5 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, true);
        assertFalse(simpleType1.equals(simpleType5));
    }
}