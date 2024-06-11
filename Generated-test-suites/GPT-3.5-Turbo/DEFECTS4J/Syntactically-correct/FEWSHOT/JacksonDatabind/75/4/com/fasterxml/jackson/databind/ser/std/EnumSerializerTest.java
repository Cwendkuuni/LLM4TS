package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class EnumSerializerTest {

    @Mock
    SerializerProvider mockSerializersProvider;
    @Mock
    JsonFormatVisitorWrapper mockVisitorWrapper;
    @Mock
    ContextualSerializer mockContextualSerializer;
    @Mock
    BeanDescription mockBeanDescription;
    @Mock
    JsonFactory mockJsonFactory;
    @Mock
    JsonGenerator mockJsonGenerator;

    private EnumSerializer enumSerializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        enumSerializer = new EnumSerializer(EnumValues.constructFromName(mockJsonFactory, Enum.class), true);
    }

    @Test
    public void testSerializeUsingIndex() throws IOException {
        // Arrange
        when(mockSerializersProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        Enum<?> en = Enum.VALUE1;
        // Act
        enumSerializer.serialize(en, mockJsonGenerator, mockSerializersProvider);

        // Assert
        verify(mockJsonGenerator, times(1)).writeNumber(en.ordinal());
    }

    @Test
    public void testSerializeUsingToString() throws IOException {
        // Arrange
        when(mockSerializersProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        Enum<?> en = Enum.VALUE1;
        // Act
        enumSerializer.serialize(en, mockJsonGenerator, mockSerializersProvider);

        // Assert
        verify(mockJsonGenerator, times(1)).writeString(en.toString());
    }

    @Test
    public void testSerializeUsingSerializedValue() throws IOException {
        // Arrange
        Enum<?> en = Enum.VALUE1;
        // Act
        enumSerializer.serialize(en, mockJsonGenerator, mockSerializersProvider);

        // Assert
        verify(mockJsonGenerator, times(1)).writeString(en.name());
    }

    @Test
    public void testGetSchemaWhenSerializeAsIndex() {
        // Arrange
        when(mockSerializersProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        when(mockSerializersProvider.constructType(Enum.class)).thenReturn(TypeFactory.defaultInstance().constructType(Enum.class));
        // Act
        enumSerializer.getSchema(mockSerializersProvider, null);

        // Assert
        verify(mockJsonGenerator, times(1)).createSchemaNode("integer", true);
    }

    @Test
    public void testGetSchemaWhenNotSerializeAsIndex() {
        // Arrange
        when(mockSerializersProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);
        when(mockSerializersProvider.constructType(Enum.class)).thenReturn(TypeFactory.defaultInstance().constructType(Enum.class));
        // Act
        enumSerializer.getSchema(mockSerializersProvider, null);

        // Assert
        verify(mockJsonGenerator, times(1)).createSchemaNode("string", true);
        verify(mockJsonGenerator, times(1)).putArray("enum");
        verify(mockJsonGenerator, times(2)).writeValue(eq(null));
    }

    @Test
    public void testAcceptJsonFormatVisitorWhenSerializeAsIndex() throws JsonMappingException {
        // Arrange
        when(mockSerializersProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        // Act
        enumSerializer.acceptJsonFormatVisitor(mockVisitorWrapper, null);

        // Assert
        verify(mockVisitorWrapper, times(1)).expectIntFormat(any());
    }

    @Test
    public void testAcceptJsonFormatVisitorWhenSerializeUsingToString() throws JsonMappingException {
        // Arrange
        when(mockSerializersProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        // Act
        enumSerializer.acceptJsonFormatVisitor(mockVisitorWrapper, null);

        // Assert
        verify(mockVisitorWrapper, times(1)).expectStringFormat(any());
        verify(mockVisitorWrapper, times(1)).enumTypes(anySet());
    }

    @Test
    public void testAcceptJsonFormatVisitorWhenSerializeUsingSerializedValue() throws IOException {
        // Arrange
        Enum<?> en = Enum.VALUE1;
        // Act
        enumSerializer.acceptJsonFormatVisitor(mockVisitorWrapper, null);

        // Assert
        verify(mockVisitorWrapper, times(1)).expectStringFormat(any());
        verify(mockVisitorWrapper, times(1)).enumTypes(anySet());
    }
}