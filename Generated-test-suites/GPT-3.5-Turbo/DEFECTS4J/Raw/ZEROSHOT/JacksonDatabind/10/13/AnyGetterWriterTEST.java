package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {
    
    @Test
    public void testGetAndSerialize() throws Exception {
        // Arrange
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer serializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        // Assert
        Mockito.verify(accessor).getValue(bean);
        Mockito.verifyNoMoreInteractions(gen, provider);
    }
    
    @Test
    public void testGetAndSerialize_withNullValue() throws Exception {
        // Arrange
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer serializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        // Assert
        Mockito.verify(accessor).getValue(bean);
        Mockito.verifyNoMoreInteractions(gen, provider);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_withNonMapValue() throws Exception {
        // Arrange
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, null);
        Object bean = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Mockito.when(accessor.getValue(bean)).thenReturn(new Object());
        
        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        // Assert
        // exception is thrown
    }
    
    @Test
    public void testGetAndSerialize_withMapSerializer() throws Exception {
        // Arrange
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer serializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Map<Object, Object> map = new HashMap<Object, Object>();
        Mockito.when(accessor.getValue(bean)).thenReturn(map);
        
        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        // Assert
        Mockito.verify(accessor).getValue(bean);
        Mockito.verify(serializer).serializeFields(map, gen, provider);
        Mockito.verifyNoMoreInteractions(gen, provider);
    }
    
    @Test
    public void testGetAndFilter() throws Exception {
        // Arrange
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer serializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);
        
        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        // Assert
        Mockito.verify(accessor).getValue(bean);
        Mockito.verifyNoMoreInteractions(gen, provider, filter);
    }
    
    @Test
    public void testGetAndFilter_withNullValue() throws Exception {
        // Arrange
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer serializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);
        
        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        // Assert
        Mockito.verify(accessor).getValue(bean);
        Mockito.verifyNoMoreInteractions(gen, provider, filter);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_withNonMapValue() throws Exception {
        // Arrange
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, null);
        Object bean = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);
        Mockito.when(accessor.getValue(bean)).thenReturn(new Object());
        
        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        // Assert
        // exception is thrown
    }
    
    @Test
    public void testGetAndFilter_withMapSerializer() throws Exception {
        // Arrange
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer serializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);
        Map<Object, Object> map = new HashMap<Object, Object>();
        Mockito.when(accessor.getValue(bean)).thenReturn(map);
        
        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        // Assert
        Mockito.verify(accessor).getValue(bean);
        Mockito.verify(serializer).serializeFilteredFields(map, gen, provider, filter, null);
        Mockito.verifyNoMoreInteractions(gen, provider, filter);
    }
    
    @Test
    public void testResolve() throws Exception {
        // Arrange
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer serializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        // Act
        anyGetterWriter.resolve(provider);
        
        // Assert
        Mockito.verify(provider).handlePrimaryContextualization(serializer, property);
        Mockito.verifyNoMoreInteractions(provider);
    }
}