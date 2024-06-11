
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = new MockJsonParser();
        DeserializationContext ctxt = new MockDeserializationContext();

        String[] result = deserializer.deserialize(jp, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserialize_nonArray() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = new MockJsonParser();
        DeserializationContext ctxt = new MockDeserializationContext();
        ctxt.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        String[] result = deserializer.deserialize(jp, ctxt);

        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserialize_customDeserializer() throws IOException {
        StringArrayDeserializer deserializerWithCustomDeserializer = new StringArrayDeserializer(new CustomDeserializer());
        JsonParser jp = new MockJsonParser();
        DeserializationContext ctxt = new MockDeserializationContext();

        String[] result = deserializerWithCustomDeserializer.deserialize(jp, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserialize_customDeserializer_nonArray() throws IOException {
        StringArrayDeserializer deserializerWithCustomDeserializer = new StringArrayDeserializer(new CustomDeserializer());
        JsonParser jp = new MockJsonParser();
        DeserializationContext ctxt = new MockDeserializationContext();
        ctxt.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        String[] result = deserializerWithCustomDeserializer.deserialize(jp, ctxt);

        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = new MockJsonParser();
        DeserializationContext ctxt = new MockDeserializationContext();
        TypeDeserializer typeDeserializer = new MockTypeDeserializer();

        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);

        assertNull(result);
    }

    @Test
    public void testContextual_deserializerIsNull() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = new MockDeserializationContext();
        BeanProperty property = new MockBeanProperty();

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        assertEquals(deserializer, result);
    }

    @Test
    public void testContextual_deserializerIsDefaultDeserializer() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = new MockDeserializationContext();
        BeanProperty property = new MockBeanProperty();
        JsonDeserializer<?> customDeserializer = new CustomDeserializer();
        ctxt.setNonContextual(customDeserializer);

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        assertEquals(customDeserializer, result);
    }

    @Test
    public void testContextual_deserializerIsNotDefaultDeserializer() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = new MockDeserializationContext();
        BeanProperty property = new MockBeanProperty();
        JsonDeserializer<?> customDeserializer = new CustomDeserializer();

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        assertNotEquals(customDeserializer, result);
    }

    private static class CustomDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) {
            return null;
        }
    }

    private static class MockJsonParser extends JsonParser {

        @Override
        public boolean isExpectedStartArrayToken() throws IOException {
            return true;
        }

        @Override
        public JsonToken nextToken() throws IOException {
            return JsonToken.END_ARRAY;
        }

        @Override
        public String getText() throws IOException {
            return null;
        }
    }

    private static class MockDeserializationContext extends DeserializationContext {

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return false;
        }

        @Override
        public ObjectBuffer leaseObjectBuffer() {
            return null;
        }

        @Override
        public void returnObjectBuffer(ObjectBuffer buf) {

        }

        @Override
        public JsonDeserializer<Object> findContextualValueDeserializer(JavaType valueType, BeanProperty property) {
            return null;
        }

        @Override
        public void handleUnknownProperty(JsonParser p, JsonDeserializer<?> deser, Object instanceOrClass, String propName) throws IOException {

        }

        @Override
        public JsonDeserializer<?> handleSecondaryContextualization(JsonDeserializer<?> deser, BeanProperty property) throws JsonMappingException {
            return deser;
        }
    }

    private static class MockBeanProperty extends BeanProperty {

        @Override
        public String getName() {
            return null;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> clazz) {
            return null;
        }

        @Override
        public JavaType getType() {
            return null;
        }

        @Override
        public PropertyName getFullName() {
            return null;
        }
    }

    private static class MockTypeDeserializer extends TypeDeserializer {

        @Override
        public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}
