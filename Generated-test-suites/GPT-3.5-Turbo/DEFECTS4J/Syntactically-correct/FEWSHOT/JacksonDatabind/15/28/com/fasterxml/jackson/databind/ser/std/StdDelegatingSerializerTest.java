package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize_withNonNullValue_shouldSerializeDelegateValue() throws IOException {
        // Arrange
        Converter<?, ?> converter = mock(Converter.class);
        Object delegateValue = new Object();
        when(converter.convert(any())).thenReturn(delegateValue);

        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, TypeFactory.unknownType(), delegateSerializer);

        Object value = new Object();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        verify(converter, times(1)).convert(value);
        verify(delegateSerializer, times(1)).serialize(delegateValue, generator, provider);
    }

    @Test
    public void testSerialize_withNullValue_shouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        Converter<?, ?> converter = mock(Converter.class);
        when(converter.convert(any())).thenReturn(null);

        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, TypeFactory.unknownType(), delegateSerializer);

        Object value = new Object();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        serializer.serialize(value, generator, provider);

        // Assert
        verify(converter, times(1)).convert(value);
        verify(provider, times(1)).defaultSerializeNull(generator);
    }

    @Test
    public void testResolve_withResolvableDelegateSerializer_shouldResolveDelegateSerializer() throws IOException {
        // Arrange
        Converter<?, ?> converter = mock(Converter.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        when(delegateSerializer instanceof ResolvableSerializer).thenReturn(true);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        serializer.resolve(provider);

        // Assert
        verify((ResolvableSerializer) delegateSerializer, times(1)).resolve(provider);
    }

    @Test
    public void testCreateContextual_withExistingDelegateSerializer_shouldNotCallSecondaryContextualization() throws IOException {
        // Arrange
        Converter<?, ?> converter = mock(Converter.class);
        JavaType delegateType = TypeFactory.unknownType();
        JsonSerializer<?> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        // Act
        serializer.createContextual(provider, property);

        // Assert
        verify(provider, never()).handleSecondaryContextualization(delegateSerializer, property);
    }

    @Test
    public void testCreateContextual_withDelegateSerializerImplementingContextualSerializer_shouldCallSecondaryContextualization() throws IOException {
        // Arrange
        Converter<?, ?> converter = mock(Converter.class);
        JavaType delegateType = TypeFactory.unknownType();
        ContextualSerializer delegateSerializer = mock(ContextualSerializer.class);
        when(delegateSerializer.createContextual(any(), any())).thenReturn(delegateSerializer);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        // Act
        serializer.createContextual(provider, property);

        // Assert
        verify(provider, times(1)).handleSecondaryContextualization(delegateSerializer, property);
    }

    @Test
    public void testGetSchema_withDelegateSerializerImplementingSchemaAware_shouldCallGetSchema() throws IOException {
        // Arrange
        Converter<?, ?> converter = mock(Converter.class);
        JavaType delegateType = TypeFactory.unknownType();
        SchemaAware delegateSerializer = mock(SchemaAware.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Object.class;
        when(delegateSerializer.getSchema(provider, typeHint)).thenReturn(null);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        // Act
        serializer.getSchema(provider, typeHint);

        // Assert
        verify(delegateSerializer, times(1)).getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchema_withDelegateSerializerNotImplementingSchemaAware_shouldCallSuperGetSchema() throws IOException {
        // Arrange
        Converter<?, ?> converter = mock(Converter.class);
        JavaType delegateType = TypeFactory.unknownType();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Object.class;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        // Act
        serializer.getSchema(provider, typeHint);

        // Assert
        verify(delegateSerializer, never()).getSchema(provider, typeHint);
        verify(provider, times(1)).findValueSerializer(delegateType);
    }

    @Test
    public void testGetSchema_withSchemaAware_shouldCallSuperGetSchema() throws IOException {
        // Arrange
        Converter<?, ?> converter = mock(Converter.class);
        JavaType delegateType = TypeFactory.unknownType();
        SchemaAware delegateSerializer = mock(SchemaAware.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Object.class;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        // Act
        serializer.getSchema(provider, typeHint, true);

        // Assert
        verify(delegateSerializer, never()).getSchema(provider, typeHint, true);
        verify(provider, times(1)).findValueSerializer(delegateType);
    }

    @Test
    public void testGetSchema_withDelegateSerializerNotImplementingSchemaAware_shouldCallSuperGetSchemaIfNotOptional() throws IOException {
        // Arrange
        Converter<?, ?> converter = mock(Converter.class);
        JavaType delegateType = TypeFactory.unknownType();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Object.class;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        // Act
        serializer.getSchema(provider, typeHint, false);

        // Assert
        verify(delegateSerializer, never()).getSchema(provider, typeHint);
        verify(provider, times(1)).findValueSerializer(delegateType);
    }

    @Test
    public void testAcceptJsonFormatVisitor_withDelegateSerializer_shouldCallAcceptJsonFormatVisitor() throws IOException {
        // Arrange
        Converter<?, ?> converter = mock(Converter.class);
        JavaType delegateType = TypeFactory.unknownType();
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        SchemaAware delegate = mock(SchemaAware.class);
        when(delegateSerializer.acceptJsonFormatVisitor(any(), any())).thenReturn(null);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        // Act
        serializer.acceptJsonFormatVisitor(delegate, delegateType);

        // Assert
        verify(delegateSerializer, times(1)).acceptJsonFormatVisitor(delegate, delegateType);
    }

    @Test
    public void testConvertValue_shouldCallConverterConvertMethod() throws IOException {
        // Arrange
        Converter<Object, Object> converter = mock(Converter.class);
        Object value = new Object();
        Object convertedValue = new Object();
        when(converter.convert(value)).thenReturn(convertedValue);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        // Act
        Object result = serializer.convertValue(value);

        // Assert
        verify(converter, times(1)).convert(value);
        assertEquals(convertedValue, result);
    }
}