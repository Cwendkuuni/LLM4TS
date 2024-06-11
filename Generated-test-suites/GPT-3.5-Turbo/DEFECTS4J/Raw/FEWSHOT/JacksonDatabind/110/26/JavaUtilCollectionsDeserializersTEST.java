package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {
    
    private DeserializationContext ctxt;
    private JavaType type;
    
    @Before
    public void setup() {
        ctxt = new DeserializationContext(null, null, null);
        type = TypeFactory.defaultInstance().constructType(String.class);
    }
    
    @Test
    public void testFindForCollection() throws JsonMappingException {
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(Collections.singletonList(null).getClass(), result.getValueClass());
    }
    
    @Test
    public void testFindForCollectionWithSingletonList() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);
        
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(Collections.singletonList(null).getClass(), result.getValueClass());
    }
    
    @Test
    public void testFindForCollectionWithSingletonSet() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);
        
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(Collections.singleton(null).getClass(), result.getValueClass());
    }
    
    @Test
    public void testFindForCollectionWithUnmodifiableList() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);
        
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(Collections.unmodifiableList(Collections.singletonList(null)).getClass(), result.getValueClass());
    }
    
    @Test
    public void testFindForCollectionWithUnmodifiableSet() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);
        
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(Collections.unmodifiableSet(Collections.singleton(null)).getClass(), result.getValueClass());
    }
    
    @Test
    public void testFindForCollectionWithInvalidType() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(Double.class);
        
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertNull(result);
    }
    
    @Test
    public void testFindForMap() throws JsonMappingException {
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(Collections.singletonMap(null, null).getClass(), result.getValueClass());
    }
    
    @Test
    public void testFindForMapWithSingletonMap() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(Collections.singletonMap(null, null).getClass(), result.getValueClass());
    }
    
    @Test
    public void testFindForMapWithUnmodifiableMap() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);
        
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertTrue(result instanceof StdDelegatingDeserializer);
        assertEquals(Collections.unmodifiableMap(Collections.singletonMap(null, null)).getClass(), result.getValueClass());
    }
    
    @Test
    public void testFindForMapWithInvalidType() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(Double.class);
        
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertNull(result);
    }
    
    @Test
    public void testConverter() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, type, List.class);
        
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, converter._kind);
        assertEquals(type.findSuperType(List.class), converter._inputType);
    }
    
    @Test
    public void testConvertWithSingletonSet() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type, Set.class);
        
        Set<?> set = Collections.singleton(null);
        
        Object result = converter.convert(set);
        
        assertEquals(Collections.singleton(null), result);
    }
    
    @Test
    public void testConvertWithSingletonList() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, type, List.class);
        
        List<?> list = Collections.singletonList(null);
        
        Object result = converter.convert(list);
        
        assertEquals(Collections.singletonList(null), result);
    }
    
    @Test
    public void testConvertWithSingletonMap() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, type, Map.class);
        
        Map<?, ?> map = Collections.singletonMap(null, null);
        
        Object result = converter.convert(map);
        
        assertEquals(Collections.singletonMap(null, null), result);
    }
    
    @Test
    public void testConvertWithUnmodifiableSet() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, type, Set.class);
        
        Set<?> set = Collections.unmodifiableSet(Collections.singleton(null));
        
        Object result = converter.convert(set);
        
        assertEquals(Collections.unmodifiableSet(Collections.singleton(null)), result);
    }
    
    @Test
    public void testConvertWithUnmodifiableList() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, type, List.class);
        
        List<?> list = Collections.unmodifiableList(Collections.singletonList(null));
        
        Object result = converter.convert(list);
        
        assertEquals(Collections.unmodifiableList(Collections.singletonList(null)), result);
    }
    
    @Test
    public void testConvertWithUnmodifiableMap() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, type, Map.class);
        
        Map<?, ?> map = Collections.unmodifiableMap(Collections.singletonMap(null, null));
        
        Object result = converter.convert(map);
        
        assertEquals(Collections.unmodifiableMap(Collections.singletonMap(null, null)), result);
    }
    
    @Test
    public void testConvertWithAsListType() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, type, List.class);
        
        List<?> list = Collections.singletonList(null);
        
        Object result = converter.convert(list);
        
        assertEquals(Collections.singletonList(null), result);
    }
    
    @Test
    public void testConvertWithInvalidSize() {
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type, Set.class);
        
        Set<?> set = Collections.singleton(null);
        
        try {
            converter._checkSingleton(2);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not deserialize Singleton container from 2 entries", e.getMessage());
        }
    }
}