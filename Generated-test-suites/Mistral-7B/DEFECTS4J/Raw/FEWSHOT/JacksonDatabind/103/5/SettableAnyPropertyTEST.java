package com.fasterxml.jackson.databind.deser;

import static com.fasterxml.jackson.databind.DeserializationTest.assertDeserialization;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyPropertyCreation() {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMethod();
        JavaType type = new SimpleType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);
        SettableAnyProperty settableAnyProperty2 = new SettableAnyProperty(property, setter, type, null, null, null).withValueDeserializer(new StubJsonDeserializer<>());
        assertNotNull(settableAnyProperty);
        assertNotNull(settableAnyProperty2);
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSetNullValue() throws IOException {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMethod();
        JavaType type = new SimpleType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        Object instance = new Object();
        String propName = "testProperty";
        settableAnyProperty.deserializeAndSet(createContextAndParser("null"), createContext(), instance, propName);
        assertNull(instance);
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSetValue() throws IOException {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMethod();
        JavaType type = new SimpleType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        Object instance = new Object();
        String propName = "testProperty";
        Object expectedValue = "value";
        String jsonString = "\"value\"";
        Object deserializedValue = new ObjectMapper().readValue(jsonString, String.class);

        settableAnyProperty.deserializeAndSet(createContextAndParser(jsonString), createContext(), instance, propName);
        assertEquals(expectedValue, instance);
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSetValueWithTypeDeserializer() throws IOException {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMethod();
        JavaType type = new SimpleType(Map.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        Object instance = new Object();
        String propName = "testProperty";
        Object expectedValue = new HashMap<>();
        String jsonString = "{\"key\": \"value\"}";
        Object deserializedValue = new ObjectMapper().readValue(jsonString, Map.class);

        settableAnyProperty.deserializeAndSet(createContextAndParser(jsonString), createContext(), instance, propName);
        assertEquals(expectedValue.getClass(), instance.getClass());
        assertEquals(expectedValue, instance);
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSetValueWithKeyDeserializer() throws IOException {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMethod();
        JavaType type = new SimpleType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, new StubKeyDeserializer(), null, null);

        Object instance = new Object();
        String propName = "testProperty";
        String key = "testKey";
        String jsonString = "{\"key\": \"value\"}";
        Object deserializedValue = new ObjectMapper().readValue(jsonString, Map.class).get(key);

        settableAnyProperty.deserializeAndSet(createContextAndParser(jsonString), createContext(), instance, propName);
        assertEquals(deserializedValue, instance);
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMethod();
        JavaType type = new SimpleType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        Object instance = new Object();
        String propName = "testProperty";

        settableAnyProperty.deserializeAndSet(createContextAndParserWithUnresolvedForwardReference(propName), createContext(), instance, propName);
    }

    @Test
    public void testSettableAnyPropertyValueDeserializer() throws IOException {
        BeanProperty property = new BeanProperty();
        AnnotatedMember setter = new AnnotatedMethod();
        JavaType type = new SimpleType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        Object instance = new Object();
        String propName = "testProperty";
        JsonDeserializer<Object> valueDeserializer = new StubJsonDeserializer<>();
        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(valueDeserializer);

        String jsonString = "{\"testProperty\": \"value\"}";
        Object deserializedValue = new ObjectMapper().readValue(jsonString, Map.class).get("testProperty");

        settableAnyPropertyWithValueDeserializer.deserializeAndSet(createContextAndParser(jsonString), createContext(), instance, propName);
        assertEquals(deserializedValue, instance);
    }

    private DeserializationContext createContext() {
        ObjectReader reader = new ObjectMapper().readerForUpdating(new Object());
        return createContext(reader);
    }

    private DeserializationContext createContext(ObjectReader reader) {
        return new TestContext(reader);
    }

    private JsonParser createContextAndParser(String jsonString) throws IOException {
        ObjectReader reader = new ObjectMapper().readerForUpdating(new Object());
        return reader.parse(new StringReader(jsonString));
    }

    private JsonParser createContextAndParserWithUnresolvedForwardReference(String propName) throws IOException {
        ObjectReader reader = new ObjectMapper().readerForUpdating(new Object());
        JsonParser parser = reader.parse(new StringReader("{\"anyProperty\":{}}"));
        parser.nextToken();
        parser.setValue(propName);
        parser.nextToken();
        return parser;
    }

    private static class StubJsonDeserializer<T> extends JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return (T) new Object();
        }
    }

    private static class StubKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return key;
        }
    }
}