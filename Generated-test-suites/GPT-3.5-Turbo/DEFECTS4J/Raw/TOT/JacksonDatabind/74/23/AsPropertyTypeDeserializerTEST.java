
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    private AsPropertyTypeDeserializer typeDeserializer;
    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;
    private JavaType baseType;
    private TypeIdResolver idRes;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;
    private As inclusion;

    @Before
    public void setUp() {
        baseType = new JavaType();
        idRes = new TypeIdResolver();
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = new JavaType();
        inclusion = As.PROPERTY;
        typeDeserializer = new AsPropertyTypeDeserializer(baseType, idRes, typePropertyName, typeIdVisible,
                defaultImpl, inclusion);
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
    }

    @Test
    public void testDeserializeTypedFromObject_WhenCanReadTypeId_True() throws IOException {
        // Arrange
        byte[] input = "test input".getBytes();
        JsonParser parser = jsonFactory.createParser(input);
        DeserializationContext context = objectMapper.getDeserializationContext();
        TokenBuffer tb = null;

        // Act
        Object result = typeDeserializer.deserializeTypedFromObject(parser, context);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeTypedFromObject_WhenTokenIsNotStartObject() throws IOException {
        // Arrange
        byte[] input = "test input".getBytes();
        JsonParser parser = jsonFactory.createParser(input);
        DeserializationContext context = objectMapper.getDeserializationContext();
        TokenBuffer tb = null;

        // Act
        Object result = typeDeserializer.deserializeTypedFromObject(parser, context);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeTypedFromObject_WhenFieldNameEqualsTypePropertyName() throws IOException {
        // Arrange
        byte[] input = "test input".getBytes();
        JsonParser parser = jsonFactory.createParser(input);
        DeserializationContext context = objectMapper.getDeserializationContext();
        TokenBuffer tb = null;

        // Act
        Object result = typeDeserializer.deserializeTypedFromObject(parser, context);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeTypedFromObject_WhenFieldNameNotEqualsTypePropertyName() throws IOException {
        // Arrange
        byte[] input = "test input".getBytes();
        JsonParser parser = jsonFactory.createParser(input);
        DeserializationContext context = objectMapper.getDeserializationContext();
        TokenBuffer tb = null;

        // Act
        Object result = typeDeserializer.deserializeTypedFromObject(parser, context);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeTypedForId_WhenTypeIdVisibleTrue() throws IOException {
        // Arrange
        byte[] input = "test input".getBytes();
        JsonParser parser = jsonFactory.createParser(input);
        DeserializationContext context = objectMapper.getDeserializationContext();
        TokenBuffer tb = null;

        // Act
        Object result = typeDeserializer.deserializeTypedForId(parser, context, tb);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeTypedForId_WhenTypeIdVisibleFalse() throws IOException {
        // Arrange
        byte[] input = "test input".getBytes();
        JsonParser parser = jsonFactory.createParser(input);
        DeserializationContext context = objectMapper.getDeserializationContext();
        TokenBuffer tb = null;

        // Act
        Object result = typeDeserializer.deserializeTypedForId(parser, context, tb);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WhenDefaultImplDeserializerExists() throws IOException {
        // Arrange
        byte[] input = "test input".getBytes();
        JsonParser parser = jsonFactory.createParser(input);
        DeserializationContext context = objectMapper.getDeserializationContext();
        TokenBuffer tb = null;

        // Act
        Object result = typeDeserializer.deserializeTypedUsingDefaultImpl(parser, context, tb);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WhenDefaultImplDeserializerNotExists() throws IOException {
        // Arrange
        byte[] input = "test input".getBytes();
        JsonParser parser = jsonFactory.createParser(input);
        DeserializationContext context = objectMapper.getDeserializationContext();
        TokenBuffer tb = null;

        // Act
        Object result = typeDeserializer.deserializeTypedUsingDefaultImpl(parser, context, tb);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeTypedFromAny_WhenTokenIsStartArray() throws IOException {
        // Arrange
        byte[] input = "test input".getBytes();
        JsonParser parser = jsonFactory.createParser(input);
        DeserializationContext context = objectMapper.getDeserializationContext();

        // Act
        Object result = typeDeserializer.deserializeTypedFromAny(parser, context);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeTypedFromAny_WhenTokenIsNotStartArray() throws IOException {
        // Arrange
        byte[] input = "test input".getBytes();
        JsonParser parser = jsonFactory.createParser(input);
        DeserializationContext context = objectMapper.getDeserializationContext();

        // Act
        Object result = typeDeserializer.deserializeTypedFromAny(parser, context);

        // Assert
        assertEquals(expectedResult, result);
    }
}

