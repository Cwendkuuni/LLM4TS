package com.example.junit; // replace with your test package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

public class JsonValueSerializerTest {

    @SuppressWarnings("unchecked")
    private static final Class<JsonValueSerializer> JsonValueSerializerClass = JsonValueSerializer.class;

    @SuppressWarnings("deprecation")
    @org.junit.Test
    public void testJsonValueSerializer() throws Exception {
        // Test constructor
        JsonValueSerializer serializer1 = new JsonValueSerializer(
                createMethod(MyClass.class, "getValueMethod"), null);
        assertEquals(MyClass.class, serializer1.getClass());

        // Test constructor with property and serializer
        MyClass myClass = new MyClass();
        JsonValueSerializer serializer2 = new JsonValueSerializer(
                serializer1, new BeanProperty(MyClass.class, "propertyName"),
                createJsonSerializer(String.class), false);
        assertEquals(MyClass.class, serializer2.getClass());
        assertEquals("propertyName", serializer2._property.getName());
        assertEquals(createJsonSerializer(String.class), serializer2._valueSerializer);

        // Test createContextual
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> valueSerializer = createJsonSerializer(Integer.class);
        SerializerProvider provider = mapper.getSerializerProvider();
        BeanProperty property = new BeanProperty(MyClass.class, "propertyName");
        JsonValueSerializer serializer3 = serializer2.withResolved(property, valueSerializer, false);
        assertEquals(valueSerializer, serializer3._valueSerializer);

        // Test serialize
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider prov = mock(SerializerProvider.class);
        when(prov.findValueSerializer(Integer.class, property)).thenReturn(valueSerializer);
        when(valueSerializer.serialize(anyInt(), eq(gen), eq(prov))).thenReturn(null);

        serializer3.serialize(myClass, gen, prov);
        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName(eq("propertyName"));
        verify(gen, times(1)).writeValue(anyInt());
        verify(gen, times(1)).writeEndObject();

        // Test serializeWithType
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        when(prov.findTypeSerializer(provider.getDefaultTyping(), Integer.class)).thenReturn(typeSerializer);

        serializer3.serializeWithType(myClass, gen, prov, typeSerializer);
        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("propertyName");
        verify(typeSerializer, times(1)).writeTypePrefixForScalar(myClass, gen);
        verify(valueSerializer, times(1)).serializeWithType(anyInt(), eq(gen), eq(prov), eq(typeSerializer));
        verify(typeSerializer, times(1)).writeTypeSuffixForScalar(myClass, gen);
        verify(gen, times(1)).writeEndObject();

        // Test getSchema
        JsonSchema schema = createJsonSchema();
        JsonNode schemaNode = schema.getSchemaProvider().schemaFromAnnotation(
                getClass().getMethod("getSchema"));
        JsonNode expectedSchemaNode = createExpectedSchemaNode();
        assertEquals(expectedSchemaNode.toString(), schemaNode.toString());

        // Test acceptJsonFormatVisitor
        JsonFormatVisitorWrapper visitor = createJsonFormatVisitorWrapper();
        serializer3.acceptJsonFormatVisitor(visitor, null);
        verify(visitor, times(1)).expectAnyFormat(eq(MyClass.class));
        verify(visitor, times(1)).expectStringFormat(eq(MyClass.class));
        verify(visitor, times(1)).enumTypes(any(LinkedHashSet.class));
    }

    // Replace these methods with actual mocks or test doubles if needed
    private JsonSerializer<Object> createJsonSerializer(Class<?> clazz) throws Exception {
        return spy(new StdSerializer<>(JsonValueSerializerClass.getDeclaredConstructor(
                AnnotatedMethod.class, JsonSerializer.class).newInstance(
                createMethod(JsonSerializer.class, "getValueMethod"), null)));
    }

    private Method createMethod(Class<?> clazz, String name) throws NoSuchMethodException {
        return clazz.getMethod(name);
    }

    private JsonSerializer<?> createJsonSerializer(Class<?> clazz) throws Exception {
        return new StdSerializer<Object>(JsonValueSerializerClass.getDeclaredConstructor(
                AnnotatedMethod.class, JsonSerializer.class).newInstance(
                createMethod(JsonSerializer.class, "getValueMethod"), null));
    }

    private ObjectMapper newObjectMapper() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void configure(SerializationConfig config, SerializerFactory factory) {
                factory.findValueSerializer(MyClass.class, null).setNullValueSerializer(null);
            }
        });
        mapper.registerModule(new SimpleModule() {
            @Override
            public void configure(DeserializationConfig config, DeserializerFactory factory) {
                factory.findValueDeserializer(MyClass.class, null).setNullValueSerializer(null);
            }
        });
        return mapper;
    }

    private JsonSchema createJsonSchema() throws Exception {
        ObjectMapper mapper = newObjectMapper();
        return mapper.getFactory().getSchemaProvider().getDefaultSchema();
    }

    private JsonFormatVisitorWrapper createJsonFormatVisitorWrapper() {
        return mock(JsonFormatVisitorWrapper.class);
    }

    // Replace this class with your test class
    private static class MyClass {

        @JsonSerialize(using = JsonValueSerializer.class)
        public Integer getValueMethod() {
            return 1;
        }

        @JsonValue
        public Integer value() {
            return 2;
        }

        public static class NestedClass {
            public String value() {
                return "nested";
            }
        }

        public NestedClass nested = new NestedClass();
    }

    private static JsonNode createExpectedSchemaNode() {
        return objectNode()
                .putPOJOProperty("propertyName", Integer.class)
                .putPOJOProperty("nested.value", String.class)
                .put("title", "MyClass")
                .put("description", "A test class")
                .put("type", "object");
    }
}