package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JavaType type = null;
        
        // Test for CLASS_AS_ARRAYS_LIST
        type = JavaType.construct(cls, CLASS_AS_ARRAYS_LIST);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        
        // Test for CLASS_SINGLETON_LIST
        type = JavaType.construct(cls, CLASS_SINGLETON_LIST);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        
        // Test for CLASS_SINGLETON_SET
        type = JavaType.construct(cls, CLASS_SINGLETON_SET);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        
        // Test for CLASS_UNMODIFIABLE_LIST
        type = JavaType.construct(cls, CLASS_UNMODIFIABLE_LIST);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        
        // Test for CLASS_UNMODIFIABLE_SET
        type = JavaType.construct(cls, CLASS_UNMODIFIABLE_SET);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        
        // Test for unknown type, returns null
        type = JavaType.construct(cls, ArrayList.class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JavaType type = null;
        
        // Test for CLASS_SINGLETON_MAP
        type = JavaType.construct(cls, CLASS_SINGLETON_MAP);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        
        // Test for CLASS_UNMODIFIABLE_MAP
        type = JavaType.construct(cls, CLASS_UNMODIFIABLE_MAP);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        
        // Test for unknown type, returns null
        type = JavaType.construct(cls, HashMap.class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testConverter() throws JsonMappingException {
        JavaType inputType = null;
        Class<?> rawSuper = null;
        
        // Test for TYPE_SINGLETON_SET
        inputType = JavaType.construct(cls, CLASS_SINGLETON_SET);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_SET, inputType, Set.class);
        assertNotNull(converter);
        
        // Test for TYPE_SINGLETON_LIST
        inputType = JavaType.construct(cls, CLASS_SINGLETON_LIST);
        converter = JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_LIST, inputType, List.class);
        assertNotNull(converter);
        
        // Test for TYPE_SINGLETON_MAP
        inputType = JavaType.construct(cls, CLASS_SINGLETON_MAP);
        converter = JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_MAP, inputType, Map.class);
        assertNotNull(converter);
        
        // Test for TYPE_UNMODIFIABLE_SET
        inputType = JavaType.construct(cls, CLASS_UNMODIFIABLE_SET);
        converter = JavaUtilCollectionsDeserializers.converter(TYPE_UNMODIFIABLE_SET, inputType, Set.class);
        assertNotNull(converter);
        
        // Test for TYPE_UNMODIFIABLE_LIST
        inputType = JavaType.construct(cls, CLASS_UNMODIFIABLE_LIST);
        converter = JavaUtilCollectionsDeserializers.converter(TYPE_UNMODIFIABLE_LIST, inputType, List.class);
        assertNotNull(converter);
        
        // Test for TYPE_UNMODIFIABLE_MAP
        inputType = JavaType.construct(cls, CLASS_UNMODIFIABLE_MAP);
        converter = JavaUtilCollectionsDeserializers.converter(TYPE_UNMODIFIABLE_MAP, inputType, Map.class);
        assertNotNull(converter);
    }
}