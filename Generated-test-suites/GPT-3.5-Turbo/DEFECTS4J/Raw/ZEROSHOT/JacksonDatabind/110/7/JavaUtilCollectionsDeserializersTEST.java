package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = TypeFactory.defaultInstance().constructType(HashMap.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testConverter() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, concreteType);
        Object converted = converter.convert(null);
        assertNull(converted);
    }

    @Test
    public void testJavaUtilCollectionsConverter() {
        JavaType inputType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter =
                new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, inputType);
        Object value = Collections.singleton(1);
        Object converted = converter.convert(value);
        assertTrue(converted instanceof Set);
        assertEquals(1, ((Set<?>) converted).size());
        assertEquals(1, ((Set<?>) converted).iterator().next());
    }

}