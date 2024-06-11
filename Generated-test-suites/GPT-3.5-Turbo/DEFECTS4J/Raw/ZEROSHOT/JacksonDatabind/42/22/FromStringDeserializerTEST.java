
package com.fasterxml.jackson.databind.deser.std;

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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

import junit.framework.TestCase;

public class FromStringDeserializerTest extends TestCase {

    public void testDeserialize() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return value;
            }
        };
        JsonParser jp = new JsonParser() {
            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public JsonToken nextToken() throws IOException {
                return null;
            }

            @Override
            public JsonToken getCurrentToken() {
                return null;
            }

            @Override
            public String getValueAsString() throws IOException {
                return "test";
            }

            @Override
            public Object getEmbeddedObject() throws IOException {
                return null;
            }

            @Override
            public boolean hasTextCharacters() {
                return false;
            }

            @Override
            public char[] getTextCharacters() throws IOException {
                return new char[0];
            }

            @Override
            public int getTextLength() throws IOException {
                return 0;
            }

            @Override
            public int getTextOffset() throws IOException {
                return 0;
            }
        };

        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        assertEquals("test", deserializer.deserialize(jp, ctxt));
    }

    public void test_deserialize_EmptyString() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return value;
            }

            @Override
            protected String _deserializeFromEmptyString() throws IOException {
                return "empty";
            }
        };
        JsonParser jp = new JsonParser() {
            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public JsonToken nextToken() throws IOException {
                return null;
            }

            @Override
            public JsonToken getCurrentToken() {
                return null;
            }

            @Override
            public String getValueAsString() throws IOException {
                return "";
            }

            @Override
            public Object getEmbeddedObject() throws IOException {
                return null;
            }

            @Override
            public boolean hasTextCharacters() {
                return false;
            }

            @Override
            public char[] getTextCharacters() throws IOException {
                return new char[0];
            }

            @Override
            public int getTextLength() throws IOException {
                return 0;
            }

            @Override
            public int getTextOffset() throws IOException {
                return 0;
            }
        };

        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        assertEquals("empty", deserializer.deserialize(jp, ctxt));
    }

    public void test_deserialize_InvalidString() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return value;
            }
        };
        JsonParser jp = new JsonParser() {
            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public JsonToken nextToken() throws IOException {
                return null;
            }

            @Override
            public JsonToken getCurrentToken() {
                return null;
            }

            @Override
            public String getValueAsString() throws IOException {
                return "invalid";
            }

            @Override
            public Object getEmbeddedObject() throws IOException {
                return null;
            }

            @Override
            public boolean hasTextCharacters() {
                return false;
            }

            @Override
            public char[] getTextCharacters() throws IOException {
                return new char[0];
            }

            @Override
            public int getTextLength() throws IOException {
                return 0;
            }

            @Override
            public int getTextOffset() throws IOException {
                return 0;
            }
        };

        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        try {
            deserializer.deserialize(jp, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Invalid String: not a valid textual representation", e.getMessage());
        }
    }
}
