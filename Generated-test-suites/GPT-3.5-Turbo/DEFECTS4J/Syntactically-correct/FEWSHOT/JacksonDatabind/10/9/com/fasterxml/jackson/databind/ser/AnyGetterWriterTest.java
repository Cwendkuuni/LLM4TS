
package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

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
    public void testGetAndSerialize() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        verify(annotatedMember).getValue(bean);
        verifyZeroInteractions(gen, provider);
    }

    @Test
    public void testGetAndSerialize_WithNullValue() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        when(annotatedMember.getValue(bean)).thenReturn(null);
        
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        verify(annotatedMember).getValue(bean);
        verifyZeroInteractions(gen, provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_WithNonMapValue() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        when(annotatedMember.getValue(bean)).thenReturn("non-map-value");
        
        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndSerialize_WithMapSerializer() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        when(annotatedMember.getValue(bean)).thenReturn(new HashMap<String, String>());
        when(mapSerializer.serializeFields(any(Map.class), any(JsonGenerator.class), any(SerializerProvider.class))).thenReturn(true);
        
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        verify(annotatedMember).getValue(bean);
        verify(mapSerializer).serializeFields(any(Map.class), any(JsonGenerator.class), any(SerializerProvider.class));
        verifyZeroInteractions(gen, provider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        verify(annotatedMember).getValue(bean);
        verifyZeroInteractions(gen, provider);
    }

    @Test
    public void testGetAndFilter_WithNullValue() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        when(annotatedMember.getValue(bean)).thenReturn(null);
        
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        verify(annotatedMember).getValue(bean);
        verifyZeroInteractions(gen, provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_WithNonMapValue() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        when(annotatedMember.getValue(bean)).thenReturn("non-map-value");
        
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
    }

    @Test
    public void testGetAndFilter_WithMapSerializer() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        when(annotatedMember.getValue(bean)).thenReturn(new HashMap<String, String>());
        when(mapSerializer.serializeFilteredFields(any(Map.class), any(JsonGenerator.class), any(SerializerProvider.class), eq(filter), isNull())).thenReturn(true);
                
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        verify(annotatedMember).getValue(bean);
        verify(mapSerializer).serializeFilteredFields(any(Map.class), any(JsonGenerator.class), any(SerializerProvider.class), eq(filter), isNull());
        verifyZeroInteractions(gen, provider);
    }
   
    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        
        when(provider.handlePrimaryContextualization(eq(mapSerializer), eq(beanProperty))).thenReturn(mapSerializer);
        
        anyGetterWriter.resolve(provider);
        
        verify(provider).handlePrimaryContextualization(eq(mapSerializer), eq(beanProperty));
    }

}
