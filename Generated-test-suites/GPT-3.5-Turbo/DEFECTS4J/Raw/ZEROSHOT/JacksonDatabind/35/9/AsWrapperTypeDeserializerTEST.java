package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyMetadata.MergeInfo;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer deserializer;
    private JavaType baseType;
    @Mock
    private TypeIdResolver typeIdResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        baseType = Mockito.mock(JavaType.class);
        typePropertyName = "type";
        typeIdVisible = false;
        defaultImpl = Object.class;
        deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, typePropertyName, typeIdVisible,
                defaultImpl);
    }

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        // Arrange
        JsonToken jsonToken = JsonToken.VALUE_STRING;
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        // Act
        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        // Arrange
        JsonToken jsonToken = JsonToken.VALUE_NUMBER_INT;
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        // Act
        Object result = deserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        // Arrange
        JsonToken jsonToken = JsonToken.VALUE_FALSE;
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        // Act
        Object result = deserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);
        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        // Arrange
        JsonToken jsonToken = JsonToken.VALUE_NULL;
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        // Act
        Object result = deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        // Assert
        Assert.assertNull(result);
    }
}