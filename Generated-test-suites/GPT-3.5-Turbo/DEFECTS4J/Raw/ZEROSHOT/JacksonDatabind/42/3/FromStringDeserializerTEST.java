package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {

    private FromStringDeserializer<Object> deserializer;

    @Test
    public void testDeserialize() throws Exception {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        // Test when jp.getCurrentToken() is JsonToken.START_ARRAY
        jp.setCurrentToken(JsonToken.START_ARRAY);
        ctxt.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);

        deserializer = new FromStringDeserializer.Std(File.class, 1);
        assertNull(deserializer.deserialize(jp, ctxt));

        // Test when jp.getAsText() returns null
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        jp.setValueAsString(null);

        deserializer = new FromStringDeserializer.Std(File.class, 1);
        assertEquals(null, deserializer.deserialize(jp, ctxt));

        // Test when jp.getCurrentToken() is JsonToken.VALUE_EMBEDDED_OBJECT
        jp.setCurrentToken(JsonToken.VALUE_EMBEDDED_OBJECT);
        jp.setEmbeddedObject(null);

        deserializer = new FromStringDeserializer.Std(File.class, 1);
        assertEquals(null, deserializer.deserialize(jp, ctxt));

        // Test when jp.getCurrentToken() is neither JsonToken.START_ARRAY nor JsonToken.VALUE_EMBEDDED_OBJECT
        jp.setCurrentToken(JsonToken.START_OBJECT);

        deserializer = new FromStringDeserializer.Std(File.class, 1);
        try {
            deserializer.deserialize(jp, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void test_deserialize() throws Exception {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        // Test when text is null
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        jp.setValueAsString(null);

        deserializer = new FromStringDeserializer.Std(File.class, 1);
        assertEquals(null, deserializer.deserialize(jp, ctxt));

        // Test when text is empty
        jp.setValueAsString("");

        deserializer = new FromStringDeserializer.Std(File.class, 1);
        assertEquals(null, deserializer.deserialize(jp, ctxt));

        // Test when _deserialize() throws IllegalArgumentException
        jp.setValueAsString("test");

        deserializer = new FromStringDeserializer.Std(File.class, 1);
        try {
            deserializer.deserialize(jp, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test when everything is valid
        jp.setValueAsString("/path/to/file");

        deserializer = new FromStringDeserializer.Std(File.class, 1);
        assertNotNull(deserializer.deserialize(jp, ctxt));
    }

    @Test
    public void test_deserializeEmbedded() throws Exception {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        // Test when ob is null
        jp.setCurrentToken(JsonToken.VALUE_EMBEDDED_OBJECT);
        jp.setEmbeddedObject(null);

        deserializer = new FromStringDeserializer.Std(File.class, 1);

        try {
            deserializer.deserialize(jp, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test when ob is of different class than _valueClass
        jp.setEmbeddedObject(new Object());

        try {
            deserializer.deserialize(jp, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test when everything is valid

        class TestClass {
        }

        jp.setEmbeddedObject(new TestClass());

        assertNotNull(deserializer.deserialize(jp, ctxt));
    }

    @Test
    public void test_deserializeFromEmptyString() throws Exception {
        deserializer = new FromStringDeserializer.Std(File.class, 1);
        assertEquals(null, deserializer._deserializeFromEmptyString());

        deserializer = new FromStringDeserializer.Std(URI.class, 3);
        assertEquals(URI.create(""), deserializer._deserializeFromEmptyString());

        deserializer = new FromStringDeserializer.Std(Class.class, 4);
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void test_construct() {
        new FromStringDeserializer.Std(File.class, 1);
    }

    @Test
    public void test_deserialize_ThrowsIllegalArgumentException() throws Exception {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        jp.setCurrentToken(JsonToken.VALUE_STRING);
        jp.setValueAsString("");

        deserializer = new FromStringDeserializer.Std(File.class, 1);
        try {
            deserializer.deserialize(jp, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void test_deserialize_ThrowsIllegalArgumentExceptionWithMessage() throws Exception {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        jp.setCurrentToken(JsonToken.VALUE_STRING);
        jp.setValueAsString("test");

        deserializer = new FromStringDeserializer.Std(File.class, 1);
        try {
            deserializer.deserialize(jp, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void test_protected_methods() throws Exception {
        deserializer = new FromStringDeserializer.Std(File.class, 1);
        deserializer._deserialize("", new DeserializationContext());
        deserializer._deserializeEncoded("", new DeserializationContext());
        deserializer._deserializeFromEmptyString();
        ;
    }
}