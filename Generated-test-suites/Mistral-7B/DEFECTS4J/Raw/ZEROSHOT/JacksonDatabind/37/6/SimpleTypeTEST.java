package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.ClassNotFoundException;
import java.lang.reflect.Type;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());

        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        assertEquals(javaType, simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidType() {
        SimpleType.construct(ArrayList.class);
    }

    @Test
    public void testNarrow() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = SimpleType.construct(stringClass);
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);

        SimpleType narrowed = stringSimpleType.narrow(String[].class);
        assertNotNull(narrowed);
        assertNull(narrowed.getRawClass());
        assertEquals(String[].class, narrowed.getRawClass());

        JavaType narrowedJavaType = stringJavaType.narrow(String[].class);
        assertEquals(narrowedJavaType, narrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidType() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = SimpleType.construct(stringClass);

        stringSimpleType.narrow(int.class);
    }

    @Test
    public void testIsContainerType() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetErasedSignature() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        simpleType.getErasedSignature(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericSignature() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        simpleType.getGenericSignature(null);
    }

    @Test
    public void testBuildCanonicalName() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());

        Class<List<Integer>> listClass = List.class.getComponentType();
        Type genericSuperClass = new TypeToken<List<Integer>>() {}.getType();
        SimpleType listSimpleType = new SimpleType(listClass, null, null, null, null, null, false);
        assertEquals("java.util.List<java.lang.Integer>", listSimpleType.buildCanonicalName());

        Class<Map<String, Integer>> mapClass = Map.class;
        TypeMapType mapType = new TypeMapType(mapClass, null, null, null);
        SimpleType mapSimpleType = new SimpleType(mapClass, null, null, null, null, null, false);
        assertEquals("java.util.Map<java.lang.String,java.lang.Integer>", mapSimpleType.buildCanonicalName());
    }

    @Test
    public void testEquals() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = SimpleType.construct(stringClass);
        SimpleType simpleType2 = SimpleType.construct(stringClass);
        SimpleType simpleType3 = SimpleType.construct(int.class);
        SimpleType simpleType4 = new SimpleType(String.class, null, null, null, null, null, false);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(simpleType4));
        assertFalse(simpleType1.equals(null));
    }

    @Test
    public void testWithContentTypeHandler() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        simpleType.withContentTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        simpleType.withContentType(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithValueHandler() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        simpleType.withValueHandler(null);
    }

    @Test
    public void testWithTypeHandler() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = SimpleType.construct(stringClass);
        Object handler = new Object() {};
        SimpleType simpleType2 = simpleType1.withTypeHandler(handler);
        assertNotSame(simpleType1, simpleType2);
        assertEquals(simpleType1.getRawClass(), simpleType2.getRawClass());
        assertEquals(handler, simpleType2.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = SimpleType.construct(stringClass);
        SimpleType simpleType2 = simpleType1.withStaticTyping();
        assertNotSame(simpleType1, simpleType2);
        assertEquals(simpleType1.getRawClass(), simpleType2.getRawClass());
        assertTrue(simpleType2.isStatic());
    }

    @Test
    public void testRefine() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType refined = simpleType.refine(String[].class, bindings, null, null);
        assertNull(refined);

        refined = simpleType.refine(List.class, bindings, javaType, null);
        assertNotNull(refined);
        assertEquals(List.class, refined.getRawClass());
        assertEquals(javaType, refined.getSuperClass());
        assertNull(refined.getSuperInterfaces());
        assertNull(refined.getTypeHandler());
        assertNull(refined.getValueHandler());
        assertFalse(refined.isStatic());
    }

    @Test
    public void testToString() throws Exception {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());

        Class<List<Integer>> listClass = List.class;
        SimpleType listSimpleType = new SimpleType(listClass, null, null, null, null, null, false);
        assertEquals("[simple type, class java.util.List]", listSimpleType.toString());

        Class<Map<String, Integer>> mapClass = Map.class;
        SimpleType mapSimpleType = new SimpleType(mapClass, null, null, null, null, null, false);
        assertEquals("[simple type, class java.util.Map]", mapSimpleType.toString());
    }
}