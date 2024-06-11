package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class CollectionTypeTest {

    @Test
    public void testCollectionTypeConstructor() {
        Class<?> collectionClass = java.util.ArrayList.class;
        JavaType javaType = CollectionType.construct(collectionClass, new TypeBindings(), null, null, null);
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(collectionClass, collectionType.getRawClass());
    }

    @Test
    public void testCollectionTypeDeprecatedConstructor() {
        Class<?> collectionClass = java.util.ArrayList.class;
        JavaType javaType = CollectionType.construct(collectionClass, null);
        CollectionType collectionType = (CollectionType) javaType;
        assertNotNull(collectionType);
        assertEquals(collectionClass, collectionType.getRawClass());
    }

    @Test
    public void testNarrowMethod() {
        Class<?> listClass = java.util.ArrayList.class;
        JavaType javaType = CollectionType.construct(listClass, null);
        CollectionType collectionType = (CollectionType) javaType;
        JavaType narrowType = collectionType.narrow(LinkedList.class);
        assertNotNull(narrowType);
        assertFalse(narrowType.isSameTypeAs(collectionType));
        assertEquals(LinkedList.class, narrowType.getRawClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedNarrowMethod() {
        Class<?> listClass = java.util.ArrayList.class;
        JavaType javaType = CollectionType.construct(listClass, null);
        CollectionType collectionType = (CollectionType) javaType;
        JavaType narrowType = collectionType._narrow(LinkedList.class);
    }

    @Test
    public void testWithContentTypeMethod() {
        Class<?> listClass = java.util.ArrayList.class;
        JavaType javaType = CollectionType.construct(listClass, null);
        CollectionType collectionType = (CollectionType) javaType;
        JavaType contentType = JavaType.constructType(int[].class);
        JavaType newJavaType = collectionType.withContentType(contentType);
        assertNotNull(newJavaType);
        assertEquals(contentType, newJavaType.getContentType());
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<?> listClass = java.util.ArrayList.class;
        JavaType javaType = CollectionType.construct(listClass, null);
        CollectionType collectionType = (CollectionType) javaType;
        Object handler = new Object();
        JavaType newJavaType = collectionType.withTypeHandler(handler);
        assertNotNull(newJavaType);
        assertNull(newJavaType.getTypeHandler());
        assertEquals(handler, newJavaType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Class<?> listClass = java.util.ArrayList.class;
        JavaType javaType = CollectionType.construct(listClass, null);
        CollectionType collectionType = (CollectionType) javaType;
        Object contentHandler = new Object();
        Object handler = new Object();
        JavaType newJavaType = collectionType.withContentTypeHandler(contentHandler);
        assertNotNull(newJavaType);
        assertEquals(contentHandler, newJavaType.getContentTypeHandler());
        assertNull(newJavaType.getTypeHandler());
        assertEquals(handler, newJavaType.getValueHandler());
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<?> listClass = java.util.ArrayList.class;
        JavaType javaType = CollectionType.construct(listClass, null);
        CollectionType collectionType = (CollectionType) javaType;
        Object handler = new Object();
        JavaType newJavaType = collectionType.withValueHandler(handler);
        assertNotNull(newJavaType);
        assertNull(newJavaType.getTypeHandler());
        assertNull(newJavaType.getContentTypeHandler());
        assertEquals(handler, newJavaType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Class<?> listClass = java.util.ArrayList.class;
        JavaType javaType = CollectionType.construct(listClass, null);
        CollectionType collectionType = (CollectionType) javaType;
        Object contentHandler = new Object();
        Object handler = new Object();
        JavaType newJavaType = collectionType.withContentValueHandler(contentHandler);
        assertNotNull(newJavaType);
        assertEquals(contentHandler, newJavaType.getContentValueHandler());
        assertNull(newJavaType.getTypeHandler());
        assertNull(newJavaType.getValueHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<?> listClass = java.util.ArrayList.class;
        JavaType javaType = CollectionType.construct(listClass, null);
        CollectionType collectionType = (CollectionType) javaType;
        JavaType newJavaType = collectionType.withStaticTyping();
        assertNotNull(newJavaType);
        assertTrue(newJavaType.isStatic());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testRefineMethod() {
        Class<?> listClass = java.util.ArrayList.class;
        JavaType javaType = CollectionType.construct(listClass, null);
        CollectionType collectionType = (CollectionType) javaType;
        Class<?> rawType = LinkedList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType newJavaType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(newJavaType);
        assertFalse(newJavaType.isSameTypeAs(collectionType));
        assertEquals(LinkedList.class, newJavaType.getRawClass());
    }
}