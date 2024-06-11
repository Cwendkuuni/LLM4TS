package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {
    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Test for CLASS_AS_ARRAYS_LIST
        type = TypeFactory.defaultInstance().constructType(Arrays.asList(null, null).getClass());
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for CLASS_SINGLETON_LIST
        type = TypeFactory.defaultInstance().constructType(Collections.singletonList(Boolean.TRUE).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for CLASS_SINGLETON_SET
        type = TypeFactory.defaultInstance().constructType(Collections.singleton(Boolean.TRUE).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for CLASS_UNMODIFIABLE_LIST
        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for CLASS_UNMODIFIABLE_SET
        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)).getClass());
        result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Test for CLASS_SINGLETON_MAP
        type = TypeFactory.defaultInstance().constructType(Collections.singletonMap("a", "b").getClass());
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);

        // Test for CLASS_UNMODIFIABLE_MAP
        type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getClass());
        result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(result);
        assertTrue(result instanceof StdDelegatingDeserializer);
    }
}