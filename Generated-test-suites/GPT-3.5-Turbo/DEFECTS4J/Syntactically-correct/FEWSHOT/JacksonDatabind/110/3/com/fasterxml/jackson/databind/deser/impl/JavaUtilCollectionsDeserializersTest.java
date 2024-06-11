package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {
    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        JavaType type1 = TypeFactory.defaultInstance().constructType(CLASS_AS_ARRAYS_LIST);
        JsonDeserializer<?> result1 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type1);
        assertNotNull(result1);
        assertEquals(StdDelegatingDeserializer.class, result1.getClass());
        
        JavaType type2 = TypeFactory.defaultInstance().constructType(CLASS_SINGLETON_LIST);
        JsonDeserializer<?> result2 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type2);
        assertNotNull(result2);
        assertEquals(StdDelegatingDeserializer.class, result2.getClass());
        
        JavaType type3 = TypeFactory.defaultInstance().constructType(CLASS_SINGLETON_SET);
        JsonDeserializer<?> result3 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type3);
        assertNotNull(result3);
        assertEquals(StdDelegatingDeserializer.class, result3.getClass());
        
        JavaType type4 = TypeFactory.defaultInstance().constructType(CLASS_UNMODIFIABLE_LIST);
        JsonDeserializer<?> result4 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type4);
        assertNotNull(result4);
        assertEquals(StdDelegatingDeserializer.class, result4.getClass());
        
        JavaType type5 = TypeFactory.defaultInstance().constructType(CLASS_UNMODIFIABLE_SET);
        JsonDeserializer<?> result5 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type5);
        assertNotNull(result5);
        assertEquals(StdDelegatingDeserializer.class, result5.getClass());
        
        JavaType type6 = TypeFactory.defaultInstance().constructType(List.class);
        JsonDeserializer<?> result6 = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type6);
        assertNull(result6);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        JavaType type1 = TypeFactory.defaultInstance().constructType(CLASS_SINGLETON_MAP);
        JsonDeserializer<?> result1 = JavaUtilCollectionsDeserializers.findForMap(ctxt, type1);
        assertNotNull(result1);
        assertEquals(StdDelegatingDeserializer.class, result1.getClass());
        
        JavaType type2 = TypeFactory.defaultInstance().constructType(CLASS_UNMODIFIABLE_MAP);
        JsonDeserializer<?> result2 = JavaUtilCollectionsDeserializers.findForMap(ctxt, type2);
        assertNotNull(result2);
        assertEquals(StdDelegatingDeserializer.class, result2.getClass());
        
        JavaType type3 = TypeFactory.defaultInstance().constructType(Map.class);
        JsonDeserializer<?> result3 = JavaUtilCollectionsDeserializers.findForMap(ctxt, type3);
        assertNull(result3);
    }
}