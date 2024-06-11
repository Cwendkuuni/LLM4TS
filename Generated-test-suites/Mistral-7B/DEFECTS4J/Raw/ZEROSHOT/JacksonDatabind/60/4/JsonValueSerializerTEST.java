package com.example.json; // replace with your package name

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.StandardSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.AnnotatedMethod;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JsonValueSerializerTest {

    @SuppressWarnings("unchecked")
    private static final JsonValueSerializer serializer = new JsonValueSerializer(
            new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getSerializableValue"), JsonValueSerializerTest.class), null);

    @JsonSerialize(using = JsonValueSerializer.class)
    private static class SerializableValue {
        private int value;

        public int getValue() {
            return value;
        }
    }

    @JsonValue
    public static int getSerializableValue(SerializableValue value) {
        return value.getValue();
    }

    @Test
    public void shouldSerializeObjectWithJsonValueAnnotation() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();

        // when
        String jsonString = mapper.writeValueAsString(new SerializableValue(42));

        // then
        assertJsonEquals("{\"value\":42}", jsonString);
    }

    @Test
    public void shouldUseProvidedSerializerWhenAnnotated() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> providedSerializer = new StdSerializer<Object>(Object.class) {};

        // when
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getSerializableValue"), JsonValueSerializerTest.class), providedSerializer);

        ObjectWriter objectWriter = mapper.writerWithContextualSerializerProvider();
        String jsonString = objectWriter.writeValueAsString(new SerializableValue(42));

        // then
        assertEquals(providedSerializer.getClass(), jsonValueSerializer.getValueSerializer().getClass());
        assertJsonEquals("{\"value\":42}", jsonString);
    }

    @Test
    public void shouldSerializeValueReturnedByJsonValueAnnotationMethod() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();

        // when
        String jsonString = mapper.writeValueAsString(new SerializableValue(42));

        // then
        assertJsonEquals("{\"value\":42}", jsonString);
    }

    @Test
    public void shouldSerializeNullIfValueReturnedByJsonValueAnnotationMethodIsNull() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();

        // when
        String jsonString = mapper.writeValueAsString(new SerializableValue(null));

        // then
        assertJsonEquals("null", jsonString);
    }

    @Test
    public void shouldThrowJsonMappingExceptionForNonJsonValueAnnotationMethod() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();

        // when / then
        assertThrows(JsonMappingException.class, () -> mapper.writeValueAsString(new Object()));
    }

    @Test
    public void shouldAcceptJsonFormatVisitor() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mapper.schemaFactory().getSchemaProvider().defaultVisitors().findSchemaVisitor(JsonFormatVisitorWrapper.class);

        // when
        serializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);

        // then
        assertNotNull(jsonFormatVisitorWrapper.getSchema());
    }

    @Test
    public void shouldHandleNaturalTypes() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();

        // when
        String jsonString = mapper.writeValueAsString(42);

        // then
        assertJsonEquals("42", jsonString);
    }

    @Test
    public void shouldHandleNullNaturalTypes() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();

        // when
        String jsonString = mapper.writeValueAsString(null);

        // then
        assertJsonEquals("null", jsonString);
    }

    private int getSerializableValue(SerializableValue value) {
        return value.getValue();
    }

    private static class TestBean {
        private TestInner inner = new TestInner();

        public TestInner getInner() {
            return inner;
        }
    }

    private static class TestInner {
        private int value;

        public int getValue() {
            return value;
        }

        @JsonValue
        public int getValueMethod() {
            return value;
        }
    }

    private static class TestJsonValueSerializer extends JsonValueSerializer {
        public TestJsonValueSerializer(AnnotatedMethod accessorMethod, JsonSerializer<Object> valueSerializer) {
            super(accessorMethod, valueSerializer, null, false);
        }

        @Override
        public JsonSerializer<Object> createContextual(SerializerProvider provider, BeanProperty property)
                throws JsonMappingException {
            return super.createContextual(provider, property).withResolved(property, new StdSerializer<Object>(Object.class) {}, false);
        }
    }

    @Test
    public void shouldAllowNestedJsonValueAnnotations() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();

        // when
        String jsonString = mapper.writeValueAsString(new TestBean());

        // then
        assertJsonEquals("{\"inner\":{\"value\":null}}", jsonString);

        // given
        JsonValueSerializer innerSerializer = new TestJsonValueSerializer(
                new AnnotatedMethod(TestBean.class.getDeclaredMethod("getInner"), TestBean.class), null);

        ObjectWriter objectWriter = mapper.writerWithContextualSerializerProvider();

        // when
        JsonSerializer<Object> valueSerializer = objectWriter.findValueSerializer(TestInner.class, null);
        JsonSerializer<Object> contextualSerializer = innerSerializer.createContextual(objectWriter.getProvider(), null);
        JsonSerializer<Object> serializedSerializer = contextualSerializer.withResolved(null, valueSerializer, false);

        // then
        assertEquals(serializedSerializer.getClass(), innerSerializer.getValueSerializer().getClass());
        assertJsonEquals("{\"inner\":null}", jsonString);
    }

    private static void assertJsonEquals(String expectedJsonString, String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        Object expectedJson = mapper.readValue(expectedJsonString, Object.class);
        Object json = mapper.readValue(jsonString, Object.class);
        assertEquals(expectedJson, json);
    }

    private static void assertJsonEquals(String expectedJsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Object expectedJson = mapper.readValue(expectedJsonString, Object.class);
        String jsonString = mapper.writeValueAsString(expectedJson);
        assertJsonEquals(jsonString, expectedJsonString);
    }

}