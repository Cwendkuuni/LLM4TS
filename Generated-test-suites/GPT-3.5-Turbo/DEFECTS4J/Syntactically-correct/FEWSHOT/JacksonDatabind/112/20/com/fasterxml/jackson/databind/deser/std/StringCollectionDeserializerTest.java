package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.ext.JavaTypeResolverBuilder;

public class StringCollectionDeserializerTest {
    
    private StringCollectionDeserializer deserializer;

    @Before
    public void setup() {
        deserializer = new StringCollectionDeserializer(null, null, null);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = new DummyJsonParser(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        DeserializationContext ctxt = new DummyDeserializationContext();
        Collection<String> result = deserializer.deserialize(p, ctxt);
        assertTrue(result.isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray() throws IOException {
        JsonParser p = new DummyJsonParser(JsonToken.VALUE_STRING);
        DeserializationContext ctxt = new DummyDeserializationContext();
        deserializer.handleNonArray(p, ctxt, new ArrayList<>());
    }
    
    @Test
    public void testIsCachable() {
        assertTrue(deserializer.isCachable());
    }
    
    private static class DummyJsonParser extends JsonParser {
        
        private final JsonToken[] tokens;
        private int currentIndex;
        
        public DummyJsonParser(JsonToken... tokens) {
            this.tokens = tokens;
            this.currentIndex = 0;
        }

        @Override
        public boolean nextFieldName(SerializableString str) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String nextFieldName() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean nextName(SerializableString str) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String nextName() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonParser skipChildren() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isExpectedStartArrayToken() {
            return tokens[currentIndex] == JsonToken.START_ARRAY;
        }

        @Override
        public void close() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isClosed() {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonToken nextToken() throws IOException {
            if (currentIndex < tokens.length) {
                return tokens[currentIndex++];
            } else {
                return null;
            }
        }
        
    }
    
    private static class DummyDeserializationContext extends DeserializationContext {
        
        @Override
        public JsonDeserializer<?> findContextualValueDeserializer(JavaType valueType, BeanProperty property) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> handleSecondaryContextualization(JsonDeserializer<?> deser, BeanProperty prop, JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return false;
        }

        @Override
        public boolean isAnnotationProcessingEnabled() {
            return false;
        }

        @Override
        public DeserializationContext with(DeserializerFactory factory) {
            return null;
        }

        @Override
        public DeserializationContext createInstance(JsonParser p,
                DeserializationConfig config) {
            return null;
        }

        @Override
        public Object readValue(JsonParser p, JavaType valueType) throws IOException {
            return null;
        }

        @Override
        public Object readValue(JsonParser p, Class<?> valueType) throws IOException {
            return null;
        }

        @Override
        public Object readKey(JsonParser p) throws IOException {
            return null;
        }

        @Override
        public JavaType constructType(Class<?> cls) {
            return null;
        }

        @Override
        public JsonParser getParser() {
            return null;
        }

        @Override
        public DeserializationConfig getConfig() {
            return null;
        }

        @Override
        public void reportMappingException(String message) throws JsonMappingException {
            throw new JsonMappingException(message);
        }

        @Override
        public void reportPropertyInputMismatch(JsonDeserializer<?> deser, String msg, Object... msgArgs) throws JsonMappingException {
            throw new JsonMappingException(String.format(msg, msgArgs));
        }

        @Override
        public NullValueProvider getNullValueProvider() {
            return null;
        }

        @Override
        public void throwBadNullValue(Class<?> instClass, String msg) throws JsonMappingException {
            throw new JsonMappingException(String.format("Cannot map null value to %s : %s", instClass.getName(), msg));
        }

        @Override
        public JsonDeserializer<?> findNonContextualValueDeserializer(JavaType type) throws JsonMappingException {
            return null;
        }
        
        @Override
        public ValueInstantiator findValueInstantiator(JavaType type) throws JsonMappingException {
            return null;
        }
        
        @Override
        public JavaTypeResolverBuilder<?> javaTypeResolverBuilderInstance(AnnotatedWithParams defaultImpl) throws JsonMappingException {
            return null;
        }

        @Override
        public void reportBadDefinition(JavaType type, String msg) throws JsonMappingException {
            throw new JsonMappingException(String.format("Cannot resolve type %s : %s", type.getRawClass().getName(), msg));
        }
        
        @Override
        public Object handleUnexpectedToken(Class<?> instClass, JsonParser p) throws IOException {
            throw new JsonMappingException(String.format("Unexpected token for class %s : %s", instClass.getName(), p.getCurrentToken().name()));
        }
        
    }
}