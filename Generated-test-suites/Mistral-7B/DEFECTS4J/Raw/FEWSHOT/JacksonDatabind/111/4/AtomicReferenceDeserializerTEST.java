package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectToValue;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.SerializableStringSerializer;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() throws JsonProcessingException {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                JsonTypeFactory.instance.constructType(AtomicReference.class, Object.class),
                null, null, null);
        DeserializationContext ctxt = new TestContext();
        AtomicReference<Object> expected = new AtomicReference<Object>();
        AtomicReference<Object> result = serializer.getNullValue(ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testGetEmptyValue() throws JsonProcessingException {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                JsonTypeFactory.instance.constructType(AtomicReference.class, Object.class),
                null, null, null);
        Object result = serializer.getEmptyValue(new TestContext());
        assertEquals(new AtomicReference<Object>(), result);
    }

    @Test
    public void testReferenceValue() throws JsonProcessingException {
        Object contents = new Object();
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                JsonTypeFactory.instance.constructType(AtomicReference.class, Object.class),
                null, null, null);
        AtomicReference<Object> result = serializer.referenceValue(contents);
        AtomicReference<Object> expected = new AtomicReference<Object>(contents);
        assertEquals(expected, result);
    }

    @Test
    public void testGetReferenced() throws JsonProcessingException {
        Object contents = "test";
        AtomicReference<Object> reference = new AtomicReference<Object>(contents);
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                JsonTypeFactory.instance.constructType(AtomicReference.class, Object.class),
                null, null, null);
        Object result = serializer.getReferenced(reference);
        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() throws JsonProcessingException {
        Object newContents = "new test";
        AtomicReference<Object> reference = new AtomicReference<Object>("test");
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                JsonTypeFactory.instance.constructType(AtomicReference.class, Object.class),
                null, null, null);
        serializer.updateReference(reference, newContents);
        assertEquals(newContents, reference.get());
    }

    @Test
    public void testSupportsUpdate() throws JsonProcessingException {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                JsonTypeFactory.instance.constructType(AtomicReference.class, Object.class),
                null, null, null);
        JsonDeserializer<Object> valueDeserializer = new StubJsonDeserializer<>();
        assertTrue(serializer.supportsUpdate(new TestContext(valueDeserializer)));
    }

    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                JsonTypeFactory.instance.constructType(AtomicReference.class, String.class),
                null, null, null);
        JsonDeserializer<String> valueDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                return new StringSerializer().deserialize(null, value, p, ctxt);
            }
        };
        String json = "{\"type\": \"com.fasterxml.jackson.databind.deser.std.AtomicReference\", \"value\": \"test\"}";
        AtomicReference<String> result = mapper.readValue(json, AtomicReference.class);
        AtomicReference<String> expected = new AtomicReference<>("test");
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_NullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                JsonTypeFactory.instance.constructType(AtomicReference.class, String.class),
                null, null, null);
        String json = "{\"type\": \"com.fasterxml.jackson.databind.deser.std.AtomicReference\", \"value\": null}";
        AtomicReference<String> result = mapper.readValue(json, AtomicReference.class);
        assertNull(result.get());
    }

    @Test
    public void testDeserialize_EmptyValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
                JsonTypeFactory.instance.constructType(AtomicReference.class, String.class),
                null, null, null);
        String json = "{\"type\": \"com.fasterxml.jackson.databind.deser.std.AtomicReference\", \"value\": {}}";
        AtomicReference<String> result = mapper.readValue(json, AtomicReference.class);
        assertEquals(new AtomicReference<String>(), result);
    }

    static class StubJsonDeserializer<T> implements JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    static class StringSerializer extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value);
        }
    }

    static class TestContext extends DeserializationContext {
        private JsonDeserializer<Object> _valueDeserializer;

        public TestContext() {
            super(null, null, null, null);
        }

        public TestContext(JsonDeserializer<Object> valueDeserializer) {
            this();
            _valueDeserializer = valueDeserializer;
        }

        @Override
        public JsonDeserializer<?> findValueDeserializer(DeserializationConfig config, JavaType type) throws JsonMappingException {
            return _valueDeserializer;
        }
    }
}