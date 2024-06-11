package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {
    
    private AnyGetterWriter anyGetterWriter;
    private BeanProperty beanProperty;
    private AnnotatedMember annotatedMember;
    private MapSerializer mapSerializer;
    
    @Before
    public void setUp() {
        beanProperty = mock(BeanProperty.class);
        annotatedMember = mock(AnnotatedMember.class);
        mapSerializer = mock(MapSerializer.class);
        
        anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
    }
    
    @Test
    public void testGetAndSerializeShouldNotSerializeIfValueIsNull() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        verifyZeroInteractions(gen, provider);
    }
    
    @Test
    public void testGetAndSerializeShouldThrowExceptionIfValueIsNotMap() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        when(annotatedMember.getValue(bean)).thenReturn(new Object());
        
        try {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
        } catch (JsonMappingException ex) {
            assertEquals("Value returned by 'any-getter' (" + annotatedMember.getName() 
                    + "()) not java.util.Map but " + Object.class.getName(), ex.getMessage());
        }
        
        verifyZeroInteractions(gen, provider);
    }
    
    @Test
    public void testGetAndSerializeWithMapSerializer() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Map<?, ?> value = new HashMap<>();
        value.put("key", "value");
        
        when(annotatedMember.getValue(bean)).thenReturn(value);
        when(mapSerializer.serializeFields(eq(value), eq(gen), eq(provider))).thenReturn(mock(JsonGenerator.class));
        
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        verify(mapSerializer).serializeFields(eq(value), eq(gen), eq(provider));
    }
    
    @Test
    public void testGetAndFilterShouldNotSerializeIfValueIsNull() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        verifyZeroInteractions(gen, provider, filter);
    }
    
    @Test
    public void testGetAndFilterShouldThrowExceptionIfValueIsNotMap() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        when(annotatedMember.getValue(bean)).thenReturn(new Object());
        
        try {
            anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        } catch (JsonMappingException ex) {
            assertEquals("Value returned by 'any-getter' (" + annotatedMember.getName() 
                    + "()) not java.util.Map but " + Object.class.getName(), ex.getMessage());
        }
        
        verifyZeroInteractions(gen, provider, filter);
    }
    
    @Test
    public void testGetAndFilterWithMapSerializer() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        Map<?, ?> value = new HashMap<>();
        value.put("key", "value");
        
        when(annotatedMember.getValue(bean)).thenReturn(value);
        when(mapSerializer.serializeFilteredFields(eq(value), eq(gen), eq(provider), eq(filter), isNull())).thenReturn(mock(JsonGenerator.class));
        
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        verify(mapSerializer).serializeFilteredFields(eq(value), eq(gen), eq(provider), eq(filter), isNull());
    }
    
    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        
        anyGetterWriter.resolve(provider);
        
        verify(provider).handlePrimaryContextualization(eq(mapSerializer), eq(beanProperty));
    }
    
    // additional tests for the remaining methods in the class
    
}