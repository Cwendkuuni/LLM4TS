

package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.cli.Util;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.bean.Property;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        // test when jp is not an expected start array token
        jp = createJsonParser(JsonToken.NOT_AVAILABLE);
        String[] result = deserializer.deserialize(jp, ctxt);
        assertNull(result);
        
        // test when _elementDeserializer is not null
        deserializer._elementDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        jp = createJsonParser(JsonToken.START_ARRAY);
        result = deserializer.deserialize(jp, ctxt);
        assertNull(result);
        
        // test when t is VALUE_STRING
        jp = createJsonParser(JsonToken.VALUE_STRING, "foo");
        result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[] {"foo"}, result);
        
        // test when t is VALUE_NULL and _elementDeserializer is null
        jp = createJsonParser(JsonToken.VALUE_NULL);
        result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[] {null}, result);
        
        // test when t is not VALUE_STRING or VALUE_NULL
        deserializer._elementDeserializer = null;
        jp = createJsonParser(JsonToken.VALUE_FALSE);
        result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[] {null}, result);
    }
    
    @Test
    public void testDeserializeCustom() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        // test when t is VALUE_NULL
        jp = createJsonParser(JsonToken.VALUE_NULL);
        String[] result = deserializer._deserializeCustom(jp, ctxt);
        assertArrayEquals(new String[] {null}, result);
        
        // test when t is not VALUE_NULL
        deserializer._elementDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        jp = createJsonParser(JsonToken.VALUE_FALSE);
        result = deserializer._deserializeCustom(jp, ctxt);
        assertArrayEquals(new String[] {null}, result);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertNull(result);
    }
    
    @Test
    public void testHandleNonArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        // test when single value is not accepted as an array
        jp = createJsonParser(JsonToken.VALUE_STRING);
        try {
            deserializer.handleNonArray(jp, ctxt);
            fail("IOException expected");
        } catch(IOException e) {
            // expected
        }
        
        // test when single value is accepted as an array and jp's current token is VALUE_NULL
        jp = createJsonParser(JsonToken.VALUE_NULL);
        String[] result = deserializer.handleNonArray(jp, ctxt);
        assertArrayEquals(new String[] {null}, result);
        
        // test when single value is accepted as an array and jp's current token is not VALUE_NULL
        jp = createJsonParser(JsonToken.VALUE_TRUE);
        result = deserializer.handleNonArray(jp, ctxt);
        assertArrayEquals(new String[] {null}, result);
    }
    
    @Test
    public void testCreateContextual() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = null;
        BeanProperty property = null;
        
        // test when deser is null and isDefaultDeserializer is true
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        assertNull(result);
        
        // test when deser is null and isDefaultDeserializer is false
        deserializer._elementDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        result = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result);
        
        // test when deser is not null
        JsonDeserializer<?> deser = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        result = deserializer.createContextual(ctxt, property);
        assertNotEquals(deserializer, result);
    }
    
    private JsonParser createJsonParser(JsonToken token) {
        return createJsonParser(token, null);
    }
    
    private JsonParser createJsonParser(JsonToken token, String text) {
        return new JsonParser() {

            @Override
            public ObjectCodec getCodec() {
                return null;
            }

            @Override
            public void setCodec(ObjectCodec oc) {

            }

            @Override
            public Version version() {
                return null;
            }

            @Override
            public void close() throws IOException {

            }

            @Override
            public JsonToken nextToken() throws IOException {
                return token;
            }

            @Override
            public JsonParser skipChildren() throws IOException {
                return null;
            }

            @Override
            public boolean hasTokenId(int id) {
                return false;
            }

            @Override
            public String getCurrentName() throws IOException {
                return null;
            }

            @Override
            public JsonStreamContext getParsingContext() {
                return null;
            }

            @Override
            public JsonLocation getTokenLocation() {
                return null;
            }

            @Override
            public JsonLocation getCurrentLocation() {
                return null;
            }

            @Override
            public void clearCurrentToken() {

            }

            @Override
            public JsonToken getCurrentToken() {
                return token;
            }

            @Override
            public int getCurrentTokenId() {
                return token.ordinal();
            }

            @Override
            public JsonToken getLastClearedToken() {
                return null;
            }

            @Override
            public boolean hasCurrentToken() {
                return false;
            }

            @Override
            public boolean hasToken(JsonToken t) {
                return false;
            }

            @Override
            public boolean isExpectedStartArrayToken() {
                return token == JsonToken.START_ARRAY;
            }

            @Override
            public boolean isExpectedStartObjectToken() {
                return false;
            }

            @Override
            public boolean isNaN() {
                return false;
            }

            @Override
            public JsonParser overrideStdFeatures(int values, int mask) {
                return null;
            }

            @Override
            public String nextFieldName() throws IOException {
                return null;
            }

            @Override
            public String nextTextValue() throws IOException {
                return text;
            }

            @Override
            public char nextChar() throws IOException {
                return 0;
            }

            @Override
            public int nextIntValue(int defaultValue) throws IOException {
                return 0;
            }

            @Override
            public long nextLongValue(long defaultValue) throws IOException {
                return 0;
            }

            @Override
            public Boolean nextBooleanValue() throws IOException {
                return null;
            }

            @Override
            public Object nextValue() throws IOException {
                return null;
            }

            @Override
            public int nextIntValue(int defaultValue) throws IOException {
                return 0;
            }

            @Override
            public long nextLongValue(long defaultValue) throws IOException {
                return 0;
            }

            @Override
            public Boolean nextBooleanValue() throws IOException {
                return null;
            }
        };
    }
}
