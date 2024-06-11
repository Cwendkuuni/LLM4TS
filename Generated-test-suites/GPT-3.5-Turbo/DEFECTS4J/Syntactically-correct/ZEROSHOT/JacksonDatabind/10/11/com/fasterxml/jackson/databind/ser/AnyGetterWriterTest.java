package com.fasterxml.jackson.databind.ser;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import static org.mockito.Mockito.*;

public class AnyGetterWriterTest {

    private BeanProperty property;
    private AnnotatedMember accessor;
    private MapSerializer serializer;
    private AnyGetterWriter anyGetterWriter;
    
    @Before
    public void setUp() {
        property = mock(BeanProperty.class);
        accessor = mock(AnnotatedMember.class);
        serializer = mock(MapSerializer.class);
        anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
    }
    
    @Test
    public void testGetAndSerialize_valueIsNull_shouldDoNothing() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        when(accessor.getValue(bean)).thenReturn(null);
        
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        verifyZeroInteractions(serializer);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_valueIsNotMap_shouldThrowException() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        when(accessor.getValue(bean)).thenReturn("not a map");
        
        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }
    
    @Test
    public void testGetAndSerialize_mapSerializerNotNull_shouldCallSerializeFields() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Map<?, ?> value = mock(Map.class);
        
        when(accessor.getValue(bean)).thenReturn(value);
        when(serializer.serializeFields(value, gen, provider)).thenReturn(null);
        
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        verify(serializer).serializeFields(value, gen, provider);
    }
    
    @Test
    public void testGetAndSerialize_mapSerializerNull_shouldDoNothing() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Map<?, ?> value = mock(Map.class);
        
        when(accessor.getValue(bean)).thenReturn(value);
        
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        verifyZeroInteractions(serializer);
    }
    
    @Test
    public void testGetAndFilter_valueIsNull_shouldDoNothing() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        when(accessor.getValue(bean)).thenReturn(null);
        
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        verifyZeroInteractions(serializer);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_valueIsNotMap_shouldThrowException() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        
        when(accessor.getValue(bean)).thenReturn("not a map");
        
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
    }
    
    @Test
    public void testGetAndFilter_mapSerializerNotNull_shouldCallSerializeFilteredFields() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        Map<?, ?> value = mock(Map.class);
        
        when(accessor.getValue(bean)).thenReturn(value);
        when(serializer.serializeFilteredFields(value, gen, provider, filter, null)).thenReturn(null);
        
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        verify(serializer).serializeFilteredFields(value, gen, provider, filter, null);
    }
    
    @Test
    public void testGetAndFilter_mapSerializerNull_shouldDoNothing() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        Map<?, ?> value = mock(Map.class);
        
        when(accessor.getValue(bean)).thenReturn(value);
        
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        verifyZeroInteractions(serializer);
    }
    
    @Test
    public void testResolve_shouldContextualizeMapSerializer() throws Exception {
        SerializerProvider provider = mock(SerializerProvider.class);
        when(provider.handlePrimaryContextualization(any(MapSerializer.class), eq(property))).thenReturn(serializer);
        
        anyGetterWriter.resolve(provider);
        
        verify(provider).handlePrimaryContextualization(serializer, property);
    }
}