package com.fasterxml.jackson.databind.ser;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize_WhenValueIsNull_ShouldReturn() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Assert
        verify(accessor).getValue(bean);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_WhenValueIsNotMap_ShouldThrowJsonMappingException() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(accessor.getValue(bean)).thenReturn("value");

        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndSerialize_WhenMapSerializerIsNotNull_ShouldSerializeFields() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Map<?, ?> value = mock(Map.class);

        when(accessor.getValue(bean)).thenReturn(value);
        when(value instanceof Map<?, ?>).thenReturn(true);

        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Assert
        verify(serializer).serializeFields(value, gen, provider);
    }

    @Test
    public void testGetAndSerialize_WhenMapSerializerIsNull_ShouldNotDoAnything() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = null;
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
 
        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndFilter_WhenValueIsNull_ShouldReturn() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // Assert
        verify(accessor).getValue(bean);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_WhenValueIsNotMap_ShouldThrowJsonMappingException() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        when(accessor.getValue(bean)).thenReturn("value");

        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
    }

    @Test
    public void testGetAndFilter_WhenMapSerializerIsNotNull_ShouldSerializeFilteredFields() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);
        Map<?, ?> value = mock(Map.class);

        when(accessor.getValue(bean)).thenReturn(value);
        when(value instanceof Map<?, ?>).thenReturn(true);

        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // Assert
        verify(serializer).serializeFilteredFields(value, gen, provider, filter, null);
    }

    @Test
    public void testGetAndFilter_WhenMapSerializerIsNull_ShouldNotDoAnything() throws Exception {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = null;
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
    }

    @Test
    public void testResolve_ShouldHandlePrimaryContextualization() throws JsonMappingException {
        // Arrange
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        MapSerializer serializer = mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        anyGetterWriter.resolve(provider);

        // Assert
        verify(provider).handlePrimaryContextualization(serializer, property);
    }
}