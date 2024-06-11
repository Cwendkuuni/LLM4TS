package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializerTest {

    private StdKeySerializer keySerializer;

    @Before
    public void setUp() {
        keySerializer = new StdKeySerializer();
    }

    @Test
    public void testSerialize_DateValue_SerializeDateKeyInvoked() throws IOException {
        // Arrange
        Date date = new Date();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        keySerializer.serialize(date, jgen, provider);

        // Assert
        verify(provider).defaultSerializeDateKey(date, jgen);
    }

    @Test
    public void testSerialize_NonDateValue_WriteFieldNameInvokedWithStringValue() throws IOException {
        // Arrange
        Object value = "test";
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        keySerializer.serialize(value, jgen, provider);

        // Assert
        verify(jgen).writeFieldName(value.toString());
    }

    @Test
    public void testGetSchema_ReturnsStringSchemaNode() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        // Act
        JsonNode schema = keySerializer.getSchema(provider, typeHint);

        // Assert
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_ExpectStringFormatInvoked() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        // Act
        keySerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor).expectStringFormat(typeHint);
    }
}