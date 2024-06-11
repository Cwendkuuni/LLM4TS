package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer typeDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TokenBuffer tokenBuffer;
    private BeanProperty mockBeanProperty;

    @Before
    public void setUp() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        typeDeserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        tokenBuffer = mock(TokenBuffer.class);
        mockBeanProperty = mock(BeanProperty.class);
    }

    @Test
    public void testConstructor() {
        AsWrapperTypeDeserializer constructorTest = new AsWrapperTypeDeserializer(mock(JavaType.class), mock(TypeIdResolver.class), "test", true, Object.class);
        assertNotNull(constructorTest);
    }

    @Test
    public void testForProperty() {
        TypeDeserializer forPropertyTest = typeDeserializer.forProperty(mockBeanProperty);
        assertTrue(forPropertyTest instanceof AsWrapperTypeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        As typeInclusionTest = typeDeserializer.getTypeInclusion();
        assertEquals(As.WRAPPER_OBJECT, typeInclusionTest);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        Object result = typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        Object result = typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        Object result = typeDeserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        Object result = typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNotNull(result);
    }

    @Test
    public void testInternalMethods() throws IOException {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(mock(JavaType.class), mock(TypeIdResolver.class), "test", true, Object.class);
        Object result = asWrapperTypeDeserializer._deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
    }

    @Test
    public void testInternalMethodsNativeTypeId() throws IOException {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(mock(JavaType.class), mock(TypeIdResolver.class), "test", true, Object.class);
        asWrapperTypeDeserializer._deserialize(jsonParser, deserializationContext);
        assertEquals(JsonToken.START_OBJECT, jsonParser.getCurrentToken());
    }
}