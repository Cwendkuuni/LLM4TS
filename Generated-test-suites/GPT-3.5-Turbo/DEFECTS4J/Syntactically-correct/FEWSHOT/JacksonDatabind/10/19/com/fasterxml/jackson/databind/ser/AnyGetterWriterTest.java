package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {
    
    private AnyGetterWriter anyGetterWriter;
    private BeanProperty mockedBeanProperty;
    private AnnotatedMember mockedAnnotatedMember;
    private MapSerializer mockedMapSerializer;
    private JsonGenerator mockedJsonGenerator;
    private SerializerProvider mockedSerializerProvider;
    private PropertyFilter mockedPropertyFilter;
    
    @Before
    public void setup() {
        mockedBeanProperty = mock(BeanProperty.class);
        mockedAnnotatedMember = mock(AnnotatedMember.class);
        mockedMapSerializer = mock(MapSerializer.class);
        mockedJsonGenerator = mock(JsonGenerator.class);
        mockedSerializerProvider = mock(SerializerProvider.class);
        mockedPropertyFilter = mock(PropertyFilter.class);
        
        anyGetterWriter = new AnyGetterWriter(mockedBeanProperty, mockedAnnotatedMember, mockedMapSerializer);
    }
    
    @Test
    public void testGetAndSerialize_WithNullValue() throws Exception {
        Object bean = new Object();
        
        when(mockedAnnotatedMember.getValue(bean)).thenReturn(null);
        
        anyGetterWriter.getAndSerialize(bean, mockedJsonGenerator, mockedSerializerProvider);
        
        verify(mockedAnnotatedMember).getValue(bean);
        verifyZeroInteractions(mockedJsonGenerator, mockedSerializerProvider, mockedMapSerializer);
    }
    
    @Test
    public void testGetAndSerialize_WithNonMapValue() throws Exception {
        Object bean = new Object();
        Object value = "test";
        
        when(mockedAnnotatedMember.getValue(bean)).thenReturn(value);
        
        try {
            anyGetterWriter.getAndSerialize(bean, mockedJsonGenerator, mockedSerializerProvider);
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + mockedAnnotatedMember.getName() 
                    + "()) not java.util.Map but " + value.getClass().getName(), e.getMessage());
        }
        
        verify(mockedAnnotatedMember).getValue(bean);
        verifyZeroInteractions(mockedJsonGenerator, mockedSerializerProvider, mockedMapSerializer);
    }
    
    @Test
    public void testGetAndSerialize_WithMapValue_NoMapSerializer() throws Exception {
        Object bean = new Object();
        Map<Object, Object> value = new HashMap<>();
        
        when(mockedAnnotatedMember.getValue(bean)).thenReturn(value);
        when(mockedMapSerializer != null).thenReturn(false);
        
        anyGetterWriter.getAndSerialize(bean, mockedJsonGenerator, mockedSerializerProvider);
        
        verify(mockedAnnotatedMember).getValue(bean);
        verify(mockedMapSerializer, never()).serializeFields(value, mockedJsonGenerator, mockedSerializerProvider);
    }
    
    @Test
    public void testGetAndSerialize_WithMapValue_MapSerializer() throws Exception {
        Object bean = new Object();
        Map<Object, Object> value = new HashMap<>();
        
        when(mockedAnnotatedMember.getValue(bean)).thenReturn(value);
        when(mockedMapSerializer != null).thenReturn(true);
        
        anyGetterWriter.getAndSerialize(bean, mockedJsonGenerator, mockedSerializerProvider);
        
        verify(mockedAnnotatedMember).getValue(bean);
        verify(mockedMapSerializer).serializeFields(value, mockedJsonGenerator, mockedSerializerProvider);
    }
    
    @Test
    public void testGetAndFilter_WithNullValue() throws Exception {
        Object bean = new Object();
        
        when(mockedAnnotatedMember.getValue(bean)).thenReturn(null);
        
        anyGetterWriter.getAndFilter(bean, mockedJsonGenerator, mockedSerializerProvider, mockedPropertyFilter);
        
        verify(mockedAnnotatedMember).getValue(bean);
        verifyZeroInteractions(mockedJsonGenerator, mockedSerializerProvider, mockedMapSerializer, mockedPropertyFilter);
    }
    
    @Test
    public void testGetAndFilter_WithNonMapValue() throws Exception {
        Object bean = new Object();
        Object value = "test";
        
        when(mockedAnnotatedMember.getValue(bean)).thenReturn(value);
        
        try {
            anyGetterWriter.getAndFilter(bean, mockedJsonGenerator, mockedSerializerProvider, mockedPropertyFilter);
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + mockedAnnotatedMember.getName() 
                    + "()) not java.util.Map but " + value.getClass().getName(), e.getMessage());
        }
        
        verify(mockedAnnotatedMember).getValue(bean);
        verifyZeroInteractions(mockedJsonGenerator, mockedSerializerProvider, mockedMapSerializer, mockedPropertyFilter);
    }
    
    @Test
    public void testGetAndFilter_WithMapValue_NoMapSerializer() throws Exception {
        Object bean = new Object();
        Map<Object, Object> value = new HashMap<>();
        
        when(mockedAnnotatedMember.getValue(bean)).thenReturn(value);
        when(mockedMapSerializer != null).thenReturn(false);
        
        anyGetterWriter.getAndFilter(bean, mockedJsonGenerator, mockedSerializerProvider, mockedPropertyFilter);
        
        verify(mockedAnnotatedMember).getValue(bean);
        verify(mockedMapSerializer, never()).serializeFilteredFields(value, mockedJsonGenerator, mockedSerializerProvider, mockedPropertyFilter, null);
    }
    
    @Test
    public void testGetAndFilter_WithMapValue_MapSerializer() throws Exception {
        Object bean = new Object();
        Map<Object, Object> value = new HashMap<>();
        
        when(mockedAnnotatedMember.getValue(bean)).thenReturn(value);
        when(mockedMapSerializer != null).thenReturn(true);
        
        anyGetterWriter.getAndFilter(bean, mockedJsonGenerator, mockedSerializerProvider, mockedPropertyFilter);
        
        verify(mockedAnnotatedMember).getValue(bean);
        verify(mockedMapSerializer).serializeFilteredFields(value, mockedJsonGenerator, mockedSerializerProvider, mockedPropertyFilter, null);
    }
    
    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider mockedProvider = mock(SerializerProvider.class);
        
        anyGetterWriter.resolve(mockedProvider);
        
        verify(mockedProvider).handlePrimaryContextualization(mockedMapSerializer, mockedBeanProperty);
    }
}