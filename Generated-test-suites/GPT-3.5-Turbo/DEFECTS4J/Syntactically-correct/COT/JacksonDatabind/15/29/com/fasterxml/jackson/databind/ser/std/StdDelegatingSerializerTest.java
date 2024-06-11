
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize_WithNullValue_ShouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Mockito.mock(Converter.class));
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        // Act
        serializer.serialize(null, gen, provider);

        // Assert
        Mockito.verify(provider).defaultSerializeNull(gen);
    }

    @Test
    public void testSerialize_WithNonNullValue_ShouldCallDelegateSerializer() throws IOException {
        // Arrange
        Object value = new Object();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Mockito.mock(Converter.class));
        serializer._delegateSerializer = Mockito.mock(JsonSerializer.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        // Act
        serializer.serialize(value, gen, provider);

        // Assert
        Mockito.verify(serializer._delegateSerializer).serialize(serializer.convertValue(value), gen, provider);
    }

    @Test
    public void testSerializeWithType_WithNonNullValue_ShouldCallDelegateSerializer() throws IOException {
        // Arrange
        Object value = new Object();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Mockito.mock(Converter.class));
        serializer._delegateSerializer = Mockito.mock(JsonSerializer.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);

        // Act
        serializer.serializeWithType(value, gen, provider, typeSer);

        // Assert
        Mockito.verify(serializer._delegateSerializer).serializeWithType(serializer.convertValue(value), gen, provider, typeSer);
    }

    @Test
    public void testIsEmpty_WithNonNullValue_ShouldCallDelegateSerializer() {
        // Arrange
        Object value = new Object();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Mockito.mock(Converter.class));
        serializer._delegateSerializer = Mockito.mock(JsonSerializer.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        Mockito.when(serializer._delegateSerializer.isEmpty(serializer.convertValue(value))).thenReturn(true);

        // Act
        boolean isEmpty = serializer.isEmpty(provider, value);

        // Assert
        Mockito.verify(serializer._delegateSerializer).isEmpty(serializer.convertValue(value));
        assertTrue(isEmpty);
    }

    // Rest of the test cases...

}
