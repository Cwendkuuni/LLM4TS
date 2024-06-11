package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

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

import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {
    @Test
    public void testDeserialize() throws Exception {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) {
                return value;
            }
        };

        JsonParser parser = new MockJsonParser("\"test\"");
        DeserializationContext context = new MockDeserializationContext();
        String result = deserializer.deserialize(parser, context);
        assertEquals("test", result);
    }

    @Test
    public void testDeserialize_EmptyString() throws Exception {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) {
                return value;
            }
        };

        JsonParser parser = new MockJsonParser("\"\"");
        DeserializationContext context = new MockDeserializationContext();
        String result = deserializer.deserialize(parser, context);
        assertEquals("", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidString() throws Exception {
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class) {
            @Override
            protected Integer _deserialize(String value, DeserializationContext ctxt) {
                return Integer.parseInt(value);
            }
        };

        JsonParser parser = new MockJsonParser("\"test\"");
        DeserializationContext context = new MockDeserializationContext();
        deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserialize_UnwrapSingleValueArray() throws Exception {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) {
                return value;
            }
        };

        JsonParser parser = new MockJsonParser("[\"test\"]");
        DeserializationContext context = new MockDeserializationContext(true); // enable UNWRAP_SINGLE_VALUE_ARRAYS
        String result = deserializer.deserialize(parser, context);
        assertEquals("test", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_UnwrapSingleValueArray_MoreThanOneValue() throws Exception {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) {
                return value;
            }
        };

        JsonParser parser = new MockJsonParser("[\"test\", \"test2\"]");
        DeserializationContext context = new MockDeserializationContext(true); // enable UNWRAP_SINGLE_VALUE_ARRAYS
        deserializer.deserialize(parser, context);
    }

    private static class MockJsonParser extends JsonParser {
        private String value;

        public MockJsonParser(String value) {
            this.value = value;
        }

        @Override
        public String getValueAsString() {
            return value;
        }

        @Override
        public String getText() {
            return value;
        }

        @Override
        public JsonToken getCurrentToken() {
            return null;
        }

        @Override
        public JsonToken nextToken() {
            return null;
        }

        // other methods omitted for brevity
    }

    private static class MockDeserializationContext extends DeserializationContext {
        private boolean unwrapSingleValueArrays;

        public MockDeserializationContext() {
            super(null, null);
        }

        public MockDeserializationContext(boolean unwrapSingleValueArrays) {
            super(null, null);
            this.unwrapSingleValueArrays = unwrapSingleValueArrays;
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            if (feature == DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS) {
                return unwrapSingleValueArrays;
            }
            return false;
        }

        // other methods omitted for brevity
    }
}