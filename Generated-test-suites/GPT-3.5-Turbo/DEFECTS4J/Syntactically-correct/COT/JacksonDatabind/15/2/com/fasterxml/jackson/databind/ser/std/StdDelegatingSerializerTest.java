
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StdDelegatingSerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private StdDelegatingSerializer stdDelegatingSerializer;

    @Before
    public void setUp() throws Exception {
        stdDelegatingSerializer = new StdDelegatingSerializer(new Converter() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        });
    }

    @Test
    public void testSerialize_WithNonNullValue_ShouldCallDelegateSerializer() throws IOException {
        // Arrange
        Object value = new Object();
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        stdDelegatingSerializer._delegateSerializer = delegateSerializer;
        
        // Act
        stdDelegatingSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assert
        Mockito.verify(delegateSerializer).serialize(value, jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerialize_WithNullValue_ShouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        Object value = null;
        
        // Act
        stdDelegatingSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Assert
        Mockito.verify(serializerProvider).defaultSerializeNull(jsonGenerator);
    }

    @Test
    public void testSerializeWithType_ShouldCallDelegateSerializer() throws IOException {
        // Arrange
        Object value = new Object();
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        stdDelegatingSerializer._delegateSerializer = delegateSerializer;
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        
        // Act
        stdDelegatingSerializer.serializeWithType(value, jsonGenerator, serializerProvider, typeSerializer);
        
        // Assert
        Mockito.verify(delegateSerializer).serializeWithType(value, jsonGenerator, serializerProvider, typeSerializer);
    }

    @Test
    public void testIsEmpty_WithNonNullValue_ShouldCallIsEmptyOnDelegateSerializer() throws IOException {
        // Arrange
        Object value = new Object();
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        stdDelegatingSerializer._delegateSerializer = delegateSerializer;
        
        // Act
        stdDelegatingSerializer.isEmpty(serializerProvider, value);
        
        // Assert
        Mockito.verify(delegateSerializer).isEmpty(serializerProvider, value);
    }

    @Test
    public void testIsEmpty_WithNullValue_ShouldCallIsEmptyOnDelegateSerializer() throws IOException {
        // Arrange
        Object value = null;
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        stdDelegatingSerializer._delegateSerializer = delegateSerializer;
        
        // Act
        stdDelegatingSerializer.isEmpty(serializerProvider, value);
        
        // Assert
        Mockito.verify(delegateSerializer).isEmpty(serializerProvider, value);
    }

    @Test
    public void testGetSchema_WithSchemaAwareDelegateSerializer_ShouldCallGetSchemaOnDelegateSerializer() throws JsonMappingException {
        // Arrange
        Type typeHint = Mockito.mock(Type.class);
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        SchemaAware schemaAwareDelegateSerializer = (SchemaAware) delegateSerializer;
        stdDelegatingSerializer._delegateSerializer = delegateSerializer;
        
        // Act
        stdDelegatingSerializer.getSchema(serializerProvider, typeHint);
        
        // Assert
        Mockito.verify(schemaAwareDelegateSerializer).getSchema(serializerProvider, typeHint);
    }

    @Test
    public void testGetSchema_WithNonSchemaAwareDelegateSerializer_ShouldCallSuperGetSchema() throws JsonMappingException {
        // Arrange
        Type typeHint = Mockito.mock(Type.class);
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        stdDelegatingSerializer._delegateSerializer = delegateSerializer;
        
        // Act
        stdDelegatingSerializer.getSchema(serializerProvider, typeHint);
        
        // Assert
        Mockito.verify(stdDelegatingSerializer).getSchema(serializerProvider, typeHint);
    }

    @Test
    public void testGetSchema_WithSchemaAwareDelegateSerializer_ShouldCallGetSchemaWithOptionalOnDelegateSerializer() throws JsonMappingException {
        // Arrange
        Type typeHint = Mockito.mock(Type.class);
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        SchemaAware schemaAwareDelegateSerializer = (SchemaAware) delegateSerializer;
        stdDelegatingSerializer._delegateSerializer = delegateSerializer;
        
        // Act
        stdDelegatingSerializer.getSchema(serializerProvider, typeHint, true);
        
        // Assert
        Mockito.verify(schemaAwareDelegateSerializer).getSchema(serializerProvider, typeHint, true);
    }
}
