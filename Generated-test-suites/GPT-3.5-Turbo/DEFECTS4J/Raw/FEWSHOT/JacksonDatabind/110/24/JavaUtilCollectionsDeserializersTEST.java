package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Null type should return null
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(ctxt, null));

        // Test for Class object types
        type = JavaType.construct(Class.class);
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(ctxt, type));

        // Test for Singleton List type
        type = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_SINGLETON_LIST);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(TYPE_SINGLETON_LIST, ((JavaUtilCollectionsConverter)((StdDelegatingDeserializer<?>) deserializer).getDelegatee())._kind);

        // Test for Singleton Set type
        type = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_SINGLETON_SET);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(TYPE_SINGLETON_SET, ((JavaUtilCollectionsConverter)((StdDelegatingDeserializer<?>) deserializer).getDelegatee())._kind);

        // Test for Unmodifiable List type
        type = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_UNMODIFIABLE_LIST);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(TYPE_UNMODIFIABLE_LIST, ((JavaUtilCollectionsConverter)((StdDelegatingDeserializer<?>) deserializer).getDelegatee())._kind);

        // Test for Unmodifiable Set type
        type = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_UNMODIFIABLE_SET);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(TYPE_UNMODIFIABLE_SET, ((JavaUtilCollectionsConverter)((StdDelegatingDeserializer<?>) deserializer).getDelegatee())._kind);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Null type should return null
        assertNull(JavaUtilCollectionsDeserializers.findForMap(ctxt, null));

        // Test for Class object types
        type = JavaType.construct(Class.class);
        assertNull(JavaUtilCollectionsDeserializers.findForMap(ctxt, type));

        // Test for Singleton Map type
        type = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_SINGLETON_MAP);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(TYPE_SINGLETON_MAP, ((JavaUtilCollectionsConverter)((StdDelegatingDeserializer<?>) deserializer).getDelegatee())._kind);

        // Test for Unmodifiable Map type
        type = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_UNMODIFIABLE_MAP);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(TYPE_UNMODIFIABLE_MAP, ((JavaUtilCollectionsConverter)((StdDelegatingDeserializer<?>) deserializer).getDelegatee())._kind);
    }

    @Test
    public void testConverter() {
        JavaType concreteType = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_SINGLETON_LIST);
        Class<?> rawSuper = List.class;
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_LIST, concreteType, rawSuper);
        assertNotNull(converter);
        assertEquals(TYPE_SINGLETON_LIST, converter._kind);
        assertEquals(concreteType.findSuperType(rawSuper), converter._inputType);
    }

    @Test
    public void testJavaUtilCollectionsConverter() {
        JavaType inputType = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_SINGLETON_LIST);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(TYPE_SINGLETON_LIST, inputType);
        assertNotNull(converter);
        assertEquals(inputType, converter._inputType);
        assertEquals(TYPE_SINGLETON_LIST, converter._kind);
    }

    @Test
    public void testConvert() {
        JavaType inputType = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_SINGLETON_SET);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(TYPE_SINGLETON_SET, inputType);

        // Test for Singleton Set type
        Set<?> set = Collections.singleton("value");
        Object result = converter.convert(set);
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertEquals(set.iterator().next(), ((Set<?>) result).iterator().next());

        // Test for Singleton List type
        List<?> list = Collections.singletonList("value");
        result = converter.convert(list);
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertEquals(list.get(0), ((Set<?>) result).iterator().next());

        // Test for Singleton Map type
        Map<?, ?> map = Collections.singletonMap("key", "value");
        result = converter.convert(map);
        assertNotNull(result);
        assertTrue(result instanceof Set);
        Map.Entry<?, ?> entry = ((Set<Map.Entry<?, ?>>) result).iterator().next();
        assertEquals(map.entrySet().iterator().next().getKey(), entry.getKey());
        assertEquals(map.entrySet().iterator().next().getValue(), entry.getValue());

        // Test for Unmodifiable Set type
        set = Collections.unmodifiableSet(set);
        result = converter.convert(set);
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertEquals(set, result);

        // Test for Unmodifiable List type
        list = Collections.unmodifiableList(list);
        result = converter.convert(list);
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertTrue(((Set<?>) result).containsAll(list));

        // Test for Unmodifiable Map type
        map = Collections.unmodifiableMap(map);
        result = converter.convert(map);
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertTrue(((Set<?>) result).containsAll(map.entrySet()));
    }

    @Test
    public void testGetInputType() {
        JavaType inputType = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_SINGLETON_LIST);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(TYPE_SINGLETON_LIST, inputType);
        assertEquals(inputType, converter.getInputType(null));
    }

    @Test
    public void testGetOutputType() {
        JavaType inputType = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_SINGLETON_LIST);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(TYPE_SINGLETON_LIST, inputType);
        assertEquals(inputType, converter.getOutputType(null));
    }

    @Test
    public void testCheckSingleton() {
        JavaType inputType = JavaType.construct(JavaUtilCollectionsDeserializers.class, CLASS_SINGLETON_LIST);
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(TYPE_SINGLETON_LIST, inputType);

        // Test for singleton size
        converter._checkSingleton(1);

        // Test for non-singleton size
        try {
            converter._checkSingleton(0);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not deserialize Singleton container from 0 entries", e.getMessage());
        }
    }
}