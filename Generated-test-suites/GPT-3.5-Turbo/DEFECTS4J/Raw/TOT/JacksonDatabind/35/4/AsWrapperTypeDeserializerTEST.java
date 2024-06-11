package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer deserializer;

    @Before
    public void setUp() {
        JavaType bt = new ObjectMapper().getTypeFactory().constructType(String.class);
        TypeIdResolver idRes = new DummyTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        // Arrange
        JsonParser jp = new ObjectMapper().getFactory().createParser("{\"type\":\"string\",\"value\":\"test\"}");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        // Act
        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);

        // Assert
        assertNotNull(result);
        assertEquals(result, "test");
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        // Arrange
        JsonParser jp = new ObjectMapper().getFactory().createParser("{\"type\":\"string\",\"value\":\"test\"}");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        // Act
        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);

        // Assert
        assertNotNull(result);
        assertEquals(result, "test");
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        // Arrange
        JsonParser jp = new ObjectMapper().getFactory().createParser("{\"type\":\"string\",\"value\":\"test\"}");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        // Act
        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);

        // Assert
        assertNotNull(result);
        assertEquals(result, "test");
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        // Arrange
        JsonParser jp = new ObjectMapper().getFactory().createParser("{\"type\":\"string\",\"value\":\"test\"}");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        // Act
        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);

        // Assert
        assertNotNull(result);
        assertEquals(result, "test");
    }

    @Test
    public void testDeserializeTypedFromObject_NullJsonParser_ThrowsException() throws IOException {
        // Arrange
        JsonParser jp = null;
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        try {
            // Act
            deserializer.deserializeTypedFromObject(jp, ctxt);

            // Assert
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Assert
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testDeserializeTypedFromArray_NullJsonParser_ThrowsException() throws IOException {
        // Arrange
        JsonParser jp = null;
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        try {
            // Act
            deserializer.deserializeTypedFromArray(jp, ctxt);

            // Assert
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Assert
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testDeserializeTypedFromScalar_NullJsonParser_ThrowsException() throws IOException {
        // Arrange
        JsonParser jp = null;
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        try {
            // Act
            deserializer.deserializeTypedFromScalar(jp, ctxt);

            // Assert
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Assert
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testDeserializeTypedFromAny_NullJsonParser_ThrowsException() throws IOException {
        // Arrange
        JsonParser jp = null;
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        try {
            // Act
            deserializer.deserializeTypedFromAny(jp, ctxt);

            // Assert
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Assert
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testDeserializeTypedFromObject_InvalidJsonParser_ThrowsException() throws IOException {
        // Arrange
        JsonParser jp = new ObjectMapper().getFactory().createParser("\"type\":\"string\",\"value\":\"test\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        try {
            // Act
            deserializer.deserializeTypedFromObject(jp, ctxt);

            // Assert
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Assert
            assertTrue(e instanceof JsonParseException);
        }
    }

    @Test
    public void testDeserializeTypedFromArray_InvalidJsonParser_ThrowsException() throws IOException {
        // Arrange
        JsonParser jp = new ObjectMapper().getFactory().createParser("\"type\":\"string\",\"value\":\"test\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        try {
            // Act
            deserializer.deserializeTypedFromArray(jp, ctxt);

            // Assert
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Assert
            assertTrue(e instanceof JsonParseException);
        }
    }

    @Test
    public void testDeserializeTypedFromScalar_InvalidJsonParser_ThrowsException() throws IOException {
        // Arrange
        JsonParser jp = new ObjectMapper().getFactory().createParser("\"type\":\"string\",\"value\":\"test\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        try {
            // Act
            deserializer.deserializeTypedFromScalar(jp, ctxt);

            // Assert
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Assert
            assertTrue(e instanceof JsonParseException);
        }
    }

    @Test
    public void testDeserializeTypedFromAny_InvalidJsonParser_ThrowsException() throws IOException {
        // Arrange
        JsonParser jp = new ObjectMapper().getFactory().createParser("\"type\":\"string\",\"value\":\"test\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper().getFactory(), null, null);

        try {
            // Act
            deserializer.deserializeTypedFromAny(jp, ctxt);

            // Assert
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Assert
            assertTrue(e instanceof JsonParseException);
        }
    }

    private static class DummyTypeIdResolver implements TypeIdResolver {
        @Override
        public void init(JavaType baseType) {
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) {
            return null;
        }

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return null;
        }
    }
}