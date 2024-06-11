
package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    @Mock
    private BeanProperty mockProperty;

    @Mock
    private AnnotatedMember mockAccessor;

    @Mock
    private MapSerializer mockMapSerializer;

    @Test
    public void testGetAndSerialize_WhenValueIsNull_ShouldNotThrowException() throws Exception {
        // Arrange
        AnyGetterWriter writer = new AnyGetterWriter(mockProperty, mockAccessor, mockMapSerializer);
        Object mockBean = Mockito.mock(Object.class);
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);

        // Act & Assert
        writer.getAndSerialize(mockBean, mockGenerator, mockProvider);
        // No exception should be thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_WhenValueIsNotMap_ShouldThrowJsonMappingException() throws Exception {
        // Arrange
        AnyGetterWriter writer = new AnyGetterWriter(mockProperty, mockAccessor, mockMapSerializer);
        Object mockBean = Mockito.mock(Object.class);
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Mockito.when(mockAccessor.getValue(mockBean)).thenReturn("not_a_map");

        // Act & Assert
        writer.getAndSerialize(mockBean, mockGenerator, mockProvider);
    }

    @Test
    public void testGetAndSerialize_WhenMapSerializerIsNull_ShouldNotThrowException() throws Exception {
        // Arrange
        AnyGetterWriter writer = new AnyGetterWriter(mockProperty, mockAccessor, null);
        Object mockBean = Mockito.mock(Object.class);
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Map<Object, Object> mockMap = new HashMap<>();
        Mockito.when(mockAccessor.getValue(mockBean)).thenReturn(mockMap);

        // Act
        writer.getAndSerialize(mockBean, mockGenerator, mockProvider);

        // Assert
        Mockito.verify(mockGenerator, Mockito.times(0)).writeObject(Mockito.any());
    }

    @Test
    public void testGetAndSerialize_WhenMapSerializerIsNotNull_ShouldSerializeFields() throws Exception {
        // Arrange
        AnyGetterWriter writer = new AnyGetterWriter(mockProperty, mockAccessor, mockMapSerializer);
        Object mockBean = Mockito.mock(Object.class);
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Map<Object, Object> mockMap = new HashMap<>();
        Mockito.when(mockAccessor.getValue(mockBean)).thenReturn(mockMap);

        // Act
        writer.getAndSerialize(mockBean, mockGenerator, mockProvider);

        // Assert
        Mockito.verify(mockMapSerializer, Mockito.times(1)).serializeFields(mockMap, mockGenerator, mockProvider);
    }

    @Test
    public void testGetAndFilter_WhenValueIsNull_ShouldNotThrowException() throws Exception {
        // Arrange
        AnyGetterWriter writer = new AnyGetterWriter(mockProperty, mockAccessor, mockMapSerializer);
        Object mockBean = Mockito.mock(Object.class);
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        PropertyFilter mockFilter = Mockito.mock(PropertyFilter.class);

        // Act & Assert
        writer.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);
        // No exception should be thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_WhenValueIsNotMap_ShouldThrowJsonMappingException() throws Exception {
        // Arrange
        AnyGetterWriter writer = new AnyGetterWriter(mockProperty, mockAccessor, mockMapSerializer);
        Object mockBean = Mockito.mock(Object.class);
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        PropertyFilter mockFilter = Mockito.mock(PropertyFilter.class);
        Mockito.when(mockAccessor.getValue(mockBean)).thenReturn("not_a_map");

        // Act & Assert
        writer.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);
    }

    @Test
    public void testGetAndFilter_WhenMapSerializerIsNull_ShouldNotThrowException() throws Exception {
        // Arrange
        AnyGetterWriter writer = new AnyGetterWriter(mockProperty, mockAccessor, null);
        Object mockBean = Mockito.mock(Object.class);
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        PropertyFilter mockFilter = Mockito.mock(PropertyFilter.class);
        Map<Object, Object> mockMap = new HashMap<>();
        Mockito.when(mockAccessor.getValue(mockBean)).thenReturn(mockMap);

        // Act
        writer.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);

        // Assert
        Mockito.verify(mockGenerator, Mockito.times(0)).writeObject(Mockito.any());
    }

    @Test
    public void testGetAndFilter_WhenMapSerializerIsNotNull_ShouldSerializeFilteredFields() throws Exception {
        // Arrange
        AnyGetterWriter writer = new AnyGetterWriter(mockProperty, mockAccessor, mockMapSerializer);
        Object mockBean = Mockito.mock(Object.class);
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        PropertyFilter mockFilter = Mockito.mock(PropertyFilter.class);
        Map<Object, Object> mockMap = new HashMap<>();
        Mockito.when(mockAccessor.getValue(mockBean)).thenReturn(mockMap);

        // Act
        writer.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);

        // Assert
        Mockito.verify(mockMapSerializer, Mockito.times(1)).serializeFilteredFields(mockMap, mockGenerator, mockProvider, mockFilter, null);
    }

    @Test
    public void testResolve_ShouldPrimaryContextualizationToMapSerializer() throws JsonMappingException {
        // Arrange
        AnyGetterWriter writer = new AnyGetterWriter(mockProperty, mockAccessor, mockMapSerializer);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        Mockito.when(mockProvider.handlePrimaryContextualization(mockMapSerializer, mockProperty)).thenReturn(mockMapSerializer);

        // Act
        writer.resolve(mockProvider);

        // Assert
        Assert.assertEquals(mockMapSerializer, writer._mapSerializer);
    }
}
