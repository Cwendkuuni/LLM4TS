package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {
    
    private AnyGetterWriter anyGetterWriter;
    private BeanProperty property;
    private AnnotatedMember accessor;
    private MapSerializer serializer;
    
    @Before
    public void setUp() {
        property = new BeanProperty();
        accessor = new AnnotatedMember();
        serializer = new MapSerializer();
        
        anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
    }
    
    @Test
    public void testGetAndSerialize_NullValue_Returns() {
        Object bean = new Object();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        try {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testGetAndSerialize_NotMap_ThrowsJsonMappingException() {
        Object bean = new Object();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        // Set up custom value
        Object value = new Object();
        accessor.setValue(bean, value);
        
        try {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
            fail("JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + accessor.getName() + "()) " +
                    "not java.util.Map but " + value.getClass().getName(), e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testGetAndSerialize_MapValue_CallsSerializeFields() {
        Object bean = new Object();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        // Set up custom value
        Map<?, ?> value = new HashMap<Object, Object>();
        accessor.setValue(bean, value);
        
        // Set up mock MapSerializer
        MapSerializer mapSerializer = new MapSerializer();
        anyGetterWriter._mapSerializer = mapSerializer;
        
        try {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
            
            assertSame(value, mapSerializer.getFieldsArg1());
            assertSame(gen, mapSerializer.getFieldsArg2());
            assertSame(provider, mapSerializer.getFieldsArg3());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testGetAndFilter_NullValue_Returns() {
        Object bean = new Object();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();
        
        try {
            anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testGetAndFilter_NotMap_ThrowsJsonMappingException() {
        Object bean = new Object();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();
        
        // Set up custom value
        Object value = new Object();
        accessor.setValue(bean, value);
        
        try {
            anyGetterWriter.getAndFilter(bean, gen, provider, filter);
            fail("JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + accessor.getName() + "()) " +
                    "not java.util.Map but " + value.getClass().getName(), e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testGetAndFilter_MapValue_CallsSerializeFilteredFields() {
        Object bean = new Object();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();
        
        // Set up custom value
        Map<?, ?> value = new HashMap<Object, Object>();
        accessor.setValue(bean, value);
        
        // Set up mock MapSerializer
        MapSerializer mapSerializer = new MapSerializer();
        anyGetterWriter._mapSerializer = mapSerializer;
        
        try {
            anyGetterWriter.getAndFilter(bean, gen, provider, filter);
            
            assertSame(value, mapSerializer.getFilteredFieldsArg1());
            assertSame(gen, mapSerializer.getFilteredFieldsArg2());
            assertSame(provider, mapSerializer.getFilteredFieldsArg3());
            assertSame(filter, mapSerializer.getFilteredFieldsArg4());
            assertNull(mapSerializer.getFilteredFieldsArg5());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testResolve_CallsHandlePrimaryContextualization() {
        SerializerProvider provider = new SerializerProvider();
        
        // Set up mock MapSerializer
        MapSerializer mapSerializer = new MapSerializer();
        anyGetterWriter._mapSerializer = mapSerializer;
        
        try {
            anyGetterWriter.resolve(provider);
            
            assertSame(mapSerializer, provider.getHandlePrimaryContextualizationArg1());
            assertSame(property, provider.getHandlePrimaryContextualizationArg2());
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }
}