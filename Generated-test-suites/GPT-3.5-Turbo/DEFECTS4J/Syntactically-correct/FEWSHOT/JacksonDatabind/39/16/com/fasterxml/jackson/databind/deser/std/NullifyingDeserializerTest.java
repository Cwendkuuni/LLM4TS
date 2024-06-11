package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.JsonReadContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer; 

public class NullifyingDeserializerTest {
    
    private class MockJsonParser extends JsonParser {

        @Override
        public void close() throws IOException {
            // TODO Auto-generated method stub
            
        }

        @Override
        public BigInteger getBigIntegerValue() throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public byte[] getBinaryValue(Base64Variant arg0) throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean getBooleanValue() throws IOException {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public byte getByteValue() throws IOException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public ObjectCodec getCodec() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public JsonLocation getCurrentLocation() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getCurrentName() throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public JsonToken getCurrentToken() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getCurrentTokenId() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean nextFieldName(SerializableString arg0) throws IOException {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int nextIntValue(int arg0) throws IOException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public String nextTextValue() throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void overrideCurrentName(String arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public JsonReadContext getParsingContext() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getText() throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public char[] getTextCharacters() throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getTextLength() throws IOException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getTextOffset() throws IOException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTextCharacters() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isClosed() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public JsonToken nextToken(JsonToken arg0) throws IOException, JsonParseException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public NumberType getNumberType() throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Number getNumberValue() throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public JsonStreamContext getParsingContext() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getLongValue() throws IOException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public JsonLocation getTokenLocation() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean canReadObjectId() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean canReadTypeId() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean hasToken(JsonToken token) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void clearCurrentToken() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public JsonToken getLastClearedToken() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void _handleEOF() throws JsonParseException {
            // TODO Auto-generated method stub
            
        }

        @Override
        public JsonParser skipChildren() throws IOException, JsonParseException {
            // TODO Auto-generated method stub
            return null;
        }
        
    }

    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        
        MockJsonParser parser = new MockJsonParser();
        DeserializationContext context = new DeserializationContext(JsonFactory.builder().build(), new DefaultDeserializationContext.Impl(new ObjectMapper(), new DefaultDeserializationContext.Impl(null, null)));
        
        assertNull(deserializer.deserialize(parser, context));
        
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        
        MockJsonParser parser = new MockJsonParser();
        DeserializationContext context = new DeserializationContext(JsonFactory.builder().build(), new DefaultDeserializationContext.Impl(new ObjectMapper(), new DefaultDeserializationContext.Impl(null, null)));
        
        assertNull(deserializer.deserializeWithType(parser, context, null));
    }
    
}