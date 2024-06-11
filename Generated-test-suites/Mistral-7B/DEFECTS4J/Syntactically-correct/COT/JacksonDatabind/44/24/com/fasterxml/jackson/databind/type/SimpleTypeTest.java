package com.example.tests;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;

public class SimpleTypeTest {

    

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingMapType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingCollectionType() {
        Class<List<String>> collectionClass = List.class;
        new SimpleType(collectionClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingArrayType() {
        Class<int[]> arrayClass = int[].class;
        new SimpleType(arrayClass);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(simpleType.getBindings(), copiedSimpleType.getBindings());
        assertEquals(simpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafeMethod() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = DeprecatedException.class)
    public void testDeprecatedConstructMethod() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test
    public void testNarrowingMethod() {
        Class<String> childClass = String.class;
        Class<Object> parentClass = Object.class;
        SimpleType parentSimpleType = new SimpleType(parentClass);
        SimpleType childSimpleType = new SimpleType(childClass);
        SimpleType narrowedChildSimpleType = childSimpleType.narrow(parentClass);
        assertEquals(childClass, narrowedChildSimpleType.getRawClass());
        assertEquals(childSimpleType.getBindings(), narrowedChildSimpleType.getBindings());
        assertNull(narrowedChildSimpleType.getSuperClass());
        assertArrayEquals(childSimpleType.getSuperInterfaces(), narrowedChildSimpleType.getSuperInterfaces());
        assertFalse(narrowedChildSimpleType.isContainerType());
    }

    @Test
    public void testWithContentTypeMethod() {
        JavaType contentType = TypeFactory.defaultInstance().constructType(String[].class);
        SimpleType simpleType = new SimpleType(String.class);
        JavaType result = simpleType.withContentType(contentType);
        assertNull(result);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Object typeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(String.class);
        JavaType result = simpleType.withTypeHandler(typeHandler);
        assertEquals(String.class, result.getRawClass());
        assertEquals(simpleType.getBindings(), result.getBindings());
        assertEquals(result.getSuperClass(), simpleType.getSuperClass());
        assertArrayEquals(result.getSuperInterfaces(), simpleType.getSuperInterfaces());
        assertFalse(result.isContainerType());
        assertEquals(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Object contentTypeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(String.class);
        JavaType result = simpleType.withContentTypeHandler(contentTypeHandler);
        assertNull(result);
    }

    @Test
    public void testWithValueHandlerMethod() {
        Object valueHandler = new Object() {};
        SimpleType simpleType = new SimpleType(String.class);
        JavaType result = simpleType.withValueHandler(valueHandler);
        assertEquals(String.class, result.getRawClass());
        assertEquals(simpleType.getBindings(), result.getBindings());
        assertEquals(result.getSuperClass(), simpleType.getSuperClass());
        assertArrayEquals(result.getSuperInterfaces(), simpleType.getSuperInterfaces());
        assertFalse(result.isContainerType());
        assertEquals(valueHandler, result.getValueHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType result = simpleType.withStaticTyping();
        assertEquals(String.class, result.getRawClass());
        assertEquals(simpleType.getBindings(), result.getBindings());
        assertEquals(result.getSuperClass(), simpleType.getSuperClass());
        assertArrayEquals(result.getSuperInterfaces(), simpleType.getSuperInterfaces());
        assertTrue(result.isContainerType());
    }

    @Test
    public void testRefineMethod() {
        Class<String> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        SimpleType simpleType = new SimpleType(rawType, bindings, superClass, superInterfaces);
        JavaType result = simpleType.refine(rawType, bindings, null, null);
        assertNull(result);
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        Class<List<String>> listClass = List.class;
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(listClass);
        String result = simpleType.buildCanonicalName();
        assertEquals("[java.util.List, java.lang.String]", result);
    }

    @Test
    public void testIsContainerTypeMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        Class<List<String>> listClass = List.class;
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(listClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("[java.util.List]", sb.toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        Class<List<List<String>>> listListClass = List.class;
        Class<List<String>> listClass = List.class;
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(listListClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[java.util.List, java.util.List, java.lang.String]", sb.toString());
    }

    
}