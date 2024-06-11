package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {
    
    @Mock
    private BeanProperty mockProperty;
    
    @Mock
    private AnnotatedMember mockAccessor;
    
    @Mock
    private MapSerializer mockSerializer;
    
    private AnyGetterWriter writer;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        writer = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
    }

    @Test
    public void testGetAndSerialize_NullValue() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        writer.getAndSerialize(bean, gen, provider);
        
        verify(mockAccessor).getValue(bean);
        verifyZeroInteractions(gen, provider, mockSerializer);
    }
    
    @Test
    public void testGetAndSerialize_NonNullValue_InvalidType() throws Exception {
        Object bean = new Object();
        Map<String, Integer> value = new HashMap<>();
        when(mockAccessor.getValue(bean)).thenReturn(value);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        try {
            writer.getAndSerialize(bean, gen, provider);
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + mockAccessor.getName() + "()) not java.util.Map but " + value.getClass().getName(), e.getMessage());
        }
        
        verify(mockAccessor).getValue(bean);
        verifyZeroInteractions(gen, provider, mockSerializer);
    }
    
    @Test
    public void testGetAndSerialize_NonNullValue_ValidType() throws Exception {
        Object bean = new Object();
        Map<String, Object> value = new HashMap<>();
        when(mockAccessor.getValue(bean)).thenReturn(value);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        writer.getAndSerialize(bean, gen, provider);
        
        verify(mockAccessor).getValue(bean);
        verify(mockSerializer).serializeFields(value, gen, provider);
    }
    
    @Test
    public void testGetAndFilter_NullValue() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        writer.getAndFilter(bean, gen, provider, filter);
        
        verify(mockAccessor).getValue(bean);
        verifyZeroInteractions(gen, provider, mockSerializer, filter);
    }
    
    @Test
    public void testGetAndFilter_NonNullValue_InvalidType() throws Exception {
        Object bean = new Object();
        Map<String, Integer> value = new HashMap<>();
        when(mockAccessor.getValue(bean)).thenReturn(value);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        try {
            writer.getAndFilter(bean, gen, provider, filter);
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + mockAccessor.getName() + "()) not java.util.Map but " + value.getClass().getName(), e.getMessage());
        }
        
        verify(mockAccessor).getValue(bean);
        verifyZeroInteractions(gen, provider, mockSerializer);
    }
    
    @Test
    public void testGetAndFilter_NonNullValue_ValidType() throws Exception {
        Object bean = new Object();
        Map<String, Object> value = new HashMap<>();
        when(mockAccessor.getValue(bean)).thenReturn(value);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        writer.getAndFilter(bean, gen, provider, filter);
        
        verify(mockAccessor).getValue(bean);
        verify(mockSerializer).serializeFilteredFields(value, gen, provider, filter, null);
    }
    
    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.handlePrimaryContextualization(mockSerializer, mockProperty)).thenReturn(mockSerializer);
        
        writer.resolve(provider);
        
        verify(provider).handlePrimaryContextualization(mockSerializer, mockProperty);
        assertEquals(mockSerializer, writer._mapSerializer);
    }
}