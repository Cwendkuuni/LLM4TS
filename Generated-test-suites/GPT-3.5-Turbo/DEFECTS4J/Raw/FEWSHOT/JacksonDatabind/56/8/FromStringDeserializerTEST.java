package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class FromStringDeserializerTest {

    public class TestDeserializer extends FromStringDeserializer<Object> {

        private static final long serialVersionUID = 1L;

        protected TestDeserializer(Class<?> valueType) {
            super(valueType);
        }

        @Override
        protected Object _deserialize(String value, DeserializationContext ctxt) throws JsonMappingException {
            // TODO Auto-generated method stub
            return null;
        }

    }

    @Test
    public void testDeserialize() throws Exception {
        TestDeserializer deserializer = new TestDeserializer(Object.class);
        JsonParser parser = null;
        DeserializationContext ctxt = null;

        // Test case 1
        parser = generateJsonParser("foo");
        assertEquals(null, deserializer.deserialize(parser, ctxt));

        // Test case 2
        parser = generateJsonParser("\"\"");
        assertEquals(null, deserializer.deserialize(parser, ctxt));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalid() throws Exception {
        TestDeserializer deserializer = new TestDeserializer(Object.class);
        JsonParser parser = null;
        DeserializationContext ctxt = null;

        // Test case - Invalid format
        parser = generateJsonParser("invalid");
        deserializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeEmbedded() throws Exception {
        TestDeserializer deserializer = new TestDeserializer(Object.class);
        JsonParser parser = null;
        DeserializationContext ctxt = null;

        // Test case - embedded object is null
        parser = generateJsonParser("null");
        assertNull(deserializer.deserialize(parser, ctxt));

        // Test case - embedded object is not null and is assignable to the value class
        TestClass testObject = new TestClass();
        parser = generateJsonParser(testObject.toString());
        assertEquals(testObject, deserializer.deserialize(parser, ctxt));

        // Test case - embedded object is not null and is not assignable to the value class
        parser = generateJsonParser("1");
        assertEquals(null, deserializer.deserialize(parser, ctxt));
    }

    private JsonParser generateJsonParser(String value) {
        // TODO Auto-generated method stub
        return null;
    }

    public class TestClass {

    }
}