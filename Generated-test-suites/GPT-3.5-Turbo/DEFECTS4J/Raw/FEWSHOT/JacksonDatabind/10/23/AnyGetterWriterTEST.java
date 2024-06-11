package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AnyGetterWriterTest {

    @Mock
    BeanProperty property;

    @Mock
    AnnotatedMember accessor;

    @Mock
    MapSerializer mapSerializer;

    @Mock
    JsonGenerator generator;

    @Mock
    SerializerProvider provider;
    
    AnyGetterWriter anyGetterWriter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerialize_NullValue_DoesNotSerialize() throws Exception {
        when(accessor.getValue(any())).thenReturn(null);
        
        anyGetterWriter.getAndSerialize(new Object(), generator, provider);
        
        verify(generator, never()).writeStartObject();
        verify(generator, never()).writeObjectField(anyString(),any());
    }

    @Test
    public void testGetAndSerialize_NonMapValue_ThrowsJsonMappingException() throws Exception {
        when(accessor.getValue(any())).thenReturn("non map value");
        
        try {
            anyGetterWriter.getAndSerialize(new Object(), generator, provider);
        } catch (JsonMappingException ex) {
            assertEquals("Value returned by 'any-getter' (" + accessor.getName() + "()) not java.util.Map but java.lang.String", 
                    ex.getMessage());
        }
    }

    @Test
    public void testGetAndSerialize_MapValue_InvokesMapSerializer() throws Exception {
        Map<String, Object> mapValue = new HashMap<>();
        mapValue.put("key1", "value1");
        mapValue.put("key2", "value2");
        
        when(accessor.getValue(any())).thenReturn(mapValue);
        when(mapSerializer.getContentType()).thenReturn(Object.class);
        
        anyGetterWriter.getAndSerialize(new Object(), generator, provider);
        
        verify(mapSerializer, times(1)).serializeFields(eq(mapValue), eq(generator), eq(provider));
    }

    @Test
    public void testGetAndFilter_NullValue_DoesNotSerialize() throws Exception {
        when(accessor.getValue(any())).thenReturn(null);
        
        anyGetterWriter.getAndFilter(new Object(), generator, provider, mock(PropertyFilter.class));
        
        verify(generator, never()).writeStartObject();
        verify(generator, never()).writeObjectField(anyString(),any());
    }

    @Test
    public void testGetAndFilter_NonMapValue_ThrowsJsonMappingException() throws Exception {
        when(accessor.getValue(any())).thenReturn("non map value");
        
        try {
            anyGetterWriter.getAndFilter(new Object(), generator, provider, mock(PropertyFilter.class));
        } catch (JsonMappingException ex) {
            assertEquals("Value returned by 'any-getter' (" + accessor.getName() + "()) not java.util.Map but java.lang.String", 
                    ex.getMessage());
        }
    }

    @Test
    public void testGetAndFilter_MapValue_InvokesMapSerializer() throws Exception {
        Map<String, Object> mapValue = new HashMap<>();
        mapValue.put("key1", "value1");
        mapValue.put("key2", "value2");
        
        when(accessor.getValue(any())).thenReturn(mapValue);
        when(mapSerializer.getContentType()).thenReturn(Object.class);
        
        anyGetterWriter.getAndFilter(new Object(), generator, provider, mock(PropertyFilter.class));
        
        verify(mapSerializer, times(1)).serializeFilteredFields(eq(mapValue), eq(generator), eq(provider), any(), isNull());
    }

    @Test
    public void testResolve() throws Exception {
        anyGetterWriter.resolve(provider);
        
        verify(provider, times(1)).handlePrimaryContextualization(eq(mapSerializer), eq(property));
    }
}