package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;

    @Mock
    private Enum<?> enumInstance;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        EnumValues enumValues = mock(EnumValues.class);
        when(enumValues.getEnumClass()).thenReturn(Enum.class);
        enumSerializer = new EnumSerializer(enumValues);
    }

    @Test
    public void testSerialize_AsIndex() throws IOException {
        // Arrange
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        // Act
        enumSerializer.serialize(enumInstance, jsonGenerator, serializerProvider);
        // Assert
        verify(jsonGenerator, times(1)).writeNumber(anyInt());
    }

    @Test
    public void testSerialize_UsingToString() throws IOException {
        // Arrange
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        // Act
        enumSerializer.serialize(enumInstance, jsonGenerator, serializerProvider);
        // Assert
        verify(jsonGenerator, times(1)).writeString(anyString());
    }

    @Test
    public void testSerialize_Default() throws IOException {
        // Arrange
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        // Act
        enumSerializer.serialize(enumInstance, jsonGenerator, serializerProvider);
        // Assert
        verify(jsonGenerator, times(1)).writeString(anyString());
    }

    @Test
    public void testGetSchema_AsIndex() {
        // Arrange
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        // Act
        enumSerializer.getSchema(serializerProvider, null);
        // Assert
        verify(serializerProvider, times(1)).constructType(null);
        verify(serializerProvider, times(1)).constructType(Enum.class);
    }

    @Test
    public void testGetSchema_AsString() {
        // Arrange
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);
        // Act
        enumSerializer.getSchema(serializerProvider, null);
        // Assert
        verify(serializerProvider, times(1)).constructType(null);
        verify(serializerProvider, times(1)).constructType(Enum.class);
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsIndex() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        // Act
        enumSerializer.acceptJsonFormatVisitor(visitorWrapper, null);
        // Assert
        verify(visitorWrapper, times(1)).getProvider();
        verify(serializerProvider, times(1)).isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        verify(serializerProvider, times(1)).isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        verify(visitorWrapper, times(1)).expectIntFormat(null);
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);
        // Act
        enumSerializer.acceptJsonFormatVisitor(visitorWrapper, null);
        // Assert
        verify(visitorWrapper, times(1)).getProvider();
        verify(serializerProvider, times(1)).isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        verify(serializerProvider, times(1)).isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        verify(visitorWrapper, times(1)).expectStringFormat(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsShapeWrittenUsingIndex_UnsupportedShape() {
        // Arrange
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        // Act and Assert
        enumSerializer._isShapeWrittenUsingIndex(Enum.class, format, false);
    }
}