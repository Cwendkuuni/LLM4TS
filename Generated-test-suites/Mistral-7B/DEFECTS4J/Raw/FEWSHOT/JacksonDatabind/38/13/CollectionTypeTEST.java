package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> listType = ArrayList.class;
        JavaType javaType = CollectionType.construct(listType, new TypeBindings(), null, null, SimpleType.construct(String.class));
        assertNotNull(javaType);
        assertEquals(listType, javaType.getRawClass());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> listType = ArrayList.class;
        JavaType javaType = CollectionType.construct(listType, SimpleType.construct(String.class));
        assertNotNull(javaType);
        assertEquals(listType, javaType.getRawClass());
    }

    @Test
    public void testRefine() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = SimpleType.construct(String.class);
        JavaType javaType = CollectionType.refine(listType, new TypeBindings(), null, null, elemType);
        assertNotNull(javaType);
        assertEquals(listType, javaType.getRawClass());
        assertEquals(elemType, javaType.getContentType());
    }

    @Test
    public void testNarrow() {
        Class<?> listType = ArrayList.class;
        JavaType javaType = CollectionType.construct(listType, null, null, null, SimpleType.construct(String.class));
        CollectionType narrowedType = (CollectionType) javaType.narrow(LinkedList.class);
        assertNotNull(narrowedType);
        assertEquals(LinkedList.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = SimpleType.construct(String.class);
        JavaType javaType = CollectionType.construct(listType, null, null, null, elemType);
        JavaType newJavaType = javaType.withContentType(SimpleType.construct(Integer.class));
        assertNotNull(newJavaType);
        assertEquals(listType, newJavaType.getRawClass());
        assertEquals(SimpleType.construct(Integer.class), newJavaType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = SimpleType.construct(String.class);
        JavaType javaType = CollectionType.construct(listType, null, null, null, elemType);
        Object handler = new Object() {};
        JavaType newJavaType = javaType.withTypeHandler(handler);
        assertNotNull(newJavaType);
        assertEquals(listType, newJavaType.getRawClass());
        assertEquals(handler, newJavaType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = SimpleType.construct(String.class);
        JavaType javaType = CollectionType.construct(listType, null, null, null, elemType);
        Object handler = new Object() {};
        JavaType newJavaType = javaType.withContentTypeHandler(handler);
        assertNotNull(newJavaType);
        assertEquals(listType, newJavaType.getRawClass());
        assertEquals(elemType.withTypeHandler(handler), newJavaType.getContentType());
        assertEquals(handler, newJavaType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = SimpleType.construct(String.class);
        JavaType javaType = CollectionType.construct(listType, null, null, null, elemType);
        Object handler = new Object() {};
        JavaType newJavaType = javaType.withValueHandler(handler);
        assertNotNull(newJavaType);
        assertEquals(listType, newJavaType.getRawClass());
        assertEquals(handler, newJavaType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> listType = ArrayList.class;
        JavaType elemType = SimpleType.construct(String.class);
        JavaType javaType = CollectionType.construct(listType, null, null, null, elemType);
        Object handler = new Object() {};
        JavaType newJavaType = javaType.withContentValueHandler(handler);
        assertNotNull(newJavaType);
        assertEquals(listType, newJavaType.getRawClass());
        assertEquals(elemType.withValueHandler(handler), newJavaType.getContentType());
        assertEquals(handler, newJavaType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> listType = ArrayList.class;
        JavaType javaType = CollectionType.construct(listType, null, null, null, SimpleType.construct(String.class));
        JavaType staticJavaType = javaType.withStaticTyping();
        assertNotNull(staticJavaType);
        assertTrue(staticJavaType.isStatic());
    }
}