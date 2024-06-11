package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        try {
            DeserializationContext ctxt = null;

            // Test CLASS_AS_ARRAYS_LIST
            JavaType type1 = TypeFactory.defaultInstance().constructType(Arrays.asList(null, null).getClass());
            JsonDeserializer<?> result1 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type1);
            assertTrue(result1 instanceof StdDelegatingDeserializer);
            Converter<?, ?> converter1 = ((StdDelegatingDeserializer<?>) result1).getConverter();
            assertTrue(converter1 instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
            assertEquals(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter1).getKind());

            // Test CLASS_SINGLETON_LIST
            JavaType type2 = TypeFactory.defaultInstance().constructType(Collections.singletonList(Boolean.TRUE).getClass());
            JsonDeserializer<?> result2 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type2);
            assertTrue(result2 instanceof StdDelegatingDeserializer);
            Converter<?, ?> converter2 = ((StdDelegatingDeserializer<?>) result2).getConverter();
            assertTrue(converter2 instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
            assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter2).getKind());

            // Test CLASS_SINGLETON_SET
            JavaType type3 = TypeFactory.defaultInstance().constructType(Collections.singleton(Boolean.TRUE).getClass());
            JsonDeserializer<?> result3 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type3);
            assertTrue(result3 instanceof StdDelegatingDeserializer);
            Converter<?, ?> converter3 = ((StdDelegatingDeserializer<?>) result3).getConverter();
            assertTrue(converter3 instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
            assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter3).getKind());

            // Test CLASS_UNMODIFIABLE_LIST
            JavaType type4 = TypeFactory.defaultInstance().constructType(Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)).getClass());
            JsonDeserializer<?> result4 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type4);
            assertTrue(result4 instanceof StdDelegatingDeserializer);
            Converter<?, ?> converter4 = ((StdDelegatingDeserializer<?>) result4).getConverter();
            assertTrue(converter4 instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
            assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter4).getKind());

            // Test CLASS_UNMODIFIABLE_SET
            JavaType type5 = TypeFactory.defaultInstance().constructType(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)).getClass());
            JsonDeserializer<?> result5 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type5);
            assertTrue(result5 instanceof StdDelegatingDeserializer);
            Converter<?, ?> converter5 = ((StdDelegatingDeserializer<?>) result5).getConverter();
            assertTrue(converter5 instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
            assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter5).getKind());

            // Test unknown type
            JavaType type6 = TypeFactory.defaultInstance().constructType(ArrayList.class);
            JsonDeserializer<?> result6 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type6);
            assertNull(result6);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException: " + e.getMessage());
        }
    }

    @Test
    public void testFindForMap() {
        try {
            DeserializationContext ctxt = null;

            // Test CLASS_SINGLETON_MAP
            JavaType type1 = TypeFactory.defaultInstance().constructType(Collections.singletonMap("a", "b").getClass());
            JsonDeserializer<?> result1 = JavaUtilCollectionsDeserializers.findForMap(ctxt, type1);
            assertTrue(result1 instanceof StdDelegatingDeserializer);
            Converter<?, ?> converter1 = ((StdDelegatingDeserializer<?>) result1).getConverter();
            assertTrue(converter1 instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
            assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter1).getKind());

            // Test CLASS_UNMODIFIABLE_MAP
            JavaType type2 = TypeFactory.defaultInstance().constructType(Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getClass());
            JsonDeserializer<?> result2 = JavaUtilCollectionsDeserializers.findForMap(ctxt, type2);
            assertTrue(result2 instanceof StdDelegatingDeserializer);
            Converter<?, ?> converter2 = ((StdDelegatingDeserializer<?>) result2).getConverter();
            assertTrue(converter2 instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
            assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, ((JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) converter2).getKind());

            // Test unknown type
            JavaType type3 = TypeFactory.defaultInstance().constructType(HashMap.class);
            JsonDeserializer<?> result3 = JavaUtilCollectionsDeserializers.findForMap(ctxt, type3);
            assertNull(result3);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException: " + e.getMessage());
        }
    }
}