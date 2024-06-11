package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {
    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.emptySet().getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        assertNotNull(deserializer);
        
        type = TypeFactory.defaultInstance().constructType(Arrays.asList().getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        assertNotNull(deserializer);
        
        type = TypeFactory.defaultInstance().constructType(Collections.singletonList(null).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        assertNotNull(deserializer);
        
        type = TypeFactory.defaultInstance().constructType(Collections.singleton(null).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        assertNotNull(deserializer);
        
        type = TypeFactory.defaultInstance().constructType(Collections.emptyMap().getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext context = new DeserializationContext(null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.emptyMap().getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, type);
        assertNotNull(deserializer);
        
        type = TypeFactory.defaultInstance().constructType(Collections.singletonMap(null, null).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(context, type);
        assertNotNull(deserializer);
        
        type = TypeFactory.defaultInstance().constructType(Collections.emptyList().getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(context, type);
        assertNull(deserializer);
    }
    
    @Test
    public void testConverter() {
        JavaType inputType = TypeFactory.defaultInstance().constructType(Collections.emptyList().getClass());
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);
        assertEquals(inputType, converter.getInputType(TypeFactory.defaultInstance()));
        assertEquals(inputType, converter.getOutputType(TypeFactory.defaultInstance()));
        
        inputType = TypeFactory.defaultInstance().constructType(Arrays.asList(null, null).getClass());
        converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(2, inputType);
        assertEquals(inputType, converter.getInputType(TypeFactory.defaultInstance()));
        assertEquals(inputType, converter.getOutputType(TypeFactory.defaultInstance()));
    }
}