package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeHandler;
import com.fasterxml.jackson.databind.type.TypeResolverBuilder;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(Object[].class);
        JavaType[] superInts = new JavaType[] { new ClassType(int[].class) };
        JavaType elemT = new ClassType(Integer.class);
        CollectionType intListType = CollectionType.construct(intListClass, bindings, superClass, superInts, elemT);
        assertEquals("java.util.ArrayList", intListType.getRawClass().getName());
        assertEquals(intListClass, intListType.getRawClass());
        assertEquals(bindings, intListType.getBindings());
        assertEquals(superClass, intListType.getSuperClass());
        assertEquals(superInts, intListType.getSuperInterfaces());
        assertEquals(elemT, intListType.getElementType());
        assertNull(intListType.getValueHandler());
        assertNull(intListType.getTypeHandler());
        assertFalse(intListType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        JavaType elemT = new ClassType(Integer.class);
        CollectionType intListType = CollectionType.construct(intListClass, elemT);
        assertEquals("java.util.ArrayList", intListType.getRawClass().getName());
        assertEquals(intListClass, intListType.getRawClass());
        assertNull(intListType.getBindings());
        assertNull(intListType.getSuperClass());
        assertNull(intListType.getSuperInterfaces());
        assertEquals(elemT, intListType.getElementType());
        assertNull(intListType.getValueHandler());
        assertNull(intListType.getTypeHandler());
        assertFalse(intListType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(Object[].class);
        JavaType[] superInts = new JavaType[] { new ClassType(int[].class) };
        JavaType elemT = new ClassType(Integer.class);
        CollectionType intListType = CollectionType.construct(intListClass, bindings, superClass, superInts, elemT);
        JavaType refinedType = intListType.refine(intListClass, bindings, superClass, superInts);
        assertEquals(intListType.getRawClass(), refinedType.getRawClass());
        assertEquals(intListType.getBindings(), refinedType.getBindings());
        assertEquals(intListType.getSuperClass(), refinedType.getSuperClass());
        assertEquals(intListType.getSuperInterfaces(), refinedType.getSuperInterfaces());
        assertEquals(intListType.getElementType(), refinedType.getElementType());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertFalse(refinedType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(Object[].class);
        JavaType superInts = new ClassType[0];
        JavaType elemT = new ClassType(Integer.class);
        JavaType contentType = new ClassType(String.class);
        CollectionType intListType = CollectionType.construct(intListClass, bindings, superClass, superInts, elemT);
        CollectionType intListWithContentType = intListType.withContentType(contentType);
        assertEquals("java.util.ArrayList<java.lang.String>", intListWithContentType.getRawClass().getName());
        assertEquals(contentType, intListWithContentType.getElementType());
        assertNull(intListWithContentType.getValueHandler());
        assertNull(intListWithContentType.getTypeHandler());
        assertFalse(intListWithContentType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(Object[].class);
        JavaType superInts = new ClassType[0];
        JavaType elemT = new ClassType(Integer.class);
        Object typeHandler = new Object();
        CollectionType intListType = CollectionType.construct(intListClass, bindings, superClass, superInts, elemT);
        CollectionType intListWithTypeHandler = intListType.withTypeHandler(typeHandler);
        assertEquals("java.util.ArrayList", intListWithTypeHandler.getRawClass().getName());
        assertEquals(elemT, intListWithTypeHandler.getElementType());
        assertEquals(typeHandler, intListWithTypeHandler.getTypeHandler());
        assertNull(intListWithTypeHandler.getValueHandler());
        assertFalse(intListWithTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(Object[].class);
        JavaType superInts = new ClassType[0];
        JavaType elemT = new ClassType(Integer.class);
        Object contentTypeHandler = new Object();
        Object typeHandler = new Object();
        CollectionType intListType = CollectionType.construct(intListClass, bindings, superClass, superInts, elemT);
        CollectionType intListWithContentTypeHandler = intListType.withContentTypeHandler(contentTypeHandler).withTypeHandler(typeHandler);
        assertEquals("java.util.ArrayList", intListWithContentTypeHandler.getRawClass().getName());
        assertEquals(elemT.withTypeHandler(contentTypeHandler), intListWithContentTypeHandler.getElementType());
        assertEquals(typeHandler, intListWithContentTypeHandler.getTypeHandler());
        assertNull(intListWithContentTypeHandler.getValueHandler());
        assertFalse(intListWithContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(Object[].class);
        JavaType superInts = new ClassType[0];
        JavaType elemT = new ClassType(Integer.class);
        Object valueHandler = new Object();
        CollectionType intListType = CollectionType.construct(intListClass, bindings, superClass, superInts, elemT);
        CollectionType intListWithValueHandler = intListType.withValueHandler(valueHandler);
        assertEquals("java.util.ArrayList", intListWithValueHandler.getRawClass().getName());
        assertEquals(elemT, intListWithValueHandler.getElementType());
        assertEquals(null, intListWithValueHandler.getValueHandler());
        assertEquals(null, intListWithValueHandler.getTypeHandler());
        assertFalse(intListWithValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(Object[].class);
        JavaType superInts = new ClassType[0];
        JavaType elemT = new ClassType(Integer.class);
        Object contentValueHandler = new Object();
        Object valueHandler = new Object();
        CollectionType intListType = CollectionType.construct(intListClass, bindings, superClass, superInts, elemT);
        CollectionType intListWithContentValueHandler = intListType.withContentValueHandler(contentValueHandler).withValueHandler(valueHandler);
        assertEquals("java.util.ArrayList", intListWithContentValueHandler.getRawClass().getName());
        assertEquals(elemT.withValueHandler(contentValueHandler), intListWithContentValueHandler.getElementType());
        assertEquals(valueHandler, intListWithContentValueHandler.getValueHandler());
        assertEquals(null, intListWithContentValueHandler.getTypeHandler());
        assertFalse(intListWithContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(Object[].class);
        JavaType superInts = new ClassType[0];
        JavaType elemT = new ClassType(Integer.class);
        CollectionType intListType = CollectionType.construct(intListClass, bindings, superClass, superInts, elemT);
        CollectionType intListWithStaticTyping = intListType.withStaticTyping();
        assertEquals("java.util.ArrayList", intListWithStaticTyping.getRawClass().getName());
        assertEquals(intListType.getRawClass(), intListWithStaticTyping.getRawClass());
        assertEquals(intListType.getBindings(), intListWithStaticTyping.getBindings());
        assertEquals(intListType.getSuperClass(), intListWithStaticTyping.getSuperClass());
        assertEquals(intListType.getSuperInterfaces(), intListWithStaticTyping.getSuperInterfaces());
        assertEquals(intListType.getElementType(), intListWithStaticTyping.getElementType());
        assertNull(intListWithStaticTyping.getValueHandler());
        assertNull(intListWithStaticTyping.getTypeHandler());
        assertTrue(intListWithStaticTyping.isStatic());
    }

    @Test
    public void testToString() {
        Class<List<Integer>> intListClass = new ArrayList<Integer>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(Object[].class);
        JavaType superInts = new ClassType[0];
        JavaType elemT = new ClassType(Integer.class);
        CollectionType intListType = CollectionType.construct(intListClass, bindings, superClass, superInts, elemT);
        assertEquals("[collection type; class java.util.ArrayList, contains class java.lang.Integer]", intListType.toString());
    }
}