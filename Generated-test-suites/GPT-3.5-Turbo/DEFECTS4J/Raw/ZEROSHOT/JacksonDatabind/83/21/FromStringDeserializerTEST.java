package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
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
        FromStringDeserializer<TestClass> deserializer = new FromStringDeserializer<TestClass>(TestClass.class) {
            @Override
            protected TestClass _deserialize(String value, DeserializationContext ctxt) throws IOException
            {
                return new TestClass(value);
            }
        };

        JsonParser parser = new JsonFactory().createParser("\"testValue\"");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        TestClass deserializedObject = deserializer.deserialize(parser, ctxt);

        Assert.assertEquals("testValue", deserializedObject.getValue());
    }

    @Test
    public void testDeserialize_EmptyString() throws IOException {
        FromStringDeserializer<TestClass> deserializer = new FromStringDeserializer<TestClass>(TestClass.class) {
            @Override
            protected TestClass _deserialize(String value, DeserializationContext ctxt) throws IOException
            {
                return new TestClass(value);
            }

            @Override
            protected TestClass _deserializeFromEmptyString() throws IOException {
                return new TestClass("default");
            }
        };

        JsonParser parser = new JsonFactory().createParser("\"\"");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        TestClass deserializedObject = deserializer.deserialize(parser, ctxt);

        Assert.assertEquals("default", deserializedObject.getValue());
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserialize_InvalidFormat() throws IOException {
        FromStringDeserializer<TestClass> deserializer = new FromStringDeserializer<TestClass>(TestClass.class) {
            @Override
            protected TestClass _deserialize(String value, DeserializationContext ctxt) throws IOException
            {
                throw new InvalidFormatException("Invalid format", value, TestClass.class);
            }
        };

        JsonParser parser = new JsonFactory().createParser("\"testValue\"");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        deserializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserialize_EmbeddedObject() throws IOException {
        FromStringDeserializer<TestClass> deserializer = new FromStringDeserializer<TestClass>(TestClass.class) {
            @Override
            protected TestClass _deserialize(String value, DeserializationContext ctxt) throws IOException
            {
                return new TestClass(value);
            }

            @Override
            protected TestClass _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException {
                return new TestClass("embeddedObject");
            }
        };

        JsonParser parser = new JsonFactory().createParser("{\"value\":\"testValue\"}");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        TestClass deserializedObject = deserializer.deserialize(parser, ctxt);

        Assert.assertEquals("embeddedObject", deserializedObject.getValue());
    }

    private static class TestClass {
        private String value;

        public TestClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}