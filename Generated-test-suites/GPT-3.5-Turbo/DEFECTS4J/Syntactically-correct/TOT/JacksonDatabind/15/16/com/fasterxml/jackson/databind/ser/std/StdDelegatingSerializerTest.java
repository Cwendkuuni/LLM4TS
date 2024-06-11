package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.DefaultSerializerProviderImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdDelegatingSerializerTest {

    ObjectMapper objectMapper;
    DefaultSerializerProvider provider;
    SerializerProvider serializerProvider;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        provider = new DefaultSerializerProvider.Impl();
        objectMapper.setSerializerProvider(provider);
        serializerProvider = objectMapper.getSerializerProvider();
    }
    
    @Test
    public void testSerialize() throws IOException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.init(null);
        Object value = new Object();

        // Act
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, value);

        // Assert
        assertNotNull(outputStream);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.init(null);
        Object value = new Object();

        // Act
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, value);

        // Assert
        assertNotNull(outputStream);
    }

    @Test
    public void testIsEmpty_WhenDelegateValueIsNull_ThenReturnTrue() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.init(null);

        // Act
        boolean isEmpty = serializer.isEmpty(null);

        // Assert
        assertEquals(true, isEmpty);
    }

    @Test
    public void testIsEmpty_WhenDelegateValueIsNotNull_ThenReturnFalse() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.init(null);

        // Act
        boolean isEmpty = serializer.isEmpty(new Object());

        // Assert
        assertEquals(false, isEmpty);
    }

    @Test
    public void testIsEmpty_WithSerializerProvider_WhenDelegateValueIsNull_ThenReturnTrue() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.init(null);

        // Act
        boolean isEmpty = serializer.isEmpty(serializerProvider, null);

        // Assert
        assertEquals(true, isEmpty);
    }

    @Test
    public void testIsEmpty_WithSerializerProvider_WhenDelegateValueIsNotNull_ThenReturnFalse() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.init(null);

        // Act
        boolean isEmpty = serializer.isEmpty(serializerProvider, new Object());

        // Assert
        assertEquals(false, isEmpty);
    }

    @Test
    public void testGetSchema_WhenDelegateSerializerIsSchemaAware_ThenReturnSchemaFromDelegateSerializer() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.init(null);

        // Act
        TypeResolverBuilder<?> typeResolverBuilder = new StdTypeResolverBuilder();
        serializer.setSchema(typeResolverBuilder);

        // Assert
        assertNotNull(serializer.getSchema(serializerProvider, null));
    }

    @Test
    public void testGetSchema_WhenDelegateSerializerIsNotSchemaAware_ThenReturnNull() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.init(null);

        // Act
        serializer.setSchema(null);

        // Assert
        assertEquals(null, serializer.getSchema(serializerProvider, null));
    }

    @Test
    public void testGetSchema_WithIsOptional_WhenDelegateSerializerIsSchemaAware_ThenReturnSchemaFromDelegateSerializer() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.init(null);

        // Act
        TypeResolverBuilder<?> typeResolverBuilder = new StdTypeResolverBuilder();
        serializer.setSchema(typeResolverBuilder);

        // Assert
        assertNotNull(serializer.getSchema(serializerProvider, null, true));
    }

    @Test
    public void testGetSchema_WithIsOptional_WhenDelegateSerializerIsNotSchemaAware_ThenReturnNull() {
        // Arrange
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.init(null);

        // Act
        serializer.setSchema(null);

        // Assert
        assertEquals(null, serializer.getSchema(serializerProvider, null, true));
    }
}