
package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDateKey() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        JsonFactory factory = Mockito.mock(JsonFactory.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Mockito.when(provider.getDefaultNullKeySerializer()).thenReturn(serializer);

        // Act
        serializer.serialize(date, generator, provider);

        // Assert
        Mockito.verify(provider, Mockito.times(1)).defaultSerializeDateKey(Mockito.any(Date.class), Mockito.eq(generator));
        Mockito.verify(generator, Mockito.never()).writeFieldName(Mockito.anyString());
    }

    @Test
    public void testSerializeNonNullValue() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        Object value = "test";
        JsonFactory factory = Mockito.mock(JsonFactory.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        Mockito.verify(provider, Mockito.never()).defaultSerializeDateKey(Mockito.any(Date.class), Mockito.eq(generator));
        Mockito.verify(generator, Mockito.times(1)).writeFieldName(Mockito.eq(value.toString()));
    }

    @Test
    public void testSerializeNullValue() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        Object value = null;
        JsonFactory factory = Mockito.mock(JsonFactory.class);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        Mockito.verify(provider, Mockito.never()).defaultSerializeDateKey(Mockito.any(Date.class), Mockito.eq(generator));
        Mockito.verify(generator, Mockito.times(1)).writeFieldName(Mockito.eq("null"));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Class<?> typeHint = Object.class;

        // Act
        JsonNode schemaNode = serializer.getSchema(provider, typeHint);

        // Assert
        Assert.assertEquals("string", schemaNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        Mockito.verify(visitor, Mockito.times(1)).expectStringFormat(typeHint);
    }
}
