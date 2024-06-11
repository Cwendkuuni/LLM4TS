package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {
    @Test
    public void testFindForCollection() {
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.emptyList().getClass());
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(null, type);
        assertTrue(result instanceof StdDelegatingDeserializer);

        type = TypeFactory.defaultInstance().constructType(Collections.singleton(null).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(null, type);
        assertTrue(result instanceof StdDelegatingDeserializer);

        type = TypeFactory.defaultInstance().constructType(Collections.singletonList(null).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(null, type);
        assertTrue(result instanceof StdDelegatingDeserializer);

        type = TypeFactory.defaultInstance().constructType(Collections.singletonMap(null, null).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(null, type);
        assertTrue(result instanceof StdDelegatingDeserializer);

        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableList(Collections.emptyList()).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(null, type);
        assertTrue(result instanceof StdDelegatingDeserializer);

        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableList(Collections.singletonList(null)).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(null, type);
        assertTrue(result instanceof StdDelegatingDeserializer);

        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableSet(Collections.singleton(null)).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(null, type);
        assertTrue(result instanceof StdDelegatingDeserializer);

        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableSet(Collections.singletonMap(null, null)).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(null, type);
        assertTrue(result instanceof StdDelegatingDeserializer);

        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableMap(Collections.singletonMap(null, null)).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(null, type);
        assertTrue(result instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForMap() {
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.singletonMap(null, null).getClass());
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(null, type);
        assertTrue(result instanceof StdDelegatingDeserializer);

        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableMap(Collections.singletonMap(null, null)).getClass());
        result = JavaUtilCollectionsDeserializers.findForMap(null, type);
        assertTrue(result instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testConverter() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(List.class);
        Class<?> rawSuper = ArrayList.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, concreteType, rawSuper);
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, converter.getClass());

        concreteType = TypeFactory.defaultInstance().constructType(Set.class);
        rawSuper = HashSet.class;
        converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, concreteType, rawSuper);
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, converter.getClass());
    }
}