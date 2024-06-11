package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

import static org.mockito.Mockito.*;

public class StdKeySerializerTest {

    @Test
    public void serialize_withDateValue_shouldSerializeDateKey() throws IOException {
        // Arrange
        Object value = new Date();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        StdKeySerializer serializer = new StdKeySerializer();

        // Act
        serializer.serialize(value, jgen, provider);

        // Assert
        verify(provider, times(1)).defaultSerializeDateKey((Date) value, jgen);
    }

    @Test
    public void serialize_withNonNullValue_shouldSerializeStringValue() throws IOException {
        // Arrange
        Object value = "test";
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        StdKeySerializer serializer = new StdKeySerializer();

        // Act
        serializer.serialize(value, jgen, provider);

        // Assert
        verify(jgen, times(1)).writeFieldName(value.toString());
    }

    @Test
    public void getSchema_withAnyType_shouldReturnStringSchemaNode() throws JsonProcessingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        StdKeySerializer serializer = new StdKeySerializer();

        // Act
        JsonNode schema = serializer.getSchema(provider, typeHint);

        // Assert
        assertThat(schema.asText()).isEqualTo("string");
    }

    @Test
    public void acceptJsonFormatVisitor_withAnyJavaType_shouldExpectStringFormat() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        StdKeySerializer serializer = new StdKeySerializer();

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor, times(1)).expectStringFormat(typeHint);
    }
}