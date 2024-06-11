package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaUtilCollectionsDeserializersTest {
    @Test
    public void testFindForCollection() throws JsonMappingException {
        JavaType type1 = TypeFactory.defaultInstance().constructType(Arrays.asList(null, null).getClass());
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, type1));

        JavaType type2 = TypeFactory.defaultInstance().constructType(Collections.singleton(Boolean.TRUE).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, type2);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);
        StdDelegatingDeserializer<?> delegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(delegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) delegatingDeserializer.getDelegatee();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, converter._kind);

        JavaType type3 = TypeFactory.defaultInstance().constructType(Arrays.asList(Boolean.TRUE).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, type3);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);
        delegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(delegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) delegatingDeserializer.getDelegatee();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, converter._kind);

        // Test other types
        JavaType type4 = TypeFactory.defaultInstance().constructType(Collections.singletonList(Boolean.TRUE).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, type4);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);
        delegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(delegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) delegatingDeserializer.getDelegatee();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, converter._kind);

        JavaType type5 = TypeFactory.defaultInstance().constructType(Collections.singletonMap("a", "b").getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, type5);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        JavaType type1 = TypeFactory.defaultInstance().constructType(Collections.singletonMap("a", "b").getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(null, type1);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);
        StdDelegatingDeserializer<?> delegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(delegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) delegatingDeserializer.getDelegatee();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, converter._kind);

        JavaType type2 = TypeFactory.defaultInstance().constructType(Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(null, type2);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);
        delegatingDeserializer = (StdDelegatingDeserializer<?>) deserializer;
        assertTrue(delegatingDeserializer.getDelegatee() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);
        converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) delegatingDeserializer.getDelegatee();
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, converter._kind);

        JavaType type3 = TypeFactory.defaultInstance().constructType(Collections.singleton(Boolean.TRUE).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(null, type3);
        assertNull(deserializer);
    }

    @Test
    public void testJavaUtilCollectionsConverter() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter1 = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null);
        assertTrue(converter1.convert(null) instanceof Set);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter2 = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null);
        assertTrue(converter2.convert(null) instanceof List);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter3 = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, null);
        assertTrue(converter3.convert(null) instanceof Map);
    }
}