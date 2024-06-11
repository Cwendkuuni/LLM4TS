package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

public class FieldPropertyTest {

    @Test
    public void testFieldProperty() throws InstantiationException, IllegalAccessException, NoSuchFieldException, IOException {
        JavaType type = createType(int[].class);
        Object instance = new int[0];
        Object jsonValue = 42;

        // Test deserializeAndSet
        JsonParser parser = createParser("{\"value\": " + jsonValue + "}");
        Object property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new Annotation[0], new AnnotatedMember(new AnnotatedClass(int[].class), "value", null), "value", null), null, null), type, null, null, new AnnotatedField(new Annotation[0], new AnnotatedMember(new AnnotatedClass(int[].class), "value", null), "value", null)) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                int value = p.getValueAsInt();
                try {
                    _field.set(instance, value);
                } catch (IllegalAccessException e) {
                    throw new IOException("Failed to set field value", e);
                }
            }
        };
        property.deserializeAndSet(parser, null, instance);
        assertEquals(jsonValue, instance[0]);

        // Test deserializeSetAndReturn
        Object result = property.deserializeSetAndReturn(parser, null, instance);
        assertEquals(instance, result);
        assertEquals(jsonValue, instance[0]);

        // Test set
        Object value = 123;
        property.set(instance, value);
        assertEquals(value, instance[0]);

        // Test setAndReturn
        Object resultSet = property.setAndReturn(instance, value);
        assertEquals(instance, resultSet);
        assertEquals(value, instance[0]);
    }

    @Test
    public void testAnnotation() throws IOException {
        JavaType type = createType(String.class);
        Object instance = new Object() {
            public String value;
        };
        String jsonValue = "test";

        JsonParser parser = createParser("{\"name\": \"value\", \"value\": \"" + jsonValue + "\"}");

        Object property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty("value"), new AnnotatedMember(new AnnotatedClass(Object.class), "value", null), "value", null), null, new JsonValue("value")), type, null, null, new AnnotatedField(new JsonProperty("value"), new AnnotatedMember(new AnnotatedClass(Object.class), "value", null), "value", null)) {
            @JsonProperty("name")
            private String name;

            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                name = p.nextToken() == JsonToken.FIELD_NAME ? p.getCurrentName() : null;
                String value = p.getValueAsString();
                try {
                    _field.set(instance, value);
                } catch (IllegalAccessException e) {
                    throw new IOException("Failed to set field value", e);
                }
            }
        };

        property.deserializeAndSet(parser, null, instance);
        assertEquals(jsonValue, ((Object) instance).value);
        assertEquals("value", name);
    }

    @Test
    public void testNullValue() throws IOException {
        JavaType type = createType(String.class);
        Object instance = new Object();

        JsonParser parser = createParser("{\"name\": null}");

        Object property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty("name"), new AnnotatedMember(new AnnotatedClass(Object.class), "name", null), "name", null), null, new JsonValue("name")), type, null, null, new AnnotatedField(new JsonProperty("name"), new AnnotatedMember(new AnnotatedClass(Object.class), "name", null), "name", null)) {
            @JsonProperty("name")
            private String name;

            @JsonNull
            private boolean nullable = true;

            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                name = p.nextToken() == JsonToken.VALUE_NULL ? null : p.getValueAsString();
            }
        };

        property.deserializeAndSet(parser, null, instance);
        assertNull(name);
    }

    @Test
    public void testTypeDeserializer() throws IOException {
        JavaType type = createType(MyClass.class);
        Object instance = new Object();

        JsonParser parser = createParser("{\"name\": \"MyClass\", \"value\": \"test\"}");

        Object property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new JsonProperty("value"), new AnnotatedMember(new AnnotatedClass(Object.class), "value", null), "value", null), null, new JsonValue("value")), type, new SimpleTypeDeserializer(), null, new AnnotatedField(new JsonProperty("value"), new AnnotatedMember(new AnnotatedClass(Object.class), "value", null), "value", null)) {
            @JsonProperty("name")
            private String name;

            @JsonTypeName("MyClass")
            private Class<?> clazz = MyClass.class;

            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                name = p.nextToken() == JsonToken.FIELD_NAME ? p.getCurrentName() : null;
                Object deserializedValue = _valueDeserializer.deserialize(p, ctxt, _valueTypeDeserializer);
                try {
                    _field.set(instance, deserializedValue);
                } catch (IllegalAccessException e) {
                    throw new IOException("Failed to set field value", e);
                }
            }
        };

        MyClass obj = new MyClass();
        property.deserializeAndSet(parser, null, instance);
        assertEquals(obj, instance);
        assertEquals("test", obj.value);
    }

    @Test
    public void testFixAccess() throws IOException {
        JavaType type = createType(int[].class);
        Object instance = new int[0];

        Object property = new FieldProperty(new BeanPropertyDefinition(new AnnotatedField(new Annotation[0], new AnnotatedMember(new AnnotatedClass(int[].class), "value", null), "value", null), null, null), type, null, null, new AnnotatedField(new Annotation[0], new AnnotatedMember(new AnnotatedClass(int[].class), "value", null), "value", null)) {
            @Override
            public void fixAccess(DeserializationConfig config) {
                ClassUtil.makeAccessible(_field);
            }
        };

        JsonParser parser = createParser("{\"value\": 42}");
        property.deserializeAndSet(parser, null, instance);
        assertEquals(42, instance[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingField() throws IOException {
        JavaType type = createType(int[].class);
        Object instance = new int[0];

        Object property = new FieldProperty(null, null, null, null, new AnnotatedField(new Annotation[0], new AnnotatedMember(new AnnotatedClass(int[].class), "nonExistentField", null), "nonExistentField", null)) {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                fail("Should not reach this point");
            }
        };

        JsonParser parser = createParser("{\"nonExistentField\": 42}");
        property.deserializeAndSet(parser, null, instance);
    }

    private Object createType(Class<?> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getTypeFactory().constructType(clazz);
    }

    private JsonParser createParser(String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(new StringReader(content)).asParser();
    }

    private static class SimpleTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vtd) throws IOException {
            return ctxt.findValueDeserializer(vtd.getType(p, ctxt), null, null);
        }
    }

    private static class MyClass {
        @JsonProperty("value")
        public String value;
    }
}