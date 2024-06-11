package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return value;
            }
        };

        JsonParser jp = null;
        DeserializationContext ctxt = null;

        // Test deserialize with non-null value
        jp.getValueAsString();
        Object result = deserializer.deserialize(jp, ctxt);
        Assert.assertNotNull(result);

        // Test deserialize with null value
        jp.getValueAsString();
        result = deserializer.deserialize(jp, ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserialize_EmbeddedObject() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(String.class) {
            @Override
            protected String _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException {
                return ob.toString();
            }
        };

        JsonParser jp = null;
        DeserializationContext ctxt = null;

        // Test deserialize with non-null embedded object
        jp.getEmbeddedObject();
        Object ob = new Object();
        Object result = deserializer.deserialize(jp, ctxt);
        Assert.assertEquals(ob.toString(), result);

        // Test deserialize with null embedded object
        jp.getEmbeddedObject();
        result = deserializer.deserialize(jp, ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeFromEmptyString() throws IOException {
        FromStringDeserializer deserializer = new FromStringDeserializer(String.class) {
            @Override
            protected String _deserializeFromEmptyString() throws IOException {
                return "empty";
            }
        };

        Object result = deserializer._deserializeFromEmptyString();
        Assert.assertEquals("empty", result);
    }

    @Test
    public void testStdDeserialize() throws IOException {
        final String stringValue = "stringValue";
        final String emptyStringValue = "";

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0) {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return stringValue;
            }
        };

        DeserializationContext ctxt = null;

        // Test deserialize with non-empty string value
        Object result = deserializer._deserialize(stringValue, ctxt);
        Assert.assertEquals(stringValue, result);

        // Test deserialize with empty string value
        result = deserializer._deserialize(emptyStringValue, ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testStdDeserialize_EmbeddedObject() throws IOException {
        final String stringValue = "stringValue";

        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0) {
            @Override
            protected Object _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException {
                return stringValue;
            }
        };

        DeserializationContext ctxt = null;

        // Test deserialize with non-null embedded object
        Object ob = new Object();
        Object result = deserializer._deserializeEmbedded(ob, ctxt);
        Assert.assertEquals(stringValue, result);

        // Test deserialize with null embedded object
        result = deserializer._deserializeEmbedded(null, ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testStdDeserializeFromEmptyString() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0) {
            @Override
            protected Object _deserializeFromEmptyString() throws IOException {
                return "empty";
            }
        };

        Object result = deserializer._deserializeFromEmptyString();
        Assert.assertEquals("empty", result);
    }

    @Test
    public void testFindDeserializer() {
        Class<?> rawType = null;
        FromStringDeserializer.Std result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNull(result);

        rawType = File.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(File.class, result.getValueClass());

        rawType = URL.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(URL.class, result.getValueClass());

        rawType = URI.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(URI.class, result.getValueClass());

        rawType = Class.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.class, result.getValueClass());

        rawType = JavaType.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(JavaType.class, result.getValueClass());

        rawType = Currency.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(Currency.class, result.getValueClass());

        rawType = Pattern.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(Pattern.class, result.getValueClass());

        rawType = Locale.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(Locale.class, result.getValueClass());

        rawType = Charset.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(Charset.class, result.getValueClass());

        rawType = TimeZone.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(TimeZone.class, result.getValueClass());

        rawType = InetAddress.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(InetAddress.class, result.getValueClass());

        rawType = InetSocketAddress.class;
        result = FromStringDeserializer.Std.findDeserializer(rawType);
        Assert.assertNotNull(result);
        Assert.assertEquals(InetSocketAddress.class, result.getValueClass());
    }
}