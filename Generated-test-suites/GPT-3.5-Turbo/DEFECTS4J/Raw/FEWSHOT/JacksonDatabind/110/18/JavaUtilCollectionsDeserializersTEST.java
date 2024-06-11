package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.Util;
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
        DeserializationContext ctxt = null;
        JavaType type = null;
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertNull(deserializer);
        
        type = TypeFactory.defaultInstance().constructType(Arrays.asList(null, null).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertNull(deserializer);
        
        type = TypeFactory.defaultInstance().constructType(Collections.singletonMap("a", "b").getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }
    
    @Test
    public void testConverter() {
        int kind = 0;
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = 
            new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
        
        assertEquals(concreteType.findSuperType(rawSuper), converter.getInputType(TypeFactory.defaultInstance()));
        assertEquals(concreteType.findSuperType(rawSuper), converter.getOutputType(TypeFactory.defaultInstance()));
    }

    @Test
    public void testJavaUtilCollectionsConverter() {
        int kind = 0;
        JavaType inputType = null;
        
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = 
            new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(kind, inputType);
        
        assertNull(converter.convert(null));
        
        switch (kind) {
            case JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET:
                Set<?> set = (Set<?>) value;
                _checkSingleton(set.size());
                assertEquals(Collections.singleton(set.iterator().next()), converter.convert(set));
                break;
            case JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST:
                List<?> list = (List<?>) value;
                _checkSingleton(list.size());
                assertEquals(Collections.singletonList(list.get(0)), converter.convert(list));
                break;
            case JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP:
                Map<?,?> map = (Map<?,?>) value;
                _checkSingleton(map.size());
                Map.Entry<?,?> entry = map.entrySet().iterator().next();
                assertEquals(Collections.singletonMap(entry.getKey(), entry.getValue()), converter.convert(map));
                break;
            case JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET:
                assertEquals(Collections.unmodifiableSet((Set<?>) value), converter.convert(value));
                break;
            case JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST:
                assertEquals(Collections.unmodifiableList((List<?>) value), converter.convert(value));
                break;
            case JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP:
                assertEquals(Collections.unmodifiableMap((Map<?,?>) value), converter.convert(value));
                break;
            case JavaUtilCollectionsDeserializers.TYPE_AS_LIST:
            default:
                assertEquals(value, converter.convert(value));
                break;
        }
    }

    private void _checkSingleton(int size) {
        if (size != 1) {
            throw new IllegalArgumentException("Can not deserialize Singleton container from " + size + " entries");
        }
    }

}