package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class StdKeySerializerTest {

    private StdKeySerializer keySerializer;
    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockSerializerProvider;
    private JavaType mockJavaType;
    private JsonFormatVisitorWrapper mockVisitor;
    private Type mockType;
    private Date mockDate;
    private JsonNode mockJsonNode;

    @Before
    public void setUp() {
        keySerializer = new StdKeySerializer();
        mockJsonGenerator = mock(JsonGenerator.class);
        mockSerializerProvider = mock(SerializerProvider.class);
        mockJavaType = mock(JavaType.class);
        mockVisitor = mock(JsonFormatVisitorWrapper.class);
        mockType = mock(Type.class);
        mockDate = mock(Date.class);
        mockJsonNode = mock(JsonNode.class);
    }

    @Test
    public void testSerializeWhenValueIsDate() throws IOException {
        // Arrange
        when(mockJavaType.getRawClass()).thenReturn(Date.class);

        // Act
        keySerializer.serialize(mockDate, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockSerializerProvider).defaultSerializeDateKey(mockDate, mockJsonGenerator);
    }

    @Test
    public void testSerializeWhenValueIsNotDate() throws IOException {
        // Arrange
        String valueString = "value";
        when(mockJavaType.getRawClass()).thenReturn(String.class);
        when(mockDate.toString()).thenReturn(valueString);

        // Act
        keySerializer.serialize(mockDate, mockJsonGenerator, mockSerializerProvider);

        // Assert
        verify(mockJsonGenerator).writeFieldName(valueString);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        // Arrange
        when(mockSerializerProvider.createSchemaNode("string")).thenReturn(mockJsonNode);

        // Act
        JsonNode schemaNode = keySerializer.getSchema(mockSerializerProvider, mockType);

        // Assert
        assertSame(mockJsonNode, schemaNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Act
        keySerializer.acceptJsonFormatVisitor(mockVisitor, mockJavaType);

        // Assert
        verify(mockVisitor).expectStringFormat(mockJavaType);
    }
}