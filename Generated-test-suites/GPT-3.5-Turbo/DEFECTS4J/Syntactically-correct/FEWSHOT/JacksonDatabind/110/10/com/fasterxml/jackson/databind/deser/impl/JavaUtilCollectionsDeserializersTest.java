
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Test CLASS_AS_ARRAYS_LIST
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_AS_ARRAYS_LIST);
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        checkConverter(result, JavaUtilCollectionsDeserializers.TYPE_AS_LIST, type, List.class);

        // Test CLASS_SINGLETON_LIST
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        checkConverter(result, JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, type, List.class);

        // Test CLASS_SINGLETON_SET
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        checkConverter(result, JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type, Set.class);

        // Test CLASS_UNMODIFIABLE_LIST
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        checkConverter(result, JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, type, List.class);

        // Test CLASS_UNMODIFIABLE_SET
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        checkConverter(result, JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, type, Set.class);

        // Test unknown class
        type = TypeFactory.defaultInstance().constructType(TestClass.class);
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(result);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Test CLASS_SINGLETON_MAP
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        checkConverter(result, JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, type, Map.class);

        // Test CLASS_UNMODIFIABLE_MAP
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);
        result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        checkConverter(result, JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, type, Map.class);

        // Test unknown class
        type = TypeFactory.defaultInstance().constructType(TestClass.class);
        result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(result);
    }

    private void checkConverter(JsonDeserializer<?> result, int kind, JavaType inputType, Class<?> rawSuper) {
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        StdDelegatingDeserializer<Object> deserializer = (StdDelegatingDeserializer<Object>) result;
        assertNotNull(deserializer.getConverter());
        assertTrue(deserializer.getConverter() instanceof JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter);

        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = (JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter) deserializer.getConverter();
        assertEquals(kind, converter._kind);
        assertEquals(inputType, converter._inputType);
        assertEquals(rawSuper, converter.getInputType(null).getRawClass());
        assertEquals(inputType, converter.getOutputType(null));
    }

    private static class TestClass {
    }
}
