package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
        DeserializationContext ctxt = null;
        
        // Test CASE_AS_ARRAYS_LIST
        JavaType typeCaseAsArraysList = TypeFactory.defaultInstance().constructType(Arrays.asList(Object.class));
        JsonDeserializer<?> deserializerCaseAsArraysList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, typeCaseAsArraysList);
        assertNull(deserializerCaseAsArraysList);
        
        // Test CLASS_SINGLETON_LIST
        JavaType typeClassSingletonList = TypeFactory.defaultInstance().constructType(Util.classSingletonList);
        JsonDeserializer<?> deserializerClassSingletonList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, typeClassSingletonList);
        assertNull(deserializerClassSingletonList);
        
        // Test CLASS_SINGLETON_SET
        JavaType typeClassSingletonSet = TypeFactory.defaultInstance().constructType(Util.classSingletonSet);
        JsonDeserializer<?> deserializerClassSingletonSet = JavaUtilCollectionsDeserializers.findForCollection(ctxt, typeClassSingletonSet);
        assertNull(deserializerClassSingletonSet);
        
        // Test CLASS_UNMODIFIABLE_LIST
        JavaType typeClassUnmodifiableList = TypeFactory.defaultInstance().constructType(Util.classUnmodifiableList);
        JsonDeserializer<?> deserializerClassUnmodifiableList = JavaUtilCollectionsDeserializers.findForCollection(ctxt, typeClassUnmodifiableList);
        assertNull(deserializerClassUnmodifiableList);
        
        // Test CLASS_UNMODIFIABLE_SET
        JavaType typeClassUnmodifiableSet = TypeFactory.defaultInstance().constructType(Util.classUnmodifiableSet);
        JsonDeserializer<?> deserializerClassUnmodifiableSet = JavaUtilCollectionsDeserializers.findForCollection(ctxt, typeClassUnmodifiableSet);
        assertNull(deserializerClassUnmodifiableSet);
    }

    @Test
    public void testFindForMap() {
        DeserializationContext ctxt = null;
        
        // Test CLASS_SINGLETON_MAP
        JavaType typeClassSingletonMap = TypeFactory.defaultInstance().constructType(Util.classSingletonMap);
        JsonDeserializer<?> deserializerClassSingletonMap = JavaUtilCollectionsDeserializers.findForMap(ctxt, typeClassSingletonMap);
        assertNull(deserializerClassSingletonMap);
        
        // Test CLASS_UNMODIFIABLE_MAP
        JavaType typeClassUnmodifiableMap = TypeFactory.defaultInstance().constructType(Util.classUnmodifiableMap);
        JsonDeserializer<?> deserializerClassUnmodifiableMap = JavaUtilCollectionsDeserializers.findForMap(ctxt, typeClassUnmodifiableMap);
        assertNull(deserializerClassUnmodifiableMap);
    }

    @Test
    public void testConverter() {
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, type, Set.class);
        assertSame(type, converter.getInputType(null));
        assertSame(type, converter.getOutputType(null));
    }
}