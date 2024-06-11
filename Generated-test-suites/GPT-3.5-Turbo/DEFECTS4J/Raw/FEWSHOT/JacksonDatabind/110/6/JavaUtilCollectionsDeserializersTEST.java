package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void testFindForCollection_with_CLASS_AS_ARRAYS_LIST() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, deserializer.getConverterInstance().getClass());
    }
    
    @Test
    public void testFindForCollection_with_CLASS_SINGLETON_LIST() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.singletonList("").getClass());
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, deserializer.getConverterInstance().getClass());
    }
    
    @Test
    public void testFindForCollection_with_CLASS_SINGLETON_SET() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.singleton("").getClass());
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, deserializer.getConverterInstance().getClass());
    }
    
    @Test
    public void testFindForCollection_with_CLASS_UNMODIFIABLE_LIST() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableList(new ArrayList<>()).getClass());
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, deserializer.getConverterInstance().getClass());
    }
    
    @Test
    public void testFindForCollection_with_CLASS_UNMODIFIABLE_SET() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableSet(new HashSet<>()).getClass());
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, deserializer.getConverterInstance().getClass());
    }
    
    @Test
    public void testFindForCollection_with_unknownType() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class);
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap_with_CLASS_SINGLETON_MAP() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.singletonMap("", "").getClass());
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, deserializer.getConverterInstance().getClass());
    }
    
    @Test
    public void testFindForMap_with_CLASS_UNMODIFIABLE_MAP() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = TypeFactory.defaultInstance().constructType(Collections.unmodifiableMap(new HashMap<>()).getClass());
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
        assertEquals(JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter.class, deserializer.getConverterInstance().getClass());
    }
    
    @Test
    public void testFindForMap_with_unknownType() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertNull(deserializer);
    }
}