package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StdKeySerializerTest {

    @Mock
    private Object value;

    @Mock
    private JsonGenerator jgen;

    @Mock
    private SerializerProvider provider;

    @Test
    public void testSerialize_DateValue_CallsDefaultSerializeDateKey() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        when(value instanceof Date).thenReturn(true);

        // Act
        serializer.serialize(value, jgen, provider);

        // Assert
        verify(provider).defaultSerializeDateKey(date, jgen);
    }

    @Test
    public void testSerialize_NonDateValue_CallsWriteFieldName() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        String expected = "test";
        when(value instanceof Date).thenReturn(false);
        when(value.toString()).thenReturn(expected);

        // Act
        serializer.serialize(value, jgen, provider);

        // Assert
        verify(jgen).writeFieldName(expected);
    }

    @Test
    public void testGetSchema_ReturnsSchemaNode() throws JsonMappingException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        Type typeHint = Object.class;
        JsonNode expectedNode = Mockito.mock(JsonNode.class);
        when(provider.createSchemaNode("string")).thenReturn(expectedNode);

        // Act
        JsonNode result = serializer.getSchema(provider, typeHint);

        // Assert
        assertEquals(expectedNode, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor_CallsExpectStringFormat() throws JsonMappingException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        JavaType typeHint = Mockito.mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor).expectStringFormat(typeHint);
    }
}