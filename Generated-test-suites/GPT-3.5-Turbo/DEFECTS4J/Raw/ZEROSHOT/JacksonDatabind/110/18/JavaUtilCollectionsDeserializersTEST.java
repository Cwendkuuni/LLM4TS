package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = null;

        // Test for CLASS_AS_ARRAYS_LIST
        type = JavaType.fromRawClass(Arrays.asList(null, null).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);

        // Test for CLASS_SINGLETON_LIST
        type = JavaType.fromRawClass(Collections.singletonList(Boolean.TRUE).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);

        // Test for CLASS_SINGLETON_SET
        type = JavaType.fromRawClass(Collections.singleton(Boolean.TRUE).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);

        // Test for CLASS_UNMODIFIABLE_LIST
        type = JavaType.fromRawClass(Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);

        // Test for CLASS_UNMODIFIABLE_SET
        type = JavaType.fromRawClass(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);

        // Test for unsupported type
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = null;

        // Test for CLASS_SINGLETON_MAP
        type = JavaType.fromRawClass(Collections.singletonMap("a", "b").getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);

        // Test for CLASS_UNMODIFIABLE_MAP
        type = JavaType.fromRawClass(Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);

        // Test for unsupported type
        assertNull(deserializer);
    }

}